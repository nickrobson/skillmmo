package dev.nickrobson.minecraft.skillmmo.mixin.client;

import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nonnull;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity implements SkillMmoPlayerDataHolder {
    @Unique
    private SkillMmoPlayerData skillMmo$clientPlayerData = new SkillMmoPlayerData();

    @Unique
    @Nonnull
    @Override
    public SkillMmoPlayerData skillMmo$getPlayerData() {
        return skillMmo$clientPlayerData;
    }

    @Unique
    @Override
    public void skillMmo$setPlayerData(@Nonnull SkillMmoPlayerData playerData) {
        this.skillMmo$clientPlayerData = playerData;
    }
}
