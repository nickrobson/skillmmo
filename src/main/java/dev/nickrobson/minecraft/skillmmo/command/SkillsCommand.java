package dev.nickrobson.minecraft.skillmmo.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

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

            // Player is the command sender
            if (player == ctx.getSource().getEntity()) {
                int availablePoints = PlayerSkillManager.getInstance().getAvailableSkillPoints(player);
                ctx.getSource().sendFeedback(
                        new TranslatableText("command.skillmmo.skills.available_points", availablePoints),
                        false
                );
            }
        }

        return 0;
    }
}
