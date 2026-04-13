package com.unforbidable.tfc.bids.api.Interfaces;

public interface IDryingEnvironment {

    boolean isExposed();
    boolean isHeated();
    float getTemperature();
    float getPrecipitation();
    float getHumidity();
    float getWetness();
    float getAirflow();
    float getSunlight();

}
