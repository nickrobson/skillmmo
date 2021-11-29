package dev.nickrobson.minecraft.skillmmo.compat.waila;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.Unlock;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.UnlockHelper;
import dev.nickrobson.minecraft.skillmmo.util.UnlockTooltipHelper;
import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

class SkillMmoEntityUnlockProvider implements IEntityComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IEntityAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        Entity entity = accessor.getEntity();

        if (PlayerSkillUnlockManager.getInstance().hasEntityUnlock(player, entity)) {
            return;
        }

        Unlock unlock = UnlockHelper.forEntity(entity);
        for (Text line : UnlockTooltipHelper.getLockedTooltipText(player, unlock)) {
            tooltip.add(line);
        }
    }
}
