package dev.nickrobson.minecraft.skillmmo.data;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.TranslationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Skill {
    private static final Logger logger = LoggerFactory.getLogger(Skill.class);

    /**
     * Translation key for this skill's name
     */
    @SerializedName("translationKey")
    public String translationKey;

    /**
     * The untranslated name of the skill. <br/>
     * Note: This is only used if the translation key is unset.
     *
     * @see #translationKey
     */
    @SerializedName("name")
    public String untranslatedName;

    @Environment(EnvType.CLIENT)
    public String getName() {
        if (translationKey != null) {
            String translation = TranslationStorage.getInstance().get(translationKey);
            if (translation != null) {
                return translation;
            }
            logger.warn("Translation key {} requested but no translation found for {}", translationKey, TranslationStorage.getInstance());
        }
        return untranslatedName;
    }

}
