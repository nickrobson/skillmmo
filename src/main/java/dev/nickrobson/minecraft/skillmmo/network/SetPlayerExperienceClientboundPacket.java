package dev.nickrobson.minecraft.skillmmo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;

public record SetPlayerExperienceClientboundPacket(
        long experience,
        int availableSkillPoints
) implements CustomPacketPayload {
    public static final Type<SetPlayerExperienceClientboundPacket> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "set_player_xp"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetPlayerExperienceClientboundPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_LONG, SetPlayerExperienceClientboundPacket::experience, ByteBufCodecs.VAR_INT, SetPlayerExperienceClientboundPacket::availableSkillPoints, SetPlayerExperienceClientboundPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
