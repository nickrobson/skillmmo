package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.logging.annotations.FieldsAreNonnullByDefault;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
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
    private final Component name;
    private final Component description;
    private final int maxLevel;
    private final Item iconItem;

    private final LoadingCache<Unlockable<?>, Optional<SkillLevel>> levelsByUnlockCache;

    public Skill(
            Identifier id,
            Component name,
            Component description,
            int maxLevel,
            Item iconItem) {
        this.id = id;
        this.name = name;
        this.description = description;
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
                            public Optional<SkillLevel> load(Unlockable<?> key) {
                                return getSkillLevels()
                                        .stream()
                                        .filter(level -> level.hasUnlock(key.type(), key.targetId()))
                                        .findFirst();
                            }
                        }
                );
    }

    public Identifier getId() {
        return id;
    }

    public Component getName() {
        return name.plainCopy();
    }

    public Component getDescription() {
        return description.plainCopy();
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
        if (level >= MIN_LEVEL && level <= maxLevel) {
            return Optional.of(new SkillLevel(this, level));
        }
        return Optional.empty();
    }

    public Optional<SkillLevel> getSkillLevelAffecting(Unlockable<?> unlock) {
        return levelsByUnlockCache.getUnchecked(unlock);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Skill.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return maxLevel == skill.maxLevel && id.equals(skill.id) && Objects.equals(name, skill.name) && Objects.equals(description, skill.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, maxLevel);
    }
}
