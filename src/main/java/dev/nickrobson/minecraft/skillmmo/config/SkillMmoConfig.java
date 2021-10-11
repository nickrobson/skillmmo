package dev.nickrobson.minecraft.skillmmo.config;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
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

    @Comment("If an block/item/etc. is locked by multiple skills, should the player need to unlock all of them or just one?")
    public boolean requireAllLockingSkillsToBeUnlocked = false;

    @ConfigEntry.Category("experience")
    @Comment("Base cost for an experience level. Experience is calculated as (base + multiplier * ((level - 1) ^ exponent))")
    public long expBaseCost = 100;

    @ConfigEntry.Category("experience")
    @Comment("Multiplier for an experience level. Experience is calculated as (base cost) + (multiplier * ((level - 1) ^ exponent))")
    public double expMultiplier = 1.33;

    @ConfigEntry.Category("experience")
    @Comment("Experience level exponent. Experience is calculated as (base cost) + (multiplier * ((level - 1) ^ exponent))")
    public double expLevelExponent = 1.8;
}
