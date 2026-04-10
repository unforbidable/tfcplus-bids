package com.unforbidable.tfc.bids.Core.Drying.Environment;

import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingEnvironment;
import net.minecraft.world.World;

public class ItemEnvironment implements IDryingEnvironment {

    protected final World world;
    protected final int blockX;
    protected final int blockY;
    protected final int blockZ;
    protected final long ticks;
    protected final DryingItem dryingItem;

    private final DynamicEnvironment dynamicEnvironment;

    private Float wetness;

    public ItemEnvironment(World world, int blockX, int blockY, int blockZ, long ticks, DryingItem dryingItem, DynamicEnvironment dynamicEnvironment) {
        this.world = world;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.ticks = ticks;
        this.dryingItem = dryingItem;
        this.dynamicEnvironment = dynamicEnvironment;
    }

    public float getRecipeMatch(DryingRecipe recipe) {
        return new EnvironmentRecipeMatcher(this, recipe).match();
    }

    public float getRecipeFailure(DryingRecipe recipe) {
        return new EnvironmentRecipeFailureChecker(this, recipe).checkFailure();
    }

    public float getWetness() {
        if (wetness == null) {
            wetness = dryingItem.wetness;
        }

        return wetness;
    }

    @Override
    public boolean isExposed() {
        return dynamicEnvironment.isExposed();
    }

    @Override
    public boolean isHeated() {
        return dynamicEnvironment.isHeated();
    }

    @Override
    public float getTemperature() {
        return dynamicEnvironment.getTemperature();
    }

    @Override
    public float getPrecipitation() {
        return dynamicEnvironment.getPrecipitation();
    }

    @Override
    public float getHumidity() {
        return dynamicEnvironment.getHumidity();
    }

}
