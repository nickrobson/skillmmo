package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Skill {
    private final Identifier id;
    private final String nameKey;
    private final String descriptionKey;
    private final Set<SkillLevel> skillLevelSet;
    private final Map<Integer, SkillLevel> skillLevelMap;

    private final SkillLevel maxSkillLevel;
    private final LoadingCache<SkillUnlockCacheKey, Optional<SkillLevel>> levelsByUnlockCache;

    public Skill(
            Identifier id,
            String nameKey,
            String descriptionKey,
            Set<SkillLevel> skillLevels) {
        {
            // Validate skill levels
            if (skillLevels.isEmpty()) {
                throw new IllegalStateException(
                        String.format("No levels found for skill '%s'", id)
                );
            }

            Set<Integer> levels = skillLevels.stream()
                    .map(SkillLevel::getLevel)
                    .collect(Collectors.toSet());
            int minLevel = levels.stream().min(Comparator.naturalOrder()).orElseThrow();
            int maxLevel = levels.stream().max(Comparator.naturalOrder()).orElseThrow();
            if (minLevel < SkillLevel.MIN_LEVEL) {
                throw new IllegalStateException(
                        String.format("Minimum level for skill '%s' is %d, should be %d or higher", id, minLevel, SkillLevel.MIN_LEVEL));
            }
            if (maxLevel > SkillLevel.MAX_LEVEL) {
                throw new IllegalStateException(
                        String.format("Maximum level for skill '%s' is %d, should be %d or lower", id, maxLevel, SkillLevel.MAX_LEVEL));
            }
            if (maxLevel < levels.size()) {
                throw new IllegalStateException(
                        String.format("Too many levels defined for skill '%s': %s", id, levels)
                );
            }
        }

        this.id = id;
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
        this.skillLevelSet = skillLevels;
        this.skillLevelMap = skillLevels.stream()
                .collect(Collectors.toMap(SkillLevel::getLevel, Function.identity()));
        this.maxSkillLevel = skillLevels.stream()
                .max(Comparator.comparing(SkillLevel::getLevel))
                .orElseThrow(() -> new IllegalStateException(
                        String.format("No skill levels found for skill '%s'", id)
                ));

        this.levelsByUnlockCache = CacheBuilder.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .build(
                        new CacheLoader<>() {
                            @Override
                            public Optional<SkillLevel> load(SkillUnlockCacheKey key) {
                                return skillLevels.stream()
                                        .filter(level ->
                                                level.getUnlocks(key.getUnlockType())
                                                        .contains(key.getUnlockIdentifier())
                                        )
                                        .findFirst();
                            }
                        }
                );

        // Initialise each skill level with this skill object for easy back-referencing
        skillLevels.forEach(skillLevel -> skillLevel.initSkill(this));
    }

    public Identifier getId() {
        return id;
    }

    public String getNameKey() {
        return nameKey;
    }

    public TranslatableText getNameText() {
        return new TranslatableText(nameKey);
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public TranslatableText getDescriptionText() {
        return new TranslatableText(descriptionKey);
    }

    public Set<SkillLevel> getSkillLevels() {
        return Collections.unmodifiableSet(skillLevelSet);
    }

    public SkillLevel getMaxLevel() {
        return maxSkillLevel;
    }

    public Optional<SkillLevel> getLevel(int skillLevel) {
        return Optional.ofNullable(skillLevelMap.get(skillLevel));
    }

    public Optional<SkillLevel> getSkillLevelAffecting(UnlockType unlockType, Identifier unlockIdentifier) {
        return levelsByUnlockCache.getUnchecked(new SkillUnlockCacheKey(unlockType, unlockIdentifier));
    }
}
