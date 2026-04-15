package com.unforbidable.tfc.bids.Core.ProcessingSurface;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.ISurfaceItemPlacer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ProcessingSurfacePlacer implements ISurfaceItemPlacer {

    @Override
    public boolean placeItemOnSurface(World world, int x, int y, int z, int face, float hitX, float hitY, float hitZ, EntityPlayer player) {
        if (!player.isSneaking() && face == 1 && world.isAirBlock(x, y + 1, z)) {
            ProcessingSurfaceRecipe recipe = ProcessingSurfaceManager.findMatchingRecipe(player.getHeldItem(), world, x, y, z);
            if (recipe != null) {
                Bids.LOG.debug("Found matching ProcessingSurfaceRecipe");

                if (!world.isRemote) {
                    world.setBlock(x, y + 1, z, BidsBlocks.processingSurface, 0, 2);
                    TileEntityProcessingSurface te = (TileEntityProcessingSurface) world.getTileEntity(x, y + 1, z);
                    ItemStack heldItem = player.getHeldItem().copy();
                    heldItem.stackSize = 1;
                    te.setInputItem(heldItem);
                    player.getHeldItem().stackSize--;
                }

                return true;
            }
        }

        return false;
    }

}
