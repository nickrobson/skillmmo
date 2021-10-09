package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SkillMmoClientNetworking implements SkillMmoNetworking {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(S2C_SKILLS, (client, handler, buf, responseSender) -> {
            Set<Skill> skillSet = buf.readCollection(HashSet::new, SkillMmoNetworking::readSkill);
            SkillManager.getInstance().initSkills(skillSet);

            SkillManager.getInstance().getSkills().forEach(skill -> {
                String message = new StringFormattedMessage(
                        "- %s: %s (%d levels)",
                        skill.getId(),
                        skill.getTranslationKey(),
                        skill.getMaxLevel().getLevel()
                ).getFormattedMessage();
                client.inGameHud.getChatHud().addMessage(new LiteralText(message));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(S2C_PLAYER_SKILLS, (client, handler, buf, responseSender) -> {
            Map<String, Byte> playerSkillLevels = buf.readMap(
                    HashMap::new,
                    PacketByteBuf::readString,
                    PacketByteBuf::readByte
            );
            if (client.player != null) {
                PlayerSkillManager.getInstance().setSkillLevels(client.player, playerSkillLevels);
            }

            playerSkillLevels.forEach((skillId, level) -> {
                String message = new StringFormattedMessage(
                        "- %s: level %d",
                        skillId,
                        level
                ).getFormattedMessage();
                client.inGameHud.getChatHud().addMessage(new LiteralText(message));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(S2C_PLAYER_XP, (client, handler, buf, responseSender) -> {

        });
    }

    public static void sendChoosePlayerSkill(SkillLevel level) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeString(level.getSkillId());

        ClientPlayNetworking.send(C2S_PLAYER_SKILL_CHOICE, packetByteBuf);
    }
}
