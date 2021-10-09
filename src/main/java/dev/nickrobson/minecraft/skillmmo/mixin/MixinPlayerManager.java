package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
    @Redirect(
            method = "onPlayerConnect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;loadPlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)Lnet/minecraft/nbt/NbtCompound;")
    )
    public NbtCompound loadPlayerData(PlayerManager playerManager, ServerPlayerEntity player) {
        NbtCompound nbt = playerManager.loadPlayerData(player);
        if (nbt == null) {
            // This is the first time the player has joined the server,
            // so initialise them with empty data
            ((SkillMmoPlayerDataHolder) player).setSkillMmoPlayerData(
                    new SkillMmoPlayerDataHolder.SkillMmoPlayerData()
            );
        }
        return nbt;
    }
}
