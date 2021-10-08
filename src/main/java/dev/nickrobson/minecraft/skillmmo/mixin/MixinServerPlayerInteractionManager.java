package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
    @Redirect(
            method = "interactItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
    )
    public TypedActionResult<ItemStack> useItem(ItemStack itemStack, World world, PlayerEntity user, Hand hand) {
        // If the player has the necessary skill level, continue as normal
        if (PlayerSkillManager.getInstance().hasItemUnlock(user, itemStack)) {
            return itemStack.use(world, user, hand);
        }
        // If the player doesn't have the necessary skill, deny the interaction
        return TypedActionResult.fail(itemStack);
    }

    @Redirect(
            method = "interactBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onUse(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;")
    )
    public ActionResult useBlock(BlockState blockState, World world, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // If the block has no block entity, ignore this interaction
        if (!blockState.hasBlockEntity()) {
            return blockState.onUse(world, player, hand, hit);
        }

        // If the player has the necessary skill level, continue as normal
        if (PlayerSkillManager.getInstance().hasBlockUnlock(player, blockState)) {
            return blockState.onUse(world, player, hand, hit);
        }

        // If the player doesn't have the necessary skill, deny the interaction
        return ActionResult.FAIL;
    }

    @Redirect(
            method = "interactBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;")
    )
    public ActionResult interactBlock(ItemStack itemStack, ItemUsageContext context) {
        // If the player has the necessary skill level, continue as normal
        if (PlayerSkillManager.getInstance().hasItemUnlock(context.getPlayer(), itemStack)) {
            return itemStack.useOnBlock(context);
        }

        // If the player doesn't have the necessary skill, deny the interaction
        return ActionResult.FAIL;
    }

    @Redirect(
            method = "tryBreakBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V")
    )
    public void onBreakBlock(Block block, World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        // If the player has the necessary skill level, continue as normal
        //  if (!PlayerSkillManager.getInstance().hasItemUnlock(player, stack)) {
            // If the player doesn't have the skill needed to harvest the block, pretend it's being broken without the item for the sake of drops
        // stack = ItemStack.EMPTY;
            // }

        // TODO - can we (should we?) fully prevent the block that was broken from dropping
        //  while keeping the drops from any block entity? i.e. for a chest that means keep chest contents but not chest
        block.afterBreak(world, player, pos, state, blockEntity, stack);
    }
}
