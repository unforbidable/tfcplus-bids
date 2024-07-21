package com.unforbidable.tfc.bids.Core.Kilns;

public class KilnValidatorResult {

    public final boolean valid;

    public KilnValidatorResult(boolean valid) {
        this.valid = valid;
    }

    public static KilnValidatorResult success() {
        return new KilnValidatorResult(true);
    }

    public static KilnValidatorResult failure() {
        return new KilnValidatorResult(false);
    }

}
