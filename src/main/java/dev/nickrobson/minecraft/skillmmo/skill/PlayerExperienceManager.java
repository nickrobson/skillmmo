package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerExperienceManager {
    private static final PlayerExperienceManager instance = new PlayerExperienceManager();
    public static PlayerExperienceManager getInstance() {
        return instance;
    }
    private PlayerExperienceManager() {}

    public long getExperience(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getExperience();
    }

    public void setExperience(ClientPlayerEntity player, long experience) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData().setExperience(experience);
    }

    public void giveExperience(ServerPlayerEntity player, long experience) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData().addExperience(experience);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            SkillMmoServerNetworking.sendPlayerXp(player);
        }
    }
}
