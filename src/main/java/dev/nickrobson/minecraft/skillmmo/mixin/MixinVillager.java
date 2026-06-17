package dev.nickrobson.minecraft.skillmmo.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import dev.nickrobson.minecraft.skillmmo.skill.SkillDenyCustomizable;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;

@Mixin(Villager.class)
public abstract class MixinVillager implements SkillDenyCustomizable {
    @Shadow
    protected abstract void setUnhappy();

    @Unique
    @Override
    public Component skillMmo$onDeny(Player player, SkillLevel requiredSkillLevel, int actualSkillLevel) {
        this.setUnhappy();

        return Component.translatable(
                "skillmmo.feedback.deny.villager.interact",
                requiredSkillLevel.getSkill().getName(),
                requiredSkillLevel.getLevel()
        );
    }
}
