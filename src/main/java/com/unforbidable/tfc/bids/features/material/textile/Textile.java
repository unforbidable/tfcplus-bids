package com.unforbidable.tfc.bids.features.material.textile;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.drying.WetnessInfo;
import com.unforbidable.tfc.bids.api.features.handwork.CardingRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.HandworkRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.HecklingRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.RopeMakingRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.SpinningRecipe;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.BarrelRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.LoomRecipe;
import com.unforbidable.tfc.bids.core.crafting.MatchingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.crafting.carding.CardingRegistry;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import com.unforbidable.tfc.bids.features.crafting.drying.DryingRegistry;
import com.unforbidable.tfc.bids.features.crafting.handwork.HandworkRegistry;
import com.unforbidable.tfc.bids.features.crafting.heckling.HecklingRegistry;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.RopeMakingRegistry;
import com.unforbidable.tfc.bids.features.crafting.spinning.SpinningRegistry;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import com.unforbidable.tfc.bids.features.device.dryingsurface.DryingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.device.processingsurface.ProcessingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.device.soakingsurface.SoakingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.material.textile.eventhandler.TextileInteractEventHandler;
import com.unforbidable.tfc.bids.features.material.textile.item.ItemTextile;
import com.unforbidable.tfc.bids.features.material.textile.main.TextileHints;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.core.crafting.actions.ExtraDrop.extraDrop;

