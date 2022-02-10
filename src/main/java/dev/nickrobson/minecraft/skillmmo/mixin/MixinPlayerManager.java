package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import dev.nickrobson.minecraft.skillmmo.recipe.PlayerLockedRecipeManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillMmoPlayerDataHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(
            method = "loadPlayerData",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    public void skillMmo$loadPlayerData(ServerPlayerEntity player, CallbackInfoReturnable<NbtCompound> cir, NbtCompound nbtCompound, NbtCompound nbtCompound2) {
        if (nbtCompound2 == null) {
            // This is the first time the player has joined the server,
            // so initialise them with empty data
            ((SkillMmoPlayerDataHolder) player).setSkillMmoPlayerData(
                    new SkillMmoPlayerDataHolder.SkillMmoPlayerData()
            );
        }
    }

    @Inject(
            method = "onPlayerConnect",
            at = @At("TAIL")
    )
    public void skillMmo$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        SkillMmoServerNetworking.sendGenericData(player);
        SkillMmoServerNetworking.sendPlayerData(player);

        PlayerLockedRecipeManager.getInstance().syncLockedRecipes(player);
    }
}
