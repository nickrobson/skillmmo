package dev.nickrobson.minecraft.skillmmo.gui;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class WClickableLabel extends WLabel {
    private Runnable onClick;

    public WClickableLabel(String text, int color) {
        super(text, color);
    }

    public WClickableLabel(Text text, int color) {
        super(text, color);
    }

    public WClickableLabel(String text) {
        super(text);
    }

    public WClickableLabel(Text text) {
        super(text);
    }

    public WClickableLabel setOnClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        if (isWithinBounds(x, y)) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            if (onClick != null) {
                onClick.run();
            }

            return InputResult.PROCESSED;
        }

        return super.onClick(x, y, button);
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (isActivationKey(ch)) {
            onClick(0, 0, 0);
        }
    }
}
