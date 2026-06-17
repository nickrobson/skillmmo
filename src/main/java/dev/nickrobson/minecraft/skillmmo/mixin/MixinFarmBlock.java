package dev.nickrobson.minecraft.skillmmo.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;

@Mixin(FarmBlock.class)
public abstract class MixinFarmBlock {
    @Shadow
    private static boolean shouldMaintainFarmland(BlockGetter world, BlockPos pos) {
        return false;
    }

    @Inject(
            method = "fallOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
            )
    )
    public void skillMmo$onLandedUpon(Level world, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo ci) {
        // We only care if there's a crop AND it's a player doing the trampling
        if (!shouldMaintainFarmland(world, pos) || !(entity instanceof Player player)) {
            return;
        }

        // Don't drop the crop if the player is missing the required skill
        BlockPos cropBlockPos = pos.above();
        BlockState blockState = world.getBlockState(cropBlockPos);
        if (!PlayerSkillUnlockManager.getInstance().hasBlockUnlock(player, blockState)) {
            world.destroyBlock(cropBlockPos, false, player);
        }
    }
}
