package dev.nickrobson.minecraft.skillmmo.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import dev.nickrobson.minecraft.skillmmo.mixin.client.ExperienceBarRendererAccess;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import org.jspecify.annotations.NonNull;

public class WExperienceBar extends WWidget {
    private static final Texture TEXTURE_EMPTY_BAR = new Texture(ExperienceBarRendererAccess.getBackgroundTexture(), Texture.Type.GUI_SPRITE);

    private final float progress;

    private final Texture textureFilledBar;

    public WExperienceBar(double progress) {
        this.progress = Mth.clamp((float) progress, 0F, 1F);
        this.textureFilledBar = new Texture(ExperienceBarRendererAccess.getProgressTexture(), Texture.Type.GUI_SPRITE);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void paint(@NonNull GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(context, x, y, getWidth(), getHeight(), TEXTURE_EMPTY_BAR, 0xFF_FFFFFF);
        ScreenDrawing.texturedRect(context, x, y, (int) (getWidth() * progress), getHeight(), textureFilledBar, 0xFF_D7F01D);
    }
}
