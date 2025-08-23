package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record SetPlayerSkillsS2CPacket(
        Map<Identifier, Integer> playerSkillLevels
) implements CustomPayload {
    public static final Id<SetPlayerSkillsS2CPacket> PACKET_ID = new Id<>(Identifier.of(SkillMmoMod.MOD_ID, "set_player_skills"));
    public static final PacketCodec<RegistryByteBuf, SetPlayerSkillsS2CPacket> PACKET_CODEC = PacketCodecs.map(SetPlayerSkillsS2CPacket::toMap, Identifier.PACKET_CODEC, PacketCodecs.VAR_INT).xmap(SetPlayerSkillsS2CPacket::new, SetPlayerSkillsS2CPacket::playerSkillLevels).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private static <K, V> Map<K, V> toMap(int capacity) {
        return new HashMap<>(capacity);
    }
}
