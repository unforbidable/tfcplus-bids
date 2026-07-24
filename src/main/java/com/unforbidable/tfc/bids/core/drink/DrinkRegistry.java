package com.unforbidable.tfc.bids.core.drink;

import com.unforbidable.tfc.bids.core.drink.registry.DrinkFluid;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkVessel;
import com.unforbidable.tfc.bids.util.registry.ListRegistry;

public class DrinkRegistry {

    public final static ListRegistry<DrinkFluid> drinks = new ListRegistry<>();
    public final static ListRegistry<DrinkVessel> vessels = new ListRegistry<>();

}
