package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
class SkillUnlockCacheKey {
    private final SkillLevelUnlockType unlockType;
    private final String unlockId;

    SkillUnlockCacheKey(SkillLevelUnlockType unlockType, String unlockId) {
        this.unlockType = unlockType;
        this.unlockId = unlockId;
    }

    public SkillLevelUnlockType getUnlockType() {
        return unlockType;
    }

    public String getUnlockId() {
        return unlockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillUnlockCacheKey that = (SkillUnlockCacheKey) o;
        return unlockType == that.unlockType && unlockId.equals(that.unlockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unlockType, unlockId);
    }
}
