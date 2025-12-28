package dev.nickrobson.minecraft.skillmmo.compat.waila;

import mcp.mobius.waila.api.IClientRegistrar;
import mcp.mobius.waila.api.IWailaClientPlugin;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class SkillMmoWailaUnlocksPlugin implements IWailaClientPlugin {
    @Override
    public void register(IClientRegistrar registrar) {
        registrar.body(new SkillMmoBlockUnlockProvider(), Block.class);
        registrar.body(new SkillMmoEntityUnlockProvider(), Entity.class);
    }
}
