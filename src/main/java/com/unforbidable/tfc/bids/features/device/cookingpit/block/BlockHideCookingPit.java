package com.unforbidable.tfc.bids.features.device.cookingpit.block;

import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityHideCookingPit;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHideCookingPit extends BlockCookingPit {

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityHideCookingPit();
    }

}
