package dev.nickrobson.minecraft.skillmmo.command;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;

public class SkillArgumentType implements ArgumentType<Skill> {
    public static final SingletonArgumentInfo<SkillArgumentType> SERIALIZER =
            SingletonArgumentInfo.contextFree(SkillArgumentType::new);

    @Override
    public Skill parse(StringReader reader) throws CommandSyntaxException {
        Identifier skillId = Identifier.read(reader);
        return SkillManager.getInstance().getSkill(skillId)
                .orElseThrow(() -> new SimpleCommandExceptionType(Component.literal("No such skill!")).createWithContext(reader));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Set<Skill> skillSet = SkillManager.getInstance().getSkills();
        return SharedSuggestionProvider.suggest(
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
                .toList();
    }
}
