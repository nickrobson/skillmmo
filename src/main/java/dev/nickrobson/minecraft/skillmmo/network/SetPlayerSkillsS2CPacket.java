package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SetPlayerSkillsS2CPacket(
        Map<Identifier, Integer> playerSkillLevels
) implements CustomPacketPayload {
    public static final Type<SetPlayerSkillsS2CPacket> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "set_player_skills"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetPlayerSkillsS2CPacket> PACKET_CODEC = ByteBufCodecs.map(SetPlayerSkillsS2CPacket::toMap, Identifier.STREAM_CODEC, ByteBufCodecs.VAR_INT).map(SetPlayerSkillsS2CPacket::new, SetPlayerSkillsS2CPacket::playerSkillLevels).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

    private static <K, V> Map<K, V> toMap(int capacity) {
        return new HashMap<>(capacity);
    }
}
