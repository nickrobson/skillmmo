package dev.nickrobson.minecraft.skillmmo.mixin;

import java.util.Collections;
import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;

@Mixin(BottleItem.class)
public class MixinBottleItem {
    @ModifyVariable(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
            ),
            ordinal = 0
    )
    public List<AreaEffectCloud> skillMmo$use(List<AreaEffectCloud> original, Level world, Player player, InteractionHand hand) {
        if (PlayerSkillUnlockManager.getInstance().hasItemUnlock(player, Items.DRAGON_BREATH)) {
            return original;
        }

        PlayerSkillUnlockManager.getInstance().reportItemUseLocked(
                player,
                Items.DRAGON_BREATH,
                (deniedPlayer, requiredSkillLevel, actualSkillLevel) ->
                        Component.translatable(
                                "skillmmo.feedback.deny.item.collect.dragon.breath",
                                requiredSkillLevel.getSkill().getName(),
                                requiredSkillLevel.getLevel(),
                                Items.DRAGON_BREATH.getName()
                        ));

        return Collections.emptyList();
    }
}
