package com.unforbidable.tfc.bids.features.crafting.firestarting;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
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
            .meta("Straw");
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

        setup.recipes()
            .addShapeless(new ItemStack(BidsItems.tinder, 2, 0), TFCItems.straw);

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
