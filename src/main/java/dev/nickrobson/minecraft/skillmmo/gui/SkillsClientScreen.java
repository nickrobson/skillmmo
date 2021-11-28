package dev.nickrobson.minecraft.skillmmo.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class SkillsClientScreen extends CottonClientScreen {
    public SkillsClientScreen(GuiDescription description) {
        super(description);
    }
}
