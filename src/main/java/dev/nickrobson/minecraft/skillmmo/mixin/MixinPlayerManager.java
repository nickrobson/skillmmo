package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.recipe.PlayerLockedRecipeManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(
            method = "loadPlayerData",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    public void skillMmo$loadPlayerData(ServerPlayerEntity player, CallbackInfoReturnable<Optional<NbtCompound>> cir, NbtCompound nbtCompound, Optional<NbtCompound> optional) {
        if (optional.isEmpty()) {
            // This is the first time the player has joined the server,
            // so initialise them with empty data
            ((SkillMmoPlayerDataHolder) player).skillMmo$setPlayerData(
                    new SkillMmoPlayerDataHolder.SkillMmoPlayerData()
            );
        }
    }

    @Inject(
            method = "onPlayerConnect",
            at = @At("TAIL")
    )
    public void skillMmo$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        PlayerLockedRecipeManager.getInstance().syncLockedRecipes(player);
    }
}
