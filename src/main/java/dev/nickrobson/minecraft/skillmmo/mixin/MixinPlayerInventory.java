package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillUnlockManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {
    @Shadow
    @Final
    public PlayerEntity player;

    @Redirect(
            method = "damageArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"
            )
    )
    public <T extends LivingEntity> void redirectDamageArmor(ItemStack instance, int amount, T entity, Consumer<T> breakCallback) {
        // Multiply armor damage if the player is unfamiliar with the item
        if (!PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, instance)) {
            amount *= SkillMmoConfig.getConfig().unskilledArmorDamageMultiplier;
        }
        instance.damage(amount, entity, breakCallback);
    }
}
