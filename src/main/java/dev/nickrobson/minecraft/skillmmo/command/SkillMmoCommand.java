package dev.nickrobson.minecraft.skillmmo.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SkillMmoCommand {
    public static void register() {
        ArgumentTypes.register(
                new Identifier(SkillMmoMod.MOD_ID, "skill").toString(),
                SkillArgumentType.class,
                new SkillArgumentType.Serializer()
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            LiteralArgumentBuilder<ServerCommandSource> command =
                    literal("skillmmo")
                            //.requires(source -> source.hasPermissionLevel(2)) // TODO - reenable; disabled for now for ease of testing :P
                            .then(defineSkillsCommand())
                            .then(defineSkillCommand());

            dispatcher.register(command);
        });
    }

    private static ArgumentBuilder<ServerCommandSource, LiteralArgumentBuilder<ServerCommandSource>> defineSkillsCommand() {
        return literal("skills")
                .then(argument("player", EntityArgumentType.player())
                        .executes(ctx -> SkillMmoCommand.executeSkillsCommand(ctx, EntityArgumentType.getPlayer(ctx, "player"))))
                .executes(ctx -> SkillMmoCommand.executeSkillsCommand(ctx, null));
    }

    private static int executeSkillsCommand(@Nonnull CommandContext<ServerCommandSource> ctx, @Nullable PlayerEntity player) {
        Set<Skill> skills = SkillManager.getInstance().getSkills();

        {
            Text heading = new TranslatableText("command.skillmmo.skills.heading", skills.size())
                    .setStyle(Style.EMPTY.withColor(Formatting.BLUE));
            ctx.getSource().sendFeedback(heading, false);
        }

        if (player == null && ctx.getSource().getEntity() instanceof PlayerEntity) {
            player = (PlayerEntity) ctx.getSource().getEntity();
        }

        if (player == null) {
            for (Skill skill : skills) {
                Text text = new TranslatableText(
                        "command.skillmmo.skills.skill_line",
                        skill.getNameText()
                );
                ctx.getSource().sendFeedback(text, false);
            }
        } else {
            for (Skill skill : skills) {
                Text text = new TranslatableText(
                        "command.skillmmo.skills.skill_line_with_level",
                        skill.getNameText(),
                        PlayerSkillManager.getInstance().getSkillLevel(player, skill)
                );
                ctx.getSource().sendFeedback(text, false);
            }
        }

        return 0;
    }

    private static ArgumentBuilder<ServerCommandSource, LiteralArgumentBuilder<ServerCommandSource>> defineSkillCommand() {
        return literal("skill")
                .then(argument("skill", new SkillArgumentType())
                        .then(literal("get")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(ctx -> {
                                            Skill skill = ctx.getArgument("skill", Skill.class);
                                            PlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");

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
                                            return 0;
                                        })
                                ))
                        .then(literal("set")
                                .then(argument("player", EntityArgumentType.player())
                                        .then(argument("level", IntegerArgumentType.integer(SkillLevel.MIN_LEVEL, SkillLevel.MAX_LEVEL))
                                                .executes(ctx -> {
                                                    Skill skill = ctx.getArgument("skill", Skill.class);
                                                    PlayerEntity player = ctx.getArgument("player", EntitySelector.class).getPlayer(ctx.getSource());
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
                                                    return 0;
                                                })
                                        )
                                ))
                );
    }
}
