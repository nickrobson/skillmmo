package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerSkillUnlockManager {
    private static final PlayerSkillUnlockManager instance = new PlayerSkillUnlockManager();

    public static PlayerSkillUnlockManager getInstance() {
        return instance;
    }

    private PlayerSkillUnlockManager() {
    }

    public void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!hasItemUnlock(player, itemStack)) {
                reportLocked(player, itemStack.getItem());
                return ActionResult.FAIL;
            }

            // If the block has a block entity, and the player doesn't have the necessary skill for the block, deny the interaction
            if (blockState.hasBlockEntity() && !hasBlockUnlock(player, blockState)) {
                reportLocked(player, blockState.getBlock());
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!hasItemUnlock(player, itemStack)) {
                reportLocked(player, itemStack.getItem());
                return TypedActionResult.fail(itemStack);
            }

            return TypedActionResult.pass(itemStack);
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStack)) {
                reportLocked(player, itemStack.getItem());
                return ActionResult.FAIL;
            }

            // If the player doesn't have the necessary skill for the entity, deny the interaction
            if (!PlayerSkillUnlockManager.getInstance().hasEntityUnlock(player, entity)) {
                reportLocked(player, entity);
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });
    }

    public boolean hasBlockUnlock(@Nullable PlayerEntity player, BlockState blockState) {
        return hasBlockUnlock(player, blockState.getBlock());
    }

    public boolean hasBlockUnlock(@Nullable PlayerEntity player, Block block) {
        Identifier blockIdentifier = Registry.BLOCK.getId(block);
        return PlayerSkillUnlockManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.BLOCK,
                blockIdentifier
        );
    }

    public boolean hasItemUnlock(@Nullable PlayerEntity player, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return true;
        }

        if (itemStack.getItem() instanceof BlockItem blockItem) {
            return hasBlockUnlock(player, blockItem.getBlock());
        }

        Identifier itemIdentifier = Registry.ITEM.getId(itemStack.getItem());
        return PlayerSkillUnlockManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.ITEM,
                itemIdentifier
        );
    }

    public boolean hasEntityUnlock(@Nullable PlayerEntity player, Entity entity) {
        Identifier entityIdentifier = Registry.ENTITY_TYPE.getId(entity.getType());
        return PlayerSkillUnlockManager.getInstance().hasUnlock(
                player,
                SkillLevelUnlockType.ENTITY,
                entityIdentifier
        );
    }

    private boolean hasUnlock(@Nullable PlayerEntity player, @Nonnull SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        if (player == null) {
            return false;
        }

        if (player.isCreative() || player.isSpectator()) {
            return true;
        }

        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlockType, unlockIdentifier);
        if (skillLevelSet.isEmpty()) {
            // If no skill levels affect the item, it's allowed!
            return true;
        }

        Predicate<SkillLevel> hasSkillLevel = level ->
                PlayerSkillManager.getInstance().hasSkillLevel(player, level.getSkill(), level.getLevel());

        boolean requireAllLockingSkillsToBeUnlocked = SkillMmoConfig.getConfig().requireAllLockingSkillsToBeUnlocked;
        return requireAllLockingSkillsToBeUnlocked
                ? skillLevelSet.stream().allMatch(hasSkillLevel)
                : skillLevelSet.stream().anyMatch(hasSkillLevel);
    }

    public void reportLocked(@Nullable PlayerEntity player, @Nonnull Block block) {
        Identifier identifier = Registry.BLOCK.getId(block);
        reportLocked(player, SkillLevelUnlockType.BLOCK, identifier, block);
    }

    public void reportLocked(@Nullable PlayerEntity player, @Nonnull Item item) {
        Identifier identifier = Registry.ITEM.getId(item);
        reportLocked(player, SkillLevelUnlockType.ITEM, identifier, item);
    }

    public void reportLocked(@Nullable PlayerEntity player, @Nonnull Entity entity) {
        Identifier identifier = Registry.ENTITY_TYPE.getId(entity.getType());
        reportLocked(player, SkillLevelUnlockType.ENTITY, identifier, entity);
    }

    private void reportLocked(
            @Nullable PlayerEntity player,
            @Nonnull SkillLevelUnlockType unlockType,
            @Nonnull Identifier identifier,
            @Nullable Object maybeSkillDenyCustomizable) {
        if (player == null) {
            return;
        }

        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlockType, identifier);
        if (skillLevelSet.isEmpty()) {
            return;
        }

        // Find the level the player is closest to reaching
        SkillLevel skillLevel = getClosestLevel(player, skillLevelSet);
        int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, skillLevel.getSkill());

        Text text = getDenyText(skillLevel, unlockType, identifier);
        player.sendMessage(text, true);

        if (maybeSkillDenyCustomizable instanceof SkillDenyCustomizable skillDenyCustomizable) {
            skillDenyCustomizable.onDeny(player, skillLevel, playerLevel);
        }
    }

    private SkillLevel getClosestLevel(PlayerEntity player, Collection<SkillLevel> skillLevelSet) {
        return skillLevelSet
                .stream()
                .min(Comparator.comparing(lvl -> {
                    int level = lvl.getLevel();
                    int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, lvl.getSkill());
                    return level - playerLevel;
                }))
                .orElse(null);
    }

    private Text getDenyText(SkillLevel skillLevel, SkillLevelUnlockType unlockType, Identifier identifier) {
        Text skillName = skillLevel.getSkill().getNameText();
        int level = skillLevel.getLevel();
        Text thingText = getThingText(unlockType, identifier);

        return switch (unlockType) {
            case BLOCK -> new TranslatableText("skillmmo.feedback.deny.block", skillName, level, thingText);
            case ITEM -> new TranslatableText("skillmmo.feedback.deny.item", skillName, level, thingText);
            case ENTITY -> new TranslatableText("skillmmo.feedback.deny.entity", skillName, level, thingText);
        };
    }

    private Text getThingText(SkillLevelUnlockType unlockType, Identifier identifier) {
        return switch (unlockType) {
            case BLOCK -> Registry.BLOCK.get(identifier).getName();
            case ITEM -> Registry.ITEM.get(identifier).getName();
            case ENTITY -> Registry.ENTITY_TYPE.get(identifier).getName();
        };
    }
}
