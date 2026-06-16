package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SkillMmoConfigurationC2SPacket(
        String modVersion
) implements CustomPacketPayload {
    public static final Type<SkillMmoConfigurationC2SPacket> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "configure_c2s"));
    public static final StreamCodec<FriendlyByteBuf, SkillMmoConfigurationC2SPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SkillMmoConfigurationC2SPacket::modVersion, SkillMmoConfigurationC2SPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
