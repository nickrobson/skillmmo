package dev.nickrobson.minecraft.skillmmo.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class WPlusButton extends WButton {
    WPlusButton(Text label) {
        super(label);
    }

    @Override
    public void setSize(int x, int y) {
        this.width = x;
        this.height = y;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        boolean hovered = (mouseX>=0 && mouseY>=0 && mouseX<getWidth() && mouseY<getHeight());

        int panel = 0xFF737373;
        int outline = 0xFF121313;
        if (!isEnabled()) {
            panel = 0xFF2B2B2B;
        } else if (hovered || isFocused()) {
            outline = 0xFFF5F5F5;
        }

        ScreenDrawing.coloredRect(matrices, x, y + 1, getWidth(), getHeight() - 2, outline);
        ScreenDrawing.coloredRect(matrices, x + 1, y, getWidth() - 2, getHeight(), outline);
        ScreenDrawing.coloredRect(matrices, x + 1, y + 1, getWidth() - 2, getHeight() - 2, panel);

        Text label = getLabel();
        if (label != null) {
            // int color = isEnabled() ? 0xE0E0E0 : 0xA0A0A0;
            // ScreenDrawing.drawStringWithShadow(matrices, label.asOrderedText(), alignment, x, y + ((getHeight() - 8) / 2), width, color);

            int color = isEnabled() ? 0xE0E0E0 : 0xA0A0A0;
            int wid = MinecraftClient.getInstance().textRenderer.getWidth(label);
            float xOffset = (width - wid) / 2f;
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, label, x + xOffset + 0.5f, y + (getHeight() - 7) / 2f, color);
        }
    }
}
