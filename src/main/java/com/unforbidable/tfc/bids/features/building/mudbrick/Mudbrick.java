package com.unforbidable.tfc.bids.features.building.mudbrick;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
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
import com.unforbidable.tfc.bids.features.building.mudbrick.block.BlockMudbrickChimney;
import com.unforbidable.tfc.bids.features.building.mudbrick.block.itemblock.ItemMudbrickChimney;
import com.unforbidable.tfc.bids.features.building.mudbrick.item.ItemDryingMudBrick;
import com.unforbidable.tfc.bids.features.building.mudbrick.render.DryingMudBrickItemRenderer;
import com.unforbidable.tfc.bids.features.building.mudbrick.tileentity.TileEntityMudBrickChimney;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.MUD_BRICK_CHIMNEY;
import static com.unforbidable.tfc.bids.api.names.BlockNames.MUD_BRICK_CHIMNEY_2;
import static com.unforbidable.tfc.bids.api.names.ItemNames.DRYING_MUD_BRICK;

/**
 * <li><b>mud brick chimney</b> - primitive chimney enabling early furnace and kiln</li>
 * <li><b>drying mud brick</b> - partially dried mud brick for intermediate drying step</li></>
 */
@FeatureName("mudbrick")
public class Mudbrick extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(MUD_BRICK_CHIMNEY, () -> new BlockMudbrickChimney(0), ItemMudbrickChimney.class)
            .harvest("shovel", 0)
            .apply(b -> b.setDirt(TFCBlocks.dirt));
        init.block(MUD_BRICK_CHIMNEY_2, () -> new BlockMudbrickChimney(16), ItemMudbrickChimney.class)
            .harvest("shovel", 0)
            .apply(b -> b.setDirt(TFCBlocks.dirt2));

        init.tileEntity(TileEntityMudBrickChimney.class, "BidsChimney");

        init.item(DRYING_MUD_BRICK, ItemDryingMudBrick::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.item(DRYING_MUD_BRICK)
            .render(new DryingMudBrickItemRenderer());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
//        CarvingRecipePattern chimneyPattern = new CarvingRecipePattern()
//            .carveLayer("    ", " ## ", " ## ", "    ")
//            .carveLayer("    ", " ## ", " ## ", "    ")
//            .carveLayer("    ", " ## ", " ## ", "    ")
//            .carveLayer("    ", " ## ", " ## ", "    ");

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
                "PB", "BB", 'P', new ItemStack(BidsItems.clayPipe, 1, 1),
                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));
            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY, 2),
                "PB", "BB", 'P', new ItemStack(TFCItems.logs, 1, 48), // Bamboo
                'B', stone.items.getItem(EnumStoneItemType.MUD_BRICK));

            // TODO add recipe when carving implemented
//            setup.registry(BidsRegistry.CARVING_RECIPES)
//                .add(new CarvingRecipe(stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICK_CHIMNEY),
//                    stone.blocks.getBlockStack(EnumStoneBlockType.MUD_BRICKS), chimneyPattern));
        }

        // TODO add mud brick surface drying recipes
    }

}
