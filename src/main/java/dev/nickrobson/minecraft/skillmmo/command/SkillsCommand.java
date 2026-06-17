package dev.nickrobson.minecraft.skillmmo.command;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.player.Player;

import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevel;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SkillsCommand {
    private SkillsCommand() {
    }

    static LiteralArgumentBuilder<CommandSourceStack> defineSkillsCommand() {
        return literal("skills")
                .then(argument("player", EntityArgument.player())
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .executes(ctx -> executeSkillsCommand(ctx, EntityArgument.getPlayer(ctx, "player"))))
                .executes(ctx -> executeSkillsCommand(ctx, null));
    }

    private static int executeSkillsCommand(@Nonnull CommandContext<CommandSourceStack> ctx, @Nullable Player player) {
        List<Skill> skills = SkillManager.getInstance().getSkills()
                .stream()
                .sorted(Comparator.comparing(Skill::getId))
                .toList();

        ctx.getSource().sendSuccess(() -> Component.translatable("skillmmo.command.skills.heading", skills.size())
                .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE)), false);

        if (player == null && ctx.getSource().getEntity() instanceof Player sourcePlayer) {
            player = sourcePlayer;
        }

        if (player == null) {
            for (Skill skill : skills) {
                ctx.getSource().sendSuccess(() -> Component.translatable(
                        "skillmmo.command.skills.skill_line",
                        skill.getName()
                ), false);
            }
        } else {
            Component playerName = player.getName();
            for (Skill skill : skills) {
                int skillLevel = PlayerSkillManager.getInstance().getSkillLevel(player, skill);
                ctx.getSource().sendSuccess(() -> Component.translatable(
                        "skillmmo.command.skills.skill_line_with_level",
                        skill.getName(),
                        skillLevel,
                        skill.getMaxLevel()
                ), false);
            }

            int availablePoints = PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player);
            if (player == ctx.getSource().getEntity()) {
                ctx.getSource().sendSuccess(
                        () -> Component.translatable("skillmmo.command.skills.available_points_self", availablePoints),
                        false
                );
            } else {
                ctx.getSource().sendSuccess(
                        () -> Component.translatable("skillmmo.command.skills.available_points_other", playerName, availablePoints),
                        false
                );
            }

            long experience = PlayerExperienceManager.getInstance().getExperience(player);
            ExperienceLevel experienceLevel = ExperienceLevelEquation.getInstance().getExperienceLevel(experience);

            if (player == ctx.getSource().getEntity()) {
                ctx.getSource().sendSuccess(
                        () -> Component.translatable("skillmmo.command.skills.player_experience_self", experienceLevel.level(), Math.round(experienceLevel.progressFraction() * 100), experienceLevel.level() + 1),
                        false
                );
            } else {
                ctx.getSource().sendSuccess(
                        () -> Component.translatable("skillmmo.command.skills.player_experience_other", playerName, experienceLevel.level(), Math.round(experienceLevel.progressFraction() * 100), experienceLevel.level() + 1),
                        false
                );
            }
        }

        return Command.SINGLE_SUCCESS;
    }
}
