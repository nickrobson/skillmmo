package dev.nickrobson.minecraft.skillmmo.compat.waila;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;

import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.VanillaUnlockables;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;
import dev.nickrobson.minecraft.skillmmo.util.UnlockTooltipHelper;

class SkillMmoBlockUnlockProvider implements IBlockComponentProvider {
    @Override
    @Environment(EnvType.CLIENT)
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        Player player = accessor.getPlayer();
        BlockState blockState = accessor.getBlockState();

        if (PlayerSkillUnlockManager.getInstance().hasBlockUnlock(player, blockState)) {
            return;
        }

        Unlockable<?> unlock = VanillaUnlockables.forBlock(blockState);
        for (Component line : UnlockTooltipHelper.getLockedTooltipText(player, unlock)) {
            tooltip.addLine(line);
        }
    }
}
