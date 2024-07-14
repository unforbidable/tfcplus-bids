package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface ICrackableBlock {

    Block getSource();

    Block getTarget();

    void crackBlock(World world, int x, int y, int z);

    boolean isCrackable(World world, int x, int y, int z);

    boolean isCrackable(Block block, int metadata);

    float getHeatResistance(World world, int x, int y, int z);

    float getHeatResistance(Block block, int metadata);

}
