package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SkillManager {
    private static final SkillManager instance = new SkillManager();
    public static SkillManager getInstance() {
        return instance;
    }

    private final Map<String, Skill> skillMap = new HashMap<>();

    /** @see #registerSkills(Set) */
    private LoadingCache<SkillUnlockCacheKey, Set<Skill>> skillsByUnlockCache;

    public void registerSkills(Set<Skill> skills) {
        this.skillMap.clear();
        this.skillsByUnlockCache = CacheBuilder.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Set<Skill> load(SkillUnlockCacheKey key) {
                        return skills.stream()
                                .filter(skill -> skill.getSkillLevelAffecting(key.getUnlockType(), key.getUnlockIdentifier()).isPresent())
                                .collect(Collectors.toSet());
                    }
                });
    }

    public Optional<Skill> getSkill(String skillId) {
        return Optional.ofNullable(skillMap.get(skillId));
    }

    public Set<Skill> getSkillsAffecting(SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        return this.skillsByUnlockCache.getUnchecked(new SkillUnlockCacheKey(unlockType, unlockIdentifier));
    }

    public Set<SkillLevel> getSkillLevelsAffecting(SkillLevelUnlockType unlockType, Identifier unlockIdentifier) {
        Set<Skill> skills = this.getSkillsAffecting(unlockType, unlockIdentifier);
        return skills.stream()
                .flatMap(skill -> skill.getSkillLevelAffecting(unlockType, unlockIdentifier).stream())
                .collect(Collectors.toSet());
    }
}
