package dev.nickrobson.minecraft.skillmmo.gui;

import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevel;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

import java.util.Comparator;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SkillsGui extends LightweightGuiDescription {
    private static final int GRID_SIZE = 18;

    private static final int ROOT_WIDTH = 10;
    private static final int LEVEL_TEXT_WIDTH = 5;
    private static final int XP_PROGRESS_TEXT_WIDTH = ROOT_WIDTH - LEVEL_TEXT_WIDTH;
    private static final int ICON_GRID_WIDTH = 1;
    private static final int NAME_GRID_WIDTH = 6;
    private static final int LEVEL_GRID_WIDTH = 2;

    public static void open() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        MinecraftClient.getInstance().setScreen(new SkillsClientScreen(new SkillsGui(player)));
    }

    public SkillsGui(ClientPlayerEntity player) {
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        root.add(
                createInfoPanel(player),
                7,
                0,
                GRID_SIZE * ROOT_WIDTH,
                GRID_SIZE + 5
        );

        root.add(
                createSkillsPanel(player),
                0,
                GRID_SIZE + 10,
                GRID_SIZE * ROOT_WIDTH,
                GRID_SIZE * 8
        );

        root.validate(this);
    }

    private WWidget createInfoPanel(ClientPlayerEntity player) {
        WPlainPanel infoPanel = new WPlainPanel();

        ExperienceLevel experienceLevel = PlayerExperienceManager.getInstance().getExperienceLevel(player);

        infoPanel.add(
                new WLabel(new TranslatableText("skillmmo.gui.skills.info.level", experienceLevel.level()))
                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
                        .setVerticalAlignment(VerticalAlignment.CENTER),
                0,
                0,
                GRID_SIZE * LEVEL_TEXT_WIDTH,
                GRID_SIZE
        );

        infoPanel.add(
                new WLabel(new TranslatableText("skillmmo.gui.skills.info.xp_progress", experienceLevel.progress(), experienceLevel.levelExperience()))
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                        .setVerticalAlignment(VerticalAlignment.CENTER),
                GRID_SIZE * LEVEL_TEXT_WIDTH,
                0,
                GRID_SIZE * XP_PROGRESS_TEXT_WIDTH - 7,
                GRID_SIZE
        );

        infoPanel.add(
                new WExperienceBar(experienceLevel.progressFraction()),
                0,
                GRID_SIZE,
                GRID_SIZE * ROOT_WIDTH - 7,
                5
        );

        return infoPanel;
    }

    private WWidget createSkillsPanel(ClientPlayerEntity player) {
        // List of player's skill levels, sorted by skill name in player's language
        List<SkillLevel> skillLevels = SkillManager.getInstance().getSkills().stream()
                .map(skill -> new SkillLevel(skill, PlayerSkillManager.getInstance().getSkillLevel(player, skill)))
                .sorted(Comparator.comparing(skillLevel -> skillLevel.getSkill().getNameText().getString()))
                .toList();

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
                    new WLabel(skillLevel.getLevel() + "/" + skill.getMaxLevel())
                            .setVerticalAlignment(VerticalAlignment.CENTER)
                            .setHorizontalAlignment(HorizontalAlignment.RIGHT),
                    ICON_GRID_WIDTH + NAME_GRID_WIDTH, 0,
                    LEVEL_GRID_WIDTH, 1
            );
        }));

        WPlainPanel skillsPanel = new WPlainPanel();

        skillsPanel.add(
                new WLabel(new TranslatableText("skillmmo.gui.skills.info.available_points", PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player)))
                        .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                        .setVerticalAlignment(VerticalAlignment.CENTER),
                0,
                0,
                GRID_SIZE * ROOT_WIDTH,
                GRID_SIZE
        );

        skillsPanel.add(
                skillLevelsPanel,
                0,
                GRID_SIZE,
                GRID_SIZE * ROOT_WIDTH,
                GRID_SIZE * 7
        );

        return skillsPanel;
    }
}
