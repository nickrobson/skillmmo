package dev.nickrobson.minecraft.skillmmo.skill.unlock;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PlayerSkillUnlockManager {
    private static final Logger logger = LogManager.getLogger(PlayerSkillUnlockManager.class);

    private static final PlayerSkillUnlockManager instance = new PlayerSkillUnlockManager();

    public static PlayerSkillUnlockManager getInstance() {
        return instance;
    }

    private PlayerSkillUnlockManager() {
    }

    public void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockState blockState = world.getBlockState(hitResult.getBlockPos());
            ItemStack itemStack = player.getItemInHand(hand);

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
                return InteractionResult.FAIL;
            }

            // If the player doesn't have the necessary skill for the block, check what the player is trying to do
            if (!hasBlockUnlock(player, blockState)) {
                if (itemStack.getItem() instanceof BlockItem) {
                    // If the player is trying to place a block against a block, allow the interaction
                    if (player.isSecondaryUseActive()) {
                        return InteractionResult.PASS;
                    }

                    // Deny interaction with blocks that have been marked as interaction-restricted
                    if (blockState.is(SkillMmoTags.interactableBlocks)) {
                        reportBlockInteractLocked(player, blockState.getBlock());
                        return InteractionResult.FAIL;
                    }

                    return InteractionResult.PASS;
                }

                reportBlockInteractLocked(player, blockState.getBlock());
                return InteractionResult.FAIL;
            }

            return InteractionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getItemInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!hasItemUnlock(player, itemStack)) {
                reportItemUseLocked(player, itemStack.getItem());
                return InteractionResult.FAIL;
            }

            return InteractionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack itemStack = player.getItemInHand(hand);

            // If the player doesn't have the necessary skill for the item they're holding, deny the interaction
            if (!hasItemUnlock(player, itemStack)) {
                reportItemUseLocked(player, itemStack.getItem());
                return InteractionResult.FAIL;
            }

            // If the player doesn't have the necessary skill for the entity, deny the interaction
            if (!hasEntityUnlock(player, entity)) {
                reportEntityInteractLocked(player, entity);
                return InteractionResult.FAIL;
            }

            return InteractionResult.PASS;
        });
    }

    public boolean hasBlockUnlock(@Nullable Player player, BlockState blockState) {
        if (blockState.isAir()) {
            return true;
        }

        return hasBlockUnlock(player, blockState.getBlock());
    }

    public boolean hasBlockUnlock(@Nullable Player player, Block block) {
        if (block instanceof AirBlock) {
            return true;
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forBlock(block));
    }

    public boolean hasItemUnlock(@Nullable Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return true;
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forItemStack(itemStack));
    }

    public boolean hasItemUnlock(@Nullable Player player, Item item) {
        if (item == Items.AIR) {
            return true;
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forItem(item));
    }

    public boolean hasEntityUnlock(@Nullable Player player, Entity entity) {
        if (entity instanceof ItemEntity itemEntity) {
            return PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemEntity.getItem());
        }

        return PlayerSkillUnlockManager.getInstance().hasUnlock(player, VanillaUnlockables.forEntity(entity));
    }

    private boolean hasUnlock(@Nullable Player player, Unlockable<?> unlockable) {
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

    public boolean hasRecipeUnlock(Player player, RecipeHolder<?> recipe) {
        // FIXME: relying on recipe displays probably isn't the right way to do this
        //  but, I think the "right" way would be to fake a craft, and that seems to be really difficult to implement
        //  buuuut... it works... so... that'll be what we do for now
        boolean anyIngredientIsFullyLocked = recipe.value().placementInfo().ingredients().stream()
                .anyMatch(ingredient ->
                        !ingredient.items()
                                .map(Holder::value)
                                .allMatch(item -> hasItemUnlock(player, item)));
        boolean outputIsLocked = recipe.value().display().stream()
                .anyMatch(recipeDisplay -> !hasSlotDisplayUnlocked(player, recipeDisplay.result()));

        return !anyIngredientIsFullyLocked && !outputIsLocked;
    }

    private boolean hasSlotDisplayUnlocked(Player player, SlotDisplay slotDisplay) {
        switch (slotDisplay) {
            case SlotDisplay.ItemSlotDisplay itemSlotDisplay -> {
                return PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemSlotDisplay.item().value());
            }
            case SlotDisplay.ItemStackSlotDisplay stackSlotDisplay -> {
                return PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, stackSlotDisplay.stack());
            }
            case SlotDisplay.WithRemainder withRemainderSlotDisplay -> {
                // we only care about the input, not the remainder here
                return hasSlotDisplayUnlocked(player, withRemainderSlotDisplay.input());
            }
            case SlotDisplay.Composite compositeSlotDisplay -> {
                // CompositeSlotDisplay represents "one of many items"
                return compositeSlotDisplay.contents().stream().anyMatch(
                        innerSlotDisplay -> hasSlotDisplayUnlocked(player, innerSlotDisplay)
                );
            }
            case SlotDisplay.TagSlotDisplay tagSlotDisplay -> {
                // TagSlotDisplay represents "one of many items"
                return BuiltInRegistries.ITEM.get(tagSlotDisplay.tag()).stream()
                        .flatMap(HolderSet.ListBacked::stream)
                        .anyMatch(
                                item -> PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, item.value())
                        );
            }
            case SlotDisplay.Empty ignored -> {
                return true; // empty slots don't matter
            }
            case SlotDisplay.AnyFuel ignored -> {
                return true; // fuel slots don't count towards unlocks
            }
            case SlotDisplay.SmithingTrimDemoSlotDisplay smithingTrimSlotDisplay -> {
                return true; // smithing trim slots don't count towards unlocks
            }
            default -> {
                logger.warn("Unsupported slot display type: {}", slotDisplay.getClass().getSimpleName());
                return true;
            }
        }
    }

    public void reportBlockBreakLocked(@Nullable Player player, Block block) {
        reportInteractLocked(player, InteractionHelper.forBlock(block, VanillaInteractionTypes.BLOCK_BREAK), block, block);
    }

    public void reportBlockInteractLocked(@Nullable Player player, Block block) {
        reportInteractLocked(player, InteractionHelper.forBlock(block, VanillaInteractionTypes.BLOCK_INTERACT), block, block);
    }

    public void reportItemUseLocked(@Nullable Player player, Item item) {
        reportItemUseLocked(player, item, null);
    }

    public void reportItemUseLocked(@Nullable Player player, Item item, @Nullable SkillDenyCustomizable skillDenyCustomizable) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            reportInteractLocked(player, InteractionHelper.forBlock(block, VanillaInteractionTypes.BLOCK_PLACE), block, skillDenyCustomizable != null ? skillDenyCustomizable : block);
        } else {
            reportInteractLocked(player, InteractionHelper.forItem(item, VanillaInteractionTypes.ITEM_USE), item, skillDenyCustomizable != null ? skillDenyCustomizable : item);
        }
    }

    public void reportEntityInteractLocked(@Nullable Player player, Entity entity) {
        reportInteractLocked(
                player,
                InteractionHelper.forEntity(entity, VanillaInteractionTypes.ENTITY_INTERACT),
                entity.getType(),
                entity
        );
    }

    private <T> void reportInteractLocked(
            @Nullable Player player,
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

        Component text = null;
        if (maybeSkillDenyCustomizable instanceof SkillDenyCustomizable skillDenyCustomizable) {
            int playerLevel = PlayerSkillManager.getInstance().getSkillLevel(player, skillLevel.getSkill());
            text = skillDenyCustomizable.skillMmo$onDeny(player, skillLevel, playerLevel);
        }
        if (text == null) {
            Component skillName = skillLevel.getSkill().getName();
            int level = skillLevel.getLevel();
            text = interaction.getDenyText(target, skillName, level);
        }
        player.displayClientMessage(text, true);
    }
}
