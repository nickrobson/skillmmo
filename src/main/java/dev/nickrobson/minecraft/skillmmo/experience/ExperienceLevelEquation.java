package dev.nickrobson.minecraft.skillmmo.experience;

import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;

import java.util.Arrays;
import java.util.Objects;

public class ExperienceLevelEquation {
    private static final int ENTRY_COUNT = SkillLevel.MAX_LEVEL - SkillLevel.MIN_LEVEL + 1;

    private static ExperienceLevelEquation instance;
    public static ExperienceLevelEquation getInstance() {
        return Objects.requireNonNull(instance, "equation instance is null");
    }
    public static void setInstance(ExperienceLevelEquation instance) {
        ExperienceLevelEquation.instance = instance;
    }

    private final double baseCost;
    private final double multiplier;
    private final double levelExponent;
    private final long[] levelExperienceArr;
    private final long[] totalExperienceArr;

    public ExperienceLevelEquation(double baseCost, double multiplier, double levelExponent) {
        this.baseCost = baseCost;
        this.multiplier = multiplier;
        this.levelExponent = levelExponent;
        this.levelExperienceArr = new long[ENTRY_COUNT];
        this.totalExperienceArr = new long[ENTRY_COUNT];

        // Prevent level exponents that will cause invalid/insane numbers
        if (levelExponent < 0 || levelExponent > 4) {
            throw new IllegalStateException("Level exponent is too low or too high. It should be between 0 (exclusive) and 4 (inclusive), but it is " + levelExponent);
        }

        this.levelExperienceArr[0] = 0L;
        this.totalExperienceArr[0] = 0L;

        for (int level = SkillLevel.MIN_LEVEL + 1; level <= SkillLevel.MAX_LEVEL; level++) {
            long experienceRequiredForLevel = (long) (baseCost + multiplier * Math.pow(level - SkillLevel.MIN_LEVEL - 1, levelExponent));
            int index = level - SkillLevel.MIN_LEVEL;
            this.levelExperienceArr[index] = experienceRequiredForLevel;
            this.totalExperienceArr[index] = this.totalExperienceArr[index - 1] + experienceRequiredForLevel;
            if (this.totalExperienceArr[index] < this.totalExperienceArr[index - 1]) {
                throw new IllegalStateException("Experience level equation grew too fast. Level " + (level - 1) + " has a higher exp cost than level " + level + ". Try reducing your multiplier and/or level exponent to ensure the number doesn't overflow.");
            }
        }
    }

    public double getBaseCost() {
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
        // Need to re-add the min level to translate from array index -> level number
        int level = Arrays.binarySearch(this.totalExperienceArr, totalExperience) + SkillLevel.MIN_LEVEL;
        if (level == SkillLevel.MAX_LEVEL) {
            return new ExperienceLevel(level, 0, getLevelExperience(level));
        }

        long experienceForLevel = getLevelExperience(level);
        long totalExperienceForLevel = getTotalExperience(level);
        long progress = Math.min(experienceForLevel, Math.max(0, totalExperience - totalExperienceForLevel));

        return new ExperienceLevel(level, progress, experienceForLevel);
    }

    public static void checkSkillLevelInRange(int level) {
        if (level < SkillLevel.MIN_LEVEL || level > SkillLevel.MAX_LEVEL) {
            throw new IllegalArgumentException("Only level numbers between " + SkillLevel.MIN_LEVEL + " and " + SkillLevel.MAX_LEVEL + " (inclusive) are supported.");
        }
    }
}
