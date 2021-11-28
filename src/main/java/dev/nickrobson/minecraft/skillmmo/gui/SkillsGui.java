package dev.nickrobson.minecraft.skillmmo.gui;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.Comparator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SkillsGui extends LightweightGuiDescription {
    private static final int GRID_SIZE = 18;

    public static void open() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        MinecraftClient.getInstance().setScreen(new SkillsClientScreen(new SkillsGui(player)));
    }

    public SkillsGui(ClientPlayerEntity player) {
        // List of player's skill levels, sorted by skill name in player's language
        List<SkillLevel> skillLevels = SkillManager.getInstance().getSkills().stream()
                .map(skill -> new SkillLevel(skill, PlayerSkillManager.getInstance().getSkillLevel(player, skill)))
                .sorted(Comparator.comparing(skillLevel -> skillLevel.getSkill().getNameText().getString()))
                .toList();

        final int ICON_GRID_WIDTH = 1;
        final int NAME_GRID_WIDTH = 6;
        final int LEVEL_GRID_WIDTH = 2;
        WListPanel<SkillLevel, WGridPanel> skillLevelsPanel = new WListPanel<>(skillLevels, () -> new WGridPanel(GRID_SIZE), ((skillLevel, grid) -> {
            Skill skill = skillLevel.getSkill();

            grid.add(
                    new WItem(new ItemStack(skill.getIconItem())),
                    0, 0,
                    ICON_GRID_WIDTH, 1
            );

            grid.add(
                    new WLabel(skill.getNameText())
                            .setVerticalAlignment(VerticalAlignment.CENTER)
                            .setHorizontalAlignment(HorizontalAlignment.LEFT),
                    ICON_GRID_WIDTH, 0,
                    NAME_GRID_WIDTH, 1
            );

            grid.add(
                    new WLabel(skillLevel.getLevel() + " / " + skill.getMaxLevel())
                            .setVerticalAlignment(VerticalAlignment.CENTER)
                            .setHorizontalAlignment(HorizontalAlignment.RIGHT),
                    ICON_GRID_WIDTH + NAME_GRID_WIDTH, 0,
                    LEVEL_GRID_WIDTH, 1
            );
        }));

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        root.add(
                skillLevelsPanel,
                0,
                10,
                GRID_SIZE * (ICON_GRID_WIDTH + NAME_GRID_WIDTH + LEVEL_GRID_WIDTH + 1),
                GRID_SIZE * 8
        );

        root.validate(this);
    }
}
