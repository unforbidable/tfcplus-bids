package com.unforbidable.tfc.bids.features.building.roughstone;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
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
import com.unforbidable.tfc.bids.features.building.roughstone.block.BlockRoughStone;
import com.unforbidable.tfc.bids.features.building.roughstone.block.BlockRoughStoneBrick;
import com.unforbidable.tfc.bids.features.building.roughstone.block.BlockRoughStoneFence;
import com.unforbidable.tfc.bids.features.building.roughstone.block.BlockRoughStoneTile;
import com.unforbidable.tfc.bids.features.building.roughstone.block.blockitem.ItemRoughStone;
import com.unforbidable.tfc.bids.features.building.roughstone.block.blockitem.ItemRoughStoneFence;
import com.unforbidable.tfc.bids.features.building.roughstone.item.ItemRoughBrick;
import com.unforbidable.tfc.bids.features.building.roughstone.item.ItemRoughTile;
import com.unforbidable.tfc.bids.features.building.roughstone.main.carvable.CarvableRoughStone;
import com.unforbidable.tfc.bids.features.building.roughstone.main.carvable.CarvableRoughStoneBrick;
import com.unforbidable.tfc.bids.features.building.roughstone.main.carvable.CarvableRoughStoneTile;
import com.unforbidable.tfc.bids.features.building.roughstone.render.RenderRoughStoneFence;
import com.unforbidable.tfc.bids.features.resource.quarry.QuarryRegistry;
import com.unforbidable.tfc.bids.features.resource.quarry.main.QuarriableStone;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("roughStone")
public class RoughStone extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.ROUGH_STONE_SED, BlockRoughStone::new, ItemRoughStone.class)
            .meta(Global.STONE_SED)
            .harvest("shovel", 0)
            // 0 - Shale, 4 - Sandstone
            .apply(b -> b.setMetaHavingTopTexture(0, 4));
        init.block(BlockNames.ROUGH_STONE_MM, BlockRoughStone::new, ItemRoughStone.class)
            .meta(Global.STONE_MM)
            .harvest("shovel", 0)
            // 1 - Slate, 2 - Phyllite, 3 - Shist
            .apply(b -> b.setMetaHavingTopTexture(1, 2, 3));
        init.block(BlockNames.ROUGH_STONE_IG_IN, BlockRoughStone::new, ItemRoughStone.class)
            .meta(Global.STONE_IGIN)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_IG_EX, BlockRoughStone::new, ItemRoughStone.class)
            .meta(Global.STONE_IGEX)
            .harvest("shovel", 0);

        init.block(BlockNames.ROUGH_STONE_BRICK_SED, BlockRoughStoneBrick::new, ItemRoughStone.class)
            .meta(Global.STONE_SED)
            // 0 - Shale, 4 - Sandstone
            .harvest("shovel", 0)
            .apply(b -> b.setMetaHavingTopTexture(0, 4));
        init.block(BlockNames.ROUGH_STONE_BRICK_MM, BlockRoughStoneBrick::new, ItemRoughStone.class)
            .meta(Global.STONE_MM)
            .harvest("shovel", 0)
            // 1 - Slate, 2 - Phyllite, 3 - Shist
            .apply(b -> b.setMetaHavingTopTexture(1, 2, 3));
        init.block(BlockNames.ROUGH_STONE_BRICK_IG_IN, BlockRoughStoneBrick::new, ItemRoughStone.class)
            .meta(Global.STONE_IGIN)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_BRICK_IG_EX, BlockRoughStoneBrick::new, ItemRoughStone.class)
            .meta(Global.STONE_IGEX)
            .harvest("shovel", 0);

        init.block(BlockNames.ROUGH_STONE_TILE_SED, BlockRoughStoneTile::new, ItemRoughStone.class)
            .meta(Global.STONE_SED)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_TILE_MM, BlockRoughStoneTile::new, ItemRoughStone.class)
            .meta(Global.STONE_MM)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_TILE_IG_IN, BlockRoughStoneTile::new, ItemRoughStone.class)
            .meta(Global.STONE_IGIN)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_TILE_IG_EX, BlockRoughStoneTile::new, ItemRoughStone.class)
            .meta(Global.STONE_IGEX)
            .harvest("shovel", 0);

        init.block(BlockNames.ROUGH_STONE_BRICK_FENCE_SED, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_BRICK_SED)), ItemRoughStoneFence.class)
            .meta(Global.STONE_SED)
            .harvest("shovel", 0)
            .apply(b -> b.setMaterialBlockTopBottom(lookup.block(BlockNames.ROUGH_STONE_TILE_SED)));
        init.block(BlockNames.ROUGH_STONE_BRICK_FENCE_MM, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_BRICK_MM)), ItemRoughStoneFence.class)
            .meta(Global.STONE_MM)
            .harvest("shovel", 0)
            .apply(b -> b.setMaterialBlockTopBottom(lookup.block(BlockNames.ROUGH_STONE_TILE_MM)));
        init.block(BlockNames.ROUGH_STONE_BRICK_FENCE_IG_IN, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_BRICK_IG_IN)), ItemRoughStoneFence.class)
            .meta(Global.STONE_IGIN)
            .harvest("shovel", 0)
            .apply(b -> b.setMaterialBlockTopBottom(lookup.block(BlockNames.ROUGH_STONE_TILE_IG_IN)));
        init.block(BlockNames.ROUGH_STONE_BRICK_FENCE_IG_EX, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_BRICK_IG_EX)), ItemRoughStoneFence.class)
            .meta(Global.STONE_IGEX)
            .harvest("shovel", 0)
            .apply(b -> b.setMaterialBlockTopBottom(lookup.block(BlockNames.ROUGH_STONE_TILE_IG_EX)));

        init.block(BlockNames.ROUGH_STONE_TILE_FENCE_SED, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_TILE_SED)), ItemRoughStoneFence.class)
            .meta(Global.STONE_SED)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_TILE_FENCE_MM, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_TILE_MM)), ItemRoughStoneFence.class)
            .meta(Global.STONE_MM)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_TILE_FENCE_IG_IN, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_TILE_IG_IN)), ItemRoughStoneFence.class)
            .meta(Global.STONE_IGIN)
            .harvest("shovel", 0);
        init.block(BlockNames.ROUGH_STONE_TILE_FENCE_IG_EX, () -> new BlockRoughStoneFence(lookup.block(BlockNames.ROUGH_STONE_TILE_IG_EX)), ItemRoughStoneFence.class)
            .meta(Global.STONE_IGEX)
            .harvest("shovel", 0);

        init.item(ItemNames.ROUGH_STONE_BRICK, ItemRoughBrick::new)
            .meta(Global.STONE_ALL);
        init.item(ItemNames.ROUGH_STONE_TILE, ItemRoughTile::new)
            .meta(Global.STONE_ALL);
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderRoughStoneFence())
            .block(BlockRoughStoneFence.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            if (stone.quern) {
                OreDictionary.registerOre("stoneQuern", stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE));
            }

            setup.recipes().addShaped(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 4),
                    "SA", "  ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
                .action(damageTool("itemAdze"));
            setup.recipes().addShaped(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 4),
                    "AS", "  ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
                .action(damageTool("itemAdze"));

            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_TILES),
                "BB", "  ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE));
            setup.recipes().addShapeless(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE, 2),
                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_TILES));

            setup.recipes().addShaped(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 4),
                    "S ", "A ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
                .action(damageTool("itemAdze"));
            setup.recipes().addShaped(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 4),
                    "A ", "S ", 'S', stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), 'A', "itemAdze")
                .action(damageTool("itemAdze"));

            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_BRICKS),
                "BB", "  ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK));
            setup.recipes().addShapeless(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK, 2),
                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE_BRICKS));

            setup.recipes().addShapeless(stone.blocks.getBlockStack(EnumStoneBlockType.SMOOTH_STONE, 2),
                    stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_STONE), "itemChisel")
                .action(damageTool("itemChisel"));

            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_BRICK_FENCE, 2),
                "B ", "B ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK));
            setup.recipes().addShapeless(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK),
                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_BRICK_FENCE, 2));

            setup.recipes().addShaped(stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_TILE_FENCE, 2),
                "B ", "B ", 'B', stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE));
            setup.recipes().addShapeless(stone.items.getItem(EnumStoneItemType.ROUGH_STONE_TILE),
                stone.blocks.getBlockStack(EnumStoneBlockType.ROUGH_TILE_FENCE, 2));

            setup.recipes().addShapeless(stone.items.getItem(EnumStoneItemType.STONE_BRICK),
                    stone.items.getItem(EnumStoneItemType.ROUGH_STONE_BRICK), "itemChisel")
                .action(damageTool("itemChisel"));
        }

        setup.recipes().addShaped(new ItemStack(TFCItems.quern),
            "  W", "PPP", 'P', "stoneQuern", 'W', "stickWood");
        setup.recipes().addShaped(new ItemStack(TFCItems.millstone),
            "PPP", "P P", "PPP", 'P', "stoneQuern");

        setup.registry(CarvingRegistry.carvable)
            .add(new CarvableRoughStone())
            .add(new CarvableRoughStoneBrick())
            .add(new CarvableRoughStoneTile());

        setup.registry(QuarryRegistry.quarriable)
            .add(new QuarriableStone(TFCBlocks.stoneSed, BidsBlocks.roughStoneSed, 1, 1))
            .add(new QuarriableStone(TFCBlocks.stoneMM, BidsBlocks.roughStoneMM, 1, 1))
            .add(new QuarriableStone(TFCBlocks.stoneIgIn, BidsBlocks.roughStoneIgIn, 1, 1))
            .add(new QuarriableStone(TFCBlocks.stoneIgEx, BidsBlocks.roughStoneIgEx, 1, 1));
    }

}
