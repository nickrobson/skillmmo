package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.data.SkillMmoResourceLoader;
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

    @Override
    public void onInitialize() {
        logger.info("Starting...");

        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new SkillMmoResourceLoader());

        AutoConfig.register(SkillMmoConfig.class, JanksonConfigSerializer::new);

        logger.info("Started! Time to test your mettle!");
    }
}
