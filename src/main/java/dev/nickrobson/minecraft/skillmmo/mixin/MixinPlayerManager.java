package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.recipe.PlayerLockedRecipeManager;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerData;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
    @Inject(
            method = "onPlayerConnect",
            at = @At("HEAD")
    )
    public void skillMmo$onPlayerConnect$HEAD(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        SkillMmoPlayerDataHolder playerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        if (playerDataHolder.skillMmo$getPlayerData() == SkillMmoPlayerData.UNINITIALISED) {
            playerDataHolder.skillMmo$setPlayerData(new SkillMmoPlayerData());
        }
    }

    @Inject(
            method = "onPlayerConnect",
            at = @At("TAIL")
    )
    public void skillMmo$onPlayerConnect$TAIL(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
        PlayerLockedRecipeManager.getInstance().syncLockedRecipes(player);
    }
}
