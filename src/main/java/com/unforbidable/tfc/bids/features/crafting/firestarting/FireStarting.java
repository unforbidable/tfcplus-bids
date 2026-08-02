package com.unforbidable.tfc.bids.features.crafting.firestarting;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.meta.Tinder;
import com.unforbidable.tfc.bids.api.meta.Tow;
import com.unforbidable.tfc.bids.api.meta.Wood;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.crafting.MatchingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.features.crafting.firestarting.eventhandler.FireStartingEventHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.item.ItemNewFirestarter;
import com.unforbidable.tfc.bids.features.crafting.firestarting.item.ItemTinder;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers.BlastFurnaceFireStartingHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers.BloomeryFireStartingHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers.ForgeFireStartingHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers.PotteryFireStartingHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.network.TinderBurningHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.network.TinderBurningPacket;
import com.unforbidable.tfc.bids.features.crafting.firestarting.render.TinderItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@FeatureName("fireStarting")
public class FireStarting extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(FireStartingConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.FIRE_PLOW, ItemNewFirestarter::new)
            .apply(i -> i.setMaxItemUseDuration(100)
                .setMaxDamage(12));
        init.item(ItemNames.HAND_DRILL, ItemNewFirestarter::new)
            .apply(i -> i.setMaxItemUseDuration(80)
                .setMaxDamage(32));
        init.item(ItemNames.BOW_DRILL, ItemNewFirestarter::new)
            .apply(i -> i.setMaxItemUseDuration(40)
                .setMaxDamage(32));

        init.item(ItemNames.TINDER, ItemTinder::new)
            .meta("Straw", "Birch Bark", "Bast", "Flax", "Jute", "Sisal", "Cotton");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new TinderItemRenderer())
            .item(BidsItems.tinder);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.network()
            .register(TinderBurningPacket.class);

        Network.handlePacket(TinderBurningPacket.class, TinderBurningHandler::handlePacket);

        setup.event()
            .handler(new FireStartingEventHandler());

        setup.ores("materialTinderGood")
            .add(new ItemStack(BidsItems.tinder, 1, Tinder.BIRCH_BARK))
            .add(new ItemStack(BidsItems.tinder, 1, Tinder.BAST))
            .add(new ItemStack(BidsItems.tinder, 1, Tinder.FLAX))
            .add(new ItemStack(BidsItems.tinder, 1, Tinder.SISAL));

        setup.ores("materialTinderExcellent")
            .add(new ItemStack(BidsItems.tinder, 1, Tinder.JUTE))
            .add(new ItemStack(BidsItems.tinder, 1, Tinder.COTTON));

        setup.ores("stickWoodBundle")
            .add(TFCItems.stickBundle)
            .add(BidsItems.smallStickBundle)
            .add(BidsItems.tiedStickBundle);

        setup.recipes()
            .addShaped(new ItemStack(BidsItems.handDrillFirestarter), "S", "B",
                'S', "stickWood", 'B', "boardWood");

        setup.recipes()
            .addShaped(new ItemStack(BidsItems.bowDrillFirestarter), "SD", "B ",
                'D', TFCItems.bow, 'S', "stickWood", 'B', "boardWood");

        setup.ores("materialTinderStraw2")
            .add(TFCItems.straw);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 2, Tinder.STRAW), "materialTinderStraw2");

        setup.ores("materialTinderBirchBark2")
            .add(new ItemStack(BidsItems.bark, 1, Wood.BIRCH));
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 2, Tinder.BIRCH_BARK), "materialTinderBirchBark2");

        setup.ores("materialTinderBast1")
            .add(BidsItems.barkCordage);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 1, Tinder.BAST), "materialTinderBast1");

        setup.ores("materialTinderBast4")
            .add(BidsItems.barkFiberCoarse, BidsItems.barkFiberSmooth, BidsItems.barkCordage);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 4, Tinder.BAST), "materialTinderBast4");

        setup.ores("materialTinderFlax1")
            .add(new ItemStack(BidsItems.tow, 1, Tow.FLAX));
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 1, Tinder.FLAX), "materialTinderFlax1");

        setup.ores("materialTinderFlax4")
            .add(TFCItems.flaxFiber, BidsItems.flaxFiberCoarse, BidsItems.flaxFiberRefined);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 4, Tinder.FLAX), "materialTinderFlax4");

        setup.ores("materialTinderJute1")
            .add(BidsItems.juteTwine)
            .add(new ItemStack(BidsItems.tow, 1, Tow.JUTE));
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 1, Tinder.JUTE), "materialTinderJute1");

        setup.ores("materialTinderJute4")
            .add(BidsItems.juteFiberCoarse, BidsItems.juteFiberRefined);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 4, Tinder.JUTE), "materialTinderJute4");

        setup.ores("materialTinderSisal1")
            .add(BidsItems.sisalTwine);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 1, Tinder.SISAL), "materialTinderSisal1");

        setup.ores("materialTinderSisal4")
            .add(BidsItems.sisalFiberCoarse, BidsItems.sisalFiberRefined, BidsItems.sisalTwine);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 4, Tinder.SISAL), "materialTinderSisal4");

        setup.ores("materialTinderCotton1")
            .add(TFCItems.cottonYarn);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 1, Tinder.COTTON), "materialTinderCotton1");

        setup.ores("materialTinderCotton4")
            .add(TFCItems.cotton, BidsItems.cottonBollRefined, BidsItems.cottonFiberCoarse, BidsItems.cottonFiberRefined);
        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 4, Tinder.COTTON), "materialTinderCotton4");

        if (FireStartingConfig.replaceOriginalFirestarterRecipes) {
            setup.recipes()
                .match(r -> r.output.is(TFCItems.fireStarter))
                .edit(r -> r.replace()
                    .setOutput(new ItemStack(BidsItems.firePlowFirestarter)));

            setup.recipes()
                .match(r -> r.output.is(TFCItems.bowFireStarter))
                .edit(MatchingRecipe::remove);
        }

        setup.registry(FireStartingRegistry.handlers)
            .add(new BloomeryFireStartingHandler(4))
            .add(new BlastFurnaceFireStartingHandler(4))
            .add(new PotteryFireStartingHandler(6))
            .add(new ForgeFireStartingHandler(8));
    }

}
