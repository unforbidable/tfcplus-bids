package com.unforbidable.tfc.bids.api.features.drying;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface DryingRackAnchorBlock {

    boolean canDryingRackAttach(World world, int x, int y, int z, ForgeDirection side);

}

