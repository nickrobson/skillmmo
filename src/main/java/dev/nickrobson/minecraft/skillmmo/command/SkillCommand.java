package dev.nickrobson.minecraft.skillmmo.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkillCommand {
    private static final DynamicCommandExceptionType ACQUIRE_SKILL_FAILURE_MAX_LEVEL = new DynamicCommandExceptionType(
            skillName -> new TranslatableText("skillmmo.command.skill.acquire.failure_max_level", skillName)
    );
    private static final SimpleCommandExceptionType ACQUIRE_SKILL_FAILURE_NO_AVAILABLE_POINTS = new SimpleCommandExceptionType(
            new TranslatableText("skillmmo.command.skill.acquire.failure_no_available_points")
    );

    private SkillCommand() {
    }

    static LiteralArgumentBuilder<ServerCommandSource> defineSkillCommand() {
        return literal("skill")
                .then(literal("acquire")
                        .requires(ctx -> ctx.getEntity() instanceof PlayerEntity)
                        .then(argument("skill", new SkillArgumentType())
                                .executes(SkillCommand::executeAcquireSkillCommand)
                        ))
                .then(literal("admin")
                        .requires(ctx -> ctx.hasPermissionLevel(2))
                        .then(literal("get")
                                .requires(ctx -> ctx.hasPermissionLevel(2))
                                .then(argument("player", EntityArgumentType.player())
                                        .then(argument("skill", new SkillArgumentType())
                                                .executes(SkillCommand::executeGetSkillLevelCommand)
                                        )
                                ))
                        .then(literal("set")
                                .requires(ctx -> ctx.hasPermissionLevel(3))
                                .then(argument("player", EntityArgumentType.player())
                                        .then(argument("skill", new SkillArgumentType())
                                                .then(argument("level", IntegerArgumentType.integer(Skill.MIN_LEVEL, Skill.MAX_LEVEL))
                                                        .executes(SkillCommand::executeSetSkillLevelCommand)
                                                )
                                        )
                                )
                        )
                        .executes(SkillCommand::executeSkillInfoCommand)
                );
    }

    private static int executeSkillInfoCommand(CommandContext<ServerCommandSource> ctx) {
        Skill skill = ctx.getArgument("skill", Skill.class);

        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "skillmmo.command.skill.info.skill",
                        new TranslatableText("%s", skill.getName()).setStyle(Style.EMPTY.withColor(Formatting.BLUE)),
                        skill.getMaxLevel()
                ),
                false
        );
        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "skillmmo.command.skill.info.description",
                        skill.getDescription()
                ),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int executeAcquireSkillCommand(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        Skill skill = ctx.getArgument("skill", Skill.class);

        PlayerSkillManager.ChooseSkillLevelResult result = PlayerSkillManager.getInstance()
                .chooseSkillLevel(player, skill);

        return switch (result) {
            case SUCCESS -> {
                int level = PlayerSkillManager.getInstance().getSkillLevel(player, skill);
                ctx.getSource().sendFeedback(
                        new TranslatableText("skillmmo.command.skill.acquire.success", level, skill.getName()),
                        false);
                yield Command.SINGLE_SUCCESS;
            }
            case FAILURE_AT_MAX_LEVEL -> throw ACQUIRE_SKILL_FAILURE_MAX_LEVEL.create(skill.getName());
            case FAILURE_NO_AVAILABLE_POINTS -> throw ACQUIRE_SKILL_FAILURE_NO_AVAILABLE_POINTS.create();
        };
    }

    private static int executeGetSkillLevelCommand(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Skill skill = ctx.getArgument("skill", Skill.class);
        PlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");

        int level = PlayerSkillManager.getInstance().getSkillLevel(player, skill);

        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "skillmmo.command.skill.player_is_level",
                        player.getName(),
                        level,
                        skill.getName()
                ),
                false
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int executeSetSkillLevelCommand(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        Skill skill = ctx.getArgument("skill", Skill.class);
        PlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
        int level = ctx.getArgument("level", Integer.class);

        PlayerSkillManager.getInstance().setSkillLevel(player, skill, level);

        ctx.getSource().sendFeedback(
                new TranslatableText(
                        "skillmmo.command.skill.player_is_now_level",
                        player.getName(),
                        level,
                        skill.getName()
                ),
                false
        );
        return Command.SINGLE_SUCCESS;
    }
}
