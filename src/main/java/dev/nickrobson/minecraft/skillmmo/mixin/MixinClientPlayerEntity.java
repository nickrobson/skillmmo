package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity implements SkillMmoPlayerDataHolder {
    private SkillMmoPlayerData skillMmo$clientPlayerData = new SkillMmoPlayerData();

    @Unique
    @Override
    public SkillMmoPlayerData getSkillMmoPlayerData() {
        return skillMmo$clientPlayerData;
    }

    @Unique
    @Override
    public void setSkillMmoPlayerData(@Nonnull SkillMmoPlayerData playerData) {
        this.skillMmo$clientPlayerData = playerData;
    }
}
