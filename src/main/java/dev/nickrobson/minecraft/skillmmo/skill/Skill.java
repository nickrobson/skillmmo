package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
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
    private final String id;
    private final @Nullable String translationKey;
    private final @Nullable String untranslatedName;
    private final Set<SkillLevel> skillLevelSet;
    private final Map<Byte, SkillLevel> skillLevelMap;

    private final SkillLevel maxSkillLevel;
    private final LoadingCache<SkillUnlockCacheKey, Optional<SkillLevel>> levelsByUnlockCache;

    public Skill(
            String id,
            @Nullable String translationKey,
            @Nullable String untranslatedName,
            Set<SkillLevel> skillLevels) {
        // Validate we have at least the translation key or the untranslated name
        if (translationKey == null && untranslatedName == null) {
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
            if (minLevel <= 0) {
                throw new IllegalStateException(
                        String.format("Minimum level for skill '%s' is %d, should be 1 or higher", id, minLevel));
            }
            if (maxLevel < levels.size()) {
                throw new IllegalStateException(
                        String.format("Too many levels defined for skill '%s': %s", id, levels)
                );
            }
        }

        this.id = id;
        this.translationKey = translationKey;
        this.untranslatedName = untranslatedName;
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

    public String getId() {
        return id;
    }

    @Nullable
    public String getTranslationKey() {
        return translationKey;
    }

    @Nullable
    public String getUntranslatedName() {
        return untranslatedName;
    }

    public String getReadableName() {
        if (translationKey != null && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            return net.minecraft.client.resource.language.TranslationStorage
                    .getInstance()
                    .get(translationKey);
        }

        if (untranslatedName != null) {
            return untranslatedName;
        }

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return String.format("Skill[%s]", translationKey);
        }

        throw new IllegalStateException("No translation key or name");
    }

    public Set<SkillLevel> getSkillLevels() {
        return Collections.unmodifiableSet(skillLevelSet);
    }

    public SkillLevel getMaxLevel() {
        return maxSkillLevel;
    }

    public Optional<SkillLevel> getLevel(byte skillLevel) {
        return Optional.ofNullable(skillLevelMap.get(skillLevel));
    }

    public Optional<SkillLevel> getSkillLevelAffecting(SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        return levelsByUnlockCache.getUnchecked(new SkillUnlockCacheKey(unlockType, unlockIdentifier));
    }
}
