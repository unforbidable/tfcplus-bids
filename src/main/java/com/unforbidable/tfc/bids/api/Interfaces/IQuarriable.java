package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IQuarriable {

    Block getRawBlock();

    Block getQuarriedBlock();

    boolean canQuarryBlockAt(World world, int x, int y, int z, int side);

    boolean isQuarryReady(World world, int x, int y, int z);

    boolean blockRequiresWedgesToDetach(Block block);

    int getDrillDamage(Block block);

    float getDrillDurationMultiplier(Block block);

    int getQuarriedBlockMetadata(int metadata);

}
