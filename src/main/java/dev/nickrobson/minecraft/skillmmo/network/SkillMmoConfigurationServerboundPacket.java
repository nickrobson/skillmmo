package dev.nickrobson.minecraft.skillmmo.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;

public record SkillMmoConfigurationServerboundPacket(
        String modVersion
) implements CustomPacketPayload {
    public static final Type<SkillMmoConfigurationServerboundPacket> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "configure_serverbound"));
    public static final StreamCodec<FriendlyByteBuf, SkillMmoConfigurationServerboundPacket> PACKET_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, SkillMmoConfigurationServerboundPacket::modVersion, SkillMmoConfigurationServerboundPacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
