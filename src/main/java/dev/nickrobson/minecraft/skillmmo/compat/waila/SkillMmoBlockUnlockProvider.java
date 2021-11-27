package dev.nickrobson.minecraft.skillmmo.compat.waila;

import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.Unlock;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.UnlockHelper;
import dev.nickrobson.minecraft.skillmmo.util.UnlockTooltipHelper;
import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

class SkillMmoBlockUnlockProvider implements IBlockComponentProvider {
    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        PlayerEntity player = accessor.getPlayer();
        BlockState blockState = accessor.getBlockState();

        if (PlayerSkillUnlockManager.getInstance().hasBlockUnlock(player, blockState)) {
            return;
        }

        Unlock unlock = UnlockHelper.forBlock(blockState);
        for (Text line : UnlockTooltipHelper.getLockedTooltipText(player, unlock)) {
            tooltip.add(line);
        }
    }
}
