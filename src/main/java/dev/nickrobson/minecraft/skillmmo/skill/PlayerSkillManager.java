package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
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

    public static void register() {
        // When players respawn, we need to copy over all their data
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            SkillMmoPlayerDataHolder oldPlayerDataHolder = (SkillMmoPlayerDataHolder) oldPlayer;
            SkillMmoPlayerDataHolder newPlayerDataHolder = (SkillMmoPlayerDataHolder) newPlayer;
            newPlayerDataHolder.setSkillMmoPlayerData(oldPlayerDataHolder.getSkillMmoPlayerData().clone());
            SkillMmoServerNetworking.sendPlayerData(newPlayer);
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!PlayerSkillManager.getInstance().hasItemUnlock(player, itemStack)) {
                return ActionResult.FAIL;
            }

            // If the block has a block entity, and the player doesn't have the necessary skill for the block, deny the interaction
            if (blockState.hasBlockEntity() && !PlayerSkillManager.getInstance().hasBlockUnlock(player, blockState)) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!PlayerSkillManager.getInstance().hasItemUnlock(player, itemStack)) {
                return TypedActionResult.fail(itemStack);
            }

            return TypedActionResult.pass(itemStack);
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!PlayerSkillManager.getInstance().hasItemUnlock(player, itemStack)) {
                return ActionResult.FAIL;
            }

            // If the player doesn't have the necessary skill for the entity, deny the interaction
            if (!PlayerSkillManager.getInstance().hasEntityUnlock(player, entity)) {
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });
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

    public int getAvailableSkillPoints(PlayerEntity player) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        return skillMmoPlayerDataHolder.getSkillMmoPlayerData().getAvailableSkillPoints();
    }

    public void setAvailableSkillPoints(ClientPlayerEntity player, int availableSkillPoints) {
        SkillMmoPlayerDataHolder skillMmoPlayerDataHolder = (SkillMmoPlayerDataHolder) player;
        skillMmoPlayerDataHolder.getSkillMmoPlayerData().setAvailableSkillPoints(availableSkillPoints);
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

        if (player.isCreative()) {
            return true;
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
        return hasBlockUnlock(player, blockState.getBlock());
    }

    public boolean hasBlockUnlock(@Nullable PlayerEntity player, Block block) {
        Identifier blockIdentifier = Registry.BLOCK.getId(block);
        return PlayerSkillManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.BLOCK,
                blockIdentifier
        );
    }

    public boolean hasItemUnlock(@Nullable PlayerEntity player, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) itemStack.getItem()).getBlock();
            return hasBlockUnlock(player, block);
        }

        Identifier itemIdentifier = Registry.ITEM.getId(itemStack.getItem());
        return PlayerSkillManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.ITEM,
                itemIdentifier
        );
    }

    public boolean hasEntityUnlock(@Nullable PlayerEntity player, Entity entity) {
        Identifier entityIdentifier = Registry.ENTITY_TYPE.getId(entity.getType());
        return PlayerSkillManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.ENTITY,
                entityIdentifier
        );
    }
}
