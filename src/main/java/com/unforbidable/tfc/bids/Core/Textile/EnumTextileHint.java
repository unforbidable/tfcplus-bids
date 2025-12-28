package com.unforbidable.tfc.bids.Core.Textile;

public enum EnumTextileHint {
    EXTRACTING("Extracting"),
    REFINING_STALK("RefiningStalk"),
    REFINING_BOLL("RefiningBoll"),
    WASHING("Washing"),
    RINSING_FIBRE("RinsingFibre"),
    RINSING_WOOL("RinsingWool"),
    RETTING_STALK("RettingStalk"),
    PEELING_STALK("PeelingStalk"),
    DRYING_FIBRE("DryingFibre"),
    DRYING_STALK("DryingStalk"),
    DRYING_WOOL("DryingWool"),
    RUBBING("Rubbing"),
    THINNING("Thinning"),
    BREAKING("Breaking"),
    SCUTCHING("Scutching"),
    HECKLING("Heckling"),
    CARDING("Carding"),
    SPINNING_CORDAGE("SpinningCordage"),
    SPINNING_TWINE("SpinningTwine"),
    SPINNING_STRING("SpinningString"),
    SPINNING_YARN("SpinningYarn"),
    TWISTING("Twisting"),
    WEAVING_BURLAP("WeavingBurlap"),
    WEAVING_CLOTH("WeavingCloth");

    EnumTextileHint(String helpString) {
        this.helpString = helpString;
    }

    public final String helpString;
}
