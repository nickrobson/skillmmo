package dev.nickrobson.minecraft.skillmmo.experience;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerExperienceManager {
    private static final PlayerExperienceManager instance = new PlayerExperienceManager();

    public static PlayerExperienceManager getInstance() {
        return instance;
    }

    private PlayerExperienceManager() {
    }

    public long getExperience(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getExperience();
    }

    public ExperienceLevel getExperienceLevel(PlayerEntity player) {
        return ExperienceLevelEquation.getInstance().getExperienceLevel(getExperience(player));
    }

    public void setExperience(PlayerEntity player, long experience) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData().setExperience(experience);
    }

    public void giveExperience(ServerPlayerEntity player, long experience) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        SkillMmoPlayerDataHolder.SkillMmoPlayerData playerData = skillMmoPlayerDataHolder.getSkillMmoPlayerData();
        long oldExperience = playerData.getExperience();
        long newExperience = playerData.addExperience(experience);

        int oldLevel = ExperienceLevelEquation.getInstance().getExperienceLevel(oldExperience).level();
        int newLevel = ExperienceLevelEquation.getInstance().getExperienceLevel(newExperience).level();

        if (oldLevel < newLevel) {
            int availableSkillPoints = playerData.addAvailableSkillPoints(getTotalSkillPoints(newLevel) - getTotalSkillPoints(oldLevel));

            player.sendMessage(new TranslatableText("skillmmo.feedback.player.level_up", newLevel, availableSkillPoints), true);
        }

        SkillMmoServerNetworking.sendPlayerXpInfo(player);
    }

    public int getTotalSkillPoints(int level) {
        // TODO: consider awarding an extra X points per Y levels?
        //       e.g. points = level + (X * level//Y)
        return level;
    }
}
