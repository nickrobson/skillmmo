package dev.nickrobson.minecraft.skillmmo.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.nickrobson.minecraft.skillmmo.mixin.client.InGameHudAccess;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

public class WExperienceBar extends WWidget {
    private static final Texture TEXTURE_EMPTY_BAR = new Texture(InGameHudAccess.getIcons(), 0F, 64F / 256F, 182 / 256F, 69 / 256F);

    private final float progress;

    private final Texture textureFilledBar;

    public WExperienceBar(double progress) {
        this.progress = MathHelper.clamp((float) progress, 0F, 1F);

        this.textureFilledBar = new Texture(InGameHudAccess.getIcons(), 0F, 69F / 256F, 182 / 256F * this.progress, 74 / 256F);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void paint(DrawContext drawContext, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(drawContext, x, y, getWidth(), getHeight(), TEXTURE_EMPTY_BAR, 0xFF_FFFFFF);
        ScreenDrawing.texturedRect(drawContext, x, y, (int) (getWidth() * progress), getHeight(), textureFilledBar, 0xFF_409FFF);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
