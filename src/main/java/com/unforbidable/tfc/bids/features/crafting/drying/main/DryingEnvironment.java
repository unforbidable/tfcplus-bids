package com.unforbidable.tfc.bids.features.crafting.drying.main;

public interface DryingEnvironment {

    boolean isExposed();
    boolean isHeated();
    float getTemperature();
    float getPrecipitation();
    float getHumidity();
    float getWetness();
    float getAirflow();
    float getSunlight();
    boolean isSmoked();
    int getFuelTasteProfile();

}
