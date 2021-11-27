package dev.nickrobson.minecraft.skillmmo.compat.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class SkillMmoWailaUnlocksPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(new SkillMmoBlockUnlockProvider(), TooltipPosition.BODY, Block.class);
        registrar.addComponent(new SkillMmoEntityUnlockProvider(), TooltipPosition.BODY, Entity.class);
    }
}
