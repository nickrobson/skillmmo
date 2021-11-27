package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.UnlockType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.StringJoiner;

public class SkillLevel {
    private final Skill skill;
    private final int level;

    public SkillLevel(Skill skill, int level) {
        this.skill = Objects.requireNonNull(skill);
        this.level = level;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public <T> Tag<T> getUnlocks(UnlockType<T> unlockType) {
        return SkillMmoTags.getUnlocksTag(this, unlockType);
    }

    public <T> boolean hasUnlock(UnlockType<T> unlockType, Identifier identifier) {
        T thing = unlockType.getRegistry().get(identifier);
        return thing != null && getUnlocks(unlockType).contains(thing);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkillLevel.class.getSimpleName() + "[", "]")
                .add("skill=" + skill.getId())
                .add("level=" + level)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillLevel that = (SkillLevel) o;
        return level == that.level && skill.equals(that.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill, level);
    }
}
