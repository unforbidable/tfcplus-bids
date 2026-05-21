package com.unforbidable.tfc.bids.features.material.firewood;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api._obsolete.BidsRegistry;
import com.unforbidable.tfc.bids.api._obsolete.Crafting.ChoppingBlockRecipe;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.api.features.woodpile.SeasoningRecipe;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.common.render.SeasonableItemRenderer;
import com.unforbidable.tfc.bids.common.render.SeasonedItemRenderer;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.EnumWoodItemType;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitConfig;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import com.unforbidable.tfc.bids.features.device.woodpile.WoodpileRegistry;
import com.unforbidable.tfc.bids.features.device.woodpile.main.seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.features.material.bark.BarkConfig;
import com.unforbidable.tfc.bids.features.material.firewood.block.BlockStackedFirewood;
import com.unforbidable.tfc.bids.features.material.firewood.item.ItemFirewood;
import com.unforbidable.tfc.bids.features.material.firewood.item.ItemFirewoodSeasoned;

import static com.unforbidable.tfc.bids.api.names.BlockNames.STACKED_FIREWOOD;
import static com.unforbidable.tfc.bids.api.names.BlockNames.STACKED_FIREWOOD_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.STACKED_FIREWOOD_3;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FIREWOOD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FIREWOOD_SEASONED;
import static com.unforbidable.tfc.bids.core.crafting.actions.CopySeasoning.copySeasoning;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.core.crafting.actions.ExtraDrop.extraDrop;

@FeatureName("firewood")
public class Firewood extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(STACKED_FIREWOOD, () -> new BlockStackedFirewood(0));
        init.block(STACKED_FIREWOOD_2, () -> new BlockStackedFirewood(16));
        init.block(STACKED_FIREWOOD_3, () -> new BlockStackedFirewood(32));

        init.item(FIREWOOD, ItemFirewood::new)
            .meta(Global.WOOD_ALL);
        init.item(FIREWOOD_SEASONED, ItemFirewoodSeasoned::new)
            .meta(Global.WOOD_ALL);
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.item(FIREWOOD)
            .render(new SeasonableItemRenderer());
        client.item(FIREWOOD_SEASONED)
            .render(new SeasonedItemRenderer());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.items.hasFirewood()) {
                setup.recipes().addShapeless(wood.items.getFirewood(),
                        wood.getOreWithSuffix("logWoodFresh"), "itemAxe")
                    .action(damageTool("itemAxe"))
                    .action(extraDrop(wood.items.getBark(), BarkConfig.dropSplittingChance))
                    .action(copySeasoning(TFCItems.logs))
                    .action(copySeasoning(BidsItems.peeledLog));

                BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                    wood.items.getFirewood(),
                    wood.items.getLog()));

                if (wood.items.hasChoppedLog()) {
                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getFirewood(),
                        wood.items.getChoppedLog()));
                }

                if (wood.items.hasPeeledLog()) {
                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getFirewood(),
                        wood.items.getPeeledLog()));
                }
            }

            if (wood.items.hasSeasonedFirewood()) {
                setup.recipes().addShapeless(wood.items.getSeasonedFirewood(),
                        wood.getOreWithSuffix("logWoodSeasoned"), "itemAxe")
                    .action(damageTool("itemAdze")) // TODO wrong tool damaged (FIX)
                    .action(extraDrop(wood.items.getBark(), BarkConfig.dropSplittingSeasonedChance));

                if (wood.items.hasSeasonedLog()) {
                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getSeasonedFirewood(),
                        wood.items.getSeasonedLog()));
                }

                if (wood.items.hasSeasonedChoppedLog()) {
                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getSeasonedFirewood(),
                        wood.items.getSeasonedChoppedLog()));
                }

                if (wood.items.hasSeasonedPeeledLog()) {
                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAxe",
                        wood.items.getSeasonedFirewood(),
                        wood.items.getSeasonedPeeledLog()));
                }

                setup.registry(WoodpileRegistry.seasoning)
                        .add(new SeasoningRecipe(wood.items.getSeasonedFirewood(),
                    wood.items.getFirewood(), SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.FIREWOOD)));
            }
        }

        setup.registry(WoodpileRegistry.renderable)
            .add(BidsItems.firewood, (WoodpileRenderable) BidsItems.firewood)
            .add(BidsItems.firewoodSeasoned, (WoodpileRenderable) BidsItems.firewoodSeasoned);

        setup.registry(FirepitRegistry.fuel)
            .add(BidsItems.firewoodSeasoned, (FirepitFuelMaterial) BidsItems.firewoodSeasoned);

        if (FirepitConfig.allowFuelUnseasonedFirewood) {
            setup.registry(FirepitRegistry.fuel)
                .add(BidsItems.firewood, (FirepitFuelMaterial) BidsItems.firewood);
        }
    }

}
