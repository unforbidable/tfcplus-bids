package com.unforbidable.tfc.bids.features.building.carving.main.carvable;

import java.util.Random;

import com.unforbidable.tfc.bids.features.building.logwall.block.BlockLogWall;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.carving.Carvable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CarvableLogWall implements Carvable {

    @Override
    public boolean canCarveBlock(Block block, int metadata) {
        return block instanceof BlockLogWall;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        return true;
    }

    @Override
    public boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public Block getCarvingBlock(Block block, int metadata) {
        return BidsBlocks.carvingWood;
    }

    @Override
    public ItemStack[] getCarvingHarvest(Block block, int metadata, Random random) {
        int damage = getItemDamage(block, metadata);
        WoodIndex wood = WoodScheme.DEFAULT.findWood(damage);
        if (wood.items.hasPeeledLog()) {
            return new ItemStack[] {
                wood.items.getSeasonedPeeledLog(),
                wood.items.getSeasonedPeeledLog()
            };
        } else {
            return new ItemStack[] {
                wood.items.getLog(),
                wood.items.getLog()
            };
        }
    }

    protected int getItemDamage(Block block, int metadata) {
        return ((BlockLogWall)block).getOffset() + metadata;
    }

    @Override
    public ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio) {
        return null;
    }

    @Override
    public String getCarvingSoundEffect() {
        return "dig.wood";
    }

}
