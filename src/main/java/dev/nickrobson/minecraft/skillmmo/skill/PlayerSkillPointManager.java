package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerSkillPointManager {
    private static final PlayerSkillPointManager instance = new PlayerSkillPointManager();

    public static PlayerSkillPointManager getInstance() {
        return instance;
    }

    private PlayerSkillPointManager() {
    }

    public int getAvailableSkillPoints(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getAvailableSkillPoints();
    }

    public void setAvailableSkillPoints(PlayerEntity player, int availableSkillPoints) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData().setAvailableSkillPoints(availableSkillPoints);
    }

    public boolean consumeAvailableSkillPoint(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().consumeAvailableSkillPoint();
    }
}
