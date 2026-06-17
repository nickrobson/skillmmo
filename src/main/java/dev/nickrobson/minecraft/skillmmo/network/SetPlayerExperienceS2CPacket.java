package dev.nickrobson.minecraft.skillmmo.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;

public record SetPlayerExperienceS2CPacket(
        long experience,
        int availableSkillPoints
) implements CustomPacketPayload {
    public static final Type<SetPlayerExperienceS2CPacket> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "set_player_xp"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetPlayerExperienceS2CPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_LONG, SetPlayerExperienceS2CPacket::experience, ByteBufCodecs.VAR_INT, SetPlayerExperienceS2CPacket::availableSkillPoints, SetPlayerExperienceS2CPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
