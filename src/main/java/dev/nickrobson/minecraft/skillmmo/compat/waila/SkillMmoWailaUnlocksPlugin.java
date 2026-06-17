package dev.nickrobson.minecraft.skillmmo.compat.waila;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

import mcp.mobius.waila.api.IClientRegistrar;
import mcp.mobius.waila.api.IWailaClientPlugin;

public class SkillMmoWailaUnlocksPlugin implements IWailaClientPlugin {
    @Override
    public void register(IClientRegistrar registrar) {
        registrar.body(new SkillMmoBlockUnlockProvider(), Block.class);
        registrar.body(new SkillMmoEntityUnlockProvider(), Entity.class);
    }
}
