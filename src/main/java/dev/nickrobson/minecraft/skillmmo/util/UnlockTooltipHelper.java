package dev.nickrobson.minecraft.skillmmo.util;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.Unlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class UnlockTooltipHelper {
    private UnlockTooltipHelper() {
    }

    public static List<Text> getLockedTooltipText(PlayerEntity player, Unlock unlock) {
        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlock);

        if (skillLevelSet.isEmpty()) {
            return List.of(new TranslatableText("skillmmo.feedback.item.locked"));
        }

        if (!Screen.hasShiftDown()) {
            SkillLevel skillLevel = PlayerSkillManager.getInstance().getClosestLevel(player, skillLevelSet);
            return List.of(
                    new TranslatableText(
                            "skillmmo.feedback.item.locked.basic",
                            skillLevel.getSkill().getNameText(),
                            skillLevel.getLevel()
                    ).setStyle(Style.EMPTY.withColor(Formatting.RED))
            );
        }

        MutableText text = skillLevelSet.size() == 1 || SkillMmoConfig.getConfig().requireAllLockingSkillsToBeUnlocked
                ? new TranslatableText("skillmmo.feedback.item.locked.advanced.heading.all")
                : new TranslatableText("skillmmo.feedback.item.locked.advanced.heading.any");

        return Stream.<Text>concat(
                Stream.of(text.setStyle(Style.EMPTY.withColor(Formatting.RED))),
                skillLevelSet.stream().map(skillLevel ->
                        new TranslatableText(
                                "skillmmo.feedback.item.locked.advanced.line",
                                skillLevel.getSkill().getNameText(),
                                skillLevel.getLevel()
                        ).setStyle(Style.EMPTY.withColor(Formatting.RED)))
        ).toList();
    }
}
