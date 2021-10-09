package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.skill.PlayerSkillManager;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class SkillMmoServerNetworking implements SkillMmoNetworking {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, packetSender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            sendSkills(player);
            sendPlayerSkills(player);
            sendPlayerXp(player);
        });

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

    public static void sendPlayerSkills(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();

        Map<Identifier, Byte> playerSkillLevels = PlayerSkillManager.getInstance().getSkills(player);
        packetByteBuf.writeMap(
                playerSkillLevels,
                PacketByteBuf::writeIdentifier,
                (BiConsumer<PacketByteBuf, Byte>) PacketByteBuf::writeByte
        );

        ServerPlayNetworking.send(player, S2C_PLAYER_SKILLS, packetByteBuf);
    }

    public static void sendPlayerXp(ServerPlayerEntity player) {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        // TODO - set xp, level, levels available to spend

        ServerPlayNetworking.send(player, S2C_PLAYER_XP, packetByteBuf);
    }
}
