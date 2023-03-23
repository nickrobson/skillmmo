package dev.nickrobson.minecraft.skillmmo.skill.unlock;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
import dev.nickrobson.minecraft.skillmmo.api.interaction.Interaction;
import dev.nickrobson.minecraft.skillmmo.api.interaction.VanillaInteractionTypes;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.interaction.InteractionHelper;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillDenyCustomizable;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
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
            // In the future, this could be improved to allow e.g. opening blocks with inventories with whatever items (since they don't affect the interaction)
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

        UseEntityCallback.EVENT.register(
                (player, world, hand, entity, hitResult) ->
                        handleEntityInteraction(player, hand, entity));
    }

    // FIXME - temporary while Fabric UseEntityCallback doesn't handle "normal" entity interactions
    public ActionResult handleEntityInteraction(PlayerEntity player, Hand hand, Entity entity) {
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
    }

    public boolean hasBlockUnlock(@Nullable PlayerEntity player, BlockState blockState) {
        if (blockState.isAir()) {
            return true;
        }

        return hasBlockUnlock(player, blockState.getBlock());
    }

    public boolean hasBlockUnlock(@Nullable PlayerEntity player, Block block) {
        if (block instanceof AirBlock) {
            return true;
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forBlock(block));
    }

    public boolean hasItemUnlock(@Nullable PlayerEntity player, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return true;
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forItemStack(itemStack));
    }

    public boolean hasItemUnlock(@Nullable PlayerEntity player, Item item) {
        if (item == Items.AIR) {
            return true;
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forItem(item));
    }

    public boolean hasEntityUnlock(@Nullable PlayerEntity player, Entity entity) {
        if (entity instanceof ItemEntity itemEntity) {
            return PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemEntity.getStack());
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forEntity(entity));
    }

    private boolean hasUnlock(@Nullable PlayerEntity player, Unlockable<?> unlockable) {
        if (!SkillMmoMod.isModEnabled) {
            return true;
        }

        if (player == null) {
            return false;
        }

        if (player.isCreative() || player.isSpectator()) {
            return true;
        }

        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlockable);
        if (skillLevelSet.isEmpty()) {
            // If no skill levels affect the item, it's allowed!
            return true;
        }

        Predicate<SkillLevel> hasSkillLevel = level -> PlayerSkillManager.getInstance().hasSkillLevel(player, level.getSkill(), level.getLevel());

        boolean requireAllLockingSkillsToBeUnlocked = SkillMmoConfig.getConfig().requireAllLockingSkillsToBeUnlocked;
        return requireAllLockingSkillsToBeUnlocked
                ? skillLevelSet.stream().allMatch(hasSkillLevel)
                : skillLevelSet.stream().anyMatch(hasSkillLevel);
    }

    public boolean hasRecipeUnlock(@Nonnull PlayerEntity player, Recipe<?> recipe) {
        boolean someIngredientsAreFullyLocked = recipe.getIngredients().stream()
                .anyMatch(ingredient -> {
                    ItemStack[] matchingStacks = ingredient.getMatchingStacks();
                    if (matchingStacks.length == 0) {
                        return false;
                    }
                    return Arrays.stream(matchingStacks)
                            .noneMatch(itemStack -> PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStack));
                });
        boolean outputIsLocked = !PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, recipe.getOutput(player.getWorld().getRegistryManager()));
        return !someIngredientsAreFullyLocked && !outputIsLocked;
    }

    public void reportBlockBreakLocked(@Nullable PlayerEntity player, Block block) {
        reportInteractLocked(player, InteractionHelper.forBlock(block, VanillaInteractionTypes.BLOCK_BREAK), block, block);
    }

    public void reportBlockInteractLocked(@Nullable PlayerEntity player, Block block) {
        reportInteractLocked(player, InteractionHelper.forBlock(block, VanillaInteractionTypes.BLOCK_INTERACT), block, block);
    }

    public void reportItemUseLocked(@Nullable PlayerEntity player, Item item) {
        reportItemUseLocked(player, item, null);
    }

    public void reportItemUseLocked(@Nullable PlayerEntity player, Item item, @Nullable SkillDenyCustomizable skillDenyCustomizable) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            reportInteractLocked(player, InteractionHelper.forBlock(block, VanillaInteractionTypes.BLOCK_PLACE), block, skillDenyCustomizable != null ? skillDenyCustomizable : block);
        } else {
            reportInteractLocked(player, InteractionHelper.forItem(item, VanillaInteractionTypes.ITEM_USE), item, skillDenyCustomizable != null ? skillDenyCustomizable : item);
        }
    }

    public void reportEntityInteractLocked(@Nullable PlayerEntity player, Entity entity) {
        reportInteractLocked(
                player,
                InteractionHelper.forEntity(entity, VanillaInteractionTypes.ENTITY_INTERACT),
                entity.getType(),
                entity
        );
    }

    private <T> void reportInteractLocked(
            @Nullable PlayerEntity player,
            Interaction<T> interaction,
            T target,
            @Nullable Object maybeSkillDenyCustomizable) {
        if (player == null) {
            return;
        }

        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(interaction.toUnlockable());
        if (skillLevelSet.isEmpty()) {
            return;
        }

        // Find the level the player is closest to reaching
        SkillLevel skillLevel = PlayerSkillManager.getInstance().getClosestLevel(player, skillLevelSet);

        Text text = null;
        if (maybeSkillDenyCustomizable instanceof SkillDenyCustomizable skillDenyCustomizable) {
            int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, skillLevel.getSkill());
            text = skillDenyCustomizable.onDeny(player, skillLevel, playerLevel);
        }
        if (text == null) {
            Text skillName = skillLevel.getSkill().getName();
            int level = skillLevel.getLevel();
            text = interaction.getDenyText(target, skillName, level);
        }
        player.sendMessage(text, true);
    }
}
