package com.unforbidable.tfc.bids.Core.DecorativeSurface;

import com.unforbidable.tfc.bids.Core.Common.Metadata.DecorativeSurfaceMetadata;
import com.unforbidable.tfc.bids.Core.ProcessingSurface.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityDecorativeSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.ISurfaceItemPlacer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class DecorativeSurfacePlacer implements ISurfaceItemPlacer {

    @Override
    public boolean placeItemOnSurface(World world, int x, int y, int z, int face, EntityPlayer player) {
        if (player.isSneaking() && face > 0) {
            if (isDecorativeSurfaceItem(player.getHeldItem())) {
                ForgeDirection dir = ForgeDirection.getOrientation(face);
                int x2 = x + dir.offsetX;
                int y2 = y + dir.offsetY;
                int z2 = z + dir.offsetZ;

                if (world.isAirBlock(x2, y2, z2)) {
                    Block block = world.getBlock(x, y, z);
                    if (block.isSideSolid(world, x, y, z, dir)) {
                        // meta is player's orientation (angle) for vertical placement
                        // and forge direction (face) for horizontal placement
                        DecorativeSurfaceMetadata meta = face == 1
                            ? DecorativeSurfaceMetadata.forHorizontalOrientation(ProcessingSurfaceHelper.getOrientation(player))
                            : DecorativeSurfaceMetadata.forVerticalFace(dir);

                        if (!world.isRemote) {
                            world.setBlock(x2, y2, z2, BidsBlocks.decorativeSurface, meta.getMetadata(), 2);
                            TileEntityDecorativeSurface te = (TileEntityDecorativeSurface) world.getTileEntity(x2, y2, z2);
                            ItemStack heldItem = player.getHeldItem().copy();
                            heldItem.stackSize = 1;
                            te.setItem(heldItem);
                            player.getHeldItem().stackSize--;
                        }

                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isDecorativeSurfaceItem(ItemStack itemStack) {
        int decorativeSurfaceItemOreId = OreDictionary.getOreID("itemDecorativeSurface");
        for (int oreId : OreDictionary.getOreIDs(itemStack)) {
            if (oreId == decorativeSurfaceItemOreId) {
                return true;
            }
        }

        return false;
    }

}
