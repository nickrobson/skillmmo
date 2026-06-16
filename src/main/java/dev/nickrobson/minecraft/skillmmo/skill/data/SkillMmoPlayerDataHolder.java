package dev.nickrobson.minecraft.skillmmo.skill.data;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface SkillMmoPlayerDataHolder {
    SkillMmoPlayerData skillMmo$getPlayerData();

    void skillMmo$setPlayerData(SkillMmoPlayerData playerData);

    static SkillMmoPlayerDataHolder getPlayerDataHolder(Player player) {
        return (SkillMmoPlayerDataHolder) player;
    }
}
