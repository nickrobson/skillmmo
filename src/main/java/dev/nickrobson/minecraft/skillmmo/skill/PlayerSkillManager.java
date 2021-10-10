package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
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

    private PlayerSkillManager() {}

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
                && player instanceof ServerPlayerEntity) {
            SkillMmoServerNetworking.sendPlayerSkills((ServerPlayerEntity) player);
        }
    }

    public void setSkillLevel(PlayerEntity player, Skill skill, int level) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData().setSkillLevel(skill.getId(), level);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER
                && player instanceof ServerPlayerEntity) {
            SkillMmoServerNetworking.sendPlayerSkills((ServerPlayerEntity) player);
        }
    }

    public int getAvailableSkillPoints(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getAvailableSkillPoints();
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

    public boolean hasUnlock(@Nullable PlayerEntity player, SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        if (player == null) {
            return false;
        }

        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlockType, unlockIdentifier);
        if (skillLevelSet.isEmpty()) {
            // If no skill levels affect the item, it's allowed!
            return true;
        }

        Predicate<SkillLevel> hasSkillLevel = level -> this.hasSkillLevel(player, level.getSkill(), level.getLevel());

        boolean requireAllLockingSkillsToBeUnlocked = SkillMmoConfig.getConfig().requireAllLockingSkillsToBeUnlocked;
        return requireAllLockingSkillsToBeUnlocked
                ? skillLevelSet.stream().allMatch(hasSkillLevel)
                : skillLevelSet.stream().anyMatch(hasSkillLevel);
    }

    public boolean hasBlockUnlock(@Nullable PlayerEntity player, BlockState blockState) {
        Identifier blockIdentifier = Registry.BLOCK.getId(blockState.getBlock());
        return PlayerSkillManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.BLOCK,
                blockIdentifier
        );
    }

    public boolean hasItemUnlock(@Nullable PlayerEntity player, ItemStack itemStack) {
        Identifier itemIdentifier = Registry.ITEM.getId(itemStack.getItem());
        return PlayerSkillManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.ITEM,
                itemIdentifier
        );
    }
}
