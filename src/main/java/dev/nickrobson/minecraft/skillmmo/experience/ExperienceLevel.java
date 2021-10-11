package dev.nickrobson.minecraft.skillmmo.experience;

public record ExperienceLevel(int level, long progress, long levelExperience) {
    public double progressFraction() {
        return levelExperience == 0
                ? 0
                : 1.0 * progress / levelExperience;
    }
}
