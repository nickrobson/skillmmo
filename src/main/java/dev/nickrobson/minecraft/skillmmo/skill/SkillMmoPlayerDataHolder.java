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
    record SkillMmoPlayerData(
            long experience,
            Map<String, Byte> skillLevels
    ) {
        public static SkillMmoPlayerData UNINITIALISED = new SkillMmoPlayerData(0, Collections.emptyMap());

        public SkillMmoPlayerData(long experience, Map<String, Byte> skillLevels) {
            this.experience = experience;
            this.skillLevels = new HashMap<>(skillLevels);
        }

        public Map<String, Byte> skillLevels() {
            return Collections.unmodifiableMap(skillLevels);
        }

        public void setSkillLevel(String skillId, byte level) {
            if (this == UNINITIALISED) {
                throw new IllegalStateException("Cannot set skill level - this player data hasn't been loaded");
            }
            this.skillLevels.put(skillId, level);
        }
    }

    SkillMmoPlayerData getSkillMmoPlayerData();
}
