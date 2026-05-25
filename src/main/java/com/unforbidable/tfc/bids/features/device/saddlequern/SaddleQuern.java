package com.unforbidable.tfc.bids.features.device.saddlequern;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipe;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipePattern;
import com.unforbidable.tfc.bids.api.features.pressing.StonePressRecipe;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.stone.EnumStoneBlockType;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneIndex;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneScheme;
import com.unforbidable.tfc.bids.features.building.carving.CarvingRegistry;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockStonePressLever;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockWorkStone;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemWorkStone;
import com.unforbidable.tfc.bids.features.device.saddlequern.main.WorkStoneType;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderStonePressLever;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderTileSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.render.RenderWorkStone;
import com.unforbidable.tfc.bids.features.device.saddlequern.tileentity.TileEntitySaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.tileentity.TileEntityStonePressLever;
import com.unforbidable.tfc.bids.features.device.saddlequern.tileentity.TileEntityStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.waila.SaddleQuernWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.ROUGH_STONE_SED;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SADDLE_QUERN_BASE;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SADDLE_QUERN_HANDSTONE;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SADDLE_QUERN_PRESSING_STONE;
import static com.unforbidable.tfc.bids.api.names.BlockNames.STONE_PRESS_LEVER;
import static com.unforbidable.tfc.bids.api.names.BlockNames.STONE_PRESS_WEIGHT;

@FeatureName("saddleQuern")
public class SaddleQuern extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(SaddleQuernConfig::load);
        config.using(StonePressConfig::load, "stonePress");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(SADDLE_QUERN_BASE, () -> new BlockSaddleQuern(lookup.block(ROUGH_STONE_SED)), ItemSaddleQuern.class)
            .harvest("shovel", 0);

        init.block(SADDLE_QUERN_HANDSTONE, () -> new BlockWorkStone(lookup.block(ROUGH_STONE_SED)), ItemWorkStone.class)
            .apply(b -> b.setWorkStoneType(WorkStoneType.SADDLE_QUERN_CRUSHING));

        init.block(SADDLE_QUERN_PRESSING_STONE, () -> new BlockWorkStone(lookup.block(ROUGH_STONE_SED)), ItemWorkStone.class)
            .apply(b -> b.setWorkStoneType(WorkStoneType.SADDLE_QUERN_PRESSING));

        init.block(STONE_PRESS_LEVER, BlockStonePressLever::new)
            .fireInfo(5, 5)
            .harvest("axe", 0);

        init.block(STONE_PRESS_WEIGHT, () -> new BlockStonePressWeight(lookup.block(ROUGH_STONE_SED)), ItemStonePressWeight.class)
            .harvest("shovel", 0);

        init.tileEntity(TileEntitySaddleQuern.class, "BidsDrainingStone");
        init.tileEntity(TileEntityStonePressLever.class, "BidsStonePressLever");
        init.tileEntity(TileEntityStonePressWeight.class, "BidsStonePressWeight");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderSaddleQuern())
            .block(BlockSaddleQuern.class);

        client.render(new RenderStonePressLever())
            .block(BlockStonePressLever.class);

        client.render(new RenderStonePressWeight())
            .block(BlockStonePressWeight.class);

        client.render(new RenderWorkStone())
            .block(BlockWorkStone.class);

        client.render(new RenderTileSaddleQuern())
            .tileEntity(TileEntitySaddleQuern.class);

        client.waila()
            .data(new SaddleQuernWailaProvider(), TileEntitySaddleQuern.class);

        client.nei()
            .hide(BidsBlocks.stonePressLever);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        CarvingRecipePattern saddleQuernPattern = new CarvingRecipePattern()
            .carveLayer("    ", " ## ", " ## ", " ## ");

        CarvingRecipePattern[] handstonePatterns = {
            new CarvingRecipePattern()
                .carveEntireLayer()
                .carveEntireLayer()
                .carveLayer("####", "#  #", "#  #", "#  #")
                .carveLayer("####", "#  #", "#  #", "#  #"),
            new CarvingRecipePattern()
                .carveEntireLayer()
                .carveEntireLayer()
                .carveLayer("####", "  ##", "  ##", "  ##")
                .carveLayer("####", "  ##", "  ##", "  ##"),
            new CarvingRecipePattern()
                .carveEntireLayer()
                .carveEntireLayer()
                .carveLayer("####", "##  ", "##  ", "##  ")
                .carveLayer("####", "##  ", "##  ", "##  ")
        };

        CarvingRecipePattern pressingStonePattern = new CarvingRecipePattern()
            .carveEntireLayer()
            .carveEntireLayer()
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ");

        CarvingRecipePattern weightStonePattern = new CarvingRecipePattern()
            .carveEntireLayer()
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ")
            .carveLayer("####", "#   ", "#   ", "#   ");

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            if (stone.soft) {
                setup.registry(CarvingRegistry.recipes)
                    .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.SADDLE_QUERN),
                        stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), saddleQuernPattern));

                for (CarvingRecipePattern handstonePattern : handstonePatterns) {
                    setup.registry(CarvingRegistry.recipes)
                        .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.HAND_STONE),
                            stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), handstonePattern));
                }

                setup.registry(CarvingRegistry.recipes)
                    .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.PRESSING_STONE),
                        stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), pressingStonePattern));

                setup.registry(CarvingRegistry.recipes)
                    .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.WEIGHT_STONE),
                        stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), weightStonePattern));
            }
        }

        // TODO more recipes to respective features

