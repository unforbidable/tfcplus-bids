package com.unforbidable.tfc.bids.Core.Kilns;

public class KilnValidatorResult<TParams> {

    public final boolean valid;
    public final TParams params;

    public KilnValidatorResult(boolean valid, TParams params) {
        this.valid = valid;
        this.params = params;
    }

}
