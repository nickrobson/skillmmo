package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import dev.nickrobson.minecraft.skillmmo.recipe.PlayerLockedRecipeManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.MathHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerSkillManager {
    private static final PlayerSkillManager instance = new PlayerSkillManager();

    public static PlayerSkillManager getInstance() {
        return instance;
    }

    public void register() {
        // When players respawn, we need to copy over all their data
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            SkillMmoPlayerDataHolder oldPlayerDataHolder = (SkillMmoPlayerDataHolder) oldPlayer;
            SkillMmoPlayerDataHolder newPlayerDataHolder = (SkillMmoPlayerDataHolder) newPlayer;
            newPlayerDataHolder.setSkillMmoPlayerData(oldPlayerDataHolder.getSkillMmoPlayerData().clone());
            SkillMmoServerNetworking.sendPlayerData(newPlayer);
        });

        // When players change world, their ClientPlayerEntity is recreated so loses its data
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            SkillMmoServerNetworking.sendPlayerData(player);
        });

        // Send the player's updated player data once they've respawned
        ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {
            SkillMmoServerNetworking.sendPlayerData(newPlayer);
        }));
    }

    private PlayerSkillManager() {
    }

    public Map<Identifier, Integer> getSkillLevels(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getSkillLevels();
    }

    public int getSkillLevel(PlayerEntity player, Skill skill) {
        return getSkillLevels(player).getOrDefault(skill.getId(), Skill.MIN_LEVEL);
    }

    public boolean hasSkillLevel(PlayerEntity player, Skill skill, int level) {
        int playerSkillLevel = getSkillLevel(player, skill);
        return playerSkillLevel >= level;
    }

    public int setSkillLevel(PlayerEntity player, Skill skill, int level) {
        int newLevel = MathHelper.clamp(level, Skill.MIN_LEVEL, Math.min(skill.getMaxLevel(), Skill.MAX_LEVEL));
        this.updateSkillLevels(player, Map.of(skill.getId(), newLevel));
        return newLevel;
    }

    public void updateSkillLevels(PlayerEntity player, Map<Identifier, Integer> changedSkillLevels) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        changedSkillLevels.forEach((skillId, level) ->
                skillMmoPlayerDataHolder.getSkillMmoPlayerData().setSkillLevel(skillId, level));

        if (player instanceof ServerPlayerEntity serverPlayer) {
            SkillMmoServerNetworking.sendPlayerSkills(serverPlayer);

            PlayerLockedRecipeManager.getInstance().syncLockedRecipes(serverPlayer);
        }
    }

    public ChooseSkillLevelResult chooseSkillLevel(PlayerEntity player, Skill skill) {
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

    public SkillLevel getClosestLevel(PlayerEntity player, Collection<SkillLevel> skillLevelSet) {
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
