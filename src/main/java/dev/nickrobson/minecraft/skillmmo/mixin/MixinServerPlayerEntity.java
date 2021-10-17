package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerExperienceManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    @Inject(
            method = "addExperience",
            at = @At("TAIL")
    )
    public void skillMmo$addExperience(int experience, CallbackInfo ci) {
        PlayerExperienceManager.getInstance()
                .giveExperience((ServerPlayerEntity) (Object) this, experience);
    }
}
