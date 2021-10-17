package dev.nickrobson.minecraft.skillmmo.mixin;

import com.mojang.authlib.GameProfile;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Shadow
    public abstract void addToOperators(GameProfile profile);

    @Redirect(
            method = "onPlayerConnect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;loadPlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)Lnet/minecraft/nbt/NbtCompound;")
    )
    public NbtCompound skillMmo$loadPlayerData(PlayerManager playerManager, ServerPlayerEntity player) {
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

    @Inject(
            method = "onPlayerConnect",
            at = @At("TAIL")
    )
    public void skillMmo$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        SkillMmoServerNetworking.sendGenericData(player);
        SkillMmoServerNetworking.sendPlayerData(player);

        // FIXME - this is only here to help me debug
        this.addToOperators(player.getGameProfile());
    }
}
