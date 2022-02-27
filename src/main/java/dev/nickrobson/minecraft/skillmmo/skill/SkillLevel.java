package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.SkillMmoTags;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.UnlockableType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.StringJoiner;

public class SkillLevel {
    private final Skill skill;
    private final int level;

    SkillLevel(Skill skill, int level) {
        this.skill = Objects.requireNonNull(skill);
        this.level = level;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public <Target> Tag<Target> getUnlocks(UnlockableType<Target> unlockableType) {
        return SkillMmoTags.getUnlocksTag(this, unlockableType);
    }

    public <Target> boolean hasUnlock(UnlockableType<Target> unlockableType, Identifier identifier) {
        Target thingType = unlockableType.getById(identifier);
        return thingType != null && getUnlocks(unlockableType).contains(thingType);
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
