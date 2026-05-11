package com.unforbidable.tfc.bids.features.building.mudbrick.main.carvable;

import com.dunk.tfc.Blocks.BlockMudBricks;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.carving.Carvable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class CarvableMudBrick implements Carvable {
    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block instanceof BlockMudBricks;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata,int equipmentTier) {
        return true;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingRock;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        int offset = block == TFCBlocks.mudBricks ? 0 : 16;
        int damage = block.damageDropped(metadata) + offset;

        return new ItemStack[] {
            new ItemStack(TFCItems.mudBrick, 1, damage),
            new ItemStack(TFCItems.mudBrick, 1, damage)
        };
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return null;
    }

    @Override
    public String getCarvingSoundEffect() {
        return "dig.stone";
    }
}
