package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/server/network/ServerPlayNetworkHandler$1")
public abstract class MixinServerPlayPlayerInteractEntityC2SPacketHandler implements PlayerInteractEntityC2SPacket.Handler {
    @Final
    @Shadow
    ServerPlayNetworkHandler field_28963;

    @Final
    @Shadow
    Entity field_28962;

    // FIXME - temporary while Fabric UseEntityCallback doesn't handle interact :(
    @Inject(
            method = "Lnet/minecraft/server/network/ServerPlayNetworkHandler$1;interact(Lnet/minecraft/util/Hand;)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void onPlayerInteractEntity(Hand hand, CallbackInfo ci) {
        PlayerEntity player = field_28963.player;
        ActionResult result = PlayerSkillUnlockManager.getInstance().handleEntityInteraction(player, hand, field_28962);

        if (result != ActionResult.PASS) {
            ci.cancel();
        }
    }
}