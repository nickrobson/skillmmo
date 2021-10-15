package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.SkillDenyCustomizable;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VillagerEntity.class)
public abstract class MixinVillagerEntity implements SkillDenyCustomizable {
    @Shadow
    protected abstract void sayNo();

    @Override
    public Text onDeny(PlayerEntity player, SkillLevel requiredSkillLevel, int actualSkillLevel) {
        this.sayNo();

        return new TranslatableText(
                "skillmmo.feedback.deny.villager.interact",
                requiredSkillLevel.getSkill(),
                requiredSkillLevel.getLevel()
        );
    }
}
