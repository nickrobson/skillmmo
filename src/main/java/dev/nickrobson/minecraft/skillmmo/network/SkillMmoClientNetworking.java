package dev.nickrobson.minecraft.skillmmo.network;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;

public class SkillMmoClientNetworking {
    private static final Logger logger = LogManager.getLogger(SkillMmoClientNetworking.class);

    public static void registerReceivers() {
        // Configuration
        ClientConfigurationNetworking.registerGlobalReceiver(SkillMmoConfigurationS2CPacket.PACKET_ID, (packet, context) -> {
            if (SkillMmoMod.MOD_VERSION_STRING.equals(packet.modVersion())) {
                SkillMmoMod.isModEnabled = true;
                SkillManager.getInstance().initSkills(packet.skills());
                ExperienceLevelEquation.setInstance(packet.experienceLevelEquation());
            }
            context.responseSender().sendPacket(new SkillMmoConfigurationC2SPacket(SkillMmoMod.MOD_VERSION_STRING));
        });

        // Play
        ClientPlayNetworking.registerGlobalReceiver(SetPlayerSkillsS2CPacket.PACKET_ID, (payload, context) -> {
            Map<Identifier, Integer> playerSkillLevels = payload.playerSkillLevels();
            logger.debug("Received player skills: {}", playerSkillLevels);

            context.client().execute(() -> {
                if (context.player() == null) {
                    logger.warn("Client player is null on {}", SetPlayerSkillsS2CPacket.PACKET_ID.id());
                } else {
                    PlayerSkillManager.getInstance().updateSkillLevels(context.player(), playerSkillLevels);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SetPlayerExperienceS2CPacket.PACKET_ID, (payload, context) -> {
            long experience = payload.experience();
            int availableSkillPoints = payload.availableSkillPoints();

            logger.debug("Received player xp: {}, available skill points: {}", experience, availableSkillPoints);

            context.client().execute(() -> {
                if (context.player() == null) {
                    logger.warn("Client player is null on {}", SetPlayerExperienceS2CPacket.PACKET_ID.id());
                } else {
                    PlayerExperienceManager.getInstance().setExperience(context.player(), experience);
                    PlayerSkillPointManager.getInstance().setAvailableSkillPoints(context.player(), availableSkillPoints);
                }
            });
        });
    }

    public static void sendChoosePlayerSkill(Skill skill) {
        ClientPlayNetworking.send(new PlayerSkillChoiceC2SPacket(skill.getId()));
        logger.debug("Sent player skill choice: {}", skill.getId());
    }
}
