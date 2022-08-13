package com.unforbidable.tfc.bids.Core.Firepit;

import java.util.ArrayList;
import java.util.List;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FirepitHelper {

    public static boolean createFirepitAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z,
            int side) {
        if (!world.isRemote && side == 1 && world.isAirBlock(x, y + 1, z)
                && isValidFirepitLocation(world, x, y, z)) {
            final List<EntityItem> rocks = findLooseRocksAt(world, x, y, z);

            if (getEntityListTotalStackSize(rocks) > 1) {
                world.setBlock(x, y + 1, z, BidsBlocks.newFirepit);
                TileEntity te = world.getTileEntity(x, y + 1, z);
                if (te instanceof TileEntityNewFirepit) {
                    TileEntityNewFirepit firepit = (TileEntityNewFirepit) te;
                    firepit.initWithKindling(itemStack, false);
                    itemStack.stackSize--;

                    consumeStackFromEntityList(rocks, 2);

                    world.markBlockForUpdate(x, y + 1, z);
                } else {
                    // We get here when the firepit block
                    // fails to create a TileEntityNewFirepit
                    world.setBlockToAir(x, y + 1, z);
                    Bids.LOG.warn("Firepit block did not create TileEntityNewFirepit tile entity");
                }
            }

            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private static List<EntityItem> findLooseRocksAt(World world, int x, int y, int z) {
        final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1.1, z + 1);
        final List<EntityItem> everything = (List<EntityItem>) world.getEntitiesWithinAABB(EntityItem.class, bounds);
        final List<EntityItem> rocks = new ArrayList<EntityItem>();

        for (EntityItem entityItem : everything) {
            if (entityItem.getEntityItem().getItem() == TFCItems.looseRock) {
                rocks.add(entityItem);
            }
        }

        return rocks;
    }

    private static int getEntityListTotalStackSize(List<EntityItem> list) {
        int count = 0;

        for (EntityItem entityItem : list) {
            final ItemStack is = entityItem.getEntityItem();
            count += is.stackSize;
        }

        return count;
    }

    private static void consumeStackFromEntityList(List<EntityItem> list, int count) {
        for (EntityItem entityItem : list) {
            final ItemStack is = entityItem.getEntityItem();
            final int canConsumeCount = Math.min(is.stackSize, count);

            count -= canConsumeCount;
            is.stackSize -= canConsumeCount;

            if (is.stackSize == 0) {
                entityItem.setDead();
            }

            if (count == 0) {
                break;
            }
        }
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

    public static EnumFuelMaterial getEnumFuelMaterial(ItemStack itemStack) {
        final ItemStack log = new ItemStack(TFCItems.logs, 1, itemStack.getItemDamage());
        return TFC_Core.getFuelMaterial(log);
    }

}
