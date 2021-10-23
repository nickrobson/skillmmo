package dev.nickrobson.minecraft.skillmmo.skill;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.UnlockType;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class SkillLevel {
    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 25;

    private final Identifier skillId;
    private final int level;
    private final Map<UnlockType, Set<Identifier>> unlocks;

    /** @see #initSkill(Skill)  */
    private Skill skill;

    public SkillLevel(Identifier skillId, int level, Map<UnlockType, Set<Identifier>> unlocks) {
        this.skillId = skillId;
        this.level = level;
        this.unlocks = unlocks;
    }

    protected void initSkill(Skill skill) {
        this.skill = skill;
    }

    public Skill getSkill() {
        return Objects.requireNonNull(skill, () -> String.format("Skill not yet initialised (skill '%s', level %d)", skillId, level));
    }

    public Identifier getSkillId() {
        return skillId;
    }

    public int getLevel() {
        return level;
    }

    public Map<UnlockType, Set<Identifier>> getUnlocks() {
        return Collections.unmodifiableMap(unlocks);
    }

    public Set<Identifier> getUnlocks(UnlockType unlockType) {
        return unlocks.getOrDefault(unlockType, Collections.emptySet());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkillLevel.class.getSimpleName() + "[", "]")
                .add("skill=" + skillId)
                .add("level=" + level)
                .toString();
    }
}
