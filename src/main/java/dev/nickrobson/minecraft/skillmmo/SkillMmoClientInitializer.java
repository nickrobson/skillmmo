package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.network.SkillMmoClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkillMmoClientInitializer implements ClientModInitializer {
    private static final Logger logger = LogManager.getLogger(SkillMmoClientInitializer.class);

    @Override
    public void onInitializeClient() {
        SkillMmoClientNetworking.init();

        logger.info("Client ready!");
    }
}
