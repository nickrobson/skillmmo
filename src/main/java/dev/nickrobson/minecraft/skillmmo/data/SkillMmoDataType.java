package dev.nickrobson.minecraft.skillmmo.data;

public final class SkillMmoDataType<T extends DataValidatable> {
    public static final SkillMmoDataType<SkillData> SKILLS = new SkillMmoDataType<>("skills", SkillData.class);
    public static final SkillMmoDataType<SkillLevelUnlocksData> SKILL_UNLOCKS = new SkillMmoDataType<>("skillunlocks", SkillLevelUnlocksData.class);

    private final String resourceCategory;
    private final Class<T> resourceClass;

    private SkillMmoDataType(String resourceCategory, Class<T> resourceClass) {
        this.resourceCategory = resourceCategory;
        this.resourceClass = resourceClass;
    }

    public String getResourceCategory() {
        return resourceCategory;
    }

    public Class<T> getResourceClass() {
        return resourceClass;
    }
}
