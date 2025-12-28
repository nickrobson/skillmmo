package dev.nickrobson.minecraft.skillmmo.mixin.client;

import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExperienceBar.class)
public interface ExperienceBarAccess {
    @Accessor("BACKGROUND")
    static Identifier getBackgroundTexture() {
        throw new AssertionError("mixin");
    }

    @Accessor("PROGRESS")
    static Identifier getProgressTexture() {
        throw new AssertionError("mixin");
    }
}
