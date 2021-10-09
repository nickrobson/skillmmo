package dev.nickrobson.minecraft.skillmmo.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
        return literal("skills").executes(ctx -> {
            Set<Skill> skills = SkillManager.getInstance().getSkills();

            {
                Text text = new LiteralText("")
                        .append(new LiteralText("Skills").setStyle(Style.EMPTY.withColor(Formatting.BLUE)))
                        .append(new LiteralText("(" + skills.size() + ")").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
                ctx.getSource().sendFeedback(text, false);
            }

            skills.forEach(skill -> {
                Text text = new LiteralText("- ")
                        .append(new TranslatableText(skill.getTranslationKey()))
                        .append(new LiteralText(" (" + skill.getId() + ")"));
                ctx.getSource().sendFeedback(text, false);
            });

            return 0;
        });
    }

    private static ArgumentBuilder<ServerCommandSource, LiteralArgumentBuilder<ServerCommandSource>> defineSkillCommand() {
        return literal("skill").then(argument("skill", new SkillArgumentType())
                .then(literal("get")
                        .then(argument("player", EntityArgumentType.player())
                                .executes(ctx -> {
                                    Skill skill = ctx.getArgument("skill", Skill.class);
                                    PlayerEntity player = ctx.getArgument("player", EntitySelector.class).getPlayer(ctx.getSource());

                                    byte level = PlayerSkillManager.getInstance().getSkillLevel(player, skill);

                                    ctx.getSource().sendFeedback(
                                            new LiteralText(String.format(
                                                    "%s has %d levels in %s",
                                                    player.getGameProfile().getName(),
                                                    level,
                                                    skill.getTranslationKey())),
                                            false
                                    );
                                    return 0;
                                })
                        ))
                .then(literal("set")
                        .then(argument("player", EntityArgumentType.player())
                                .then(argument("level", IntegerArgumentType.integer(1, Byte.MAX_VALUE))
                                        .executes(ctx -> {
                                            Skill skill = ctx.getArgument("skill", Skill.class);
                                            PlayerEntity player = ctx.getArgument("player", EntitySelector.class).getPlayer(ctx.getSource());
                                            byte level = ctx.getArgument("level", Integer.class).byteValue();

                                            PlayerSkillManager.getInstance().setSkillLevel(player, skill, level);

                                            ctx.getSource().sendFeedback(
                                                    new LiteralText(String.format(
                                                            "%s is now level %d in %s",
                                                            player.getGameProfile().getName(),
                                                            level,
                                                            skill.getTranslationKey())),
                                                    false
                                            );
                                            return 0;
                                        })
                                )
                        ))
        );
    }
}
