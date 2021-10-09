package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface SkillMmoPlayerDataHolder {
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    class SkillMmoPlayerData {
        public static final SkillMmoPlayerData UNINITIALISED = new SkillMmoPlayerData(0, 0, Collections.emptyMap());

        private long experience;
        private int availableSkillPoints;
        private final Map<String, Byte> skillLevels;

        public SkillMmoPlayerData() {
            this(0L, 0, new HashMap<>());
        }

        public SkillMmoPlayerData(long experience, int availableSkillPoints, Map<String, Byte> skillLevels) {
            this.experience = experience;
            this.availableSkillPoints = availableSkillPoints;
            this.skillLevels = new HashMap<>(skillLevels);
        }

        private void checkInitialised() {
            if (this == UNINITIALISED) {
                throw new IllegalStateException("Cannot set skill level - this player data hasn't been loaded");
            }
        }

        public long getExperience() {
            return experience;
        }

        public void addExperience(long experience) {
            this.checkInitialised();
            this.experience += experience;
        }

        public int getAvailableSkillPoints() {
            return availableSkillPoints;
        }

        public void addAvailableSkillPoint() {
            this.checkInitialised();
            this.availableSkillPoints++;
        }

        public boolean consumeAvailableSkillPoints() {
            this.checkInitialised();
            if (this.availableSkillPoints > 0) {
                this.availableSkillPoints--;
                return true;
            }
            return false;
        }

        public Map<String, Byte> getSkillLevels() {
            return Collections.unmodifiableMap(skillLevels);
        }

        public void setSkillLevel(String skillId, byte level) {
            this.checkInitialised();
            this.skillLevels.put(skillId, level);
        }
    }

    SkillMmoPlayerData getSkillMmoPlayerData();
    void setSkillMmoPlayerData(SkillMmoPlayerData playerData);
}
