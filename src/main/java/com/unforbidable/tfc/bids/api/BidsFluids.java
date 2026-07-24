package com.unforbidable.tfc.bids.api;

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
    public static final Fluid flaxSeedOil = fluid(FluidNames.FLAX_SEED_OIL);
    public static final Fluid weakWoodAshLye = fluid(FluidNames.WEAK_WOOD_ASH_LYE);
    public static final Fluid woodAshLye = fluid(FluidNames.WOOD_ASH_LYE);
    public static final Fluid tallowWoodAshLye = fluid(FluidNames.TALLOW_WOOD_ASH_LYE);
    public static final Fluid oliveOilWeakWoodAshLye = fluid(FluidNames.OLIVE_OIL_WEAK_WOOD_ASH_LYE);
    public static final Fluid fishOilWeakWoodAshLye = fluid(FluidNames.FISH_OIL_WEAK_WOOD_ASH_LYE);
    public static final Fluid flaxSeedOilWeakWoodAshLye = fluid(FluidNames.FLAX_SEED_OIL_WEAK_WOOD_ASH_LYE);
    public static final Fluid soap = fluid(FluidNames.SOAP);
    public static final Fluid uncuredSoap = fluid(FluidNames.UNCURED_SOAP);
    public static final Fluid soapyWater = fluid(FluidNames.SOAPY_WATER);

    private static Fluid fluid(String name) {
        Fluid fluid = FluidRegistry.getFluid(name.toLowerCase(Locale.ENGLISH));
        if (fluid == null) {
            Bids.LOG.error("Fluid not found in fluid registry: {}", name);
        }
        return fluid;
    }

}
