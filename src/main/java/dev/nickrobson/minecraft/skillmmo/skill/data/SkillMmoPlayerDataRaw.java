package dev.nickrobson.minecraft.skillmmo.skill.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

public record SkillMmoPlayerDataRaw(
        long experience,
        int availableSkillPoints,
        // skillId -> level
        Map<Identifier, Integer> skillLevels,
        // recipeTypeId -> List<recipeId>
        Map<Identifier, List<Identifier>> lockedRecipes
) {
    public static final Codec<SkillMmoPlayerDataRaw> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.optionalFieldOf("experience", 0L).forGetter(SkillMmoPlayerDataRaw::experience),
            Codec.INT.optionalFieldOf("availableSkillPoints", 0).forGetter(SkillMmoPlayerDataRaw::availableSkillPoints),
            Codec.unboundedMap(Identifier.CODEC, Codec.INT).optionalFieldOf("skillLevels", Collections.emptyMap()).forGetter(SkillMmoPlayerDataRaw::skillLevels),
            Codec.unboundedMap(Identifier.CODEC, Codec.list(Identifier.CODEC)).optionalFieldOf("lockedRecipes", Collections.emptyMap()).forGetter(SkillMmoPlayerDataRaw::lockedRecipes)
    ).apply(instance, SkillMmoPlayerDataRaw::new));
}
