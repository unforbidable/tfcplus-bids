package com.unforbidable.tfc.bids.features.material.logs;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
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
import com.unforbidable.tfc.bids.features.device.woodpile.WoodpileRegistry;
import com.unforbidable.tfc.bids.features.device.woodpile.main.renderable.RenderableLogsTFC;
import com.unforbidable.tfc.bids.features.device.woodpile.main.seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.features.material.bark.BarkConfig;
import com.unforbidable.tfc.bids.features.material.logs.item.ItemLogsSeasoned;
import com.unforbidable.tfc.bids.features.material.logs.item.ItemPeeledLog;
import com.unforbidable.tfc.bids.features.material.logs.item.ItemPeeledLogSeasoned;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

import static com.unforbidable.tfc.bids.api.names.ItemNames.LOG_SEASONED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.PEELED_LOG;
import static com.unforbidable.tfc.bids.api.names.ItemNames.PEELED_LOG_SEASONED;
import static com.unforbidable.tfc.bids.core.crafting.actions.CopySeasoning.copySeasoning;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.core.crafting.actions.ExtraDrop.extraDrop;
import static com.unforbidable.tfc.bids.core.crafting.actions.KeepItem.keepItem;

@FeatureName("logs")
public class Logs extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(LOG_SEASONED, ItemLogsSeasoned::new);
        init.item(PEELED_LOG, ItemPeeledLog::new);
        init.item(PEELED_LOG_SEASONED, ItemPeeledLogSeasoned::new);
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.item(PEELED_LOG)
            .render(new SeasonableItemRenderer());
        client.item(LOG_SEASONED)
            .render(new SeasonedItemRenderer());
        client.item(PEELED_LOG_SEASONED)
            .render(new SeasonedItemRenderer());

        client.run(() -> MinecraftForgeClient.registerItemRenderer(TFCItems.logs, new SeasonableItemRenderer()));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("logWood")
            .add(BidsItems.logsSeasoned)
            .add(BidsItems.peeledLog)
            .add(BidsItems.peeledLogSeasoned);

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            // TODO allow TFC logs only if configured (FEATURE)
//            if (wood.hardwood) {
//                OreDictionary.registerOre("logWoodPlugAndFeather", wood.items.getLog());
//                OreDictionary.registerOre("logWoodPlugAndFeather", wood.items.getChoppedLog());
//            }

            if (wood.items.hasSeasonedLog()) {
                setup.ores("logWoodAny")
                    .add(wood.items.getSeasonedLog());
                setup.ores(wood.getOreWithSuffix("logWood"))
                    .add(wood.items.getSeasonedLog());
                setup.ores(wood.getOreWithSuffix("logWoodSeasoned"))
                    .add(wood.items.getSeasonedLog());

                setup.registry(WoodpileRegistry.seasoning)
                    .add(new SeasoningRecipe(wood.items.getSeasonedLog(),
                        wood.items.getLog(), SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.LOG)));

                if (wood.items.hasChoppedLog()) {
                    setup.ores("logWoodAny")
                        .add(wood.items.getSeasonedChoppedLog());
                    setup.ores(wood.getOreWithSuffix("logWood"))
                        .add(wood.items.getSeasonedChoppedLog());
                    setup.ores(wood.getOreWithSuffix("logWoodSeasoned"))
                        .add(wood.items.getSeasonedChoppedLog());

                    setup.registry(WoodpileRegistry.seasoning)
                        .add(new SeasoningRecipe(wood.items.getSeasonedChoppedLog(),
                            wood.items.getChoppedLog(), SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.CHOPPED_LOG)));
                }
            }

            if (wood.items.hasPeeledLog()) {
                setup.ores("logWoodAny")
                    .add(wood.items.getPeeledLog());
                setup.ores(wood.getOreWithSuffix("logWood"))
                    .add(wood.items.getPeeledLog());
                setup.ores(wood.getOreWithSuffix("logWoodFresh"))
                    .add(wood.items.getPeeledLog());
                setup.ores(wood.getOreWithSuffix("logWoodPeeled"))
                    .add(wood.items.getPeeledLog());

                setup.recipes().addShapeless(wood.items.getPeeledLog(),
                        wood.items.getLog(), "itemAdze")
                    .action(damageTool("itemAdze"))
                    .action(extraDrop(wood.items.getBark(), BarkConfig.dropPeelingChance))
                    .action(copySeasoning(TFCItems.logs));

//                BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                    wood.items.getPeeledLog(), wood.items.getLog()));

                if (wood.items.hasChoppedLog()) {
                    setup.recipes().addShapeless(wood.items.getPeeledLog(),
                            wood.items.getChoppedLog(), "itemAdze")
                        .action(damageTool("itemAdze"))
                        .action(extraDrop(wood.items.getBark(), BarkConfig.dropPeelingChance));

//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                        wood.items.getPeeledLog(), wood.items.getChoppedLog()));
                }
            }

            if (wood.items.hasSeasonedPeeledLog()) {
                setup.ores("logWoodPeeledSeasoned")
                    .add(wood.items.getSeasonedPeeledLog());
                setup.ores("logWoodAny")
                    .add(wood.items.getSeasonedPeeledLog());
                setup.ores(wood.getOreWithSuffix("logWood"))
                    .add(wood.items.getSeasonedPeeledLog());
                setup.ores(wood.getOreWithSuffix("logWoodSeasoned"))
                    .add(wood.items.getSeasonedPeeledLog());
                setup.ores(wood.getOreWithSuffix("logWoodPeeledSeasoned"))
                    .add(wood.items.getSeasonedPeeledLog());

                if (wood.hardwood) {
                    setup.ores("logWoodPlugAndFeather")
                        .add(wood.items.getSeasonedPeeledLog());
                }

                setup.recipes().addShapeless(wood.items.getSeasonedPeeledLog(),
                        wood.items.getSeasonedLog(), "itemAdze")
                    .action(damageTool("itemAdze"))
                    .action(extraDrop(wood.items.getBark(), BarkConfig.dropPeelingSeasonedChance));

//                BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                    wood.items.getSeasonedPeeledLog(), wood.items.getSeasonedLog()));

                setup.registry(WoodpileRegistry.seasoning)
                    .add(new SeasoningRecipe(wood.items.getSeasonedPeeledLog(),
                        wood.items.getPeeledLog(), SeasoningHelper.getWoodSeasoningDuration(wood, EnumWoodItemType.PEELED_LOG)));

                if (wood.items.hasSeasonedChoppedLog()) {
                    setup.recipes().addShapeless(wood.items.getSeasonedPeeledLog(),
                            wood.items.getSeasonedChoppedLog(), "itemAdze")
                        .action(damageTool("itemAdze"))
                        .action(extraDrop(wood.items.getBark(), BarkConfig.dropPeelingSeasonedChance));

//                    BidsRegistry.CHOPPING_BLOCK_RECIPES.register(new ChoppingBlockRecipe("blockChoppingBlock", "itemAdze",
//                        wood.items.getSeasonedPeeledLog(), wood.items.getSeasonedChoppedLog()));
                }
            }

            // TODO: sawing planks drops sawdust (FEATURE)
            // TODO: actually replace TFC recipes using ore (CLEANUP)

            // Copies of TFC recipes for items made logs
            if (wood.items.hasLumber()) {
                setup.recipes().addShapeless(wood.items.getLumber(8),
                        wood.getOreWithSuffix("logWoodPeeledSeasoned"), "itemSaw")
                    .action(damageTool("itemSaw"));
            }

            // Copies of TFC recipes for block made from logs
            if (wood.items.hasPeeledLog() || wood.items.hasSeasonedLog()) {
                setup.recipes().addShaped(wood.blocks.getWoodSupport(8),
                        "A2", " 2", '2', wood.getOreWithSuffix("logWood"), 'A', "itemSaw")
                    .action(damageTool("itemSaw"));

                setup.recipes().addShaped(wood.blocks.getFence(6),
                    "LPL", "LPL", 'L', wood.getOreWithSuffix("logWood"), 'P', wood.items.getLumber());
            }
        }

        // Copies of TFC recipes for generic wood items made logs
        setup.recipes().addShapeless(new ItemStack(TFCItems.pole),
                "logWoodAny", "itemKnife")
            .action(damageTool("itemKnife"));
        setup.recipes().addShaped(new ItemStack(TFCItems.clayTile),
                " X", "XL", 'L', "logWoodAny", 'X', "lumpClay")
            .action(keepItem("logWoodAny"));
        setup.recipes().addShapeless(new ItemStack(TFCItems.paddle),
                TFCItems.pole, "logWoodAny", "itemKnife")
            .action(damageTool("itemKnife"));

        setup.registry(WoodpileRegistry.renderable)
            .add(BidsItems.logsSeasoned, new RenderableLogsTFC())
            .add(BidsItems.peeledLog, (WoodpileRenderable) BidsItems.peeledLog)
            .add(BidsItems.peeledLogSeasoned, (WoodpileRenderable) BidsItems.peeledLogSeasoned);
    }

}
