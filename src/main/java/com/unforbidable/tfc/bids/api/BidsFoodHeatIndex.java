package com.unforbidable.tfc.bids.api;

import com.dunk.tfc.api.HeatIndex;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class BidsFoodHeatIndex extends HeatIndex {

    private final HeatIndex boilingHeatIndex;
    private final HeatIndex steamingHeatIndex;

    public BidsFoodHeatIndex(ItemStack in, int sh, double temp, ItemStack bakingOutput, ItemStack boilingOutput, ItemStack steamingOutput) {
        super(in, sh, temp, bakingOutput);

        this.boilingHeatIndex = new HeatIndex(in, sh, temp, boilingOutput);
        this.steamingHeatIndex = new HeatIndex(in, sh, temp, steamingOutput);
    }

    @Override
    public HeatIndex setKeepNBT(boolean k) {
        boilingHeatIndex.setKeepNBT(k);
        steamingHeatIndex.setKeepNBT(k);

        return super.setKeepNBT(k);
    }

    public boolean hasBoilingOutput() {
        return boilingHeatIndex.hasOutput();
    }

    public boolean hasSteamingOutput() {
        return steamingHeatIndex.hasOutput();
    }

    public ItemStack getBoilingOutput(ItemStack inputItemStack) {
        return boilingHeatIndex.getOutput(inputItemStack, new Random());
    }

    public ItemStack getSteamingOutput(ItemStack inputItemStack) {
        return steamingHeatIndex.getOutput(inputItemStack, new Random());
    }
}
