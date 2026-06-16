package dev.nickrobson.minecraft.skillmmo.util;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

public class UnlockTooltipHelper {
    private UnlockTooltipHelper() {
    }

    public static List<Component> getLockedTooltipText(Player player, Unlockable<?> unlockable) {
        Set<SkillLevel> skillLevelSet = SkillManager.getInstance().getSkillLevelsAffecting(unlockable);

        if (skillLevelSet.isEmpty()) {
            return List.of(Component.translatable("skillmmo.feedback.item.locked"));
        }

        if (skillLevelSet.size() == 1 || !player.isShiftKeyDown()) {
            SkillLevel skillLevel = PlayerSkillManager.getInstance().getClosestLevel(player, skillLevelSet);
            return List.of(
                    Component.translatable(
                            "skillmmo.feedback.item.locked.basic",
                            skillLevel.getSkill().getName(),
                            skillLevel.getLevel()
                    ).setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
            );
        }

        MutableComponent text = SkillMmoConfig.getConfig().requireAllLockingSkillsToBeUnlocked
                ? Component.translatable("skillmmo.feedback.item.locked.advanced.heading.all")
                : Component.translatable("skillmmo.feedback.item.locked.advanced.heading.any");

        return Stream.<Component>concat(
                Stream.of(text.setStyle(Style.EMPTY.withColor(ChatFormatting.RED))),
                skillLevelSet.stream().map(skillLevel -> Component.translatable(
                        "skillmmo.feedback.item.locked.advanced.line",
                        skillLevel.getSkill().getName(),
                        skillLevel.getLevel()
                ).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)))
        ).toList();
    }
}
