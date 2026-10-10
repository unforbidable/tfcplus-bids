package com.unforbidable.tfc.bids.features.device.saddlequern.main;

import com.unforbidable.tfc.bids.api.features.quern.QuernRecipe;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.features.device.saddlequern.SaddleQuernConfig;

public class SaddleQuernHelper {

    public static SaddleQuernRecipe adaptQuernRecipe(QuernRecipe source) {
        if (source.getResistance() < 4 || SaddleQuernConfig.allowGrindAny) {
            return new SaddleQuernRecipe(source.getInput(), source.getOutput());
        }

        return null;
    }

}
