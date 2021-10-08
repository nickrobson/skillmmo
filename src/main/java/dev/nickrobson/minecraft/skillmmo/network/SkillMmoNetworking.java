package dev.nickrobson.minecraft.skillmmo.network;

import dev.nickrobson.minecraft.skillmmo.SkillMmoMod;
import dev.nickrobson.minecraft.skillmmo.skill.Skill;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevel;
import dev.nickrobson.minecraft.skillmmo.skill.SkillLevelUnlockType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SkillMmoNetworking {
    Identifier C2S_PLAYER_SKILL_CHOICE = new Identifier(SkillMmoMod.MOD_ID, "player_skill_choice");

    Identifier S2C_SKILLS = new Identifier(SkillMmoMod.MOD_ID, "skills");
    Identifier S2C_PLAYER_SKILLS = new Identifier(SkillMmoMod.MOD_ID, "player_skills");
    Identifier S2C_PLAYER_XP = new Identifier(SkillMmoMod.MOD_ID, "player_xp");

    @Nullable
    static String readNullableString(@Nonnull PacketByteBuf packetByteBuf) {
        return packetByteBuf
                .readOptional(PacketByteBuf::readString)
                .orElse(null);
    }

    static void writeNullableString(@Nonnull PacketByteBuf packetByteBuf, @Nullable String string) {
        packetByteBuf.writeOptional(
                Optional.ofNullable(string),
                PacketByteBuf::writeString
        );
    }

    static Skill readSkill(@Nonnull PacketByteBuf packetByteBuf) {
        String id = packetByteBuf.readString();
        String translationKey = readNullableString(packetByteBuf);
        String untranslatedName = readNullableString(packetByteBuf);
        Set<SkillLevel> skillLevels = packetByteBuf.readCollection(HashSet::new, buf -> readSkillLevel(id, buf));

        return new Skill(id, translationKey, untranslatedName, skillLevels);
    }

    static void writeSkill(@Nonnull PacketByteBuf packetByteBuf, @Nonnull Skill skill) {
        packetByteBuf.writeString(skill.getId());
        writeNullableString(packetByteBuf, skill.getTranslationKey());
        writeNullableString(packetByteBuf, skill.getUntranslatedName());
        packetByteBuf.writeCollection(skill.getSkillLevels(), SkillMmoNetworking::writeSkillLevel);
    }

    static SkillLevel readSkillLevel(String skillId, @Nonnull PacketByteBuf packetByteBuf) {
        byte level = packetByteBuf.readByte();
        Map<SkillLevelUnlockType, Set<Identifier>> unlocks = packetByteBuf.readMap(
                HashMap::new,
                buf -> buf.readEnumConstant(SkillLevelUnlockType.class),
                buf -> buf.readCollection(HashSet::new, PacketByteBuf::readIdentifier)
        );

        return new SkillLevel(skillId, level, unlocks);
    }

    static void writeSkillLevel(@Nonnull PacketByteBuf packetByteBuf, @Nonnull SkillLevel skillLevel) {
        packetByteBuf.writeByte(skillLevel.getLevel());
        packetByteBuf.writeMap(
                skillLevel.getUnlocks(),
                PacketByteBuf::writeEnumConstant,
                (buf, ids) -> buf.writeCollection(ids, PacketByteBuf::writeIdentifier)
        );
    }
}
