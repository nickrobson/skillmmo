package dev.nickrobson.minecraft.skillmmo.mixin;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.util.SkillMmoBooleanGameRuleSetter;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(GameRules.class)
public abstract class MixinGameRules {
    @Shadow
    @Final
    private Map<GameRules.Key<?>, GameRules.Rule<?>> rules;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    private void onRegisterGameRule(CallbackInfo ci) {
        if (SkillMmoConfig.getConfig().enableDoLimitedCraftingGameruleInAllNewWorlds) {
            GameRules.Rule<?> rule = this.rules.get(GameRules.DO_LIMITED_CRAFTING);
            if (rule instanceof SkillMmoBooleanGameRuleSetter setter) {
                setter.skillMmo$setValue(true);
            }
        }
    }

    @Mixin(GameRules.BooleanRule.class)
    public static abstract class MixinBooleanRule implements SkillMmoBooleanGameRuleSetter {
        @Shadow
        private boolean value;

        @Override
        public void skillMmo$setValue(boolean enabled) {
            this.value = enabled;
        }
    }
}
