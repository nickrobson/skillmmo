package dev.nickrobson.minecraft.skillmmo.mixin.client;

import org.jspecify.annotations.NonNull;

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
    @NonNull
    @Override
    public SkillMmoPlayerData skillMmo$getPlayerData() {
        return skillMmo$clientPlayerData;
    }

    @Unique
    @Override
    public void skillMmo$setPlayerData(@NonNull SkillMmoPlayerData playerData) {
        this.skillMmo$clientPlayerData = playerData;
    }
}
