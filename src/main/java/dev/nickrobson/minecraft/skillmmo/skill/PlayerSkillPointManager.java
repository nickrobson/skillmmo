package dev.nickrobson.minecraft.skillmmo.skill;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;

import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerSkillPointManager {
    private static final PlayerSkillPointManager instance = new PlayerSkillPointManager();

    public static PlayerSkillPointManager getInstance() {
        return instance;
    }

    private PlayerSkillPointManager() {
    }

    public int getAvailableSkillPoints(Player player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        return skillMmoPlayerDataHolder.skillMmo$getPlayerData().getAvailableSkillPoints();
    }

    public void setAvailableSkillPoints(Player player, int availableSkillPoints) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        skillMmoPlayerDataHolder.skillMmo$getPlayerData().setAvailableSkillPoints(availableSkillPoints);
    }

    public boolean consumeAvailableSkillPoint(Player player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        return skillMmoPlayerDataHolder.skillMmo$getPlayerData().consumeAvailableSkillPoint();
    }
}
