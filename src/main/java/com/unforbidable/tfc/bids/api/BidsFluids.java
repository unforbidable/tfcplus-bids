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
    public static final Fluid skimmedMilk = fluid(FluidNames.SKIMMED_MILK);
    public static final Fluid cream = fluid(FluidNames.CREAM);

    private static Fluid fluid(String name) {
        Fluid fluid = FluidRegistry.getFluid(name.toLowerCase(Locale.ENGLISH));
        if (fluid == null) {
            Bids.LOG.error("Fluid not found in fluid registry: {}", name);
        }
        return fluid;
    }




    public static final Fluid OILYFISHWATER = new FluidBaseTFC("OilyFishWater").setBaseColor(0x124220);
    public static final Fluid FISHOIL = new FluidBaseTFC("FishOil").setBaseColor(0xa1a36f);
    public static final Fluid GOATMILKVINEGAR = new FluidBaseTFC("GoatMilkVinegar").setBaseColor(0xfffbe8);
    public static final Fluid GOATMILKCURDLED = new FluidBaseTFC("GoatMilkCurdled").setBaseColor(0xfffbe8);
    public static final Fluid TALLOW = new FluidBaseTFC("Tallow").setBaseColor(0xf0db3a);
    public static final Fluid SKIMMEDMILKVINEGAR = new FluidBaseTFC("SkimmedMilkVinegar").setBaseColor(0xfffbe8);
    public static final Fluid SKIMMEDMILKCURDLED = new FluidBaseTFC("SkimmedMilkCurdled").setBaseColor(0xfffbe8);
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
