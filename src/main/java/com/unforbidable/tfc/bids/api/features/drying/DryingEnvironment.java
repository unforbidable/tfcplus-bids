package com.unforbidable.tfc.bids.api.features.drying;

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
