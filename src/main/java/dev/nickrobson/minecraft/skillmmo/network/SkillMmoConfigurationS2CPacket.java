package dev.nickrobson.minecraft.skillmmo.network;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.experience.ExperienceLevelEquation;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;

public record SkillMmoConfigurationS2CPacket(
        String modVersion,
        Set<Skill> skills,
        ExperienceLevelEquation experienceLevelEquation
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SkillMmoConfigurationS2CPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "configure_s2c"));

    private static final StreamCodec<FriendlyByteBuf, Skill> SKILL_PACKET_CODEC = StreamCodec.of(SkillMmoConfigurationS2CPacket::writeSkill, SkillMmoConfigurationS2CPacket::readSkill);
    private static final StreamCodec<FriendlyByteBuf, ExperienceLevelEquation> EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC = StreamCodec.of(SkillMmoConfigurationS2CPacket::writeExperienceLevelEquation, SkillMmoConfigurationS2CPacket::readExperienceLevelEquation);

    public static final StreamCodec<FriendlyByteBuf, SkillMmoConfigurationS2CPacket> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SkillMmoConfigurationS2CPacket::modVersion,
            SKILL_PACKET_CODEC.apply(ByteBufCodecs.collection(SkillMmoConfigurationS2CPacket::newSet)),
            SkillMmoConfigurationS2CPacket::skills,
            EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC,
            SkillMmoConfigurationS2CPacket::experienceLevelEquation,
            SkillMmoConfigurationS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

    private static <T> Set<T> newSet(int capacity) {
        return new HashSet<>(capacity);
    }

    private static Skill readSkill(@Nonnull FriendlyByteBuf packetByteBuf) {
        Identifier id = packetByteBuf.readIdentifier();
        Component nameText = ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.decode(packetByteBuf);
        Component descriptionText = ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.decode(packetByteBuf);
        int maxLevel = packetByteBuf.readVarInt();
        Item iconItem = BuiltInRegistries.ITEM.getValue(packetByteBuf.readIdentifier());

        return new Skill(id, nameText, descriptionText, maxLevel, iconItem);
    }

    private static void writeSkill(@Nonnull FriendlyByteBuf packetByteBuf, @Nonnull Skill skill) {
        packetByteBuf.writeIdentifier(skill.getId());
        ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.encode(packetByteBuf, skill.getName());
        ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.encode(packetByteBuf, skill.getDescription());
        packetByteBuf.writeVarInt(skill.getMaxLevel());
        packetByteBuf.writeIdentifier(BuiltInRegistries.ITEM.getKey(skill.getIconItem()));
    }

    private static ExperienceLevelEquation readExperienceLevelEquation(@Nonnull FriendlyByteBuf packetByteBuf) {
        long baseCost = packetByteBuf.readLong();
        double multiplier = packetByteBuf.readDouble();
        double levelExponent = packetByteBuf.readDouble();

        return new ExperienceLevelEquation(baseCost, multiplier, levelExponent);
    }

    private static void writeExperienceLevelEquation(@Nonnull FriendlyByteBuf packetByteBuf, @Nonnull ExperienceLevelEquation equation) {
        packetByteBuf.writeLong(equation.getBaseCost());
        packetByteBuf.writeDouble(equation.getMultiplier());
        packetByteBuf.writeDouble(equation.getLevelExponent());
    }
}
