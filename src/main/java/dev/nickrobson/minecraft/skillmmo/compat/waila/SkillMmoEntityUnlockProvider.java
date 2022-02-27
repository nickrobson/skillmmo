package dev.nickrobson.minecraft.skillmmo.compat.waila;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.UnlockTooltipHelper;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

class SkillMmoEntityUnlockProvider implements IEntityComponentProvider {
    @Override
    @Environment(EnvType.CLIENT)
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        Entity entity = accessor.getEntity();

        if (PlayerSkillUnlockManager.getInstance().hasEntityUnlock(player, entity)) {
            return;
        }

        Unlockable<?> unlockable = VanillaUnlockables.forEntity(entity);
        for (Text line : UnlockTooltipHelper.getLockedTooltipText(player, unlockable)) {
            tooltip.add(line);
        }
    }
}
