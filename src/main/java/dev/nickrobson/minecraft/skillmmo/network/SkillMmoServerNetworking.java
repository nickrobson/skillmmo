package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class SkillMmoServerNetworking implements SkillMmoNetworking {
    private static final Logger logger = LoggerFactory.getLogger(SkillMmoServerNetworking.class);

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(C2S_PLAYER_SKILL_CHOICE, (server, player, handler, buf, responseSender) -> {
            Identifier skillId = buf.readIdentifier();
            SkillManager.getInstance().getSkill(skillId).ifPresent(skill -> {
                PlayerSkillManager.ChooseSkillLevelResult result = PlayerSkillManager.getInstance().chooseSkillLevel(player, skill);
                switch (result) {
                    case FAILURE_AT_MAX_LEVEL -> player.sendMessage(
                            new TranslatableText("skillmmo.feedback.player.skill_choice.failed_max_level")
                                    .setStyle(Style.EMPTY.withFormatting(Formatting.RED)),
                            false);
                    case FAILURE_NO_AVAILABLE_POINTS -> player.sendMessage(
                            new TranslatableText("skillmmo.feedback.player.skill_choice.failed_no_points")
                                    .setStyle(Style.EMPTY.withFormatting(Formatting.RED)),
                            false);
                }
            });

            logger.debug("Received skill choice from {}: {}", player.getGameProfile().getName(), skillId);
        });
    }

    private static void sendSkills(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        Set<Skill> skills = SkillManager.getInstance().getSkills();
        packetByteBuf.writeCollection(skills, SkillMmoNetworking::writeSkill);

        ServerPlayNetworking.send(player, S2C_SKILLS, packetByteBuf);
        logger.debug("Sent skills to player '{}': {}", player.getGameProfile().getName(), skills);
    }

    private static void sendExperienceLevelEquation(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        ExperienceLevelEquation experienceLevelEquation = ExperienceLevelEquation.getInstance();
        SkillMmoNetworking.writeExperienceLevelEquation(packetByteBuf, experienceLevelEquation);

        ServerPlayNetworking.send(player, S2C_EXPERIENCE_LEVEL_EQUATION, packetByteBuf);
        logger.debug("Sent experience level equation to player '{}': {}", player.getGameProfile().getName(), experienceLevelEquation);
    }

    public static void sendGenericData(ServerPlayerEntity player) {
        sendSkills(player);
        sendExperienceLevelEquation(player);
    }

    public static void sendPlayerXpInfo(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        long experience = PlayerExperienceManager.getInstance().getExperience(player);
        packetByteBuf.writeLong(experience);

        int availableSkillPoints = PlayerSkillPointManager.getInstance().getAvailableSkillPoints(player);
        packetByteBuf.writeVarInt(availableSkillPoints);

        ServerPlayNetworking.send(player, S2C_PLAYER_XP, packetByteBuf);
        logger.debug("Sent player xp to player '{}': {}, available skill points: {}", player.getGameProfile().getName(), experience, availableSkillPoints);
    }

    public static void sendPlayerSkills(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        Map<Identifier, Integer> playerSkillLevels = PlayerSkillManager.getInstance().getSkillLevels(player);
        packetByteBuf.writeMap(
                playerSkillLevels,
                PacketByteBuf::writeIdentifier,
                PacketByteBuf::writeVarInt
        );

        ServerPlayNetworking.send(player, S2C_PLAYER_SKILLS, packetByteBuf);
        logger.debug("Sent player skills to player '{}': {}", player.getGameProfile().getName(), playerSkillLevels);
    }

    public static void sendPlayerData(ServerPlayerEntity player) {
        sendPlayerXpInfo(player);
        sendPlayerSkills(player);
    }
}
