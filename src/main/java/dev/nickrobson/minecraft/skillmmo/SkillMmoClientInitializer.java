package dev.nickrobson.minecraft.skillmmo;

import dev.nickrobson.minecraft.skillmmo.gui.SkillsClientScreen;
import dev.nickrobson.minecraft.skillmmo.network.SkillMmoClientNetworking;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;

public class SkillMmoClientInitializer implements ClientModInitializer {
    private static final Logger logger = LogManager.getLogger(SkillMmoClientInitializer.class);

    private static final KeyMapping.Category SKILLMMO_KEYBINDING_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("skillmmo", "keybindings"));
    private static final KeyMapping OPEN_SKILLS_KEYBINDING = new KeyMapping("skillmmo.keybindings.binding.open_skills", GLFW.GLFW_KEY_K, SKILLMMO_KEYBINDING_CATEGORY);

    @Override
    public void onInitializeClient() {
        SkillMmoClientNetworking.registerReceivers();

        ClientConfigurationConnectionEvents.INIT.register((handler, context) -> {
            SkillManager.getInstance().initSkills(Collections.emptySet());
            SkillMmoMod.isModEnabled = false;
        });

        KeyMapping openSkillsKeyBinding = KeyBindingHelper.registerKeyBinding(OPEN_SKILLS_KEYBINDING);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openSkillsKeyBinding.consumeClick()) {
                if (SkillMmoMod.isModEnabled && Minecraft.getInstance().screen == null) {
                    SkillsClientScreen.open();
                }
            }
        });

        logger.info("Client ready!");
    }
}
