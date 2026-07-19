package com.unforbidable.tfc.bids.compat.waila;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaEntityProvider;
import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WailaInit extends Initializable {

    @SideOnly(Side.CLIENT)
    @Override
    public void initClientOnly(FMLInitializationEvent event) {
        Bids.LOG.info("Register WAILA setup callback");

        FMLInterModComms.sendMessage("Waila", "register", WailaDataProvider.class.getCanonicalName() + ".setup");
        FMLInterModComms.sendMessage("Waila", "register", WailaEntityProvider.class.getCanonicalName() + ".setup");

        // TODO register providers for respective features

        //WailaProvider.addProvider(new CrucibleProvider(), TileEntityCrucible.class);
        //WailaProvider.addProvider(new FurnaceProvider(), TileEntityChimney.class, TEChimney.class);
        //WailaProvider.addProvider(new QuarryProvider(), TileEntityQuarry.class);
//        WailaProvider.addProvider(new CarvingProvider(), TileEntityCarving.class);
//        WailaProvider.addProvider(new WoodPileProvider(), TileEntityWoodPile.class);
//        WailaProvider.addProvider(new FirepitProvider(), TileEntityNewFirepit.class);
//        WailaProvider.addProvider(new DryingRackProvider(), TileEntityDryingRack.class);
//        WailaProvider.addProvider(new ChoppingBlockProvider(), TileEntityChoppingBlock.class);
//        WailaProvider.addProvider(new SaddleQuernProvider(), TileEntitySaddleQuern.class);
//        WailaProvider.addProvider(new ClayLampProvider(), TileEntityClayLamp.class);
//        WailaProvider.addProvider(new GenericSoilProvider(), BlockAquifer.class);
//        WailaProvider.addProvider(new CookingPotProvider(), TileEntityCookingPot.class);
//        WailaProvider.addProvider(new ScrewPressBarrelProvider(), TileEntityScrewPressBarrel.class);
//        WailaProvider.addProvider(new CrackedOreProvider(), BlockCrackedOre.class, BlockCrackedOre2.class, BlockCrackedOre3.class);
//        WailaProvider.addProvider(new ProcessingSurfaceProvider(), TileEntityProcessingSurface.class);
//        WailaProvider.addProvider(new DecorativeSurfaceProvider(), TileEntityDecorativeSurface.class);
//        WailaProvider.addProvider(new SoakingSurfaceProvider(), TileEntitySoakingSurface.class);
//        WailaProvider.addProvider(new DryingSurfaceProvider(), TileEntityDryingSurface.class);
//
//        WailaEntityProvider.addProvider(new AnimalMilkProvider(), IAnimal.class);

    }

}
