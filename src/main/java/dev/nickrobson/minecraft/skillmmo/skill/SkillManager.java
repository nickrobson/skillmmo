package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.nickrobson.minecraft.skillmmo.skill.unlock.Unlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SkillManager {
    private static final SkillManager instance = new SkillManager();

    public static SkillManager getInstance() {
        return instance;
    }

    private final Set<Skill> skillSet = new HashSet<>();
    private final Map<Identifier, Skill> skillMap = new HashMap<>();

    private SkillManager() {
    }

    /**
     * @see #initSkills(Set)
     */
    private LoadingCache<Unlock, Set<Skill>> skillsByUnlockCache;

    public void initSkills(Set<Skill> skills) {
        this.skillSet.clear();
        this.skillSet.addAll(skills);

        this.skillMap.clear();
        this.skillMap.putAll(skills.stream().collect(Collectors.toMap(Skill::getId, Function.identity())));

        this.skillsByUnlockCache = CacheBuilder.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull Set<Skill> load(Unlock unlock) {
                        return skills.stream()
                                .filter(skill -> skill.getSkillLevelAffecting(unlock).isPresent())
                                .collect(Collectors.toSet());
                    }
                });
    }

    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(this.skillSet);
    }

    public Optional<Skill> getSkill(Identifier skillId) {
        return Optional.ofNullable(this.skillMap.get(skillId));
    }

    public Set<Skill> getSkillsAffecting(Unlock unlock) {
        return this.skillsByUnlockCache.getUnchecked(unlock);
    }

    public Set<SkillLevel> getSkillLevelsAffecting(Unlock unlock) {
        Set<Skill> skills = this.getSkillsAffecting(unlock);
        return skills.stream()
                .flatMap(skill -> skill.getSkillLevelAffecting(unlock).stream())
                .collect(Collectors.toSet());
    }
}
