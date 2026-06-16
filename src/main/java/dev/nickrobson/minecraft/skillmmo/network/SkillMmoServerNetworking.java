package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class SkillMmoServerNetworking {
    private static final Logger logger = LogManager.getLogger(SkillMmoServerNetworking.class);

    public static void registerReceivers() {
        // Configuration
        ServerConfigurationConnectionEvents.CONFIGURE.register((handler, server) -> {
            if (ServerConfigurationNetworking.canSend(handler, SkillMmoConfigurationS2CPacket.PACKET_ID)) {
                Set<Skill> skillSet = SkillManager.getInstance().getInstalledSkills();
                ExperienceLevelEquation experienceLevelEquation = ExperienceLevelEquation.getInstance();
                handler.addTask(new SkillMmoConfigurationTask(SkillMmoMod.MOD_VERSION_STRING, skillSet, experienceLevelEquation));
            } else {
                handler.disconnect(Component.literal("This server requires you to install %s in order to join.".formatted(SkillMmoMod.MOD_VERSION_STRING)));
            }
        });

        ServerConfigurationNetworking.registerGlobalReceiver(SkillMmoConfigurationC2SPacket.PACKET_ID, (payload, context) -> {
            if (SkillMmoMod.MOD_VERSION_STRING.equals(payload.modVersion())) {
                context.networkHandler().completeTask(SkillMmoConfigurationTask.KEY);
            } else {
                context.networkHandler().disconnect(Component.literal("This server is running %s but you are using %s. Please install the same mod version as the server.".formatted(SkillMmoMod.MOD_VERSION_STRING, payload.modVersion())));
            }
        });

        // Play
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            sendPlayerData(handler.player);
        }));

        ServerPlayNetworking.registerGlobalReceiver(PlayerSkillChoiceC2SPacket.PACKET_ID, (payload, context) -> {
            Identifier skillId = payload.skillId();
            ServerPlayer player = context.player();

            SkillManager.getInstance().getSkill(skillId).ifPresent(skill -> {
                PlayerSkillManager.ChooseSkillLevelResult result = PlayerSkillManager.getInstance().chooseSkillLevel(player, skill);
                switch (result) {
                    case FAILURE_AT_MAX_LEVEL -> player.displayClientMessage(
                            Component.translatable("skillmmo.feedback.player.skill_choice.failed_max_level", skill.getMaxLevel())
                                    .setStyle(Style.EMPTY.applyFormat(ChatFormatting.RED)),
                            false);
                    case FAILURE_NO_AVAILABLE_POINTS -> player.displayClientMessage(
                            Component.translatable("skillmmo.feedback.player.skill_choice.failed_no_points")
                                    .setStyle(Style.EMPTY.applyFormat(ChatFormatting.RED)),
                            false);
                }
                sendPlayerData(player);
            });

            logger.debug("Received skill choice from {}: {}", player.getGameProfile().name(), skillId);
        });
    }

    public static void sendPlayerXpInfo(ServerPlayer player) {
        long experience = PlayerExperienceManager.getInstance().getExperience(player);
        int availableSkillPoints = PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player);

        ServerPlayNetworking.send(player, new SetPlayerExperienceS2CPacket(experience, availableSkillPoints));
        logger.debug("Sent player xp to player '{}': {}, available skill points: {}", player.getGameProfile().name(), experience, availableSkillPoints);
    }

    public static void sendPlayerSkills(ServerPlayer player) {
        Map<Identifier, Integer> playerSkillLevels = PlayerSkillManager.getInstance().getSkillLevels(player);

        ServerPlayNetworking.send(player, new SetPlayerSkillsS2CPacket(playerSkillLevels));
        logger.debug("Sent player skills to player '{}': {}", player.getGameProfile().name(), playerSkillLevels);
    }

    public static void sendPlayerData(ServerPlayer player) {
        sendPlayerXpInfo(player);
        sendPlayerSkills(player);
    }
}
