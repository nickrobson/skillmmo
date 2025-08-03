package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SkillMmoConfigurationS2CPacket(
        String modVersion
) implements CustomPayload {
    public static final CustomPayload.Id<SkillMmoConfigurationS2CPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier(SkillMmoMod.MOD_ID, "configure_s2c"));
    public static final PacketCodec<PacketByteBuf, SkillMmoConfigurationS2CPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, SkillMmoConfigurationS2CPacket::modVersion, SkillMmoConfigurationS2CPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
