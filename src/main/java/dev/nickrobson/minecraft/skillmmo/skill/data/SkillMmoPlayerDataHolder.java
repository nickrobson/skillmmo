package dev.nickrobson.minecraft.skillmmo.skill.data;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface SkillMmoPlayerDataHolder {
    SkillMmoPlayerData skillMmo$getPlayerData();

    void skillMmo$setPlayerData(SkillMmoPlayerData playerData);

    static SkillMmoPlayerDataHolder getPlayerDataHolder(PlayerEntity player) {
        return (SkillMmoPlayerDataHolder) player;
    }
}