//        setup.registry(SaddleQuernRegistry.recipes)
//            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.wheatCrushed), new ItemStack(TFCItems.wheatGrain)));
//        setup.registry(SaddleQuernRegistry.recipes)
//            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.barleyCrushed), new ItemStack(TFCItems.barleyGrain)));
//        setup.registry(SaddleQuernRegistry.recipes)
//            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.oatCrushed), new ItemStack(TFCItems.oatGrain)));
//        setup.registry(SaddleQuernRegistry.recipes)
//            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.riceCrushed), new ItemStack(TFCItems.riceGrain)));
//        setup.registry(SaddleQuernRegistry.recipes)
//            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.ryeCrushed), new ItemStack(TFCItems.ryeGrain)));
//        setup.registry(SaddleQuernRegistry.recipes)
//            .add(new SaddleQuernRecipe(new ItemStack(BidsItems.cornmealCrushed), new ItemStack(TFCItems.maizeEar)));

        // TODO investigate how come only food stuff can be processed but other recipes exist

        setup.registry(SaddleQuernRegistry.recipes)
            .add(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 2, 9), // Salt
                new ItemStack(TFCItems.looseRock, 1, 5)));

        setup.registry(SaddleQuernRegistry.recipes)
            .add(new SaddleQuernRecipe(new ItemStack(TFCItems.dye, 1, 15), // Bone Meal
                new ItemStack(TFCItems.bone)))
            .add(new SaddleQuernRecipe(new ItemStack(TFCItems.dye, 1, 15), // Bone Meal
                new ItemStack(TFCItems.boneFragment)));

        if (SaddleQuernConfig.allowGrindHematite) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 5), // Hematite
                    new ItemStack(TFCItems.smallOreChunk, 1, 3)));
        }

        if (SaddleQuernConfig.allowGrindLimonite) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 7), // Limonite
                    new ItemStack(TFCItems.smallOreChunk, 1, 11)));
        }

        if (SaddleQuernConfig.allowGrindMalachite) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 1, 8), // Malachite
                    new ItemStack(TFCItems.smallOreChunk, 1, 9)));
        }

        if (SaddleQuernConfig.allowGrindLapisLazuli) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.powder, 2, 6), // Lapis Lazuli
                    new ItemStack(TFCItems.oreChunk, 1, 318)));
        }

        // TODO more stone press recipes to respective features

        // TODO consider maintaining a single list of universal pressing recipes, adapted for stone press and screw press

        float inputRatio = 1 / StonePressConfig.efficiency; // input multiplier (for non-food input)
        float outputRatio = StonePressConfig.efficiency; // output multiplier (for food input)

        setup.registry(StonePressRegistry.recipes)
            .add(new StonePressRecipe(new FluidStack(TFCFluids.GRAPEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.grapes), 0.5f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.CANEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.sugarcane), 0.8f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.LEMONJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.lemon), 0.65f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.ORANGEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.orange), 0.5f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.PEACHJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.peach), 0.55f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.PLUMJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.plum), 0.65f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.FIGJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.fig), 0.5f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.CHERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.cherry), 0.7f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.DATEJUICE, 6),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.date), 0.8f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.PAPAYAJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.papaya), 0.6f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.strawberry), 0.65f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.blackberry), 0.61f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.blueberry), 0.6f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.bunchberry), 0.68f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.cranberry), 0.7f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.elderberry), 0.58f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.gooseberry), 0.6f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.raspberry), 0.6f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.snowberry), 0.66f * inputRatio)))
            .add(new StonePressRecipe(new FluidStack(TFCFluids.AGAVEJUICE, Math.round(40 * outputRatio)),
                new ItemStack(TFCItems.agave, 1)));

//        ItemStack steamedFish = BidsFood.setSteamed(ItemFoodTFC.createTag(new ItemStack(TFCItems.fishRaw), 0.5f * inputRatio), true);
//        // Require fish to be steamed to medium level
//        Food.setCooked(steamedFish, CookingHelper.getTempForItemStackCookedLevel(steamedFish, 3));
//        setup.registry(StonePressRegistry.recipes)
//            .add(new StonePressRecipe(new FluidStack(BidsFluids.OILYFISHWATER, 10), steamedFish));
//
//        setup.registry(StonePressRegistry.recipes)
//            .add(new StonePressRecipe(new FluidStack(BidsFluids.FLAXSEEDOIL, 10),
//                ItemFoodTFC.createTag(new ItemStack(BidsItems.flaxSeeds), 0.8f * inputRatio)));
    }

}
