package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class MixinFarmlandBlock {
    @Shadow
    private static boolean hasCrop(BlockView world, BlockPos pos) {
        return false;
    }

    @Inject(
            method = "onLandedUpon",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void skillMmo$onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        // We only care if there's a crop AND it's a player doing the trampling
        if (!hasCrop(world, pos) || !(entity instanceof PlayerEntity player)) {
            return;
        }

        // Don't drop the crop if the player is missing the required skill
        BlockPos cropBlockPos = pos.up();
        BlockState blockState = world.getBlockState(cropBlockPos);
        if (!PlayerSkillUnlockManager.getInstance().hasBlockUnlock(player, blockState)) {
            world.breakBlock(cropBlockPos, false, player);
        }
    }
}
