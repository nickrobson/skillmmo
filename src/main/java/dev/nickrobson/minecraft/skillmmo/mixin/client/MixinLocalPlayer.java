package dev.nickrobson.minecraft.skillmmo.mixin.client;

import javax.annotation.Nonnull;

import net.minecraft.client.player.LocalPlayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerData;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public class MixinLocalPlayer implements SkillMmoPlayerDataHolder {
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
