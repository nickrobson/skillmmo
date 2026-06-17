package dev.nickrobson.minecraft.skillmmo.gui;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class WCharButton extends WWidget {
    private final char text;
    private boolean enabled = true;
    @Nullable
    private Component tooltip;
    @Nullable
    private Runnable onClick;

    WCharButton(char text) {
        super();
        this.text = text;
    }

    public WCharButton setTooltip(@Nullable Component tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public WCharButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public WCharButton setOnClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return enabled;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY) {
        boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight());

        int panel = 0xFF737373;
        int outline = 0xFF121313;
        if (!enabled) {
            panel = 0xFF2B2B2B;
        } else if (hovered || isFocused()) {
            outline = 0xFFF5F5F5;
        }

        ScreenDrawing.coloredRect(context, x, y + 1, getWidth(), getHeight() - 2, outline);
        ScreenDrawing.coloredRect(context, x + 1, y, getWidth() - 2, getHeight(), outline);
        ScreenDrawing.coloredRect(context, x + 1, y + 1, getWidth() - 2, getHeight() - 2, panel);

        Component text = Component.literal(String.valueOf(this.text));
        int textWidth = Minecraft.getInstance().font.width(text);
        int color = enabled ? 0xFFE0E0E0 : 0xFFA0A0A0;
        context.text(Minecraft.getInstance().font, text, x + (width - textWidth) / 2, y + (getHeight() - 8) / 2, color);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(MouseButtonEvent click, boolean doubled) {
        return this.onClick((int) click.x(), (int) click.y());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onKeyPressed(KeyEvent input) {
        if (isActivationKey(input.key())) {
            return this.onClick(0, 0);
        }
        return InputResult.IGNORED;
    }

    private InputResult onClick(int x, int y) {
        if (enabled && isWithinBounds(x, y)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            if (onClick != null) {
                onClick.run();
            }

            return InputResult.PROCESSED;
        }

        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if (this.tooltip != null) {
            tooltip.add(this.tooltip);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void addNarrations(NarrationElementOutput builder) {
        if (tooltip != null) {
            builder.add(NarratedElementType.TITLE, AbstractWidget.wrapDefaultNarrationMessage(tooltip));
        }

        if (enabled) {
            if (isFocused()) {
                builder.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.focused"));
            } else if (isHovered()) {
                builder.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
            }
        }
    }
}
