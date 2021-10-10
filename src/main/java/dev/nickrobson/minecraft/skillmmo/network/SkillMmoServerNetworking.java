package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Set;

public class SkillMmoServerNetworking implements SkillMmoNetworking {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(C2S_PLAYER_SKILL_CHOICE, (server, player, handler, buf, responseSender) -> {
            Identifier skillId = buf.readIdentifier();
            SkillManager.getInstance().getSkill(skillId)
                    .ifPresent(skill ->
                            PlayerSkillManager.getInstance().chooseSkillLevel(player, skill)
                    );
        });
    }

    public static void sendSkills(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        Set<Skill> skills = SkillManager.getInstance().getSkills();
        packetByteBuf.writeCollection(skills, SkillMmoNetworking::writeSkill);

        ServerPlayNetworking.send(player, S2C_SKILLS, packetByteBuf);
    }

    public static void sendPlayerXp(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        long experience = PlayerSkillManager.getInstance().getExperience(player);
        packetByteBuf.writeLong(experience);
        // TODO: send player level here? (how will levels work..?)

        int availableSkillPoints = PlayerSkillManager.getInstance().getAvailableSkillPoints(player);
        packetByteBuf.writeVarInt(availableSkillPoints);

        ServerPlayNetworking.send(player, S2C_PLAYER_XP, packetByteBuf);
    }

    public static void sendPlayerSkills(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        Map<Identifier, Integer> playerSkillLevels = PlayerSkillManager.getInstance().getSkills(player);
        packetByteBuf.writeMap(
                playerSkillLevels,
                PacketByteBuf::writeIdentifier,
                PacketByteBuf::writeVarInt
        );

        ServerPlayNetworking.send(player, S2C_PLAYER_SKILLS, packetByteBuf);
    }

    public static void sendPlayerData(ServerPlayerEntity player) {
        sendPlayerXp(player);
        sendPlayerSkills(player);
    }
}
