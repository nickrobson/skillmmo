package dev.nickrobson.minecraft.skillmmo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import dev.nickrobson.minecraft.skillmmo.command.SkillMmoCommands;
import dev.nickrobson.minecraft.skillmmo.config.SkillMmoConfig;
import dev.nickrobson.minecraft.skillmmo.data.SkillMmoResourceReloadListener;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoNetworking;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoServerNetworking;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.PlayerSkillUnlockManager;

public class SkillMmoMod implements ModInitializer {
    private static final Logger logger = LogManager.getLogger(SkillMmoMod.class);
    public static boolean isModEnabled = true;

    public static final String MOD_ID = "skillmmo";
    public static final String MOD_NAME = "SkillMMO";
    public static final String MOD_VERSION = "0.1.13";
    public static final String MOD_VERSION_STRING = MOD_NAME + " v" + MOD_VERSION;

    @Override
    public void onInitialize() {
        logger.info("Starting {}...", MOD_VERSION_STRING);

        SkillMmoConfig config = AutoConfig.register(SkillMmoConfig.class, JanksonConfigSerializer::new).getConfig();

        ExperienceLevelEquation.setInstance(new ExperienceLevelEquation(config.expBaseCost, config.expMultiplier, config.expLevelExponent));

        SkillMmoNetworking.registerPackets();
        SkillMmoServerNetworking.registerReceivers();

        ResourceLoader.get(PackType.SERVER_DATA)
                .registerReloadListener(Identifier.fromNamespaceAndPath("skillmmo", "resources"), new SkillMmoResourceReloadListener());

        SkillMmoCommands.register(); // must be after resource loading

        PlayerSkillManager.getInstance().register();
        PlayerSkillUnlockManager.getInstance().register();

        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            SkillMmoDebugger.printDebugInfo();
        });

        logger.info("Ready! Time to test your mettle!");
    }
}
