package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public record SkillMmoConfigurationS2CPacket(
        String modVersion,
        Set<Skill> skills,
        ExperienceLevelEquation experienceLevelEquation
) implements CustomPayload {
    public static final CustomPayload.Id<SkillMmoConfigurationS2CPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of(SkillMmoMod.MOD_ID, "configure_s2c"));

    private static final PacketCodec<PacketByteBuf, Skill> SKILL_PACKET_CODEC = PacketCodec.ofStatic(SkillMmoConfigurationS2CPacket::writeSkill, SkillMmoConfigurationS2CPacket::readSkill);
    private static final PacketCodec<PacketByteBuf, ExperienceLevelEquation> EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC = PacketCodec.ofStatic(SkillMmoConfigurationS2CPacket::writeExperienceLevelEquation, SkillMmoConfigurationS2CPacket::readExperienceLevelEquation);

    public static final PacketCodec<PacketByteBuf, SkillMmoConfigurationS2CPacket> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            SkillMmoConfigurationS2CPacket::modVersion,
            SKILL_PACKET_CODEC.collect(PacketCodecs.toCollection(SkillMmoConfigurationS2CPacket::newSet)),
            SkillMmoConfigurationS2CPacket::skills,
            EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC,
            SkillMmoConfigurationS2CPacket::experienceLevelEquation,
            SkillMmoConfigurationS2CPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private static <T> Set<T> newSet(int capacity) {
        return new HashSet<>(capacity);
    }

    private static Skill readSkill(@Nonnull PacketByteBuf packetByteBuf) {
        Identifier id = packetByteBuf.readIdentifier();
        Text nameText = TextCodecs.PACKET_CODEC.decode(packetByteBuf);
        Text descriptionText = TextCodecs.PACKET_CODEC.decode(packetByteBuf);
        int maxLevel = packetByteBuf.readVarInt();
        Item iconItem = Registries.ITEM.get(packetByteBuf.readIdentifier());

        return new Skill(id, nameText, descriptionText, maxLevel, iconItem);
    }

    private static void writeSkill(@Nonnull PacketByteBuf packetByteBuf, @Nonnull Skill skill) {
        packetByteBuf.writeIdentifier(skill.getId());
        TextCodecs.PACKET_CODEC.encode(packetByteBuf, skill.getName());
        TextCodecs.PACKET_CODEC.encode(packetByteBuf, skill.getDescription());
        packetByteBuf.writeVarInt(skill.getMaxLevel());
        packetByteBuf.writeIdentifier(Registries.ITEM.getId(skill.getIconItem()));
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
