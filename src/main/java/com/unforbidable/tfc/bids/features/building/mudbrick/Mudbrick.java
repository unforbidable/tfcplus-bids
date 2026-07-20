package com.unforbidable.tfc.bids.features.building.mudbrick;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipe;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipePattern;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.drying.WetnessInfo;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.stone.EnumStoneBlockType;
import com.unforbidable.tfc.bids.core.schemes.stone.EnumStoneItemType;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneIndex;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneScheme;
import com.unforbidable.tfc.bids.features.building.carving.CarvingRegistry;
import com.unforbidable.tfc.bids.features.building.mudbrick.block.BlockMudbrickChimney;
import com.unforbidable.tfc.bids.features.building.mudbrick.block.itemblock.ItemMudbrickChimney;
import com.unforbidable.tfc.bids.features.building.mudbrick.item.ItemDryingMudBrick;
import com.unforbidable.tfc.bids.features.building.mudbrick.render.DryingMudBrickItemRenderer;
import com.unforbidable.tfc.bids.features.building.mudbrick.tileentity.TileEntityMudBrickChimney;
import com.unforbidable.tfc.bids.features.crafting.drying.DryingRegistry;
import com.unforbidable.tfc.bids.features.device.dryingsurface.DryingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.device.dryingsurface.main.rendering.MudBrickRenderInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

/**
 * <li><b>mud brick chimney</b> - primitive chimney enabling early furnace and kiln</li>
 * <li><b>drying mud brick</b> - partially dried mud brick for intermediate drying step</li></>
 */
@FeatureName("mudbrick")
public class Mudbrick extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.MUD_BRICK_CHIMNEY, () -> new BlockMudbrickChimney(0), ItemMudbrickChimney.class)
            .harvest("adze", 0)
            .apply(b -> b.setDirt(TFCBlocks.dirt));
        init.block(BlockNames.MUD_BRICK_CHIMNEY_2, () -> new BlockMudbrickChimney(16), ItemMudbrickChimney.class)
            .harvest("adze", 0)
            .apply(b -> b.setDirt(TFCBlocks.dirt2));

        init.tileEntity(TileEntityMudBrickChimney.class, "BidsChimney");

        init.item(ItemNames.DRYING_MUD_BRICK, ItemDryingMudBrick::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new DryingMudBrickItemRenderer())
            .item(BidsItems.dryingMudBrick);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        CarvingRecipePattern chimneyPattern = new CarvingRecipePattern()
            .carveLayer("    ", " ## ", " ## ", "    ")
            .carveLayer("    ", " ## ", " ## ", "    ")
            .carveLayer("    ", " ## ", " ## ", "    ")
            .carveLayer("    ", " ## ", " ## ", "    ");

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
                "PB", "BB", 'P', new ItemStack(BidsItems.clayPipe, 1, 1),
                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));
            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
                "PB", "BB", 'P', new ItemStack(TFCItems.logs, 1, 48), // Bamboo
                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));

            setup.registry(CarvingRegistry.recipes)
                .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY),
                    stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICKS), chimneyPattern));
        }

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            setup.registry(DryingSurfaceRegistry.recipes).add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                    .consumes(stone.items.getItem(EnumStoneItemType.MUD_BRICK_WET))
                    .produces(stone.items.getItem(EnumStoneItemType.MUD_BRICK_DRYING), stone.items.getItem(EnumStoneItemType.MUD))
                    .dry()
                    .notWet()
                    .hours(20)
                    .build())
                .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                    .consumes(stone.items.getItem(EnumStoneItemType.MUD_BRICK_DRYING))
                    .produces(stone.items.getItem(EnumStoneItemType.MUD_BRICK), stone.items.getItem(EnumStoneItemType.MUD))
                    .dry()
                    .notWet()
                    .hours(10)
                    .build());
        }

        setup.registry(DryingSurfaceRegistry.render)
            .add(TFCItems.mudBrick, new MudBrickRenderInfo(false))
            .add(BidsItems.dryingMudBrick, new MudBrickRenderInfo(true));

        setup.registry(DryingRegistry.wetness)
            .add(TFCItems.mudBrick, new WetnessInfo(500, 1f))
            .add(BidsItems.dryingMudBrick, new WetnessInfo(500, 0.5f));
    }

}
