package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collections;
import java.util.List;

@Mixin(GlassBottleItem.class)
public class MixinGlassBottleItem {
    @ModifyVariable(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BEFORE,
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"
            ),
            ordinal = 0
    )
    public List<AreaEffectCloudEntity> skillMmo$use(List<AreaEffectCloudEntity> original, World world, PlayerEntity player, Hand hand) {
        if (PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, Items.DRAGON_BREATH)) {
            return original;
        }

        PlayerSkillUnlockManager.getInstance().reportItemUseLocked(
                player,
                Items.DRAGON_BREATH,
                (deniedPlayer, requiredSkillLevel, actualSkillLevel) ->
                        Text.translatable(
                                "skillmmo.feedback.deny.item.collect.dragon.breath",
                                requiredSkillLevel.getSkill().getName(),
                                requiredSkillLevel.getLevel(),
                                Items.DRAGON_BREATH.getName()
                        ));

        return Collections.emptyList();
    }
}
