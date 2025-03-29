package com.unforbidable.tfc.bids.WAILA;

import com.dunk.tfc.TileEntities.TEChimney;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Blocks.*;
import com.unforbidable.tfc.bids.TileEntities.*;
import com.unforbidable.tfc.bids.WAILA.Providers.*;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WailaSetup {

    @SideOnly(Side.CLIENT)
    public static void init() {
        initProviders();
        registerCallbacks();
    }

    @SideOnly(Side.CLIENT)
    private static void initProviders() {
        Bids.LOG.info("Initialize WAILA providers");

        WailaProvider.addProvider(new CrucibleProvider(), TileEntityCrucible.class);
        WailaProvider.addProvider(new FurnaceProvider(), TileEntityChimney.class, TEChimney.class);
        WailaProvider.addProvider(new QuarryProvider(), TileEntityQuarry.class);
        WailaProvider.addProvider(new CarvingProvider(), TileEntityCarving.class);
        WailaProvider.addProvider(new WoodPileProvider(), TileEntityWoodPile.class);
        WailaProvider.addProvider(new FirepitProvider(), TileEntityNewFirepit.class);
        WailaProvider.addProvider(new DryingRackProvider(), TileEntityDryingRack.class);
        WailaProvider.addProvider(new ChoppingBlockProvider(), TileEntityChoppingBlock.class);
        WailaProvider.addProvider(new SaddleQuernProvider(), TileEntitySaddleQuern.class);
        WailaProvider.addProvider(new ClayLampProvider(), TileEntityClayLamp.class);
        WailaProvider.addProvider(new GenericSoilProvider(), BlockAquifer.class);
        WailaProvider.addProvider(new CookingPotProvider(), TileEntityCookingPot.class);
        WailaProvider.addProvider(new ScrewPressBarrelProvider(), TileEntityScrewPressBarrel.class);
        WailaProvider.addProvider(new CrackedOreProvider(), BlockCrackedOre.class, BlockCrackedOre2.class, BlockCrackedOre3.class);
        WailaProvider.addProvider(new ProcessingSurfaceProvider(), TileEntityProcessingSurface.class);

        WailaEntityProvider.addProvider(new AnimalMilkProvider(), IAnimal.class);
    }

    @SideOnly(Side.CLIENT)
    private static void registerCallbacks() {
        Bids.LOG.info("Register WAILA setup callback");

        FMLInterModComms.sendMessage("Waila", "register", WailaProvider.class.getCanonicalName() + ".setup");
        FMLInterModComms.sendMessage("Waila", "register", WailaEntityProvider.class.getCanonicalName() + ".setup");
    }

}
