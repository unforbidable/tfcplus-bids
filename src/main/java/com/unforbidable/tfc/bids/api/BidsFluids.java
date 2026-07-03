package com.unforbidable.tfc.bids.api;

import com.dunk.tfc.Core.FluidBaseTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.names.FluidNames;
import java.util.Locale;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class BidsFluids {

    public static final Fluid goatMilk = fluid(FluidNames.GOAT_MILK);
    public static final Fluid cookingMixture = fluid(FluidNames.COOKING_MIXTURE);
    public static final Fluid tallow = fluid(FluidNames.TALLOW);
    public static final Fluid skimmedMilk = fluid(FluidNames.SKIMMED_MILK);
    public static final Fluid cream = fluid(FluidNames.CREAM);
    public static final Fluid goatMilkVinegar = fluid(FluidNames.GOAT_MILK_VINEGAR);
    public static final Fluid goatMilkCurdled = fluid(FluidNames.GOAT_MILK_CURDLED);
    public static final Fluid skimmedMilkVinegar = fluid(FluidNames.SKIMMED_MILK_VINEGAR);
    public static final Fluid skimmedMilkCurdled = fluid(FluidNames.SKIMMED_MILK_CURDLED);
    public static final Fluid oilyFishWater = fluid(FluidNames.OILY_FISH_WATER);
    public static final Fluid fishOil = fluid(FluidNames.FISH_OIL);


    private static Fluid fluid(String name) {
        Fluid fluid = FluidRegistry.getFluid(name.toLowerCase(Locale.ENGLISH));
        if (fluid == null) {
            Bids.LOG.error("Fluid not found in fluid registry: {}", name);
        }
        return fluid;
    }




    public static final Fluid WEAKWOODASHLYE = new FluidBaseTFC("WeakWoodAshLye").setBaseColor(0xffc075);
    public static final Fluid WOODASHLYE = new FluidBaseTFC("WoodAshLye").setBaseColor(0xd88a10);
    public static final Fluid TALLOWWOODASHLYE = new FluidBaseTFC("TallowWoodAshLye").setBaseColor(0xcc9258);
    public static final Fluid OLIVEOILWEAKWOODASHLYE = new FluidBaseTFC("OliveOilWeakWoodAshLye").setBaseColor(0xcda55f);
    public static final Fluid FISHOILWEAKWOODASHLYE = new FluidBaseTFC("FishOilWeakWoodAshLye").setBaseColor(0xcda55f);
    public static final Fluid FLAXSEEDOILWEAKWOODASHLYE = new FluidBaseTFC("FlaxSeedOilWeakWoodAshLye").setBaseColor(0xcda55f);
    public static final Fluid SOAP = new FluidBaseTFC("Soap").setBaseColor(0xecc29b);
    public static final Fluid UNCUREDSOAP = new FluidBaseTFC("UncuredSoap").setBaseColor(0xdeb186);
    public static final Fluid SOAPYWATER = new FluidBaseTFC("SoapyWater").setBaseColor(0x305090);
    public static final Fluid FLAXSEEDOIL = new FluidBaseTFC("FlaxSeedOil").setBaseColor(0x977d59);

}
