package dev.nickrobson.minecraft.skillmmo;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;

import dev.nickrobson.minecraft.skillmmo.gui.SkillsClientScreen;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoClientNetworking;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;

public class SkillMmoClientInitializer implements ClientModInitializer {
    private static final Logger logger = LogManager.getLogger(SkillMmoClientInitializer.class);

    private static final KeyMapping.Category SKILLMMO_KEYMAPPING_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("skillmmo", "keymappings"));
    private static final KeyMapping OPEN_SKILLS_KEYMAPPING = new KeyMapping("skillmmo.keymappings.mapping.open_skills", GLFW.GLFW_KEY_K, SKILLMMO_KEYMAPPING_CATEGORY);

    @Override
    public void onInitializeClient() {
        SkillMmoClientNetworking.registerReceivers();

        ClientConfigurationConnectionEvents.INIT.register((handler, context) -> {
            SkillManager.getInstance().initSkills(Collections.emptySet());
            SkillMmoMod.isModEnabled = false;
        });

        KeyMapping openSkillsKeyMapping = KeyMappingHelper.registerKeyMapping(OPEN_SKILLS_KEYMAPPING);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSkillsKeyMapping.consumeClick()) {
                if (SkillMmoMod.isModEnabled && Minecraft.getInstance().screen == null) {
                    SkillsClientScreen.open();
                }
            }
        });

        logger.info("Client ready!");
    }
}
