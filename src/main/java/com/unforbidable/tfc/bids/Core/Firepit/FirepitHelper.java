package com.unforbidable.tfc.bids.Core.Firepit;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FirepitHelper {

    public static boolean createFirepitAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z,
            int side) {
        if (!world.isRemote && side == 1 && world.isAirBlock(x, y + 1, z)
                && isValidFirepitLocation(world, x, y, z)) {
            world.setBlock(x, y + 1, z, BidsBlocks.newFirepit);
            TileEntity te = world.getTileEntity(x, y + 1, z);
            if (te instanceof TileEntityNewFirepit) {
                TileEntityNewFirepit firepit = (TileEntityNewFirepit) te;
                firepit.initWithKindling(itemStack, false);

                world.markBlockForUpdate(x, y + 1, z);
            } else {
                // We get here when the firepit block
                // fails to create a TileEntityNewFirepit
                world.setBlockToAir(x, y + 1, z);
                Bids.LOG.warn("Firepit block did not create TileEntityNewFirepit tile entity");
            }

            return true;
        }

        return false;

    }

    private static boolean isValidFirepitLocation(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return world.isSideSolid(x, y, z, ForgeDirection.UP)
                && block.getMaterial() != Material.wood
                && block.getMaterial() != Material.cloth
                && world.isAirBlock(x, y + 1, z)
                && block != TFCBlocks.charcoal
                && block != Blocks.coal_block
                && block != TFCBlocks.pottery;
    }

}
