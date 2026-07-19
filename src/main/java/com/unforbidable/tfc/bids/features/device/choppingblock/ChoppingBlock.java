package com.unforbidable.tfc.bids.features.device.choppingblock;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipe;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipePattern;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.building.carving.CarvingRegistry;
import com.unforbidable.tfc.bids.features.device.choppingblock.block.BlockChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.block.itemblock.ItemChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.nei.ChoppingNeiHandler;
import com.unforbidable.tfc.bids.features.device.choppingblock.render.RenderChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.render.RenderTileChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.tileentity.TileEntityChoppingBlock;
import com.unforbidable.tfc.bids.features.device.choppingblock.waila.ChoppingBlockWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.unforbidable.tfc.bids.api.names.BlockNames.CHOPPING_BLOCK;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CHOPPING_BLOCK_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CHOPPING_BLOCK_3;

@FeatureName("choppingBlock")
public class ChoppingBlock extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(CHOPPING_BLOCK, () -> new BlockChoppingBlock(TFCBlocks.woodVert), ItemChoppingBlock.class)
            .fireInfo(5, 5);
        init.block(CHOPPING_BLOCK_2, () -> new BlockChoppingBlock(TFCBlocks.woodVert2), ItemChoppingBlock.class)
            .fireInfo(5, 5);
        init.block(CHOPPING_BLOCK_3, () -> new BlockChoppingBlock(TFCBlocks.woodVert3), ItemChoppingBlock.class)
            .fireInfo(5, 5);

        init.tileEntity(TileEntityChoppingBlock.class, "BidsChoppingBlock");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderChoppingBlock())
            .block(BlockChoppingBlock.class);

        client.render(new RenderTileChoppingBlock())
            .tileEntity(TileEntityChoppingBlock.class);

        client.waila()
            .data(new ChoppingBlockWailaProvider(), TileEntityChoppingBlock.class);

        client.nei()
            .handler(new ChoppingNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        CarvingRecipePattern choppingBlockPattern = new CarvingRecipePattern()
            .carveEntireLayer();

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.blocks.hasChoppingBlock()) {
                setup.ores("blockChoppingBlock")
                    .add(wood.blocks.getChoppingBlock());
            }

            if (wood.blocks.hasChoppingBlock()) {
                setup.registry(CarvingRegistry.recipes)
                    .add(new CarvingRecipe(wood.blocks.getChoppingBlock(),
                        wood.blocks.getWoodVert(), choppingBlockPattern));
            }
        }
    }

}
