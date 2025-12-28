package dev.nickrobson.minecraft.skillmmo.gui;

import dev.nickrobson.minecraft.skillmmo.mixin.client.ExperienceBarAccess;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class WExperienceBar extends WWidget {
    private static final Texture TEXTURE_EMPTY_BAR = new Texture(ExperienceBarAccess.getBackgroundTexture(), Texture.Type.GUI_SPRITE);

    private final float progress;

    private final Texture textureFilledBar;

    public WExperienceBar(double progress) {
        this.progress = MathHelper.clamp((float) progress, 0F, 1F);
        this.textureFilledBar = new Texture(ExperienceBarAccess.getProgressTexture(), Texture.Type.GUI_SPRITE);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void paint(DrawContext drawContext, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(drawContext, x, y, getWidth(), getHeight(), TEXTURE_EMPTY_BAR, 0xFF_FFFFFF);
        ScreenDrawing.texturedRect(drawContext, x, y, (int) (getWidth() * progress), getHeight(), textureFilledBar, 0xFF_D7F01D);
    }
}
