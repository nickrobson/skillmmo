package dev.nickrobson.minecraft.skillmmo.config;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = SkillMmoMod.MOD_ID)
public class SkillMmoConfig implements ConfigData {
    public static SkillMmoConfig getConfig() {
        return AutoConfig.getConfigHolder(SkillMmoConfig.class).get();
    }

    @Comment("Should all levels be lost on death?")
    public boolean loseAllLevelsOnDeath = false;

    @Comment("How many levels for each skill should be lost on death?")
    public int levelsLostOnDeath = 0;

    @Comment("Should levels be lost in all skills or just one random skill?")
    public boolean loseLevelsInAllSkillsOnDeath = false;

    @Comment("If an block/item/etc. is locked by multiple skills, should the player need to unlock all of them or just one?")
    public boolean requireAllLockingSkillsToBeUnlocked = false;

    @Comment("Announce required skill when breaking blocks?")
    public boolean announceRequiredSkillWhenBreakingBlock = true;

    @ConfigEntry.Category("Unskilled")
    @Comment("Amount to multiply armor damage by when the player doesn't know how to use a certain type of armor")
    public float unskilledArmorDamageMultiplier = 2.5F;

    @ConfigEntry.Category("Experience / Levelling")
    @Comment("Base cost for an experience level. Experience is calculated as (base cost) + (multiplier * ((level - 1) ^ exponent))")
    public long expBaseCost = 100;

    @ConfigEntry.Category("Experience / Levelling")
    @Comment("Multiplier for an experience level. Experience is calculated as (base cost) + (multiplier * ((level - 1) ^ exponent))")
    public double expMultiplier = 1.33;

    @ConfigEntry.Category("Experience / Levelling")
    @Comment("Experience level exponent. Experience is calculated as (base cost) + (multiplier * ((level - 1) ^ exponent))")
    public double expLevelExponent = 1.8;

    @Override
    public void validatePostLoad() throws ValidationException {
        ConfigData.super.validatePostLoad();

        if (levelsLostOnDeath < 0) {
            throw new ValidationException("levelsLostOnDeath should be 0 or more");
        }
        if (levelsLostOnDeath > Skill.MAX_LEVEL) {
            throw new ValidationException("levelsLostOnDeath should be less than " + Skill.MAX_LEVEL);
        }

        if (unskilledArmorDamageMultiplier < 1) {
            throw new ValidationException("unskilledArmorDamageMultiplier should be 1 or more");
        }

        if (expBaseCost <= 0) {
            throw new ValidationException("expBaseCost should be greater than 0");
        }
        if (expMultiplier <= 0) {
            throw new ValidationException("expMultiplier should be greater than 0");
        }
        if (expLevelExponent <= 0) {
            throw new ValidationException("expLevelExponent should be greater than 0");
        }
    }
}
