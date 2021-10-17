package dev.nickrobson.minecraft.skillmmo.experience;

import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class ExperienceLevelEquation {
    private static final int ENTRY_COUNT = SkillLevel.MAX_LEVEL - SkillLevel.MIN_LEVEL + 1;

    private static ExperienceLevelEquation instance;

    public static ExperienceLevelEquation getInstance() {
        return Objects.requireNonNull(instance, "equation instance is null");
    }

    public static void setInstance(ExperienceLevelEquation instance) {
        ExperienceLevelEquation.instance = instance;
    }

    private final long baseCost;
    private final double multiplier;
    private final double levelExponent;
    private final long[] levelExperienceArr; // experience required to get to the END of each level, starting from the start of it
    private final long[] totalExperienceArr; // total experience required to get to the END of each level, starting from the start of level 0

    public ExperienceLevelEquation(long baseCost, double multiplier, double levelExponent) {
        this.baseCost = baseCost;
        this.multiplier = multiplier;
        this.levelExponent = levelExponent;
        this.levelExperienceArr = new long[ENTRY_COUNT];
        this.totalExperienceArr = new long[ENTRY_COUNT];

        // Prevent level exponents that will cause invalid/insane numbers
        if (levelExponent < 0 || levelExponent > 4) {
            throw new IllegalStateException("Level exponent is too low or too high. It should be between 0 (exclusive) and 4 (inclusive), but it is " + levelExponent);
        }

        this.levelExperienceArr[0] = baseCost;
        this.totalExperienceArr[0] = baseCost;

        for (int level = SkillLevel.MIN_LEVEL + 1; level <= SkillLevel.MAX_LEVEL; level++) {
            long experienceRequiredForLevel = (long) (baseCost + multiplier * Math.pow(level - SkillLevel.MIN_LEVEL, levelExponent));
            int index = level - SkillLevel.MIN_LEVEL;
            this.levelExperienceArr[index] = experienceRequiredForLevel;
            this.totalExperienceArr[index] = this.totalExperienceArr[index - 1] + experienceRequiredForLevel;
            if (this.totalExperienceArr[index] < this.totalExperienceArr[index - 1]) {
                throw new IllegalStateException("Experience level equation grew too fast. Level " + (level - 1) + " has a higher exp cost than level " + level + ". Try reducing your multiplier and/or level exponent to ensure the number doesn't overflow.");
            }
        }
    }

    public long getBaseCost() {
        return baseCost;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public double getLevelExponent() {
        return levelExponent;
    }

    public long getLevelExperience(int level) {
        checkSkillLevelInRange(level);
        return levelExperienceArr[level - SkillLevel.MIN_LEVEL];
    }

    public long getTotalExperience(int level) {
        checkSkillLevelInRange(level);
        return totalExperienceArr[level - SkillLevel.MIN_LEVEL];
    }

    public ExperienceLevel getExperienceLevel(long totalExperience) {
        if (totalExperience < 0L) {
            return new ExperienceLevel(SkillLevel.MIN_LEVEL, 0, getLevelExperience(SkillLevel.MIN_LEVEL + 1));
        }

        int bsIndex = Arrays.binarySearch(this.totalExperienceArr, totalExperience);
        // If the value is >=0 then it's the exact level value
        // If the value is <0 then it's (-level - 1), so we derive level from it
        // Need to re-add the min level to translate from array index -> level number
        int level = SkillLevel.MIN_LEVEL + (bsIndex >= 0 ? bsIndex : -bsIndex - 1);
        if (level == SkillLevel.MAX_LEVEL) {
            return new ExperienceLevel(level, 0, 0);
        }

        long experienceForLevel = getLevelExperience(level);
        long totalExperienceForPreviousLevel = level == SkillLevel.MIN_LEVEL
                ? 0L
                : getTotalExperience(level - 1);
        long progress = Math.min(experienceForLevel, Math.max(0, totalExperience - totalExperienceForPreviousLevel));

        return new ExperienceLevel(level, progress, experienceForLevel);
    }

    public static void checkSkillLevelInRange(int level) {
        if (level < SkillLevel.MIN_LEVEL || level > SkillLevel.MAX_LEVEL) {
            throw new IllegalArgumentException("Only level numbers between " + SkillLevel.MIN_LEVEL + " and " + SkillLevel.MAX_LEVEL + " (inclusive) are supported. Supplied: " + level);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExperienceLevelEquation.class.getSimpleName() + "[", "]")
                .add("baseCost=" + baseCost)
                .add("multiplier=" + multiplier)
                .add("levelExponent=" + levelExponent)
                .toString();
    }
}
