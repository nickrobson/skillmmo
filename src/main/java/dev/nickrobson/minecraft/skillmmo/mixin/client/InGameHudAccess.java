package dev.nickrobson.minecraft.skillmmo.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface InGameHudAccess {
    @Accessor("ICONS")
    public static Identifier getIcons() {
        throw new AssertionError("mixin");
    }
}
