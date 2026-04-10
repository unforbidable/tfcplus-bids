package com.unforbidable.tfc.bids.Core.Drying.Environment;

import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingEnvironment;

public class EnvironmentRecipeMatcher {

    // warm is considered at least temp 5C
    // with square gains capped at 20C
    // 10C -> 11%
    // 15C -> 44%
    private static final int WARM_TEMP_MIN = 5;
    private static final int WARM_TEMP_CAP = 20 * 2 - WARM_TEMP_MIN * 2;

    private final IDryingEnvironment env;
    private final DryingRecipe recipe;

    public EnvironmentRecipeMatcher(IDryingEnvironment env, DryingRecipe recipe) {
        this.env = env;
        this.recipe = recipe;
    }

    public float match() {
        return matchCover() * matchFreezing() * matchWarm() * matchWet() * matchDry();
    }

    private float matchDry() {
        if (recipe.isRequiresDry()) {
            return env.getWetness() == 0 ? (1 - env.getHumidity()) : 0;
        } else {
            return 1;
        }
    }

    private float matchWet() {
        if (recipe.isRequiresWet()) {
            return env.getWetness() > 0 ? 1 : 0;
        } else {
            return 1;
        }
    }

    private float matchWarm() {
        if (recipe.isRequiresWarm()) {
            return env.getTemperature() > WARM_TEMP_MIN ? Math.min((env.getTemperature() - WARM_TEMP_MIN) * (env.getTemperature() - WARM_TEMP_MIN) / WARM_TEMP_CAP, 1f) : 0;
        } else {
            return 1;
        }
    }

    private float matchFreezing() {
        if (recipe.isRequiresFreezing()) {
            return env.getTemperature() < 0 ? 1 : 0;
        } else {
            return 1;
        }
    }

    private float matchCover() {
        if (recipe.isRequiresCover()) {
            return !env.isExposed() ? 1 : 0;
        } else {
            return 1;
        }
    }

}
