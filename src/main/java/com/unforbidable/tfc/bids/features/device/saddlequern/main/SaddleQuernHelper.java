package com.unforbidable.tfc.bids.features.device.saddlequern.main;

import com.unforbidable.tfc.bids.api.features.quern.QuernRecipe;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;

public class SaddleQuernHelper {

    public static SaddleQuernRecipe adaptQuernRecipe(QuernRecipe source) {
            return new SaddleQuernRecipe(source.getInput(), source.getOutput());
    }

}
