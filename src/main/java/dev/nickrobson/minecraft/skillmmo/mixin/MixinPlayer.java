package dev.nickrobson.minecraft.skillmmo.mixin;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerData;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataRaw;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;

@Mixin(Player.class)
public abstract class MixinPlayer implements SkillMmoPlayerDataHolder {
    @Unique
    private static final String SKILLMMO_DATA_KEY = "skillMmo";

    @Unique
    private SkillMmoPlayerData skillMmo$playerData = null;

    @Shadow
    @Final
    Inventory inventory;

    @Inject(
            method = "readAdditionalSaveData",
            at = @At(value = "TAIL")
    )
    public void skillMmo$readNbtData(ValueInput view, CallbackInfo ci) {
        if (!SkillMmoMod.isModEnabled) {
            return;
        }

        this.skillMmo$playerData = view.read(SKILLMMO_DATA_KEY, SkillMmoPlayerDataRaw.CODEC)
                .map(SkillMmoPlayerData::new)
                .orElseGet(SkillMmoPlayerData::new);
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At(value = "TAIL")
    )
    public void skillMmo$writeNbtData(ValueOutput view, CallbackInfo ci) {
        if (!SkillMmoMod.isModEnabled) {
            return;
        }

        SkillMmoPlayerData playerData = this.skillMmo$getPlayerData();
        view.store(SKILLMMO_DATA_KEY, SkillMmoPlayerDataRaw.CODEC, playerData.toRaw());
    }

    @Unique
    @Nonnull
    @Override
    public SkillMmoPlayerData skillMmo$getPlayerData() {
        return skillMmo$playerData != null
                ? skillMmo$playerData
                : SkillMmoPlayerData.UNINITIALISED;
    }

    @Unique
    @Override
    public void skillMmo$setPlayerData(@Nonnull SkillMmoPlayerData playerData) {
        this.skillMmo$playerData = playerData;
    }

    // This prevents blocks from dropping items when you haven't unlocked them
    @Inject(
            method = "hasCorrectToolForDrops",
            at = @At("HEAD"),
            cancellable = true
    )
    public void skillMmo$canHarvest(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this; // safe as this is a mixin for PlayerEntity
        ItemStack itemStackInHand = player.getMainHandItem();
        if (!PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStackInHand) && !(itemStackInHand.getItem() instanceof BlockItem)) {
            PlayerSkillUnlockManager.getInstance().reportItemUseLocked(player, itemStackInHand.getItem());
            cir.setReturnValue(false);
        } else if (!PlayerSkillUnlockManager.getInstance().hasBlockUnlock(player, state)) {
            if (SkillMmoConfig.getConfig().announceRequiredSkillWhenBreakingBlock) {
                // Only announce what skill is required to break a certain block if configured – it can be quite verbose
                PlayerSkillUnlockManager.getInstance().reportBlockBreakLocked(player, state.getBlock());
            }
            cir.setReturnValue(false);
        }
    }
}
