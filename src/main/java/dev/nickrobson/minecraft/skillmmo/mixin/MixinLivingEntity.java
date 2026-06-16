package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraft.world.entity.LivingEntity.canGlideUsing;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @ModifyVariable(
            method = "checkTotemDeathProtection",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;",
                    ordinal = 0
            )
    )
    private ItemStack skillMmo$tryUseTotem(ItemStack original) {
        // If the player is trying to use a totem but doesn't have the necessary level, pretend they're not using a totem!
        //noinspection ConstantConditions
        if ((Object) this instanceof Player player
                && !PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, original)) {
            PlayerSkillUnlockManager.getInstance().reportItemUseLocked(player, original.getItem());
            return ItemStack.EMPTY;
        }
        return original;
    }

    @Redirect(
            method = "doHurtEquipment",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"
            )
    )
    public void skillMmo$damageArmor(ItemStack instance, int amount, LivingEntity entity, EquipmentSlot slot) {
        // Multiply armor damage if the player is unfamiliar with the item
        //noinspection ConstantConditions
        if ((Object) this instanceof Player player
                && !PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, instance)) {
            amount = (int) (amount * SkillMmoConfig.getConfig().unskilledArmorDamageMultiplier);
        }
        instance.hurtAndBreak(amount, entity, slot);
    }

    @Redirect(
            method = "canGlide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;canGlideUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"
            )
    )
    public boolean skillMmo$canGlide(ItemStack stack, EquipmentSlot slot) {
        if ((LivingEntity) (Object) this instanceof Player player
                && !PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, stack)) {
            return false;
        }
        return canGlideUsing(stack, slot);
    }
}
