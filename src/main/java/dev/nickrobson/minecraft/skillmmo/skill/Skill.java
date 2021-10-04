package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Skill {
    private final String id;
    private final @Nullable String translationKey;
    private final @Nullable String name;
    private final Map<Byte, SkillLevel> skillLevelMap;

    private final SkillLevel maxSkillLevel;
    private final LoadingCache<SkillUnlockCacheKey, SkillLevel> levelsByUnlockCache;

    public Skill(
            String id,
            @Nullable String translationKey,
            @Nullable String name,
            Set<SkillLevel> skillLevels) {
        // Validate we have at least the translation key or the name
        if (translationKey == null && name == null) {
            throw new IllegalStateException(
                    String.format("Neither translation key nor name supplied for skill '%s'", id)
            );
        }

        {
            // Validate skill levels
            if (skillLevels.isEmpty()) {
                throw new IllegalStateException(
                        String.format("No levels found for skill '%s'", id)
                );
            }

            Set<Byte> levels = skillLevels.stream()
                    .map(SkillLevel::getLevel)
                    .collect(Collectors.toSet());
            byte minLevel = levels.stream().min(Comparator.naturalOrder()).orElseThrow();
            byte maxLevel = levels.stream().max(Comparator.naturalOrder()).orElseThrow();
            if (minLevel != 1) {
                throw new IllegalStateException(
                        String.format("Minimum level for skill '%s' is %d, should be 1", id, minLevel));
            }
            if (maxLevel != skillLevels.size()) {
                throw new IllegalStateException(
                        String.format("Maximum level for skill '%s' is %d, should be %d", id, minLevel, skillLevels.size()));
            }
            if (maxLevel != levels.size()) {
                List<Byte> missingLevels = IntStream.rangeClosed(1, maxLevel)
                        .boxed()
                        .map(Integer::byteValue)
                        .filter(n -> !levels.contains(n))
                        .collect(Collectors.toList());
                throw new IllegalStateException(
                        String.format("Missing defined levels for skill '%s': %s", id, missingLevels)
                );
            }
        }

        this.id = id;
        this.translationKey = translationKey;
        this.name = name;
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
                            public SkillLevel load(SkillUnlockCacheKey key) {
                                return skillLevels.stream()
                                        .filter(level ->
                                                level.getUnlocks(key.getUnlockType())
                                                        .contains(key.getUnlockIdentifier())
                                        )
                                        .findFirst()
                                        .orElse(null);
                            }
                        }
                );

        // Initialise each skill level with this skill object for easy back-referencing
        skillLevels.forEach(skillLevel -> skillLevel.initSkill(this));
    }

    public SkillLevel getMaxLevel() {
        return maxSkillLevel;
    }

    public SkillLevel getLevel(byte skillLevel) {
        return Objects.requireNonNull(
                skillLevelMap.get(skillLevel),
                () -> String.format("No level %d defined for skill '%s'", skillLevel, id)
        );
    }

    public Optional<SkillLevel> getSkillLevelAffecting(SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        return Optional.ofNullable(
                levelsByUnlockCache.getUnchecked(new SkillUnlockCacheKey(unlockType, unlockIdentifier))
        );
    }
}
