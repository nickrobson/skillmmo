package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "tryUseTotem",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/LivingEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;",
                    ordinal = 0
            )
    )
    private ItemStack skillMmo$tryUseTotem(ItemStack original) {
        // If the player is trying to use a totem but doesn't have the necessary level, pretend they're not using a totem!
        //noinspection ConstantConditions
        if ((Object) this instanceof PlayerEntity player
                && !PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, original)) {
            PlayerSkillUnlockManager.getInstance().reportItemUseLocked(player, original.getItem());
            return ItemStack.EMPTY;
        }
        return original;
    }
}
