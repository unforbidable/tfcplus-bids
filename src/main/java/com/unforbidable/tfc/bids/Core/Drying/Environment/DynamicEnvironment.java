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

    public float getHumidity() {
        if (humidity == null) {
            // Humidity is 1 during rain and 0 when freezing
            // based on the current temperature and precipitation
            // otherwise calculated from rainfall
            float generalHumidity = EnvironmentHelper.getHumidity(world, blockX, blockY, blockZ, getTemperature(), getPrecipitation());

            // Rain or snow falling on block prevents drying
            // otherwise humidity can be reduced by nearby heat source
            float humidityHeatedMultiplier = isHeated() ? 0.25f : 1f;
            humidity = isExposed() && getPrecipitation() > 0 ? 1 : generalHumidity * humidityHeatedMultiplier;
        }

        return humidity;
    }

    public boolean isExposed() {
        return staticEnvironment.isExposed();
    }

    public boolean isHeated() {
        // This is where we can try to detect past heat sources that existed at given ticks
        return staticEnvironment.isHeated();
    }

}
