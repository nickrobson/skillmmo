package dev.nickrobson.minecraft.skillmmo.mixin.client;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.UnlockTooltipHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Inject(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    ordinal = 0,
                    target = "Lnet/minecraft/item/ItemStack;getHideFlags()I"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void skillMmo$getTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        if (player == null) {
            return;
        }

        ItemStack itemStack = (ItemStack) (Object) this;
        if (PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStack)) {
            return;
        }

        Unlockable<?> unlockable = VanillaUnlockables.forItemStack(itemStack);
        list.addAll(UnlockTooltipHelper.getLockedTooltipText(player, unlockable));
    }
}
