package com.unforbidable.tfc.bids.Core.Drying.Environment;

import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import net.minecraft.world.World;

public class DynamicEnvironment {

    protected final World world;
    protected final int blockX;
    protected final int blockY;
    protected final int blockZ;
    protected final long ticks;

    private final StaticEnvironment staticEnvironment;

    private Float temperature;
    private Float precipitation;
    private Float humidity;
    private Float sunlight;

    public DynamicEnvironment(World world, int blockX, int blockY, int blockZ, long ticks, StaticEnvironment staticEnvironment) {
        this.world = world;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.ticks = ticks;
        this.staticEnvironment = staticEnvironment;
    }

    public ItemEnvironment ofItem(DryingItem item) {
        return new ItemEnvironment(world, blockX, blockY, blockZ, ticks, item, this);
    }

    public float getTemperature() {
        if (temperature == null) {
            temperature = EnvironmentHelper.getTemperature(world, blockX, blockY, blockZ, ticks);
        }

        return temperature;
    }

    public float getPrecipitation() {
        if (precipitation == null) {
            precipitation = EnvironmentHelper.getPrecipitation(world, blockX, blockY, blockZ, ticks);
        }

        return precipitation;
    }

    public float getSunlight() {
        if (sunlight == null) {
            sunlight = isExposed() ? EnvironmentHelper.getSunlight(world, blockX, blockY, blockZ, ticks) : 0;
        }

        return sunlight;
    }

    public float getHumidity() {
        if (humidity == null) {
            if (isExposed() && getPrecipitation() > 0) {
                // when it rains or snows without cover, humidity is always 100%
                humidity = 1f;
            } else {
                float generalHumidity = EnvironmentHelper.getHumidity(world, blockX, blockY, blockZ, getTemperature(), getPrecipitation());
                if (generalHumidity == 0) {
                    // if humidity is already 0% due to freezing temperature
                    // it cannot be reduced anymore by multipliers below
                    humidity = 0f;
                } else {
                    // Rain or snow falling on block prevents drying
                    // otherwise humidity can be reduced by nearby heat source
                    float humidityHeatedMultiplier = isHeated() ? 0.5f : 1f;

                    // Similarly, sunshine helps with drying
                    float humiditySunlightMultiplier = getSunlight() > 0 ? (1 - getSunlight() * 0.25f) : 1f;

                    humidity = generalHumidity * humidityHeatedMultiplier * humiditySunlightMultiplier;
                }
            }
        }

        return humidity;
    }

    public boolean isExposed() {
        return staticEnvironment.isExposed();
    }

    public float getAirflow() {
        return staticEnvironment.getAirflow();
    }

    public boolean isHeated() {
        // This is where we can try to detect past heat sources that existed at given ticks
        return staticEnvironment.isHeated();
    }

    public boolean isSmoked() {
        // This is where we can try to detect past smoke sources that existed at given ticks
        return staticEnvironment.isSmoked();
    }

    public int getFuelTasteProfile() {
        return staticEnvironment.getFuelTasteProfile();
    }

}
