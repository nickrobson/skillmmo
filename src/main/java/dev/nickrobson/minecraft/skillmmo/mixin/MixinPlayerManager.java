package dev.nickrobson.minecraft.skillmmo.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.nickrobson.minecraft.skillmmo.recipe.PlayerLockedRecipeManager;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerData;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.util.ErrorReporter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(
            method = "loadPlayerData",
            at = @At("RETURN")
    )
    public void skillMmo$loadPlayerData(ServerPlayerEntity player, ErrorReporter errorReporter, CallbackInfoReturnable<Optional<ReadView>> cir, @Local Optional optional) {
        if (optional.isEmpty()) {
            // This is the first time the player has joined the server,
            // so initialise them with empty data
            ((SkillMmoPlayerDataHolder) player).skillMmo$setPlayerData(
                    new SkillMmoPlayerData()
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
