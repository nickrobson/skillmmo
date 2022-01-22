package dev.nickrobson.minecraft.skillmmo.experience;

import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class ExperienceLevelEquation {
    private static final int MAX_EXPERIENCE_LEVEL = 10_000;

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

    private int entryCount = 1_000;
    private int highestCalculated = 0;
    private long[] levelExperienceArr; // experience required to get to the END of each level, starting from the start of it
    private long[] totalExperienceArr; // total experience required to get to the END of each level, starting from the start of level 0

    public ExperienceLevelEquation(long baseCost, double multiplier, double levelExponent) {
        this.baseCost = baseCost;
        this.multiplier = multiplier;
        this.levelExponent = levelExponent;
        this.levelExperienceArr = new long[entryCount];
        this.totalExperienceArr = new long[entryCount];

        // Prevent level exponents that will cause invalid/insane numbers
        if (levelExponent < 0 || levelExponent > 4) {
            throw new IllegalStateException("Level exponent is too low or too high. It should be between 0 (exclusive) and 4 (inclusive), but it is " + levelExponent);
        }

        this.levelExperienceArr[0] = baseCost;
        this.totalExperienceArr[0] = baseCost;

        calculateLevelExperienceUpToLevel(entryCount - 1);
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
        calculateLevelExperienceUpToLevel(level);
        return levelExperienceArr[level - Skill.MIN_LEVEL];
    }

    public long getTotalExperience(int level) {
        calculateLevelExperienceUpToLevel(level);
        return totalExperienceArr[level - Skill.MIN_LEVEL];
    }

    public ExperienceLevel getExperienceLevel(long totalExperience) {
        if (totalExperience < 0L) {
            return new ExperienceLevel(Skill.MIN_LEVEL, 0, getLevelExperience(Skill.MIN_LEVEL + 1));
        }

        while (totalExperience > this.totalExperienceArr[this.totalExperienceArr.length - 1]) {
            calculateLevelExperienceUpToLevel(Skill.MIN_LEVEL + this.totalExperienceArr.length);
        }

        int bsIndex = Arrays.binarySearch(this.totalExperienceArr, totalExperience);
        // If the value is >=0 then it's the exact level value (so we add 1 since it really means you've completed that level)
        // If the value is <0 then it's (-level - 1), so we convert it to the level number
        // Need to re-add the min level to translate from array index -> level number
        int level = Skill.MIN_LEVEL + (bsIndex >= 0 ? bsIndex + 1 : -bsIndex - 1);

        long experienceForLevel = getLevelExperience(level);
        long totalExperienceForPreviousLevel = level == Skill.MIN_LEVEL
                ? 0L
                : getTotalExperience(level - 1);
        long progress = Math.min(experienceForLevel, Math.max(0, totalExperience - totalExperienceForPreviousLevel));

        return new ExperienceLevel(level, progress, experienceForLevel);
    }

    public void calculateLevelExperienceUpToLevel(int level) {
        int target = MathHelper.clamp(level, 0, MAX_EXPERIENCE_LEVEL);
        if (target < highestCalculated) {
            return;
        }

        if (target >= entryCount) {
            while (entryCount <= target) {
                entryCount *= 2;
            }

            levelExperienceArr = Arrays.copyOf(levelExperienceArr, entryCount);
            totalExperienceArr = Arrays.copyOf(totalExperienceArr, entryCount);
        }

        for (int i = highestCalculated + 1; i < entryCount; i++) {
            long experienceRequiredForLevel = (long) (baseCost + multiplier * Math.pow(i - 1, levelExponent));
            this.levelExperienceArr[i] = experienceRequiredForLevel;
            this.totalExperienceArr[i] = this.totalExperienceArr[i - 1] + experienceRequiredForLevel;
            if (this.totalExperienceArr[i] < this.totalExperienceArr[i - 1]) {
                throw new IllegalStateException("Experience level equation grew too fast. Level %d has a higher exp cost than level %d. Try reducing your multiplier and/or level exponent to ensure the number doesn't overflow.".formatted(i - 1, i));
            }
        }

        highestCalculated = entryCount - 1;
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
