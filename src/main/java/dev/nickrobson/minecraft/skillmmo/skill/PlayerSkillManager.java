package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerSkillManager {
    private static final PlayerSkillManager instance = new PlayerSkillManager();
    public static PlayerSkillManager getInstance() {
        return instance;
    }

    public boolean hasSkillLevel(PlayerEntity playerEntity, SkillLevel skillLevel) {
        int playerSkillLevel = Math.max(0, getSkills(playerEntity).getOrDefault(skillLevel.getSkillId(), (byte) 0));
        return playerSkillLevel >= skillLevel.getLevel();
    }

    private Map<String, Byte> getSkills(PlayerEntity playerEntity) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) playerEntity;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().skillLevels();
    }

    private void setSkillLevel(PlayerEntity playerEntity, SkillLevel skillLevel) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) playerEntity;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData()
                .setSkillLevel(skillLevel.getSkillId(), skillLevel.getLevel());
    }
}
