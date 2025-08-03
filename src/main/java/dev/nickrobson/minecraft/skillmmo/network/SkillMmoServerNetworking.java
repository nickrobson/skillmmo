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
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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
                handler.addTask(new SkillMmoConfigurationTask(SkillMmoMod.MOD_VERSION_STRING));
            } else {
                handler.disconnect(Text.literal("This server requires you to install %s in order to join.".formatted(SkillMmoMod.MOD_VERSION_STRING)));
            }
        });

        ServerConfigurationNetworking.registerGlobalReceiver(SkillMmoConfigurationC2SPacket.PACKET_ID, (payload, context) -> {
            if (SkillMmoMod.MOD_VERSION_STRING.equals(payload.modVersion())) {
                context.networkHandler().completeTask(SkillMmoConfigurationTask.KEY);
            } else {
                context.networkHandler().disconnect(Text.literal("This server is running %s but you are using %s. Please install the same mod version as the server.".formatted(SkillMmoMod.MOD_VERSION_STRING, payload.modVersion())));
            }
        });

        // Play
        ServerPlayNetworking.registerGlobalReceiver(PlayerSkillChoiceC2SPacket.PACKET_ID, (payload, context) -> {
            Identifier skillId = payload.skillId();
            ServerPlayerEntity player = context.player();

            SkillManager.getInstance().getSkill(skillId).ifPresent(skill -> {
                PlayerSkillManager.ChooseSkillLevelResult result = PlayerSkillManager.getInstance().chooseSkillLevel(player, skill);
                switch (result) {
                    case FAILURE_AT_MAX_LEVEL -> player.sendMessage(
                            Text.translatable("skillmmo.feedback.player.skill_choice.failed_max_level", skill.getMaxLevel())
                                    .setStyle(Style.EMPTY.withFormatting(Formatting.RED)),
                            false);
                    case FAILURE_NO_AVAILABLE_POINTS -> player.sendMessage(
                            Text.translatable("skillmmo.feedback.player.skill_choice.failed_no_points")
                                    .setStyle(Style.EMPTY.withFormatting(Formatting.RED)),
                            false);
                }
                sendPlayerData(player);
            });

            logger.debug("Received skill choice from {}: {}", player.getGameProfile().getName(), skillId);
        });
    }

    private static void sendSkills(ServerPlayerEntity player) {
        Set<Skill> skills = SkillManager.getInstance().getSkills();

        ServerPlayNetworking.send(player, new SetSkillsS2CPacket(skills));
        logger.debug("Sent skills to player '{}': {}", player.getGameProfile().getName(), skills);
    }

    private static void sendExperienceLevelEquation(ServerPlayerEntity player) {
        ExperienceLevelEquation experienceLevelEquation = ExperienceLevelEquation.getInstance();

        ServerPlayNetworking.send(player, new SetExperienceLevelEquationS2CPacket(experienceLevelEquation));
        logger.debug("Sent experience level equation to player '{}': {}", player.getGameProfile().getName(), experienceLevelEquation);
    }

    public static void sendGenericData(ServerPlayerEntity player) {
        sendSkills(player);
        sendExperienceLevelEquation(player);
    }

    public static void sendPlayerXpInfo(ServerPlayerEntity player) {
        long experience = PlayerExperienceManager.getInstance().getExperience(player);
        int availableSkillPoints = PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player);

        ServerPlayNetworking.send(player, new SetPlayerExperienceS2CPacket(experience, availableSkillPoints));
        logger.debug("Sent player xp to player '{}': {}, available skill points: {}", player.getGameProfile().getName(), experience, availableSkillPoints);
    }

    public static void sendPlayerSkills(ServerPlayerEntity player) {
        Map<Identifier, Integer> playerSkillLevels = PlayerSkillManager.getInstance().getSkillLevels(player);

        ServerPlayNetworking.send(player, new SetPlayerSkillsS2CPacket(playerSkillLevels));
        logger.debug("Sent player skills to player '{}': {}", player.getGameProfile().getName(), playerSkillLevels);
    }

    public static void sendPlayerData(ServerPlayerEntity player) {
        sendPlayerXpInfo(player);
        sendPlayerSkills(player);
    }
}
