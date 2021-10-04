package dev.nickrobson.minecraft.skillmmo.config;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = SkillMmoMod.MOD_ID)
public class SkillMmoConfig implements ConfigData {
    @Comment("Should all levels be lost on death?")
    public boolean loseAllLevelsOnDeath = false;

    @Comment("How many levels for each skill should be lost on death?")
    public int levelsLostOnDeath = 0;

    @Comment("If an block/item/etc. is locked by multiple skills, should the player need to unlock all of them or just one?")
    public boolean requireAllLockingSkillsToBeUnlocked = false;
}
