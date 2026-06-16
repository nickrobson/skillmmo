package dev.nickrobson.minecraft.skillmmo.mixin.client;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.UnlockTooltipHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Inject(
            method = "addDetailsToTooltip",
            at = @At(value = "RETURN")
    )
    public void skillMmo$appendTooltip(Item.TooltipContext context, TooltipDisplay displayComponent, @Nullable Player player, TooltipFlag type, Consumer<Component> textConsumer, CallbackInfo ci) {
        if (player == null) {
            return;
        }

        ItemStack itemStack = (ItemStack) (Object) this;
        if (PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, itemStack)) {
            return;
        }

        Unlockable<?> unlockable = VanillaUnlockables.forItemStack(itemStack);
        UnlockTooltipHelper.getLockedTooltipText(player, unlockable).forEach(textConsumer);
    }
}
