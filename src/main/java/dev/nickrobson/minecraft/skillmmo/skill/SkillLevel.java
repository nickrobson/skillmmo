package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SkillLevel {
    private final String skillId;
    private final byte level;
    private final Map<SkillLevelUnlockType, Set<Identifier>> unlocks;

    /** @see #initSkill(Skill)  */
    private Skill skill;

    public SkillLevel(String skillId, byte level, Map<SkillLevelUnlockType, Set<Identifier>> unlocks) {
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

    public String getSkillId() {
        return skillId;
    }

    public byte getLevel() {
        return level;
    }

    public Map<SkillLevelUnlockType, Set<Identifier>> getUnlocks() {
        return Collections.unmodifiableMap(unlocks);
    }

    public Set<Identifier> getUnlocks(SkillLevelUnlockType unlockType) {
        return unlocks.getOrDefault(unlockType, Collections.emptySet());
    }
}
