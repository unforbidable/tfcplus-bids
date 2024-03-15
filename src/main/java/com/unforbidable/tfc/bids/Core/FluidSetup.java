package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Lamp.Fuels.FuelFishOil;
import com.unforbidable.tfc.bids.Core.Lamp.Fuels.FuelOliveOil;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.LampRegistry;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidSetup extends BidsFluids {

    public static void preInit() {
        registerFluids();
        registerLampFuels();
    }

    private static void registerFluids() {
        Bids.LOG.info("Register fluids");

        FluidRegistry.registerFluid(OILYFISHWATER);
        FluidRegistry.registerFluid(FISHOIL);
        FluidRegistry.registerFluid(GOATMILK);
        FluidRegistry.registerFluid(GOATMILKVINEGAR);
        FluidRegistry.registerFluid(GOATMILKCURDLED);
        FluidRegistry.registerFluid(TALLOW);
    }

    private static void registerLampFuels() {
        Bids.LOG.info("Register lamp fuels");

        LampRegistry.registerFuel(TFCFluids.OLIVEOIL, FuelOliveOil.class);
        LampRegistry.registerFuel(BidsFluids.FISHOIL, FuelFishOil.class);
    }

}
