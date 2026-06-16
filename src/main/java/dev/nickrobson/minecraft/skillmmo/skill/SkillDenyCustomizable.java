package dev.nickrobson.minecraft.skillmmo.skill;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 * When something is denied from being used, and it implements this interface,
 * {@link #skillMmo$onDeny(Player, SkillLevel, int)} is called.
 * <p>
 * This might be a {@link net.minecraft.world.level.block.Block}, {@link net.minecraft.world.item.Item},
 * or {@link net.minecraft.world.entity.Entity}.
 * (Support for other types of things may be added at a later date - see the issue tracker.)
 */
public interface SkillDenyCustomizable {
    /**
     * Called when a player's skill level is insufficient to use this object.
     *
     * @param player             the player that tried to use this
     * @param requiredSkillLevel the skill level (or one of them) that is required
     * @param actualSkillLevel   the skill level that the player has
     * @return a message to send to the player as feedback
     */
    @Nullable
    Component skillMmo$onDeny(Player player, SkillLevel requiredSkillLevel, int actualSkillLevel);
}
