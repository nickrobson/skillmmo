package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetPlayerExperienceS2CPacket(
        long experience,
        int availableSkillPoints
) implements CustomPayload {
    public static final Id<SetPlayerExperienceS2CPacket> PACKET_ID = new Id<>(Identifier.of(SkillMmoMod.MOD_ID, "set_player_xp"));
    public static final PacketCodec<RegistryByteBuf, SetPlayerExperienceS2CPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.VAR_LONG, SetPlayerExperienceS2CPacket::experience, PacketCodecs.VAR_INT, SetPlayerExperienceS2CPacket::availableSkillPoints, SetPlayerExperienceS2CPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
