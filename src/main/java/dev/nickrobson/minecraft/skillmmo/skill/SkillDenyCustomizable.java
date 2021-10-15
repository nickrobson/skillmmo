package dev.nickrobson.minecraft.skillmmo.skill;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import javax.annotation.Nullable;

/**
 * When something is denied from being used and it implements this interface,
 * {@link #onDeny(PlayerEntity, SkillLevel, int)} is called.
 *
 * This might be a {@link net.minecraft.block.Block}, {@link net.minecraft.item.Item},
 * or {@link net.minecraft.entity.Entity}.
 * (Support for other types of things may be added at a later date - see the issue tracker.)
 */
public interface SkillDenyCustomizable {
    /**
     * Called when a player's skill level is insufficient to use this object.
     *
     * @param player the player that tried to use this
     * @param requiredSkillLevel the skill level (or one of them) that is required
     * @param actualSkillLevel the skill level that the player has
     *
     * @return a message to send to the player as feedback
     */
    @Nullable
    Text onDeny(PlayerEntity player, SkillLevel requiredSkillLevel, int actualSkillLevel);
}
