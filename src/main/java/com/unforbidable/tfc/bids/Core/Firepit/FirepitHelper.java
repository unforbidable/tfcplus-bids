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
        // The following method returns ASH material for Oak and OAK material for Oak
        // TFC_Core.getFuelMaterial(log);
        // So instead of calling it the logic is cloned here and corrected
        int dam = itemStack.getItem() == TFCItems.logs ? itemStack.getItemDamage() / 2 : itemStack.getItemDamage();
        if (dam == 0)
            return EnumFuelMaterial.OAK;
        else if (dam == 1)
            return EnumFuelMaterial.ASPEN;
        else if (dam == 2)
            return EnumFuelMaterial.BIRCH;
        else if (dam == 3)
            return EnumFuelMaterial.CHESTNUT;
        else if (dam == 4)
            return EnumFuelMaterial.DOUGLASFIR;
        else if (dam == 5)
            return EnumFuelMaterial.HICKORY;
        else if (dam == 6)
            return EnumFuelMaterial.MAPLE;
        else if (dam == 7)
            return EnumFuelMaterial.ASH;
        else if (dam == 8)
            return EnumFuelMaterial.PINE;
        else if (dam == 9)
            return EnumFuelMaterial.REDWOOD;
        else if (dam == 10)
            return EnumFuelMaterial.SPRUCE;
        else if (dam == 11)
            return EnumFuelMaterial.SYCAMORE;
        else if (dam == 12)
            return EnumFuelMaterial.WHITECEDAR;
        else if (dam == 13)
            return EnumFuelMaterial.WHITEELM;
        else if (dam == 14)
            return EnumFuelMaterial.WILLOW;
        else if (dam == 15)
            return EnumFuelMaterial.KAPOK;
        else if (dam == 16)
            return EnumFuelMaterial.ACACIA;
        else if (dam == 17)
            return EnumFuelMaterial.PALM;
        else if (dam == 18)
            return EnumFuelMaterial.EBONY;
        else if (dam == 19)
            return EnumFuelMaterial.FEVER;
        else if (dam == 20)
            return EnumFuelMaterial.BAOBAB;
        else if (dam == 21)
            return EnumFuelMaterial.LIMBA;
        else if (dam == 22)
            return EnumFuelMaterial.MAHOGANY;
        else if (dam == 23)
            return EnumFuelMaterial.TEAK;
        else if (dam == 24)
            return EnumFuelMaterial.BAMBOO;
        else if (dam == 25)
            return EnumFuelMaterial.GINGKO;
        else if (dam == 26)
            return EnumFuelMaterial.FRUITWOOD;
        else
            return EnumFuelMaterial.ASPEN;
    }

}
