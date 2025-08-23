package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;

public record SetExperienceLevelEquationS2CPacket(
        ExperienceLevelEquation experienceLevelEquation
) implements CustomPayload {
    private static final PacketCodec<RegistryByteBuf, ExperienceLevelEquation> EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC = PacketCodec.ofStatic(SetExperienceLevelEquationS2CPacket::writeExperienceLevelEquation, SetExperienceLevelEquationS2CPacket::readExperienceLevelEquation);

    public static final Id<SetExperienceLevelEquationS2CPacket> PACKET_ID = new Id<>(Identifier.of(SkillMmoMod.MOD_ID, "set_experience_level_equation"));
    public static final PacketCodec<RegistryByteBuf, SetExperienceLevelEquationS2CPacket> PACKET_CODEC = EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC.xmap(SetExperienceLevelEquationS2CPacket::new, SetExperienceLevelEquationS2CPacket::experienceLevelEquation);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private static ExperienceLevelEquation readExperienceLevelEquation(@Nonnull PacketByteBuf packetByteBuf) {
        long baseCost = packetByteBuf.readLong();
        double multiplier = packetByteBuf.readDouble();
        double levelExponent = packetByteBuf.readDouble();

        return new ExperienceLevelEquation(baseCost, multiplier, levelExponent);
    }

    private static void writeExperienceLevelEquation(@Nonnull PacketByteBuf packetByteBuf, @Nonnull ExperienceLevelEquation equation) {
        packetByteBuf.writeLong(equation.getBaseCost());
        packetByteBuf.writeDouble(equation.getMultiplier());
        packetByteBuf.writeDouble(equation.getLevelExponent());
    }
}
