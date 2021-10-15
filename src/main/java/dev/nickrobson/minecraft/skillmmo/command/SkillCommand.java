package dev.nickrobson.minecraft.skillmmo.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import javax.annotation.Nonnull;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SkillCommand {
    private SkillCommand() {
    }

    static LiteralArgumentBuilder<ServerCommandSource> defineSkillCommand() {
        return literal("skill")
                .then(argument("skill", new SkillArgumentType())
                        .then(literal("get")
                                .then(argument("player", EntityArgumentType.player())
                                        .requires(ctx -> ctx.hasPermissionLevel(2))
                                        .executes(ctx -> executeGetSkillLevelCommand(ctx, EntityArgumentType.getPlayer(ctx, "player")))
                                )
                                .executes(ctx -> executeGetSkillLevelCommand(ctx, ctx.getSource().getPlayer()))
                        )
                        .then(literal("set")
                                .requires(ctx -> ctx.hasPermissionLevel(3))
                                .then(argument("player", EntityArgumentType.player())
                                        .then(argument("level", IntegerArgumentType.integer(SkillLevel.MIN_LEVEL, SkillLevel.MAX_LEVEL))
                                                .executes(SkillCommand::executeSetSkillLevelCommand)
                                        )
                                )
                        )
                        .executes(SkillCommand::executeSkillInfoCommand)
                );
    }

    private static int executeSkillInfoCommand(@Nonnull CommandContext<ServerCommandSource> ctx) {
        Skill skill = ctx.getArgument("skill", Skill.class);

        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "command.skillmmo.skill.info.skill",
                        skill.getNameText().setStyle(Style.EMPTY.withColor(Formatting.BLUE)),
                        skill.getMaxLevel().getLevel()
                ),
                false
        );
        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "command.skillmmo.skill.info.description",
                        skill.getDescriptionText()
                ),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int executeGetSkillLevelCommand(@Nonnull CommandContext<ServerCommandSource> ctx, PlayerEntity player) {
        Skill skill = ctx.getArgument("skill", Skill.class);

        int level = PlayerSkillManager.getInstance().getSkillLevel(player, skill);

        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "command.skillmmo.skill.player_is_level",
                        player.getName(),
                        level,
                        skill.getNameText()
                ),
                false
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int executeSetSkillLevelCommand(@Nonnull CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Skill skill = ctx.getArgument("skill", Skill.class);
        PlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
        int level = ctx.getArgument("level", Integer.class);

        PlayerSkillManager.getInstance().setSkillLevel(player, skill, level);

        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "command.skillmmo.skill.player_is_now_level",
                        player.getName(),
                        level,
                        skill.getNameText()
                ),
                false
        );
        return Command.SINGLE_SUCCESS;
    }
}
