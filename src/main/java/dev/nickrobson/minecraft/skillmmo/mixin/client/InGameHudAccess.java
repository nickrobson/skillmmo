package dev.nickrobson.minecraft.skillmmo.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudAccess {
    @Accessor("EXPERIENCE_BAR_BACKGROUND_TEXTURE")
    static Identifier getExperienceBarBackgroundTexture() {
        throw new AssertionError("mixin");
    }

    @Accessor("EXPERIENCE_BAR_PROGRESS_TEXTURE")
    static Identifier getExperienceBarProgressTexture() {
        throw new AssertionError("mixin");
    }
}
