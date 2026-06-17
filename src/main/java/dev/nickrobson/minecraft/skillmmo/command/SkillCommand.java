package dev.nickrobson.minecraft.skillmmo.command;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.player.Player;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkillCommand {
    private static final DynamicCommandExceptionType ACQUIRE_SKILL_FAILURE_MAX_LEVEL = new DynamicCommandExceptionType(
            skillName -> Component.translatable("skillmmo.command.skill.acquire.failure_max_level", skillName)
    );
    private static final SimpleCommandExceptionType ACQUIRE_SKILL_FAILURE_NO_AVAILABLE_POINTS = new SimpleCommandExceptionType(
            Component.translatable("skillmmo.command.skill.acquire.failure_no_available_points")
    );

    private SkillCommand() {
    }

    static LiteralArgumentBuilder<CommandSourceStack> defineSkillCommand() {
        return literal("skill")
                .then(literal("acquire")
                        .requires(ctx -> ctx.getEntity() instanceof Player)
                        .then(argument("skill", new SkillArgumentType())
                                .executes(SkillCommand::executeAcquireSkillCommand)
                        ))
                .then(literal("admin")
                        .requires(ctx -> ctx.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                        .then(literal("get")
                                .requires(ctx -> ctx.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                                .then(argument("player", EntityArgument.player())
                                        .then(argument("skill", new SkillArgumentType())
                                                .executes(SkillCommand::executeGetSkillLevelCommand)
                                        )
                                ))
                        .then(literal("set")
                                .requires(ctx -> ctx.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                .then(argument("player", EntityArgument.player())
                                        .then(argument("skill", new SkillArgumentType())
                                                .then(argument("level", IntegerArgumentType.integer(Skill.MIN_LEVEL, Skill.MAX_LEVEL))
                                                        .executes(SkillCommand::executeSetSkillLevelCommand)
                                                )
                                        )
                                )
                        )
                        .then(literal("add")
                                .requires(ctx -> ctx.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                .then(argument("player", EntityArgument.player())
                                        .then(argument("skill", new SkillArgumentType())
                                                .then(argument("level", IntegerArgumentType.integer(Skill.MIN_LEVEL, Skill.MAX_LEVEL))
                                                        .executes(ctx -> executeAddSkillLevelCommand(ctx, 1))
                                                )
                                        )
                                )
                        )
                        .then(literal("remove")
                                .requires(ctx -> ctx.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                                .then(argument("player", EntityArgument.player())
                                        .then(argument("skill", new SkillArgumentType())
                                                .then(argument("level", IntegerArgumentType.integer(Skill.MIN_LEVEL, Skill.MAX_LEVEL))
                                                        .executes(ctx -> executeAddSkillLevelCommand(ctx, -1))
                                                )
                                        )
                                )
                        )
                )
                .then(literal("info")
                        .then(argument("skill", new SkillArgumentType())
                                .executes(SkillCommand::executeSkillInfoCommand))
                );
    }

    private static int executeSkillInfoCommand(CommandContext<CommandSourceStack> ctx) {
        Skill skill = ctx.getArgument("skill", Skill.class);
        Component skillName = skill.getName();

        ctx.getSource().sendSuccess(
                () -> Component.translatable(
                        "skillmmo.command.skill.info.skill",
                        skillName instanceof MutableComponent mutableText
                                ? mutableText.setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE))
                                : skillName,
                        skill.getMaxLevel()
                ),
                false
        );
        ctx.getSource().sendSuccess(
                () -> Component.translatable(
                        "skillmmo.command.skill.info.description",
                        skill.getDescription()
                ),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int executeAcquireSkillCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        Skill skill = ctx.getArgument("skill", Skill.class);

        PlayerSkillManager.ChooseSkillLevelResult result = PlayerSkillManager.getInstance()
                .chooseSkillLevel(player, skill);

        return switch (result) {
            case SUCCESS -> {
                int level = PlayerSkillManager.getInstance().getSkillLevel(player, skill);
                ctx.getSource().sendSuccess(
                        () -> Component.translatable("skillmmo.command.skill.acquire.success", level, skill.getName()),
                        false);
                yield Command.SINGLE_SUCCESS;
            }
            case FAILURE_AT_MAX_LEVEL -> throw ACQUIRE_SKILL_FAILURE_MAX_LEVEL.create(skill.getName());
            case FAILURE_NO_AVAILABLE_POINTS -> throw ACQUIRE_SKILL_FAILURE_NO_AVAILABLE_POINTS.create();
        };
    }

    private static int executeGetSkillLevelCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Skill skill = ctx.getArgument("skill", Skill.class);
        Player player = EntityArgument.getPlayer(ctx, "player");

        int level = PlayerSkillManager.getInstance().getSkillLevel(player, skill);

        ctx.getSource().sendSuccess(
                () -> Component.translatable(
                        "skillmmo.command.skill.player_is_level",
                        player.getName(),
                        level,
                        skill.getName()
                ),
                false
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int executeSetSkillLevelCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Skill skill = ctx.getArgument("skill", Skill.class);
        Player player = EntityArgument.getPlayer(ctx, "player");
        int level = ctx.getArgument("level", Integer.class);

        int newLevel = PlayerSkillManager.getInstance().setSkillLevel(player, skill, level);

        ctx.getSource().sendSuccess(
                () -> Component.translatable(
                        "skillmmo.command.skill.player_is_now_level",
                        player.getName(),
                        newLevel,
                        skill.getName()
                ),
                true
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int executeAddSkillLevelCommand(CommandContext<CommandSourceStack> ctx, int multiplier) throws CommandSyntaxException {
        Skill skill = ctx.getArgument("skill", Skill.class);
        Player player = EntityArgument.getPlayer(ctx, "player");
        int levelDelta = ctx.getArgument("level", Integer.class) * multiplier;

        int currentLevel = PlayerSkillManager.getInstance().getSkillLevel(player, skill);
        int newLevel = PlayerSkillManager.getInstance().setSkillLevel(player, skill, currentLevel + levelDelta);

        ctx.getSource().sendSuccess(
                () -> Component.translatable(
                        "skillmmo.command.skill.player_is_now_level",
                        player.getName(),
                        newLevel,
                        skill.getName()
                ),
                true
        );

        boolean isSelf = false;
        if (ctx.getSource().getEntity() instanceof Player playerSource) {
            isSelf = playerSource.getGameProfile().id().equals(player.getGameProfile().id());
        }
        if (!isSelf) {
            player.displayClientMessage(
                    Component.translatable("skillmmo.command.skill.you_are_now_level", newLevel, skill.getName()),
                    false
            );
        }
        return Command.SINGLE_SUCCESS;
    }
}
