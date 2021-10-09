package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.command.SkillMmoCommand;
import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.data.SkillMmoResourceLoader;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkillMmoMod implements ModInitializer {
    private static final Logger logger = LogManager.getLogger(SkillMmoMod.class);

    public static final String MOD_ID = "skillmmo";
    public static final String MOD_NAME = "SkillMMO";
    public static final String MOD_VERSION = "0.0.1";
    public static final String MOD_VERSION_STRING = MOD_NAME + " v" + MOD_VERSION;

    @Override
    public void onInitialize() {
        logger.info("Starting...");

        AutoConfig.register(SkillMmoConfig.class, JanksonConfigSerializer::new);

        SkillMmoServerNetworking.register();

        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new SkillMmoResourceLoader());

        SkillMmoCommand.register(); // must be after resource loading

        logger.info("Battle stations ready! Time to test your mettle!");
    }
}
