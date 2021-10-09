package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
    @Inject(
            method = "loadPlayerData",
            at = @At("RETURN")
    )
    public void loadPlayerData(ServerPlayerEntity player, CallbackInfoReturnable<NbtCompound> cir) {
        NbtCompound nbt = cir.getReturnValue();
        if (nbt == null) {
            // This is the first time the player has joined the server,
            // so initialise them with empty data
            ((SkillMmoPlayerDataHolder) player).setSkillMmoPlayerData(
                    new SkillMmoPlayerDataHolder.SkillMmoPlayerData()
            );
        }
    }
}
