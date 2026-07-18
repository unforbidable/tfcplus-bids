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
import com.unforbidable.tfc.bids.features.device.firepit.item.ItemKindling;
import com.unforbidable.tfc.bids.features.device.processingsurface.ProcessingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.device.soakingsurface.SoakingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.material.textile.eventhandler.TextileInteractEventHandler;
import com.unforbidable.tfc.bids.features.material.textile.item.ItemTextile;
import com.unforbidable.tfc.bids.features.material.textile.main.TextileHints;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BARK_CORDAGE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BARK_FIBER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BARK_FIBER_COARSE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BARK_FIBER_KINDLING;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BARK_FIBER_SMOOTH;
import static com.unforbidable.tfc.bids.api.names.ItemNames.COTTON_BOLL;
import static com.unforbidable.tfc.bids.api.names.ItemNames.COTTON_BOLL_REFINED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.COTTON_FIBER_COARSE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.COTTON_FIBER_REFINED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAX_FIBER_COARSE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAX_FIBER_REFINED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAX_STALK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAX_STALK_BROKEN;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAX_STALK_DRIED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAX_STALK_RETTED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUTE_FIBER_COARSE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUTE_FIBER_REFINED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUTE_STALK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUTE_STALK_RETTED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.JUTE_TWINE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.SISAL_FIBER_COARSE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.SISAL_FIBER_REFINED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.SISAL_FIBER_RINSED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.SISAL_TWINE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOOL_DRIED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOOL_FIBER_COARSE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOOL_FIBER_REFINED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOOL_RINSED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOOL_WASHED;
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
        init.item(BARK_FIBER, ItemTextile::new)
            .hints(TextileHints.DRYING_FIBER);
        init.item(BARK_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.RUBBING, TextileHints.SPINNING_CORDAGE);
        init.item(BARK_FIBER_SMOOTH, ItemTextile::new)
            .hints(TextileHints.SPINNING_CORDAGE);
        init.item(BARK_CORDAGE, ItemTextile::new)
            .hints(TextileHints.TWISTING)
            .apply(i -> i.setMaterialColor(0x8c7a4b));

        init.item(BARK_FIBER_KINDLING, ItemKindling::new)
            .apply(i -> i.setFuelKindlingQuality(1f));

        init.item(SISAL_FIBER_RINSED, ItemTextile::new)
            .hints(TextileHints.DRYING_FIBER);
        init.item(SISAL_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.CARDING, TextileHints.SPINNING_TWINE);
        init.item(SISAL_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_TWINE)
            .apply(i -> i.setMaterialColor(0xfdd9ab));
        init.item(SISAL_TWINE, ItemTextile::new)
            .hints(TextileHints.TWISTING, TextileHints.WEAVING_BURLAP)
            .apply(i -> i.setMaterialColor(0x97784f));

        init.item(JUTE_STALK, ItemTextile::new)
            .hints(TextileHints.RETTING_STALK);
        init.item(JUTE_STALK_RETTED, ItemTextile::new)
            .hints(TextileHints.PEELING_STALK);
        init.item(JUTE_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.HECKLING, TextileHints.SPINNING_TWINE);
        init.item(JUTE_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_TWINE)
            .apply(i -> i.setMaterialColor(0xddccb7));
        init.item(JUTE_TWINE, ItemTextile::new)
            .hints(TextileHints.TWISTING, TextileHints.WEAVING_BURLAP)
            .apply(i -> i.setMaterialColor(0x765d42));

        init.item(FLAX_STALK, ItemTextile::new)
            .hints(TextileHints.RETTING_STALK);
        init.item(FLAX_STALK_RETTED, ItemTextile::new)
            .hints(TextileHints.DRYING_STALK);
        init.item(FLAX_STALK_DRIED, ItemTextile::new)
            .hints(TextileHints.BREAKING_HAND, TextileHints.BREAKING_SURFACE);
        init.item(FLAX_STALK_BROKEN, ItemTextile::new)
            .hints(TextileHints.SCUTCHING_HAND, TextileHints.SCUTCHING_SURFACE);
        init.item(FLAX_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.HECKLING, TextileHints.SPINNING_STRING);
        init.item(FLAX_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_STRING)
            .apply(i -> i.setMaterialColor(0xb0c389));

        init.item(COTTON_BOLL, ItemTextile::new)
            .hints(TextileHints.REFINING_BOLL);
        init.item(COTTON_BOLL_REFINED, ItemTextile::new)
            .hints(TextileHints.WILLOWING_COTTON);
        init.item(COTTON_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.CARDING, TextileHints.SPINNING_YARN);
        init.item(COTTON_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_YARN)
            .apply(i -> i.setMaterialColor(0xf3faf0));

        init.item(WOOL_WASHED, ItemTextile::new)
            .hints(TextileHints.RINSING_WOOL);
        init.item(WOOL_RINSED, ItemTextile::new)
            .hints(TextileHints.DRYING_WOOL);
        init.item(WOOL_DRIED, ItemTextile::new)
            .hints(TextileHints.WILLOWING_WOOL);
        init.item(WOOL_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.CARDING, TextileHints.SPINNING_YARN);
        init.item(WOOL_FIBER_REFINED, ItemTextile::new)
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
            if (wood.hasBarkFibers) {
                setup.ores("itemBarkHasFibers")
                    .add(wood.items.getBark());

                setup.help(wood.items.getBark())
                    .hints(TextileHints.EXTRACTING);
            }

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

        // TODO move to respective feature
        //setup.ores("itemFlaxBreakingTool")
        //    .add(BidsItems.woodenMallet);
        //setup.ores("itemFlaxScutchingTool")
        //   .add(BidsItems.scutchingKnife);

        // TODO allow refining and extracting using a hand axe

        setup.recipes().addShapeless(new ItemStack(BidsItems.barkFiber),
                "itemBarkHasFibers", "itemKnife")
            .action(damageTool("itemKnife"));

        // TODO allow refined bark fiber to make kindling (FEATURE)
        setup.recipes().addShapeless(new ItemStack(BidsItems.barkFibreKindling),
            "stickWood", "stickWood", "stickWood", BidsItems.barkFiberCoarse);
        setup.recipes().addShapeless(new ItemStack(BidsItems.barkFibreKindling),
            BidsItems.smallStickBundle, BidsItems.barkFiberCoarse);

        setup.recipes().addShapeless(new ItemStack(BidsItems.flaxStalk),
                TFCItems.flax, "itemKnife")
            .action(damageTool("itemKnife"))
            .action(extraDrop(ItemFoodTFC.createTag(new ItemStack(BidsItems.flaxSeeds), 6)));

        setup.recipes().addShapeless(new ItemStack(BidsItems.juteStalk),
                TFCItems.jute, "itemKnife")
            .action(damageTool("itemKnife"));

        setup.recipes().addShapeless(new ItemStack(BidsItems.cottonBollRefined),
                BidsItems.cottonBoll, "itemKnife")
            .action(damageTool("itemKnife"));

        // Refining TFC cotton in case it has not been converted
        setup.recipes().addShapeless(new ItemStack(BidsItems.cottonBollRefined),
                TFCItems.cotton, "itemKnife")
            .action(damageTool("itemKnife"));

        setup.registry(DryingRegistry.wetness)
            .add(BidsItems.barkFiber, new WetnessInfo(500, 1f))
            .add(BidsItems.sisalFiberRinsed, new WetnessInfo(500, 1f))
            .add(TFCItems.juteFiber, new WetnessInfo(500, 1f))
            .add(BidsItems.flaxStalk, new WetnessInfo(1000, 1f))
            .add(BidsItems.flaxStalkRetted, new WetnessInfo(500, 0.5f))
            .add(BidsItems.woolRinsed, new WetnessInfo(1000, 1f));

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(BidsItems.barkFiber))
                .produces(new ItemStack(BidsItems.barkFiberCoarse))
                .dry()
                .hours(12)
                .build())
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
                .consumes(new ItemStack(BidsItems.barkFiber))
                .produces(new ItemStack(BidsItems.barkFiberCoarse))
                .dry()
                .hours(12)
                .build())
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
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkRetted),
                new ItemStack(BidsItems.flaxStalk), "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.juteStalkRetted),
                new ItemStack(BidsItems.juteStalk), "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.sisalFiberRinsed),
                new ItemStack(TFCItems.sisalFiber), "blockFreshWater", 0))
            // Washing wool can be skipped however the wool needs to be rinsed for an extended period of time
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed),
                new ItemStack(TFCItems.wool), "blockFreshWater", 20))
            .add(new SoakingSurfaceRecipe(new ItemStack(BidsItems.woolRinsed),
                new ItemStack(BidsItems.woolWashed), "blockFreshWater", 0));

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxStalkBroken),
                new ItemStack(BidsItems.flaxStalkDried),
                "itemFlaxBreakingTool", "blockFlaxWorkingSurface", 0.25f))
            .add(new ProcessingSurfaceRecipe(new ItemStack(BidsItems.flaxFiberCoarse),
                new ItemStack(BidsItems.flaxStalkBroken),
                "itemFlaxScutchingTool", "blockFlaxWorkingSurface", 0.25f));

        setup.registry(HandworkRegistry.recipes)
            .add(new HandworkRecipe(new ItemStack(BidsItems.barkFiberSmooth), new ItemStack(BidsItems.barkFiberCoarse), 80))
            .add(new HandworkRecipe(new ItemStack(TFCItems.juteFiber), new ItemStack(BidsItems.juteStalkRetted), 80))
            .add(new HandworkRecipe(new ItemStack(BidsItems.flaxStalkBroken), new ItemStack(BidsItems.flaxStalkDried), 240))
            .add(new HandworkRecipe(new ItemStack(BidsItems.flaxFiberCoarse), new ItemStack(BidsItems.flaxStalkBroken), 240))
            .add(new HandworkRecipe(new ItemStack(BidsItems.cottonFiberCoarse), new ItemStack(BidsItems.cottonBollRefined), 60))
            .add(new HandworkRecipe(new ItemStack(BidsItems.woolFiberCoarse), new ItemStack(BidsItems.woolDried), 60));

        // Flax fiber spinning recipe is preserved as a way to convert TFC+ Flax fibers to string
        // since unlike other fibers it is not used as an intermediate material in the new extended textile processing
        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(TFCItems.flaxFiber), 120));

        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(BidsItems.flaxFiberRefined), 120))
            .add(new SpinningRecipe(new ItemStack(TFCItems.cottonYarn, 6), new ItemStack(BidsItems.cottonFiberRefined), 120))
            .add(new SpinningRecipe(new ItemStack(TFCItems.woolYarn, 8), new ItemStack(BidsItems.woolFiberRefined), 120))
            .add(new SpinningRecipe(new ItemStack(BidsItems.barkCordage, 2), new ItemStack(BidsItems.barkFiberSmooth), 80))
            .add(new SpinningRecipe(new ItemStack(BidsItems.sisalTwine, 2), new ItemStack(BidsItems.sisalFiberRefined), 120))
            .add(new SpinningRecipe(new ItemStack(BidsItems.juteTwine, 2), new ItemStack(BidsItems.juteFiberRefined), 120));

        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(TFCItems.linenString, 4), new ItemStack(BidsItems.flaxFiberCoarse), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(TFCItems.cottonYarn, 6), new ItemStack(BidsItems.cottonFiberCoarse), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(TFCItems.woolYarn, 8), new ItemStack(BidsItems.woolFiberCoarse), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(BidsItems.sisalTwine, 2), new ItemStack(BidsItems.sisalFiberCoarse), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(BidsItems.juteTwine, 2), new ItemStack(BidsItems.juteFiberCoarse), 120 * 4))
            .add(new SpinningRecipe(new ItemStack(BidsItems.barkCordage, 2), new ItemStack(BidsItems.barkFiberCoarse), 80 * 4));

        setup.registry(RopeMakingRegistry.recipes)
            .add(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(TFCItems.linenString, 16), 600))
            .add(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.barkCordage, 12), 600))
            .add(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.sisalTwine, 8), 600))
            .add(new RopeMakingRecipe(new ItemStack(TFCItems.rope), new ItemStack(BidsItems.juteTwine, 12), 600));

        setup.registry(CardingRegistry.recipes)
            .add(new CardingRecipe(new ItemStack(BidsItems.sisalFiberRefined), new ItemStack(BidsItems.sisalFiberCoarse), 80))
            .add(new CardingRecipe(new ItemStack(BidsItems.cottonFiberRefined), new ItemStack(BidsItems.cottonFiberCoarse), 80))
            .add(new CardingRecipe(new ItemStack(BidsItems.woolFiberRefined), new ItemStack(BidsItems.woolFiberCoarse), 80));

        setup.registry(HecklingRegistry.recipes)
            .add(new HecklingRecipe(new ItemStack(BidsItems.juteFiberRefined), new ItemStack(BidsItems.juteFiberCoarse), 120))
            .add(new HecklingRecipe(new ItemStack(BidsItems.flaxFiberRefined), new ItemStack(BidsItems.flaxFiberCoarse), 120));

        // TODO enable when soap is added
//        setup.registry(CookingRegistry.recipes)
//            .add(CookingRecipe.builder()
//                .consumes(new FluidStack(BidsFluids.SOAPYWATER, 100), new ItemStack(TFCItems.wool))
//                .produces(new ItemStack(BidsItems.woolWashed, 1))
//                .withHeat()
//                .inTime(50)
//                .build());

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
            .add(new DryingRackTyingEquipment(BidsItems.barkCordage, false, Blocks.wool, 1))
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
