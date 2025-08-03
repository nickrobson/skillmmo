package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SkillMmoConfigurationC2SPacket(
        String modVersion
) implements CustomPayload {
    public static final Id<SkillMmoConfigurationC2SPacket> PACKET_ID = new Id<>(new Identifier(SkillMmoMod.MOD_ID, "configure_c2s"));
    public static final PacketCodec<PacketByteBuf, SkillMmoConfigurationC2SPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.STRING, SkillMmoConfigurationC2SPacket::modVersion, SkillMmoConfigurationC2SPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
