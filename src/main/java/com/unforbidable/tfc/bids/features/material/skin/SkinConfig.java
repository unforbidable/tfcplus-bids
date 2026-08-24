package com.unforbidable.tfc.bids.features.material.skin;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class SkinConfig {

    public static boolean enableAnimalSkinDropReplacement = true;
    public static float hairDamageDecayPercent = 0.20f;
    public static float butcheringSkillGainPerSmallSkinFleshed = 0.5f;
    public static float butcheringSkillGainPerSmallSkinDehaired = 0.5f;
    public static float butcheringSkillGainPerSmallSkinWorked = 0.5f;

    public static void load(FeatureConfig config) {
        enableAnimalSkinDropReplacement = config.getBoolean(
            "enableAnimalSkinDropReplacement",
            enableAnimalSkinDropReplacement,
            "Set this to true if you want to replace dropped rawhide and fur with higher-yield animal skins that decay and require more processing.");

        hairDamageDecayPercent = config.getFloat(
            "hairDamageDecayPercent",
            hairDamageDecayPercent, 0.05f, 0.25f,
            "Higher values increase the percentage of decay when a Clean skin to starts to loose hair. At this point, the skin can no longer be preserved and needs to be dehaired and turned into rawhide or leather.");

        butcheringSkillGainPerSmallSkinFleshed = config.getFloat(
            "butcheringSkillGainPerSmallSkinFleshed",
            butcheringSkillGainPerSmallSkinFleshed, 0, 1f,
            "Higher values increase the amount of Butchering skill points gained for fleshing a fresh skin of small weight.");
        butcheringSkillGainPerSmallSkinDehaired = config.getFloat(
            "butcheringSkillGainPerSmallSkinDehaired",
            butcheringSkillGainPerSmallSkinDehaired, 0, 1f,
            "Higher values increase the amount of Butchering skill points gained for dehairing a prepared skin of small weight.");
        butcheringSkillGainPerSmallSkinWorked = config.getFloat(
            "butcheringSkillGainPerSmallSkinWorked",
            butcheringSkillGainPerSmallSkinWorked, 0, 1f,
            "Higher values increase the amount of Butchering skill points gained for working a dried skin of small weight.");
    }

}
