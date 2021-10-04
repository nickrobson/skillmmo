package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.FieldsAreNonnullByDefault;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
class SkillUnlockCacheKey {
    private final SkillLevelUnlockType unlockType;
    private final Identifier unlockIdentifier;

    SkillUnlockCacheKey(SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        this.unlockType = unlockType;
        this.unlockIdentifier = unlockIdentifier;
    }

    public SkillLevelUnlockType getUnlockType() {
        return unlockType;
    }

    public Identifier getUnlockIdentifier() {
        return unlockIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkillUnlockCacheKey that = (SkillUnlockCacheKey) o;
        return unlockType == that.unlockType && unlockIdentifier.equals(that.unlockIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unlockType, unlockIdentifier);
    }
}
