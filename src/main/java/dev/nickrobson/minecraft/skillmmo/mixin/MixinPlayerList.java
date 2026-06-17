package dev.nickrobson.minecraft.skillmmo.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.nickrobson.minecraft.skillmmo.recipe.PlayerLockedRecipeManager;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerData;
import dev.nickrobson.minecraft.skillmmo.skill.data.SkillMmoPlayerDataHolder;

@Mixin(PlayerList.class)
public abstract class MixinPlayerList {
    @Inject(
            method = "placeNewPlayer",
            at = @At("HEAD")
    )
    public void skillMmo$onPlayerConnect$HEAD(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
        SkillMmoPlayerDataHolder playerDataHolder = SkillMmoPlayerDataHolder.getPlayerDataHolder(player);
        if (playerDataHolder.skillMmo$getPlayerData() == SkillMmoPlayerData.UNINITIALISED) {
            playerDataHolder.skillMmo$setPlayerData(new SkillMmoPlayerData());
        }
    }

    @Inject(
            method = "placeNewPlayer",
            at = @At("TAIL")
    )
    public void skillMmo$onPlayerConnect$TAIL(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
        PlayerLockedRecipeManager.getInstance().syncLockedRecipes(player);
    }
}
