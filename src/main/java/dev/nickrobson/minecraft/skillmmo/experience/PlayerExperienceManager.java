package dev.nickrobson.minecraft.skillmmo.experience;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerData;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerExperienceManager {
    private static final PlayerExperienceManager instance = new PlayerExperienceManager();

    public static PlayerExperienceManager getInstance() {
        return instance;
    }

    private PlayerExperienceManager() {
    }

    public long getExperience(Player player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        return skillMmoPlayerDataHolder.skillMmo$getPlayerData().getExperience();
    }

    public ExperienceLevel getExperienceLevel(Player player) {
        return ExperienceLevelEquation.getInstance().getExperienceLevel(getExperience(player));
    }

    public void setExperience(Player player, long experience) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        skillMmoPlayerDataHolder.skillMmo$getPlayerData().setExperience(experience);
    }

    public void giveExperience(ServerPlayer player, long experience) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        SkillMmoPlayerData playerData = skillMmoPlayerDataHolder.skillMmo$getPlayerData();
        long oldExperience = playerData.getExperience();
        long newExperience = playerData.addExperience(experience);

        int oldLevel = ExperienceLevelEquation.getInstance().getExperienceLevel(oldExperience).level();
        int newLevel = ExperienceLevelEquation.getInstance().getExperienceLevel(newExperience).level();

        if (oldLevel < newLevel) {
            int availableSkillPoints = playerData.addAvailableSkillPoints(getTotalSkillPoints(newLevel) - getTotalSkillPoints(oldLevel));

            player.sendOverlayMessage(Component.translatable("skillmmo.feedback.player.level_up", newLevel, availableSkillPoints));
        }

        SkillMmoServerNetworking.sendPlayerXpInfo(player);
    }

    public int getTotalSkillPoints(int level) {
        // TODO: consider awarding an extra X points per Y levels?
        //       e.g. points = level + (X * level//Y)
        return level;
    }
}
