package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
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

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction.
            // This isn't the most correct check for this, since there's no reason why you shouldn't able to
            //     right-click stone with an iron axe, but it's one of those things where it's incredibly difficult
            //     to get this right due to all the quirks around shift-right-click, interactable items,
            //     tools (e.g. stripping logs, making paths, and using a shield), etc., and it'd probably require
            //     implementing fake player/world interactions to create a truly correct solution, so... doing it
            //     like this is fine. In theory, players probably won't be able to acquire the locked tools anyway,
            //     unless they find them in chests, so this is the lesser of the two evils between
            //     blocking everything (this solution) and aiming for perfect correctness with weird edge cases.
            if (!hasItemUnlock(player, itemStack)) {
                reportItemUseLocked(player, itemStack.getItem());
                return ActionResult.FAIL;
            }

            // If the player doesn't have the necessary skill for the block, check what the player is trying to do
            if (!hasBlockUnlock(player, blockState)) {
                if (itemStack.getItem() instanceof BlockItem) {
                    // If the player is trying to place a block against a block, allow the interaction
                    if (player.shouldCancelInteraction()) {
                        return ActionResult.PASS;
                    }

                    // Deny interaction with blocks that have been marked as interaction-restricted
                    if (blockState.isIn(SkillMmoTags.interactableBlocks)) {
                        reportBlockInteractLocked(player, blockState.getBlock());
                        return ActionResult.FAIL;
                    }

                    return ActionResult.PASS;
                }

                reportBlockInteractLocked(player, blockState.getBlock());
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!hasItemUnlock(player, itemStack)) {
                reportItemUseLocked(player, itemStack.getItem());
                return TypedActionResult.fail(itemStack);
            }

            return TypedActionResult.pass(itemStack);
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack itemStack = player.getStackInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!hasItemUnlock(player, itemStack)) {
                reportItemUseLocked(player, itemStack.getItem());
                return ActionResult.FAIL;
            }

            // If the player doesn't have the necessary skill for the entity, deny the interaction
            if (!hasEntityUnlock(player, entity)) {
                reportEntityInteractLocked(player, entity);
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
                UnlockType.BLOCK,
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
                UnlockType.ITEM,
                itemIdentifier
        );
    }

    public boolean hasEntityUnlock(@Nullable PlayerEntity player, Entity entity) {
        Identifier entityIdentifier = Registry.ENTITY_TYPE.getId(entity.getType());
        return PlayerSkillUnlockManager.getInstance().hasUnlock(
                player,
                UnlockType.ENTITY,
                entityIdentifier
        );
    }

    private boolean hasUnlock(@Nullable PlayerEntity player, UnlockType unlockType, Identifier unlockIdentifier) {
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

    public void reportBlockBreakLocked(@Nullable PlayerEntity player, Block block) {
        Identifier identifier = Registry.BLOCK.getId(block);
        reportInteractLocked(player, Interaction.BLOCK_BREAK, identifier, block);
    }

    public void reportBlockInteractLocked(@Nullable PlayerEntity player, Block block) {
        Identifier identifier = Registry.BLOCK.getId(block);
        reportInteractLocked(player, Interaction.BLOCK_INTERACT, identifier, block);
    }

    public void reportItemUseLocked(@Nullable PlayerEntity player, Item item) {
        if (item instanceof BlockItem blockItem) {
            Identifier identifier = Registry.BLOCK.getId(blockItem.getBlock());
            reportInteractLocked(player, Interaction.BLOCK_PLACE, identifier, item);
        } else {
            Identifier identifier = Registry.ITEM.getId(item);
            reportInteractLocked(player, Interaction.ITEM_USE, identifier, item);
        }
    }

    public void reportEntityInteractLocked(@Nullable PlayerEntity player, Entity entity) {
        Identifier identifier = Registry.ENTITY_TYPE.getId(entity.getType());
        reportInteractLocked(player, Interaction.ENTITY_INTERACT, identifier, entity);
    }

    private void reportInteractLocked(
            @Nullable PlayerEntity player,
            Interaction interaction,
            Identifier identifier,
            @Nullable Object maybeSkillDenyCustomizable) {
        if (player == null) {
            return;
        }

        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(interaction.unlockType, identifier);
        if (skillLevelSet.isEmpty()) {
            return;
        }

        // Find the level the player is closest to reaching
        SkillLevel skillLevel = getClosestLevel(player, skillLevelSet);
        int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, skillLevel.getSkill());

        Text text = null;
        if (maybeSkillDenyCustomizable instanceof SkillDenyCustomizable skillDenyCustomizable) {
            text = skillDenyCustomizable.onDeny(player, skillLevel, playerLevel);
        }
        if (text == null) {
            text = getDenyText(skillLevel, interaction, identifier);
        }
        player.sendMessage(text, true);
    }

    private SkillLevel getClosestLevel(PlayerEntity player, Collection<SkillLevel> skillLevelSet) {
        return skillLevelSet
                .stream()
                .filter(lvl -> !PlayerSkillManager.getInstance().hasSkillLevel(player, lvl.getSkill(), lvl.getLevel()))
                .min(Comparator.comparing(lvl -> {
                    int level = lvl.getLevel();
                    int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, lvl.getSkill());
                    return level - playerLevel;
                }))
                .orElse(null);
    }

    private Text getDenyText(SkillLevel skillLevel, Interaction interaction, Identifier identifier) {
        Text skillName = skillLevel.getSkill().getNameText();
        int level = skillLevel.getLevel();
        Text thingName = getThingName(interaction.unlockType, identifier);

        return switch (interaction) {
            case BLOCK_BREAK -> new TranslatableText("skillmmo.feedback.deny.block.break", skillName, level, thingName);
            case BLOCK_INTERACT -> new TranslatableText("skillmmo.feedback.deny.block.interact", skillName, level, thingName);
            case BLOCK_PLACE -> new TranslatableText("skillmmo.feedback.deny.block.place", skillName, level, thingName);
            case ITEM_USE -> new TranslatableText("skillmmo.feedback.deny.item.use", skillName, level, thingName);
            case ENTITY_INTERACT -> new TranslatableText("skillmmo.feedback.deny.entity.interact", skillName, level, thingName);
        };
    }

    private Text getThingName(UnlockType unlockType, Identifier identifier) {
        return switch (unlockType) {
            case BLOCK -> Registry.BLOCK.get(identifier).getName();
            case ITEM -> Registry.ITEM.get(identifier).getName();
            case ENTITY -> Registry.ENTITY_TYPE.get(identifier).getName();
        };
    }

    public enum Interaction {
        BLOCK_BREAK(UnlockType.BLOCK),
        BLOCK_INTERACT(UnlockType.BLOCK),
        BLOCK_PLACE(UnlockType.BLOCK),
        ITEM_USE(UnlockType.ITEM),
        ENTITY_INTERACT(UnlockType.ENTITY);

        public final UnlockType unlockType;

        Interaction(UnlockType unlockType) {
            this.unlockType = unlockType;
        }
    }
}
