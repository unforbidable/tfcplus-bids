package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Lamp.Fuels.FuelFishOil;
import com.unforbidable.tfc.bids.Core.Lamp.Fuels.FuelOliveOil;
import com.unforbidable.tfc.bids.api.BidsCookingMixtures;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Crafting.CookingMixture;
import com.unforbidable.tfc.bids.api.Crafting.CookingManager;
import com.unforbidable.tfc.bids.api.LampRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidSetup extends BidsFluids {

    public static void preInit() {
        registerFluids();
        registerLampFuels();
        registerCookingFluids();
    }

    private static void registerCookingFluids() {
        Bids.LOG.info("Register cooking fluids");

        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.BEAN, 0x99420f));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.BEAN_WATER, 0x996140));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.BEAN_STEW, 0x693111,
            new ItemStack(BidsItems.stew, 1, 0)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.BEAN_SOUP, 0x633f2a,
            new ItemStack(BidsItems.soup, 1, 0)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.MEAT, 0xc2230e));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.MEAT_WATER, 0xc44b3b));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.MEAT_STEW, 0x6b150a,
            new ItemStack(BidsItems.stew, 1, 1)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.MEAT_SOUP, 0x702b22,
            new ItemStack(BidsItems.soup, 1, 1)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.FISH, 0xcc5047));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.FISH_WATER, 0xc96f69));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.FISH_STEW, 0x732721,
            new ItemStack(BidsItems.stew, 1, 2)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.FISH_SOUP, 0x78413d,
            new ItemStack(BidsItems.soup, 1, 2)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.VEGETABLE, 0x516b15));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.VEGETABLE_WATER, 0x5d6e38));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.VEGETABLE_STEW, 0x32330c,
            new ItemStack(BidsItems.stew, 1, 3)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.VEGETABLE_SOUP, 0x3f4021,
            new ItemStack(BidsItems.soup, 1, 3)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.CEREAL, 0xaba557));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.CEREAL_WATER, 0xb3af7d));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.CEREAL_MILK, 0xe0ddb1));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.PORRIDGE_WATER, 0xa19e81,
            new ItemStack(BidsItems.porridge, 1, 0)));
        CookingManager.registerCookingMixture(new CookingMixture(BidsCookingMixtures.PORRIDGE_MILK, 0xd1d0ba,
            new ItemStack(BidsItems.porridge, 1, 1)));
    }

    private static void registerFluids() {
        Bids.LOG.info("Register fluids");

        FluidRegistry.registerFluid(OILYFISHWATER);
        FluidRegistry.registerFluid(FISHOIL);
        FluidRegistry.registerFluid(GOATMILK);
        FluidRegistry.registerFluid(GOATMILKVINEGAR);
        FluidRegistry.registerFluid(GOATMILKCURDLED);
        FluidRegistry.registerFluid(TALLOW);
        FluidRegistry.registerFluid(COOKINGMIXTURE);
    }

    private static void registerLampFuels() {
        Bids.LOG.info("Register lamp fuels");

        LampRegistry.registerFuel(TFCFluids.OLIVEOIL, FuelOliveOil.class);
        LampRegistry.registerFuel(BidsFluids.FISHOIL, FuelFishOil.class);
    }

}
