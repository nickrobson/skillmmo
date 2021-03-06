package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.experience.PlayerExperienceManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillPointManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SkillMmoClientNetworking implements SkillMmoNetworking {
    private static final Logger logger = LogManager.getLogger(SkillMmoClientNetworking.class);

    public static void register() {
        ClientLoginNetworking.registerGlobalReceiver(LOGIN_HANDSHAKE, (client, handler, packetByteBuf, packetSender) -> {
            SkillMmoMod.isModEnabled = true;
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeString(SkillMmoMod.MOD_VERSION_STRING);
            return CompletableFuture.completedFuture(buf);
        });

        ClientPlayNetworking.registerGlobalReceiver(S2C_SKILLS, (client, handler, buf, responseSender) -> {
            SkillMmoMod.isModEnabled = true;
            Set<Skill> skillSet = buf.readCollection(HashSet::new, SkillMmoNetworking::readSkill);
            SkillManager.getInstance().initSkills(skillSet);
            logger.debug("Received skills: {}", skillSet);
        });

        ClientPlayNetworking.registerGlobalReceiver(S2C_EXPERIENCE_LEVEL_EQUATION, (client, handler, buf, responseSender) -> {
            ExperienceLevelEquation experienceLevelEquation = SkillMmoNetworking.readExperienceLevelEquation(buf);
            ExperienceLevelEquation.setInstance(experienceLevelEquation);
            logger.debug("Received experience level equation: {}", experienceLevelEquation);
        });

        ClientPlayNetworking.registerGlobalReceiver(S2C_PLAYER_SKILLS, (client, handler, buf, responseSender) -> {
            Map<Identifier, Integer> playerSkillLevels = buf.readMap(
                    HashMap::new,
                    PacketByteBuf::readIdentifier,
                    PacketByteBuf::readVarInt
            );
            logger.debug("Received player skills: {}", playerSkillLevels);

            client.execute(() -> {
                if (client.player == null) {
                    logger.warn("Client player is null on {}", S2C_PLAYER_SKILLS);
                } else {
                    PlayerSkillManager.getInstance().updateSkillLevels(client.player, playerSkillLevels);
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(S2C_PLAYER_XP, (client, handler, buf, responseSender) -> {
            long experience = buf.readLong();
            int availableSkillPoints = buf.readVarInt();

            logger.debug("Received player xp: {}, available skill points: {}", experience, availableSkillPoints);

            client.execute(() -> {
                if (client.player == null) {
                    logger.warn("Client player is null on {}", S2C_PLAYER_XP);
                } else {
                    PlayerExperienceManager.getInstance().setExperience(client.player, experience);
                    PlayerSkillPointManager.getInstance().setAvailableSkillPoints(client.player, availableSkillPoints);
                }
            });
        });
    }

    public static void sendChoosePlayerSkill(Skill skill) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeIdentifier(skill.getId());

        ClientPlayNetworking.send(C2S_PLAYER_SKILL_CHOICE, packetByteBuf);
        logger.debug("Sent player skill choice: {}", skill.getId());
    }
}
