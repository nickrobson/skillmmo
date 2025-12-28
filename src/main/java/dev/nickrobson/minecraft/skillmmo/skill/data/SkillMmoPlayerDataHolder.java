package dev.nickrobson.minecraft.skillmmo.skill.data;

import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface SkillMmoPlayerDataHolder {
    SkillMmoPlayerData skillMmo$getPlayerData();

    void skillMmo$setPlayerData(SkillMmoPlayerData playerData);
}
