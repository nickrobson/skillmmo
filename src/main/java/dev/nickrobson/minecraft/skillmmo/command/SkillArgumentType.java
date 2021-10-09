package dev.nickrobson.minecraft.skillmmo.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SkillArgumentType implements ArgumentType<Skill> {
    @Override
    public Skill parse(StringReader reader) throws CommandSyntaxException {
        Identifier skillId = Identifier.fromCommandInput(reader);
        return SkillManager.getInstance().getSkill(skillId)
                .orElseThrow(() -> new SimpleCommandExceptionType(new LiteralText("No such skill!")).createWithContext(reader));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Set<Skill> skillSet = SkillManager.getInstance().getSkills();
        return CommandSource.suggestMatching(
                skillSet.stream()
                        .map(Skill::getId)
                        .map(Identifier::toString)
                        .sorted(),
                builder
        );
    }

    @Override
    public Collection<String> getExamples() {
        Set<Skill> skillSet = SkillManager.getInstance().getSkills();
        if (skillSet.isEmpty()) {
            return Collections.emptyList();
        }
        return skillSet.stream()
                .map(Skill::getId)
                .map(Identifier::toString)
                .sorted()
                .toList()
                .subList(0, 2);
    }

    public static class Serializer extends ConstantArgumentSerializer<SkillArgumentType> {
        public Serializer() {
            super(SkillArgumentType::new);
        }
    }
}
