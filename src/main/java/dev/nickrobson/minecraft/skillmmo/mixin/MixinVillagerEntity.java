package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.SkillDenyCustomizable;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VillagerEntity.class)
public abstract class MixinVillagerEntity implements SkillDenyCustomizable {
    @Shadow
    protected abstract void sayNo();

    @Unique
    @Override
    public Text onDeny(PlayerEntity player, SkillLevel requiredSkillLevel, int actualSkillLevel) {
        this.sayNo();

        return Text.translatable(
                "skillmmo.feedback.deny.villager.interact",
                requiredSkillLevel.getSkill().getName(),
                requiredSkillLevel.getLevel()
        );
    }
}
