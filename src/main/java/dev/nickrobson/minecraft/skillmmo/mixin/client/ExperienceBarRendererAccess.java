package dev.nickrobson.minecraft.skillmmo.mixin.client;

import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExperienceBarRenderer.class)
public interface ExperienceBarRendererAccess {
    @Accessor("EXPERIENCE_BAR_BACKGROUND_SPRITE")
    static Identifier getBackgroundTexture() {
        throw new AssertionError("mixin");
    }

    @Accessor("EXPERIENCE_BAR_PROGRESS_SPRITE")
    static Identifier getProgressTexture() {
        throw new AssertionError("mixin");
    }
}
