package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.Unlock;
import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.IntStream;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Skill {
    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 25;

    private final Identifier id;
    private final String nameKey;
    private final String descriptionKey;
    private final int maxLevel;
    private final Item iconItem;

    private final LoadingCache<Unlock, Optional<SkillLevel>> levelsByUnlockCache;

    public Skill(
            Identifier id,
            String nameKey,
            String descriptionKey,
            int maxLevel,
            Item iconItem) {
        this.id = id;
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
        this.maxLevel = maxLevel;
        this.iconItem = iconItem;

        if (maxLevel < MIN_LEVEL + 1) {
            throw new IllegalStateException(
                    "Max level for skill '%s' is %d, should be %d or higher".formatted(id, maxLevel, MIN_LEVEL + 1));
        }
        if (maxLevel > MAX_LEVEL) {
            throw new IllegalStateException(
                    "Maximum level for skill '%s' is %d, should be %d or lower".formatted(id, maxLevel, MAX_LEVEL));
        }

        this.levelsByUnlockCache = CacheBuilder.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .build(
                        new CacheLoader<>() {
                            @Override
                            public Optional<SkillLevel> load(Unlock key) {
                                return getSkillLevels()
                                        .stream()
                                        .filter(level -> level.hasUnlock(key.unlockType(), key.identifier()))
                                        .findFirst();
                            }
                        }
                );
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

    public int getMaxLevel() {
        return maxLevel;
    }

    public Item getIconItem() {
        return iconItem;
    }

    public List<SkillLevel> getSkillLevels() {
        return IntStream.rangeClosed(1, maxLevel)
                .mapToObj(level -> new SkillLevel(this, level))
                .toList();
    }

    public Optional<SkillLevel> getLevel(int level) {
        if (level > MIN_LEVEL && level <= MAX_LEVEL) {
            return Optional.of(new SkillLevel(this, level));
        }
        return Optional.empty();
    }

    public Optional<SkillLevel> getSkillLevelAffecting(Unlock unlock) {
        return levelsByUnlockCache.getUnchecked(unlock);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Skill.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("nameKey='" + nameKey + "'")
                .add("descriptionKey='" + descriptionKey + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return maxLevel == skill.maxLevel && id.equals(skill.id) && Objects.equals(nameKey, skill.nameKey) && Objects.equals(descriptionKey, skill.descriptionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameKey, descriptionKey, maxLevel);
    }
}
