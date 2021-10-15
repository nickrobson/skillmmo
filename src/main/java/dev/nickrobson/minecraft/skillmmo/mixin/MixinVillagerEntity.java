package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.SkillDenyCustomizable;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VillagerEntity.class)
public abstract class MixinVillagerEntity implements SkillDenyCustomizable {
    @Shadow
    protected abstract void sayNo();

    @Override
    public void onDeny(PlayerEntity player, SkillLevel skillLevel, int actualSkillLevel) {
        this.sayNo();
    }
}
