package dev.nickrobson.minecraft.skillmmo.network;

import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.NonNull;

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

public record SkillMmoConfigurationClientboundPacket(
        String modVersion,
        Set<Skill> skills,
        ExperienceLevelEquation experienceLevelEquation
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SkillMmoConfigurationClientboundPacket> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(SkillMmoMod.MOD_ID, "configure_clientbound"));

    private static final StreamCodec<FriendlyByteBuf, Skill> SKILL_PACKET_CODEC = StreamCodec.of(SkillMmoConfigurationClientboundPacket::writeSkill, SkillMmoConfigurationClientboundPacket::readSkill);
    private static final StreamCodec<FriendlyByteBuf, ExperienceLevelEquation> EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC = StreamCodec.of(SkillMmoConfigurationClientboundPacket::writeExperienceLevelEquation, SkillMmoConfigurationClientboundPacket::readExperienceLevelEquation);

    public static final StreamCodec<FriendlyByteBuf, SkillMmoConfigurationClientboundPacket> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SkillMmoConfigurationClientboundPacket::modVersion,
            SKILL_PACKET_CODEC.apply(ByteBufCodecs.collection(SkillMmoConfigurationClientboundPacket::newSet)),
            SkillMmoConfigurationClientboundPacket::skills,
            EXPERIENCE_LEVEL_EQUATION_PACKET_CODEC,
            SkillMmoConfigurationClientboundPacket::experienceLevelEquation,
            SkillMmoConfigurationClientboundPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }

    private static <T> Set<T> newSet(int capacity) {
        return new HashSet<>(capacity);
    }

    private static Skill readSkill(@NonNull FriendlyByteBuf packetByteBuf) {
        Identifier id = packetByteBuf.readIdentifier();
        Component nameText = ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.decode(packetByteBuf);
        Component descriptionText = ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.decode(packetByteBuf);
        int maxLevel = packetByteBuf.readVarInt();
        Item iconItem = BuiltInRegistries.ITEM.getValue(packetByteBuf.readIdentifier());

        return new Skill(id, nameText, descriptionText, maxLevel, iconItem);
    }

    private static void writeSkill(@NonNull FriendlyByteBuf packetByteBuf, @NonNull Skill skill) {
        packetByteBuf.writeIdentifier(skill.getId());
        ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.encode(packetByteBuf, skill.getName());
        ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.encode(packetByteBuf, skill.getDescription());
        packetByteBuf.writeVarInt(skill.getMaxLevel());
        packetByteBuf.writeIdentifier(BuiltInRegistries.ITEM.getKey(skill.getIconItem()));
    }

    private static ExperienceLevelEquation readExperienceLevelEquation(@NonNull FriendlyByteBuf packetByteBuf) {
        long baseCost = packetByteBuf.readLong();
        double multiplier = packetByteBuf.readDouble();
        double levelExponent = packetByteBuf.readDouble();

        return new ExperienceLevelEquation(baseCost, multiplier, levelExponent);
    }

    private static void writeExperienceLevelEquation(@NonNull FriendlyByteBuf packetByteBuf, @NonNull ExperienceLevelEquation equation) {
        packetByteBuf.writeLong(equation.getBaseCost());
        packetByteBuf.writeDouble(equation.getMultiplier());
        packetByteBuf.writeDouble(equation.getLevelExponent());
    }
}
