package com.unforbidable.tfc.bids.core._obsolete;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixture;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidSetup extends BidsFluids {

    public static void preInit() {
        registerFluids();
        registerLampFuels();
        registerCookingFluids();
    }

    private static void registerCookingFluids() {
        Bids.LOG.info("Register cooking fluids");

//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.BEAN, 0x99420f));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.BEAN_WATER, 0x996140));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.BEAN_STEW, 0x693111,
//            new ItemStack(BidsItems.stew, 1, 0)));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.BEAN_SOUP, 0x633f2a,
//            new ItemStack(BidsItems.soup, 1, 0)));
//
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.MEAT, 0xc2230e));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.MEAT_WATER, 0xc44b3b));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.MEAT_STEW, 0x6b150a,
//            new ItemStack(BidsItems.stew, 1, 1)));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.MEAT_SOUP, 0x702b22,
//            new ItemStack(BidsItems.soup, 1, 1)));
//
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.FISH, 0xcc5047));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.FISH_WATER, 0xc96f69));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.FISH_STEW, 0x732721,
//            new ItemStack(BidsItems.stew, 1, 2)));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.FISH_SOUP, 0x78413d,
//            new ItemStack(BidsItems.soup, 1, 2)));
//
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.VEGETABLE, 0x516b15));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.VEGETABLE_WATER, 0x5d6e38));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.VEGETABLE_STEW, 0x32330c,
//            new ItemStack(BidsItems.stew, 1, 3)));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.VEGETABLE_SOUP, 0x3f4021,
//            new ItemStack(BidsItems.soup, 1, 3)));
//
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.CEREAL, 0xaba557));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.CEREAL_WATER, 0xb3af7d));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.CEREAL_MILK, 0xe0ddb1));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.PORRIDGE_WATER, 0xa19e81,
//            new ItemStack(BidsItems.porridge, 1, 0)));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.PORRIDGE_MILK, 0xd1d0ba,
//            new ItemStack(BidsItems.porridge, 1, 1)));
//
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.EGG, 0xe0d44a));
//        BidsRegistry.COOKING_MIXTURES.register(new CookingMixture(CookingMixtureNames.OMELET, 0xdbc386,
//            new ItemStack(BidsItems.omelet, 1, 0)));
    }

    private static void registerFluids() {
        Bids.LOG.info("Register fluids");

        FluidRegistry.registerFluid(OILYFISHWATER);
        FluidRegistry.registerFluid(FISHOIL);
//        FluidRegistry.registerFluid(goatMilk);
        FluidRegistry.registerFluid(GOATMILKVINEGAR);
        FluidRegistry.registerFluid(GOATMILKCURDLED);
        FluidRegistry.registerFluid(tallow);
//        FluidRegistry.registerFluid(cookingMixture);
        FluidRegistry.registerFluid(skimmedMilk);
        FluidRegistry.registerFluid(skimmedMilkVinegar);
        FluidRegistry.registerFluid(skimmedMilkCurdled);
        FluidRegistry.registerFluid(cream);
        FluidRegistry.registerFluid(WEAKWOODASHLYE);
        FluidRegistry.registerFluid(WOODASHLYE);
        FluidRegistry.registerFluid(TALLOWWOODASHLYE);
        FluidRegistry.registerFluid(OLIVEOILWEAKWOODASHLYE);
        FluidRegistry.registerFluid(FISHOILWEAKWOODASHLYE);
        FluidRegistry.registerFluid(FLAXSEEDOILWEAKWOODASHLYE);
        FluidRegistry.registerFluid(SOAP);
        FluidRegistry.registerFluid(UNCUREDSOAP);
        FluidRegistry.registerFluid(SOAPYWATER);
        FluidRegistry.registerFluid(FLAXSEEDOIL);
    }

    private static void registerLampFuels() {
        Bids.LOG.info("Register lamp fuels");

//        BidsRegistry.LAMP_FUEL.register(TFCFluids.OLIVEOIL, new FuelOliveOil());
//        BidsRegistry.LAMP_FUEL.register(BidsFluids.FISHOIL, new FuelFishOil());
//        BidsRegistry.LAMP_FUEL.register(BidsFluids.FLAXSEEDOIL, new FuelFlaxSeedOil());
    }

}
