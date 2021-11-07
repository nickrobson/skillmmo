package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

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
    }

    private PlayerSkillManager() {
    }

    public Map<Identifier, Integer> getSkillLevels(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getSkillLevels();
    }

    public int getSkillLevel(PlayerEntity player, Skill skill) {
        return getSkillLevels(player).getOrDefault(skill.getId(), SkillLevel.MIN_LEVEL);
    }

    public boolean hasSkillLevel(PlayerEntity player, Skill skill, int level) {
        int playerSkillLevel = getSkillLevel(player, skill);
        return playerSkillLevel >= level;
    }

    public void setSkillLevel(PlayerEntity player, Skill skill, int level) {
        this.updateSkillLevels(player, Map.of(skill.getId(), level));
    }

    public void updateSkillLevels(PlayerEntity player, Map<Identifier, Integer> changedSkillLevels) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        changedSkillLevels.forEach((skillId, level) ->
                skillMmoPlayerDataHolder.getSkillMmoPlayerData().setSkillLevel(skillId, level));

        if (player instanceof ServerPlayerEntity serverPlayer) {
            SkillMmoServerNetworking.sendPlayerSkills(serverPlayer);
        }
    }

    public ChooseSkillLevelResult chooseSkillLevel(PlayerEntity player, Skill skill) {
        int currentLevel = getSkillLevel(player, skill);
        if (currentLevel >= skill.getMaxLevel().getLevel()) {
            return ChooseSkillLevelResult.FAILURE_AT_MAX_LEVEL;
        }

        if (!PlayerSkillPointManager.getInstance().consumeAvailableSkillPoint(player)) {
            return ChooseSkillLevelResult.FAILURE_NO_AVAILABLE_POINTS;
        }

        setSkillLevel(player, skill, currentLevel + 1);
        return ChooseSkillLevelResult.SUCCESS;
    }

    public SkillLevel getClosestLevel(PlayerEntity player, Collection<SkillLevel> skillLevelSet) {
        return skillLevelSet
                .stream()
                .filter(lvl -> !PlayerSkillManager.getInstance().hasSkillLevel(player, lvl.getSkill(), lvl.getLevel()))
                .min(Comparator.comparing(lvl -> {
                    int level = lvl.getLevel();
                    int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, lvl.getSkill());
                    return level - playerLevel;
                }))
                .orElse(null);
    }

    public enum ChooseSkillLevelResult {
        SUCCESS,
        FAILURE_AT_MAX_LEVEL,
        FAILURE_NO_AVAILABLE_POINTS
    }
}
