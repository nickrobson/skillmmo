package dev.nickrobson.minecraft.skillmmo.skill;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import dev.nickrobson.minecraft.skillmmo.recipe.PlayerLockedRecipeManager;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;

@MethodsReturnNonnullByDefault
@NullMarked
public class PlayerSkillManager {
    private static final PlayerSkillManager instance = new PlayerSkillManager();

    public static PlayerSkillManager getInstance() {
        return instance;
    }

    public void register() {
        // When players respawn, we need to copy over all their data
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            SkillMmoPlayerDataHolder oldPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(oldPlayer);
            SkillMmoPlayerDataHolder newPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(newPlayer);
            newPlayerDataHolder.skillMmo$setPlayerData(oldPlayerDataHolder.skillMmo$getPlayerData().clone());
            SkillMmoServerNetworking.sendPlayerData(newPlayer);
        });

        // When players change world, their player entity is recreated so loses its data
        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) -> {
            SkillMmoServerNetworking.sendPlayerData(player);
        });

        // Send the player's updated player data once they've respawned
        ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {
            SkillMmoServerNetworking.sendPlayerData(newPlayer);
        }));
    }

    private PlayerSkillManager() {
    }

    public Map<Identifier, Integer> getSkillLevels(Player player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        return skillMmoPlayerDataHolder.skillMmo$getPlayerData().getSkillLevels();
    }

    public int getSkillLevel(Player player, Skill skill) {
        return getSkillLevels(player).getOrDefault(skill.getId(), Skill.MIN_LEVEL);
    }

    public boolean hasSkillLevel(Player player, Skill skill, int level) {
        int playerSkillLevel = getSkillLevel(player, skill);
        return playerSkillLevel >= level;
    }

    public int setSkillLevel(Player player, Skill skill, int level) {
        int newLevel = Mth.clamp(level, Skill.MIN_LEVEL, Math.min(skill.getMaxLevel(), Skill.MAX_LEVEL));
        this.updateSkillLevels(player, Map.of(skill.getId(), newLevel));
        return newLevel;
    }

    public void updateSkillLevels(Player player, Map<Identifier, Integer> changedSkillLevels) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        changedSkillLevels.forEach((skillId, level) ->
                skillMmoPlayerDataHolder.skillMmo$getPlayerData().setSkillLevel(skillId, level));

        if (player instanceof ServerPlayer serverPlayer) {
            SkillMmoServerNetworking.sendPlayerSkills(serverPlayer);

            PlayerLockedRecipeManager.getInstance().syncLockedRecipes(serverPlayer);
        }
    }

    public ChooseSkillLevelResult chooseSkillLevel(Player player, Skill skill) {
        int currentLevel = getSkillLevel(player, skill);
        if (currentLevel >= skill.getMaxLevel()) {
            return ChooseSkillLevelResult.FAILURE_AT_MAX_LEVEL;
        }

        if (!PlayerSkillPointManager.getInstance().consumeAvailableSkillPoint(player)) {
            return ChooseSkillLevelResult.FAILURE_NO_AVAILABLE_POINTS;
        }

        setSkillLevel(player, skill, currentLevel + 1);
        return ChooseSkillLevelResult.SUCCESS;
    }

    public SkillLevel getClosestLevel(Player player, Collection<SkillLevel> skillLevelSet) {
        if (skillLevelSet.isEmpty()) {
            throw new IllegalArgumentException("Expected a non-empty set of skill levels");
        }

        if (skillLevelSet.size() == 1) {
            return skillLevelSet.stream().findAny().get();
        }

        return skillLevelSet
                .stream()
                .filter(lvl -> !PlayerSkillManager.getInstance().hasSkillLevel(player, lvl.getSkill(), lvl.getLevel()))
                .min(Comparator
                        .<SkillLevel, Integer>comparing(lvl -> {
                            int level = lvl.getLevel();
                            int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, lvl.getSkill());
                            return level - playerLevel;
                        })
                        .thenComparing(lvl -> lvl.getSkill().getId()))
                .orElseThrow();
    }

    public enum ChooseSkillLevelResult {
        SUCCESS,
        FAILURE_AT_MAX_LEVEL,
        FAILURE_NO_AVAILABLE_POINTS
    }
}
