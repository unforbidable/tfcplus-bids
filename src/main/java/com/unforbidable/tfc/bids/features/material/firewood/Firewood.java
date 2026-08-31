package com.unforbidable.tfc.bids.features.material.firewood;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.choppingblock.ChoppingBlockRecipe;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.api.features.woodpile.SeasoningRecipe;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
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
import com.unforbidable.tfc.bids.features.device.choppingblock.ChoppingBlockRegistry;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitConfig;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import com.unforbidable.tfc.bids.features.device.woodpile.WoodpileRegistry;
import com.unforbidable.tfc.bids.features.device.woodpile.main.seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.features.material.bark.BarkConfig;
import com.unforbidable.tfc.bids.features.material.firewood.block.BlockStackedFirewood;
import com.unforbidable.tfc.bids.features.material.firewood.item.ItemFirewood;
import com.unforbidable.tfc.bids.features.material.firewood.item.ItemFirewoodSeasoned;

import static com.unforbidable.tfc.bids.core.crafting.actions.CopySeasoning.copySeasoning;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.core.crafting.actions.ExtraDrop.extraDrop;

@FeatureName("firewood")
public class Firewood extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.STACKED_FIREWOOD, () -> new BlockStackedFirewood(0));
        init.block(BlockNames.STACKED_FIREWOOD_2, () -> new BlockStackedFirewood(16));
        init.block(BlockNames.STACKED_FIREWOOD_3, () -> new BlockStackedFirewood(32));

        init.item(ItemNames.FIREWOOD, ItemFirewood::new)
            .meta(Global.WOOD_ALL);
        init.item(ItemNames.FIREWOOD_SEASONED, ItemFirewoodSeasoned::new)
            .meta(Global.WOOD_ALL);
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new SeasonableItemRenderer())
            .item(BidsItems.firewood);

        client.render(new SeasonedItemRenderer())
            .item(BidsItems.firewoodSeasoned);
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

                setup.registry(ChoppingBlockRegistry.recipes)
                    .add(new ChoppingBlockRecipe(wood.items.getLog(), wood.items.getFirewood(),
                        wood.items.getBark(), BarkConfig.dropSplittingChance,
                        "blockChoppingBlock", "itemAxe"));

                if (wood.items.hasChoppedLog()) {
                    setup.registry(ChoppingBlockRegistry.recipes)
                        .add(new ChoppingBlockRecipe(wood.items.getChoppedLog(), wood.items.getFirewood(),
                            wood.items.getBark(), BarkConfig.dropSplittingChance,
                            "blockChoppingBlock", "itemAxe"));
                }

                if (wood.items.hasPeeledLog()) {
                    setup.registry(ChoppingBlockRegistry.recipes)
                        .add(new ChoppingBlockRecipe(wood.items.getPeeledLog(), wood.items.getFirewood(),
                            wood.items.getBark(), BarkConfig.dropSplittingChance,
                            "blockChoppingBlock", "itemAxe"));
                }
            }

            if (wood.items.hasSeasonedFirewood()) {
                setup.recipes().addShapeless(wood.items.getSeasonedFirewood(),
                        wood.getOreWithSuffix("logWoodSeasoned"), "itemAxe")
                    .action(damageTool("itemAxe"))
                    .action(extraDrop(wood.items.getBark(), BarkConfig.dropSplittingSeasonedChance));

                if (!wood.hardwood) {
                    setup.recipes().addShapeless(wood.items.getSeasonedFirewood(),
                            wood.getOreWithSuffix("logWoodSeasoned"), "itemHandAxe")
                        .action(damageTool("itemHandAxe"));
                }

                if (wood.items.hasSeasonedLog()) {
                    setup.registry(ChoppingBlockRegistry.recipes)
                        .add(new ChoppingBlockRecipe(wood.items.getSeasonedLog(), wood.items.getSeasonedFirewood(),
                            wood.items.getBark(), BarkConfig.dropSplittingSeasonedChance,
                            "blockChoppingBlock", "itemAxe"));
                }

                if (wood.items.hasSeasonedChoppedLog()) {
                    setup.registry(ChoppingBlockRegistry.recipes)
                        .add(new ChoppingBlockRecipe(wood.items.getSeasonedChoppedLog(), wood.items.getSeasonedFirewood(),
                            wood.items.getBark(), BarkConfig.dropSplittingSeasonedChance,
                            "blockChoppingBlock", "itemAxe"));
                }

                if (wood.items.hasSeasonedPeeledLog()) {
                    setup.registry(ChoppingBlockRegistry.recipes)
                        .add(new ChoppingBlockRecipe(wood.items.getSeasonedPeeledLog(), wood.items.getSeasonedFirewood(),
                            wood.items.getBark(), BarkConfig.dropSplittingSeasonedChance,
                            "blockChoppingBlock", "itemAxe"));
                }

                setup.registry(WoodpileRegistry.seasoning)
                    .add(new SeasoningRecipe(wood.items.getFirewood(), wood.items.getSeasonedFirewood(),
                        SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.FIREWOOD)));
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
