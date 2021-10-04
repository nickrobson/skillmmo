package dev.nickrobson.minecraft.skillmmo.skill;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SkillLevel {
    private final String skillId;
    private final byte level;
    private final Map<SkillLevelUnlockType, Set<String>> unlocks;

    /** @see #initSkill(Skill)  */
    private Skill skill;

    public SkillLevel(String skillId, byte level, Map<SkillLevelUnlockType, Set<String>> unlocks) {
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

    public Set<String> getUnlocks(SkillLevelUnlockType unlockType) {
        return unlocks.getOrDefault(unlockType, Collections.emptySet());
    }
}
