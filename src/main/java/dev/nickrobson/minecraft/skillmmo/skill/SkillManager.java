package dev.nickrobson.minecraft.skillmmo.skill;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import dev.nickrobson.minecraft.skillmmo.api.unlockable.Unlockable;
import net.minecraft.util.Identifier;

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

    // Skills set in the current side's datapack
    private final Set<Skill> installedSkillSet = new HashSet<>();

    // Skills active on the current game session (BEWARE: the server can sync to the client a different set of skills to what the client has installed in their datapack!)
    private final Set<Skill> skillSet = new HashSet<>();
    private final Map<Identifier, Skill> skillMap = new HashMap<>();

    private SkillManager() {
    }

    /**
     * @see #initSkills(Set)
     */
    private LoadingCache<Unlockable<?>, Set<Skill>> skillsByUnlockCache;

    /**
     * Register the skills that are installed by the current side's datapack.
     * Should ONLY be called by the resource loader.
     */
    public void initInstalledSkills(Set<Skill> skillSet) {
        this.installedSkillSet.clear();
        this.installedSkillSet.addAll(skillSet);
        this.initSkills(skillSet);
    }

    /**
     * Set which skills are <i>active</i> in the mod (primarily important for client-side).
     * <br />
     * <br />
     * This is not necessarily the same as the skills installed in the datapack,
     * as this list is controlled by the server and synced to the client (to enable
     * the server to define active skills).
     * <br />
     * <br />
     * Should be called in server-to-client sync.
     */
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
                    public Set<Skill> load(Unlockable<?> unlock) {
                        return skills.stream()
                                .filter(skill -> skill.getSkillLevelAffecting(unlock).isPresent())
                                .collect(Collectors.toSet());
                    }
                });
    }

    /**
     * Gets the installed skills, according to the current side's datapack.
     * <br />
     * <br />
     * Should be used only during server-to-client sync.
     * Use {@link #getSkills()} instead if you need the active skills during gameplay.
     */
    public Set<Skill> getInstalledSkills() {
        return Collections.unmodifiableSet(this.installedSkillSet);
    }

    /**
     * Gets the current active skills for the current gameplay session.
     * <br />
     * <br />
     * Should be used only during gameplay.
     * Use {@link #getInstalledSkills()} instead if you need the skills installed in the datapack.
     */
    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(this.skillSet);
    }

    public Optional<Skill> getSkill(Identifier skillId) {
        return Optional.ofNullable(this.skillMap.get(skillId));
    }

    public Set<Skill> getSkillsAffecting(Unlockable<?> unlockable) {
        return this.skillsByUnlockCache.getUnchecked(unlockable);
    }

    public Set<SkillLevel> getSkillLevelsAffecting(Unlockable<?> unlockable) {
        Set<Skill> skills = this.getSkillsAffecting(unlockable);
        return skills.stream()
                .flatMap(skill -> skill.getSkillLevelAffecting(unlockable).stream())
                .collect(Collectors.toSet());
    }
}
