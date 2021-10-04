package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

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

    public boolean canInteract(PlayerEntity playerEntity, SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlockType, unlockIdentifier);
        if (skillLevelSet.isEmpty()) {
            // If no skill levels affect the item, it's allowed!
            return true;
        }

        Predicate<SkillLevel> hasSkillLevel = level -> this.hasSkillLevel(playerEntity, level);

        boolean requireAllLockingSkillsToBeUnlocked = SkillMmoConfig.getConfig().requireAllLockingSkillsToBeUnlocked;
        if (requireAllLockingSkillsToBeUnlocked) {
            return skillLevelSet.stream().allMatch(hasSkillLevel);
        }
        return skillLevelSet.stream().anyMatch(hasSkillLevel);
    }
}
