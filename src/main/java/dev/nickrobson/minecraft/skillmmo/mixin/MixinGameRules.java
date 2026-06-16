package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import net.minecraft.world.rule.GameRules;
import net.minecraft.world.rule.ServerGameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRules.class)
public abstract class MixinGameRules {
    @Shadow
    @Final
    private ServerGameRules rules;

    @Inject(method = "<init>(Lnet/minecraft/resource/featuretoggle/FeatureSet;)V", at = @At("RETURN"))
    private void onRegisterGameRule(CallbackInfo ci) {
        if (SkillMmoConfig.getConfig().enableDoLimitedCraftingGameruleInAllNewWorlds) {
            this.rules.put(GameRules.LIMITED_CRAFTING, true);
        }
    }
}
