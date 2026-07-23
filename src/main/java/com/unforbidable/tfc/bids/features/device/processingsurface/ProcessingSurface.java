package com.unforbidable.tfc.bids.features.device.processingsurface;

import com.dunk.tfc.Core.Recipes;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.names.BlockNames;
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
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.device.processingsurface.block.BlockProcessingSurface;
import com.unforbidable.tfc.bids.features.device.processingsurface.eventhandler.ProcessingSurfaceEventHandler;
import com.unforbidable.tfc.bids.features.device.processingsurface.render.RenderProcessingSurface;
import com.unforbidable.tfc.bids.features.device.processingsurface.tileentity.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.features.device.processingsurface.waila.ProcessingSurfaceWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@FeatureName("processingSurface")
public class ProcessingSurface extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(ProcessingSurfaceConfig::load, "crafting");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.PROCESSING_SURFACE, BlockProcessingSurface::new);

        init.tileEntity(TileEntityProcessingSurface.class, "BidsProcessingSurface");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderProcessingSurface())
            .block(BlockProcessingSurface.class);

        client.waila()
            .data(new ProcessingSurfaceWailaProvider(), TileEntityProcessingSurface.class);

        client.nei()
            .hide(BidsBlocks.processingSurface);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new ProcessingSurfaceEventHandler());

        for (Item knife : Recipes.knives) {
            setup.ores("itemScrapingTool")
                .add(knife);
        }

        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.blocks.hasThickLog()) {
                setup.ores("blockScrapingSurface")
                    .add(wood.blocks.getThickLog())
                    .add(wood.blocks.getThickLogAlt());
            }

            if (wood.blocks.hasStackedLogs()) {
                setup.ores("blockScrapingSurface")
                    .add(wood.blocks.getStackedLogs())
                    .add(wood.blocks.getStackedLogsAlt());
            }
        }

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            OreDictionary.registerOre("blockScrapingSurface", stone.blocks.getBlockStack(EnumStoneBlockType.RAW));
        }

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.soakedHide, 1, 0), new ItemStack(TFCItems.scrapedHide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.soakedHide, 1, 1), new ItemStack(TFCItems.scrapedHide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.soakedHide, 1, 2), new ItemStack(TFCItems.scrapedHide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4));

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.fur, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.fur, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.fur, 1, 2), new ItemStack(TFCItems.hide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.furScrap, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.furScrap, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.furScrap, 1, 2), new ItemStack(TFCItems.hide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4));

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.wolfFur, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.wolfFur, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.wolfFur, 1, 2), new ItemStack(TFCItems.hide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.wolfFurScrap, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.wolfFurScrap, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.wolfFurScrap, 1, 2), new ItemStack(TFCItems.hide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4));

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.bearFur, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.bearFur, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.bearFur, 1, 2), new ItemStack(TFCItems.hide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.bearFurScrap, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.bearFurScrap, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.bearFurScrap, 1, 2), new ItemStack(TFCItems.hide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4));

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.sheepSkin, 1, 0), new ItemStack(TFCItems.hide, 1, 0),
                "itemScrapingTool", "blockScrapingSurface", 1))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.sheepSkin, 1, 1), new ItemStack(TFCItems.hide, 1, 1),
                "itemScrapingTool", "blockScrapingSurface", 2))
            .add(new ProcessingSurfaceRecipe(new ItemStack(TFCItems.sheepSkin, 1, 2), new ItemStack(TFCItems.hide, 1, 2),
                "itemScrapingTool", "blockScrapingSurface", 4));
    }

}
