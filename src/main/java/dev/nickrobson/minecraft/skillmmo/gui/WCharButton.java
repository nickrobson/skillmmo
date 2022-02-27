package dev.nickrobson.minecraft.skillmmo.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;

public class WCharButton extends WWidget {
    private final char text;
    private boolean enabled = true;
    @Nullable
    private Text tooltip;
    @Nullable
    private Runnable onClick;

    WCharButton(char text) {
        super();
        this.text = text;
    }

    public WCharButton setTooltip(@Nullable Text tooltip) {
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

    @Override
    public void setSize(int x, int y) {
        this.width = x;
        this.height = y;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight());

        int panel = 0xFF737373;
        int outline = 0xFF121313;
        if (!enabled) {
            panel = 0xFF2B2B2B;
        } else if (hovered || isFocused()) {
            outline = 0xFFF5F5F5;
        }

        ScreenDrawing.coloredRect(matrices, x, y + 1, getWidth(), getHeight() - 2, outline);
        ScreenDrawing.coloredRect(matrices, x + 1, y, getWidth() - 2, getHeight(), outline);
        ScreenDrawing.coloredRect(matrices, x + 1, y + 1, getWidth() - 2, getHeight() - 2, panel);

        Text text = new LiteralText(String.valueOf(this.text));
        int color = enabled ? 0xE0E0E0 : 0xA0A0A0;
        int wid = MinecraftClient.getInstance().textRenderer.getWidth(text);
        float xOffset = (width - wid) / 2f;
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x + xOffset + 0.4f, y + (getHeight() - 8) / 2f + 0.4f, color);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        if (enabled && isWithinBounds(x, y)) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            if (onClick != null) {
                onClick.run();
            }

            return InputResult.PROCESSED;
        }

        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (isActivationKey(ch)) {
            onClick(0, 0, 0);
        }
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
    public void addNarrations(NarrationMessageBuilder builder) {
        if (tooltip != null) {
            builder.put(NarrationPart.TITLE, ClickableWidget.getNarrationMessage(tooltip));
        }

        if (enabled) {
            if (isFocused()) {
                builder.put(NarrationPart.USAGE, new TranslatableText("narration.button.usage.focused"));
            } else if (isHovered()) {
                builder.put(NarrationPart.USAGE, new TranslatableText("narration.button.usage.hovered"));
            }
        }
    }
}