@FeatureName("textile")
public class Textile extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(TextileConfig::load, "crafting");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.SISAL_FIBER_RINSED, ItemTextile::new)
            .hints(TextileHints.DRYING_FIBER);
        init.item(ItemNames.SISAL_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.CARDING, TextileHints.SPINNING_TWINE);
        init.item(ItemNames.SISAL_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_TWINE)
            .apply(i -> i.setMaterialColor(0xfdd9ab));
        init.item(ItemNames.SISAL_TWINE, ItemTextile::new)
            .hints(TextileHints.TWISTING, TextileHints.WEAVING_BURLAP)
            .apply(i -> i.setMaterialColor(0x97784f));

        init.item(ItemNames.JUTE_STALK, ItemTextile::new)
            .hints(TextileHints.RETTING_STALK);
        init.item(ItemNames.JUTE_STALK_RETTED, ItemTextile::new)
            .hints(TextileHints.PEELING_STALK);
        init.item(ItemNames.JUTE_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.HECKLING, TextileHints.SPINNING_TWINE);
        init.item(ItemNames.JUTE_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_TWINE)
            .apply(i -> i.setMaterialColor(0xddccb7));
        init.item(ItemNames.JUTE_TWINE, ItemTextile::new)
            .hints(TextileHints.TWISTING, TextileHints.WEAVING_BURLAP)
            .apply(i -> i.setMaterialColor(0x765d42));

        init.item(ItemNames.FLAX_STALK, ItemTextile::new)
            .hints(TextileHints.RETTING_STALK);
        init.item(ItemNames.FLAX_STALK_RETTED, ItemTextile::new)
            .hints(TextileHints.DRYING_STALK);
        init.item(ItemNames.FLAX_STALK_DRIED, ItemTextile::new)
            .hints(TextileHints.BREAKING_HAND, TextileHints.BREAKING_SURFACE);
        init.item(ItemNames.FLAX_STALK_BROKEN, ItemTextile::new)
            .hints(TextileHints.SCUTCHING_HAND, TextileHints.SCUTCHING_SURFACE);
        init.item(ItemNames.FLAX_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.HECKLING, TextileHints.SPINNING_STRING);
        init.item(ItemNames.FLAX_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_STRING)
            .apply(i -> i.setMaterialColor(0xb0c389));

        init.item(ItemNames.COTTON_BOLL, ItemTextile::new)
            .hints(TextileHints.REFINING_BOLL);
        init.item(ItemNames.COTTON_BOLL_REFINED, ItemTextile::new)
            .hints(TextileHints.WILLOWING_COTTON);
        init.item(ItemNames.COTTON_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.CARDING, TextileHints.SPINNING_YARN);
        init.item(ItemNames.COTTON_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_YARN)
            .apply(i -> i.setMaterialColor(0xf3faf0));

        init.item(ItemNames.WOOL_WASHED, ItemTextile::new)
            .hints(TextileHints.RINSING_WOOL);
        init.item(ItemNames.WOOL_RINSED, ItemTextile::new)
            .hints(TextileHints.DRYING_WOOL);
        init.item(ItemNames.WOOL_DRIED, ItemTextile::new)
            .hints(TextileHints.WILLOWING_WOOL);
        init.item(ItemNames.WOOL_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.CARDING, TextileHints.SPINNING_YARN);
        init.item(ItemNames.WOOL_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_YARN)
            .apply(i -> i.setMaterialColor(0xf7f7e6));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new TextileInteractEventHandler());

        setup.help(TFCItems.wool)
            .hints(TextileHints.WASHING_WOOL, TextileHints.RINSING_WOOL);
        setup.help(TFCItems.cotton)
            .hints(TextileHints.REFINING_BOLL);
        setup.help(TFCItems.flax)
            .hints(TextileHints.REFINING_STALK);
        setup.help(TFCItems.jute)
            .hints(TextileHints.REFINING_STALK);
        setup.help(TFCItems.linenString)
            .hints(TextileHints.TWISTING, TextileHints.WEAVING_CLOTH);
        setup.help(TFCItems.agave)
            .hints(TextileHints.EXTRACTING);
        setup.help(TFCItems.juteFiber)
            .hints(TextileHints.DRYING_FIBER);
        setup.help(TFCItems.sisalFiber)
            .hints(TextileHints.RINSING_FIBER);

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.blocks.hasThickLog()) {
                setup.ores("blockFlaxWorkingSurface")
                    .add(wood.blocks.getThickVert());
            }
        }

        setup.ores("itemFlaxBreakingTool")
            .add(TFCItems.pole)
            .add(TFCItems.stick);
        setup.ores("itemFlaxScutchingTool")
            .add(TFCItems.pole)
            .add(TFCItems.stick);
        setup.ores("itemPrimitiveTool")
            .add(TFCItems.pole)
            .add(TFCItems.stick);

        setup.ores("materialFiber")
            .add(BidsItems.flaxFiberCoarse, BidsItems.flaxFiberRefined)
            .add(BidsItems.juteFiberCoarse, BidsItems.juteFiberRefined)
            .add(BidsItems.sisalFiberCoarse, BidsItems.sisalFiberRefined)
            .add(BidsItems.woolFiberCoarse, BidsItems.woolFiberRefined)
            .add(BidsItems.cottonFiberCoarse, BidsItems.cottonFiberRefined);

        setup.ores("materialString")
            .add(BidsItems.juteTwine)
            .add(BidsItems.sisalTwine);

        setup.recipes().addShapeless(new ItemStack(BidsItems.flaxStalk),
                TFCItems.flax, "itemScrapingTool")
            .action(damageTool("itemScrapingTool"))
            .action(extraDrop(ItemFoodTFC.createTag(new ItemStack(BidsItems.flaxSeeds), 2)));

        setup.recipes().addShapeless(new ItemStack(BidsItems.juteStalk),
                TFCItems.jute, "itemScrapingTool")
            .action(damageTool("itemScrapingTool"));

        setup.recipes().addShapeless(new ItemStack(BidsItems.cottonBollRefined),
                BidsItems.cottonBoll, "itemScrapingTool")
            .action(damageTool("itemScrapingTool"));

        // Refining TFC cotton in case it has not been converted
        setup.recipes().addShapeless(new ItemStack(BidsItems.cottonBollRefined),
                TFCItems.cotton, "itemScrapingTool")
            .action(damageTool("itemScrapingTool"));

        setup.recipes().addShapeless(new ItemStack(TFCItems.sisalFiber),
                TFCItems.agave, "itemHandAxe")
            .action(damageTool("itemHandAxe"));

        setup.registry(DryingRegistry.wetness)
            .add(BidsItems.sisalFiberRinsed, new WetnessInfo(500, 1f))
            .add(TFCItems.juteFiber, new WetnessInfo(500, 1f))
            .add(BidsItems.flaxStalk, new WetnessInfo(1000, 1f))
            .add(BidsItems.flaxStalkRetted, new WetnessInfo(500, 0.5f))
            .add(BidsItems.woolRinsed, new WetnessInfo(1000, 1f));

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(BidsItems.sisalFiberRinsed))
                .produces(new ItemStack(BidsItems.sisalFiberCoarse))
                .dry()
                .hours(8)
                .build())
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(TFCItems.juteFiber))
                .produces(new ItemStack(BidsItems.juteFiberCoarse))
                .dry()
                .hours(12)
                .build())
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(BidsItems.flaxStalkRetted))
                .produces(new ItemStack(BidsItems.flaxStalkDried))
                .dry()
                .hours(16)
                .build())
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(BidsItems.woolRinsed))
                .produces(new ItemStack(BidsItems.woolDried))
                .dry()
                .hours(2)
                .build());

        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(new ItemStack(BidsItems.flaxStalk))
                .produces(new ItemStack(BidsItems.flaxStalkRetted))
                .wet()
                .warm()
                .hours(20)
                .build())
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(new ItemStack(BidsItems.flaxStalkRetted))
                .produces(new ItemStack(BidsItems.flaxStalkDried))
                .dry()
                .hours(20)
                .build());

        setup.registry(SoakingSurfaceRegistry.recipes)
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.flaxStalk), new ItemStack(BidsItems.flaxStalkRetted),
                "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.juteStalk), new ItemStack(BidsItems.juteStalkRetted),
                "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(TFCItems.sisalFiber), new ItemStack(BidsItems.sisalFiberRinsed),
                "blockFreshWater", 0))
            // Washing wool can be skipped however the wool needs to be rinsed for an extended period of time
            .add(new SoakingSurfaceRecipe(new ItemStack(TFCItems.wool), new ItemStack(BidsItems.woolRinsed),
                "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolWashed), new ItemStack(BidsItems.woolRinsed),
                "blockFreshWater", 0));

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkDried), new ItemStack(BidsItems.flaxStalkBroken),
                "itemFlaxBreakingTool", "blockFlaxWorkingSurface", 0.25f))
            .add(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkBroken), new ItemStack(BidsItems.flaxFiberCoarse),
                "itemFlaxScutchingTool", "blockFlaxWorkingSurface", 0.25f));

        setup.registry(HandworkRegistry.recipes)
            .add(new HandworkRecipe(new ItemStack(BidsItems.juteStalkRetted), new ItemStack(TFCItems.juteFiber), 80))
            .add(new HandworkRecipe(new ItemStack(BidsItems.flaxStalkDried), new ItemStack(BidsItems.flaxStalkBroken), 240))
            .add(new HandworkRecipe(new ItemStack(BidsItems.flaxStalkBroken), new ItemStack(BidsItems.flaxFiberCoarse), 240))
            .add(new HandworkRecipe(new ItemStack(BidsItems.cottonBollRefined), new ItemStack(BidsItems.cottonFiberCoarse), 60))
            .add(new HandworkRecipe(new ItemStack(BidsItems.woolDried), new ItemStack(BidsItems.woolFiberCoarse), 60));

        // Flax fiber spinning recipe is preserved as a way to convert TFC+ Flax fibers to string
        // since unlike other fibers it is not used as an intermediate material in the new extended textile processing
        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(TFCItems.flaxFiber), new ItemStack(TFCItems.linenString, 4), 120));

        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(BidsItems.flaxFiberRefined), new ItemStack(TFCItems.linenString, 4), 120))
            .add(new SpinningRecipe(new ItemStack(BidsItems.cottonFiberRefined), new ItemStack(TFCItems.cottonYarn, 6), 120))
            .add(new SpinningRecipe(new ItemStack(BidsItems.woolFiberRefined), new ItemStack(TFCItems.woolYarn, 8), 120))
            .add(new SpinningRecipe(new ItemStack(BidsItems.sisalFiberRefined), new ItemStack(BidsItems.sisalTwine, 2), 120))
            .add(new SpinningRecipe(new ItemStack(BidsItems.juteFiberRefined), new ItemStack(BidsItems.juteTwine, 2), 120));

        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(BidsItems.flaxFiberCoarse), new ItemStack(TFCItems.linenString, 4), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(BidsItems.cottonFiberCoarse), new ItemStack(TFCItems.cottonYarn, 6), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(BidsItems.woolFiberCoarse), new ItemStack(TFCItems.woolYarn, 8), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(BidsItems.sisalFiberCoarse), new ItemStack(BidsItems.sisalTwine, 2), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(BidsItems.juteFiberCoarse), new ItemStack(BidsItems.juteTwine, 2), 120 * 4));

        setup.registry(RopeMakingRegistry.recipes)
            .add(new RopeMakingRecipe(new ItemStack(TFCItems.linenString, 16), new ItemStack(TFCItems.rope), 600))
            .add(new RopeMakingRecipe(new ItemStack(BidsItems.sisalTwine, 8), new ItemStack(TFCItems.rope), 600))
            .add(new RopeMakingRecipe(new ItemStack(BidsItems.juteTwine, 12), new ItemStack(TFCItems.rope), 600));

        setup.registry(CardingRegistry.recipes)
            .add(new CardingRecipe(new ItemStack(BidsItems.sisalFiberCoarse), new ItemStack(BidsItems.sisalFiberRefined), 80))
            .add(new CardingRecipe(new ItemStack(BidsItems.cottonFiberCoarse), new ItemStack(BidsItems.cottonFiberRefined), 80))
            .add(new CardingRecipe(new ItemStack(BidsItems.woolFiberCoarse), new ItemStack(BidsItems.woolFiberRefined), 80));

        setup.registry(HecklingRegistry.recipes)
            .add(new HecklingRecipe(new ItemStack(BidsItems.juteFiberCoarse), new ItemStack(BidsItems.juteFiberRefined), 120))
            .add(new HecklingRecipe(new ItemStack(BidsItems.flaxFiberCoarse), new ItemStack(BidsItems.flaxFiberRefined), 120));

        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new FluidStack(BidsFluids.soapyWater, 100), new ItemStack(TFCItems.wool))
                .produces(new ItemStack(BidsItems.woolWashed, 1))
                .withHeat()
                .inTime(50)
                .build());

        setup.registry(TfcRegistry.Recipes.barrel)
            .add(BarrelRecipe.add(builder -> builder
                .consumes(new ItemStack(TFCItems.sisalFiber), new FluidStack(TFCFluids.FRESHWATER, 100))
                .produces(new ItemStack(BidsItems.sisalFiberRinsed), new FluidStack(TFCFluids.FRESHWATER, 100))
                .withSealTime(0).withMinTechLevel(0).beingSealed(false)))
            .add(BarrelRecipe.add(builder -> builder
                .consumes(new ItemStack(BidsItems.juteStalk), new FluidStack(TFCFluids.FRESHWATER, 200))
                .produces(new ItemStack(BidsItems.juteStalkRetted), new FluidStack(TFCFluids.FRESHWATER, 200))
                .withMinTechLevel(0).beingSealed(false)))
            .add(BarrelRecipe.add(builder -> builder
                .consumes(new ItemStack(BidsItems.flaxStalk), new FluidStack(TFCFluids.FRESHWATER, 200))
                .produces(new ItemStack(BidsItems.flaxStalkRetted), new FluidStack(TFCFluids.FRESHWATER, 200))
                .withMinTechLevel(0).beingSealed(false)))
            .add(BarrelRecipe.add(builder -> builder
                .consumes(new ItemStack(TFCItems.wool), new FluidStack(TFCFluids.FRESHWATER, 200))
                .produces(new ItemStack(BidsItems.woolRinsed), new FluidStack(TFCFluids.FRESHWATER, 200))
                .withSealTime(16).withMinTechLevel(0).beingSealed(false)))
            .add(BarrelRecipe.add(builder -> builder
                .consumes(new ItemStack(BidsItems.woolWashed), new FluidStack(TFCFluids.FRESHWATER, 100))
                .produces(new ItemStack(BidsItems.woolRinsed), new FluidStack(TFCFluids.FRESHWATER, 100))
                .withSealTime(0).withMinTechLevel(0).beingSealed(false)));

        setup.registry(TfcRegistry.Recipes.barrel)
            .add(BarrelRecipe.addMultiItem(builder -> builder
                .consumes(new ItemStack(BidsItems.sisalTwine), new FluidStack(TFCFluids.WAX, 200))
                .produces(new ItemStack(TFCBlocks.candleOff), new FluidStack(TFCFluids.WAX, 200))
                .keepingStackSize(false).withSealTime(0).beingSealed(false).withMinTechLevel(0)))
            .add(BarrelRecipe.addMultiItem(builder -> builder
                .consumes(new ItemStack(BidsItems.juteTwine), new FluidStack(TFCFluids.WAX, 200))
                .produces(new ItemStack(TFCBlocks.candleOff), new FluidStack(TFCFluids.WAX, 200))
                .keepingStackSize(false).withSealTime(0).beingSealed(false).withMinTechLevel(0)));

        setup.registry(TfcRegistry.Recipes.barrel)
            .add(BarrelRecipe.addMultiItem(builder -> builder
                .consumes(new ItemStack(BidsItems.cottonBollRefined), new FluidStack(TFCFluids.AMMONIUMCHLORIDE, 250))
                .produces(new ItemStack(TFCItems.ammoniumChlorideBall), new FluidStack(TFCFluids.AMMONIUMCHLORIDE, 250))
                .keepingStackSize(false).withSealTime(0).beingSealed(false).withMinTechLevel(0)));

        ResourceLocation ropeRes = new ResourceLocation("terrafirmacraftplus", "textures/blocks/Rope.png");
        setup.registry(TfcRegistry.Recipes.loom)
            .add(LoomRecipe.add(new ItemStack(BidsItems.sisalTwine, 20), new ItemStack(TFCItems.burlapCloth, 1), ropeRes))
            .add(LoomRecipe.add(new ItemStack(BidsItems.juteTwine, 16), new ItemStack(TFCItems.burlapCloth, 1), ropeRes));

        setup.registry(DryingRackRegistry.tyingEquipment)
            .add(new DryingRackTyingEquipment(BidsItems.sisalTwine, false, Blocks.wool, 1))
            .add(new DryingRackTyingEquipment(BidsItems.juteTwine, false, Blocks.wool, 1));

        if (TextileConfig.removeOriginalSpindleSpinningRecipes) {
            setup.recipes()
                .match(r -> r.input.contains(TFCItems.spindle))
                .edit(MatchingRecipe::remove);
        }

        if (TextileConfig.removeOriginalRopeMakingRecipes) {
            setup.recipes()
                .match(r -> r.output.is(TFCItems.rope))
                .edit(MatchingRecipe::remove);
        }

        if (TextileConfig.removeOriginalBurlapFiberLoomRecipes) {
            setup.registry(TfcRegistry.Recipes.loom)
                .add(LoomRecipe.remove(TFCItems.sisalFiber))
                .add(LoomRecipe.remove(TFCItems.juteFiber));
        }
    }

}
