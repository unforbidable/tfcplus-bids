package com.unforbidable.tfc.bids.features.device.saddlequern;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipe;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipePattern;
import com.unforbidable.tfc.bids.api.features.quern.SaddleQuernRecipe;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.compat.tfc.meta.DyeMeta;
import com.unforbidable.tfc.bids.compat.tfc.meta.LooseRockMeta;
import com.unforbidable.tfc.bids.compat.tfc.meta.OreMeta;
import com.unforbidable.tfc.bids.compat.tfc.meta.PowderMeta;
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
import com.unforbidable.tfc.bids.features.crafting.pressing.PressingRegistry;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockStonePressLever;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.BlockWorkStone;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemSaddleQuern;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemStonePressWeight;
import com.unforbidable.tfc.bids.features.device.saddlequern.block.item.ItemWorkStone;
import com.unforbidable.tfc.bids.features.device.saddlequern.main.StonePressHelper;
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

@FeatureName("saddleQuern")
public class SaddleQuern extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(SaddleQuernConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.SADDLE_QUERN_BASE, () -> new BlockSaddleQuern(lookup.block(BlockNames.ROUGH_STONE_SED)), ItemSaddleQuern.class)
            .harvest("shovel", 0);

        init.block(BlockNames.SADDLE_QUERN_HANDSTONE, () -> new BlockWorkStone(lookup.block(BlockNames.ROUGH_STONE_SED)), ItemWorkStone.class)
            .apply(b -> b.setWorkStoneType(WorkStoneType.SADDLE_QUERN_CRUSHING));

        init.block(BlockNames.SADDLE_QUERN_PRESSING_STONE, () -> new BlockWorkStone(lookup.block(BlockNames.ROUGH_STONE_SED)), ItemWorkStone.class)
            .apply(b -> b.setWorkStoneType(WorkStoneType.SADDLE_QUERN_PRESSING));

        init.block(BlockNames.STONE_PRESS_LEVER, BlockStonePressLever::new)
            .fireInfo(5, 5)
            .harvest("axe", 0);

        init.block(BlockNames.STONE_PRESS_WEIGHT, () -> new BlockStonePressWeight(lookup.block(BlockNames.ROUGH_STONE_SED)), ItemStonePressWeight.class)
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
                    .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE),
                        stone.blocks.getBlockStack(EnumStoneBlockType.SADDLE_QUERN), saddleQuernPattern));

                for (CarvingRecipePattern handstonePattern : handstonePatterns) {
                    setup.registry(CarvingRegistry.recipes)
                        .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE),
                            stone.blocks.getBlockStack(EnumStoneBlockType.HAND_STONE), handstonePattern));
                }

                setup.registry(CarvingRegistry.recipes)
                    .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE),
                        stone.blocks.getBlockStack(EnumStoneBlockType.PRESSING_STONE), pressingStonePattern));

                setup.registry(CarvingRegistry.recipes)
                    .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE),
                        stone.blocks.getBlockStack(EnumStoneBlockType.WEIGHT_STONE), weightStonePattern));
            }
        }

        // Salt
        setup.registry(SaddleQuernRegistry.recipes)
            .add(new SaddleQuernRecipe(new ItemStack(TFCItems.looseRock, 1, LooseRockMeta.ROCK_SALT),
                new ItemStack(TFCItems.powder, 2, PowderMeta.SALT)));

        setup.registry(SaddleQuernRegistry.recipes)
            .add(new SaddleQuernRecipe(new ItemStack(TFCItems.bone),
                new ItemStack(TFCItems.dye, 1, DyeMeta.BONE)))
            .add(new SaddleQuernRecipe(new ItemStack(TFCItems.boneFragment),
                new ItemStack(TFCItems.dye, 1, DyeMeta.BONE)));

        if (SaddleQuernConfig.allowGrindHematite) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.smallOreChunk, 1, OreMeta.HEMATITE),
                    new ItemStack(TFCItems.powder, 1, PowderMeta.HEMATITE)));
        }

        if (SaddleQuernConfig.allowGrindLimonite) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.smallOreChunk, 1, OreMeta.LIMONITE),
                    new ItemStack(TFCItems.powder, 1, PowderMeta.LIMONITE)));
        }

        if (SaddleQuernConfig.allowGrindMalachite) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.smallOreChunk, 1, OreMeta.MALACHITE),
                    new ItemStack(TFCItems.powder, 1, PowderMeta.MALACHITE)));
        }

        if (SaddleQuernConfig.allowGrindLapisLazuli) {
            setup.registry(SaddleQuernRegistry.recipes)
                .add(new SaddleQuernRecipe(new ItemStack(TFCItems.oreChunk, 1, OreMeta.LAPIS_LAZULI),
                    new ItemStack(TFCItems.powder, 2, PowderMeta.LAPIS_LAZULI)));
        }

        setup.registry(StonePressRegistry.recipes)
            .adapt(PressingRegistry.recipes, StonePressHelper::adaptPressingRecipe);
    }

}
