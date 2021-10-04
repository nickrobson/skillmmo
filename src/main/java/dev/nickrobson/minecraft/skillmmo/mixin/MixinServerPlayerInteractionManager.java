package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevelUnlockType;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {
    @Inject(
            method = "interactItem",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;"
            ),
            cancellable = true)
    public void interactItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        Identifier blockIdentifier = Registry.ITEM.getId(stack.getItem());
        if (!PlayerSkillManager.getInstance().canInteract(player, SkillLevelUnlockType.ITEM, blockIdentifier)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(
            method = "interactBlock",
            at = {
                    @At(
                            value = "INVOKE",
                            shift = At.Shift.BEFORE,
                            target = "Lnet/minecraft/block/BlockState;onUse(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"
                    ),
                    @At(
                            value = "INVOKE",
                            shift = At.Shift.BEFORE,
                            target = "Lnet/minecraft/item/ItemStack;useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;"
                    )
            },
            cancellable = true)
    public void interactBlock(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        BlockState blockState = world.getBlockState(hitResult.getBlockPos());
        Identifier blockIdentifier = Registry.BLOCK.getId(blockState.getBlock());
        if (!PlayerSkillManager.getInstance().canInteract(player, SkillLevelUnlockType.BLOCK, blockIdentifier)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
