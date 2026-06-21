package dev.nickrobson.minecraft.skillmmo.skill.data;

import org.jspecify.annotations.NullMarked;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;

@MethodsReturnNonnullByDefault
@NullMarked
public interface SkillMmoPlayerDataHolder {
    SkillMmoPlayerData skillMmo$getPlayerData();

    void skillMmo$setPlayerData(SkillMmoPlayerData playerData);

    static SkillMmoPlayerDataHolder getPlayerDataHolder(Player player) {
        return (SkillMmoPlayerDataHolder) player;
    }
}
