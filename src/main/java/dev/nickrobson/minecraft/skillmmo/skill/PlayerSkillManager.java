package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
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

    public Map<Identifier, Integer> getSkills(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getSkillLevels();
    }

    public int getSkillLevel(PlayerEntity player, Skill skill) {
        return getSkills(player).getOrDefault(skill.getId(), SkillLevel.MIN_LEVEL);
    }

    public boolean hasSkillLevel(PlayerEntity player, Skill skill, int level) {
        int playerSkillLevel = getSkillLevel(player, skill);
        return playerSkillLevel >= level;
    }

    public void setSkillLevels(PlayerEntity player, Map<Identifier, Integer> playerSkillLevels) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        playerSkillLevels.forEach((skillId, level) ->
                skillMmoPlayerDataHolder.getSkillMmoPlayerData().setSkillLevel(skillId, level));

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER
                && player instanceof ServerPlayerEntity serverPlayer) {
            SkillMmoServerNetworking.sendPlayerSkills(serverPlayer);
        }
    }

    public void setSkillLevel(PlayerEntity player, Skill skill, int level) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData().setSkillLevel(skill.getId(), level);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER
                && player instanceof ServerPlayerEntity serverPlayer) {
            SkillMmoServerNetworking.sendPlayerSkills(serverPlayer);
        }
    }

    public boolean chooseSkillLevel(PlayerEntity player, Skill skill) {
        int currentLevel = getSkillLevel(player, skill);
        if (currentLevel >= SkillLevel.MAX_LEVEL) {
            return false;
        }

        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        if (!skillMmoPlayerDataHolder.getSkillMmoPlayerData().consumeAvailableSkillPoints()) {
            return false;
        }

        setSkillLevel(player, skill, currentLevel + 1);
        return true;
    }
}
