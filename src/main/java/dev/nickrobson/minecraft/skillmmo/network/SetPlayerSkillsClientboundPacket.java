package dev.nickrobson.minecraft.skillmmo.network;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;

public record SetPlayerSkillsClientboundPacket(
        Map<Identifier, Integer> playerSkillLevels
) implements CustomPacketPayload {
    public static final Type<SetPlayerSkillsClientboundPacket> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "set_player_skills"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetPlayerSkillsClientboundPacket> PACKET_CODEC = ByteBufCodecs.map(SetPlayerSkillsClientboundPacket::toMap, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_INT).map(SetPlayerSkillsClientboundPacket::new, SetPlayerSkillsClientboundPacket::playerSkillLevels).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

    private static <K, V> Map<K, V> toMap(int capacity) {
        return new HashMap<>(capacity);
    }
}
