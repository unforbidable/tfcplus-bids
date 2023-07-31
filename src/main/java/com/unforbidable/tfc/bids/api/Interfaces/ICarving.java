package com.unforbidable.tfc.bids.api.Interfaces;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ICarving {

    boolean canCarveBlock(Block block, int metadata);

    boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier);

    boolean canCarveBlockAt(Block block, int metadata, World world, int x, int y, int z, int side);

    Block getCarvingBlock(Block block, int metadata);

    ItemStack[] getCarvingHarvest(Block block, int metadata, Random random);

    ItemStack getCarvingExtraHarvest(Block block, int metadata, Random random, float bitRatio);

    String getCarvingSoundEffect();

}
