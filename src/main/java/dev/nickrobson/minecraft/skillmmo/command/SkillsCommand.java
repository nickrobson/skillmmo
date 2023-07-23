package dev.nickrobson.minecraft.skillmmo.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevel;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SkillsCommand {
    private SkillsCommand() {
    }

    static LiteralArgumentBuilder<ServerCommandSource> defineSkillsCommand() {
        return literal("skills")
                .then(argument("player", EntityArgumentType.player())
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(ctx -> executeSkillsCommand(ctx, EntityArgumentType.getPlayer(ctx, "player"))))
                .executes(ctx -> executeSkillsCommand(ctx, null));
    }

    private static int executeSkillsCommand(@Nonnull CommandContext<ServerCommandSource> ctx, @Nullable PlayerEntity player) {
        List<Skill> skills = SkillManager.getInstance().getSkills()
                .stream()
                .sorted(Comparator.comparing(Skill::getId))
                .toList();

        ctx.getSource().sendFeedback(() -> Text.translatable("skillmmo.command.skills.heading", skills.size())
                .setStyle(Style.EMPTY.withColor(Formatting.BLUE)), false);

        if (player == null && ctx.getSource().getEntity() instanceof PlayerEntity sourcePlayer) {
            player = sourcePlayer;
        }

        if (player == null) {
            for (Skill skill : skills) {
                ctx.getSource().sendFeedback(() -> Text.translatable(
                        "skillmmo.command.skills.skill_line",
                        skill.getName()
                ), false);
            }
        } else {
            Text playerName = player.getName();
            for (Skill skill : skills) {
                int skillLevel = PlayerSkillManager.getInstance().getSkillLevel(player, skill);
                ctx.getSource().sendFeedback(() -> Text.translatable(
                        "skillmmo.command.skills.skill_line_with_level",
                        skill.getName(),
                        skillLevel,
                        skill.getMaxLevel()
                ), false);
            }

            int availablePoints = PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player);
            if (player == ctx.getSource().getEntity()) {
                ctx.getSource().sendFeedback(
                        () -> Text.translatable("skillmmo.command.skills.available_points_self", availablePoints),
                        false
                );
            } else {
                ctx.getSource().sendFeedback(
                        () -> Text.translatable("skillmmo.command.skills.available_points_other", playerName, availablePoints),
                        false
                );
            }

            long experience = PlayerExperienceManager.getInstance().getExperience(player);
            ExperienceLevel experienceLevel = ExperienceLevelEquation.getInstance().getExperienceLevel(experience);

            if (player == ctx.getSource().getEntity()) {
                ctx.getSource().sendFeedback(
                        () -> Text.translatable("skillmmo.command.skills.player_experience_self", experienceLevel.level(), Math.round(experienceLevel.progressFraction() * 100), experienceLevel.level() + 1),
                        false
                );
            } else {
                ctx.getSource().sendFeedback(
                        () -> Text.translatable("skillmmo.command.skills.player_experience_other", playerName, experienceLevel.level(), Math.round(experienceLevel.progressFraction() * 100), experienceLevel.level() + 1),
                        false
                );
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
