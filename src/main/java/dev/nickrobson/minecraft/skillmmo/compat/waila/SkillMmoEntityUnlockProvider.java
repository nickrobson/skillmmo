package dev.nickrobson.minecraft.skillmmo.compat.waila;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.UnlockTooltipHelper;

class SkillMmoEntityUnlockProvider implements IEntityComponentProvider {
    @Override
    @Environment(EnvType.CLIENT)
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        Player player = accessor.getPlayer();
        Entity entity = accessor.getEntity();

        if (PlayerSkillUnlockManager.getInstance().hasEntityUnlock(player, entity)) {
            return;
        }

        Unlockable<?> unlockable = VanillaUnlockables.forEntity(entity);
        for (Component line : UnlockTooltipHelper.getLockedTooltipText(player, unlockable)) {
            tooltip.addLine(line);
        }
    }
}
