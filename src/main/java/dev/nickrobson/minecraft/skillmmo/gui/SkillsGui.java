package dev.nickrobson.minecraft.skillmmo.gui;

import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevel;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoClientNetworking;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WDynamicLabel;
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
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Environment(EnvType.CLIENT)
public class SkillsGui extends LightweightGuiDescription {
    private static final int GRID_SIZE = 18;

    private static final int ROOT_WIDTH = 11;
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

    private final List<WButton> acquireSkillButtons = new ArrayList<>();

    public SkillsGui(ClientPlayerEntity player) {
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setInsets(new Insets(4));

        root.add(
                createInfoPanel(player),
                4,
                0,
                GRID_SIZE * ROOT_WIDTH,
                GRID_SIZE + 5
        );

        root.add(
                createSkillsPanel(player),
                0,
                GRID_SIZE + 12,
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
                GRID_SIZE * XP_PROGRESS_TEXT_WIDTH - 4,
                GRID_SIZE
        );

        infoPanel.add(
                new WExperienceBar(experienceLevel.progressFraction()),
                0,
                GRID_SIZE,
                GRID_SIZE * ROOT_WIDTH - 4,
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

        int availableSkillPoints = PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player);
        WListPanel<SkillLevel, WPlainPanel> skillLevelsPanel = new WListPanel<>(skillLevels, WPlainPanel::new, ((skillLevel, skillLevelPanel) -> {
            Skill skill = skillLevel.getSkill();

            skillLevelPanel.add(
                    new WItem(new ItemStack(skill.getIconItem())),
                    0,
                    0,
                    GRID_SIZE * ICON_GRID_WIDTH,
                    GRID_SIZE
            );

            skillLevelPanel.add(
                    new WLabel(skill.getNameText())
                            .setVerticalAlignment(VerticalAlignment.CENTER)
                            .setHorizontalAlignment(HorizontalAlignment.LEFT),
                    GRID_SIZE * ICON_GRID_WIDTH + 6,
                    0,
                    GRID_SIZE * NAME_GRID_WIDTH - 6,
                    GRID_SIZE
            );

            skillLevelPanel.add(
                    new WDynamicLabel(() -> PlayerSkillManager.getInstance().getSkillLevel(player, skill) + "/" + skill.getMaxLevel())
                            .setAlignment(HorizontalAlignment.RIGHT),
                    GRID_SIZE * (ICON_GRID_WIDTH + NAME_GRID_WIDTH),
                    5,
                    GRID_SIZE * LEVEL_GRID_WIDTH,
                    GRID_SIZE
            );

            WButton acquireSkillButton = new WPlusButton(new LiteralText("+"))
                    .setEnabled(availableSkillPoints > 0 && skillLevel.getLevel() < skill.getMaxLevel())
                    .setAlignment(HorizontalAlignment.CENTER);

            AtomicInteger levelUps = new AtomicInteger(0);
            acquireSkillButton.setOnClick(() -> {
                if (PlayerSkillPointManager.getInstance().consumeAvailableSkillPoint(player)) {
                    SkillMmoClientNetworking.sendChoosePlayerSkill(skill);

                    int updatedAvailableSkillPoints = PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player);

                    if (skillLevel.getLevel() + levelUps.incrementAndGet() >= skill.getMaxLevel()) {
                        acquireSkillButton.setEnabled(false);
                        acquireSkillButtons.remove(acquireSkillButton);
                    }

                    acquireSkillButtons.forEach(button ->
                            button.setEnabled(updatedAvailableSkillPoints > 0));
                }
            });

            if (skillLevel.getLevel() < skill.getMaxLevel()) {
                acquireSkillButtons.add(acquireSkillButton);
            }

            skillLevelPanel.add(
                    acquireSkillButton,
                    GRID_SIZE * (ICON_GRID_WIDTH + NAME_GRID_WIDTH + LEVEL_GRID_WIDTH) + 8,
                    4,
                    10,
                    10
            );
        }));

        WPlainPanel skillsPanel = new WPlainPanel();

        skillsPanel.add(
                new WDynamicLabel(() -> I18n.translate("skillmmo.gui.skills.info.available_points", PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player)))
                        .setAlignment(HorizontalAlignment.RIGHT),
                0,
                0,
                GRID_SIZE * ROOT_WIDTH,
                GRID_SIZE - 4
        );

        skillsPanel.add(
                skillLevelsPanel,
                0,
                GRID_SIZE - 4,
                GRID_SIZE * ROOT_WIDTH,
                GRID_SIZE * 7
        );

        return skillsPanel;
    }
}
