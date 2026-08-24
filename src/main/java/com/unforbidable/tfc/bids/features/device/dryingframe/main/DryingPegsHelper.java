package com.unforbidable.tfc.bids.features.device.dryingframe.main;

import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.features.building.peg.block.BlockPeg;
import com.unforbidable.tfc.bids.features.device.dryingframe.tileentity.TileEntityDryingPegs;
import com.unforbidable.tfc.bids.features.device.dryingrack.main.DryingRackHelper;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFreshSkin;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DryingPegsHelper {

    public static final int CORDAGE_AMOUNT_REQUIRED = 4;

    public static void placeDryingPegs(World world, int x, int y, int z, ItemStack itemStack, EntityPlayer player) {
        world.setBlock(x, y, z, BidsBlocks.dryingPegs, 0, 2);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityDryingPegs) {
            int slot = findCordage(player);
            ItemStack cordage = player.inventory.getStackInSlot(slot).copy();
            cordage.stackSize = CORDAGE_AMOUNT_REQUIRED;

            player.inventory.decrStackSize(slot, CORDAGE_AMOUNT_REQUIRED);

            TileEntityDryingPegs dryingPegs = (TileEntityDryingPegs) te;
            dryingPegs.placeItemStack(itemStack, cordage, player);
        }
    }

    public static ForgeDirection findValidDryingPegsNeighbor(World world, int x, int y, int z, int face) {
        int preferredSide = face < 2 ? 0 : face - 2;
        for (int i = 0; i < 4; i++) {
            int side = ((i + preferredSide) & 3) + 2;
            ForgeDirection dir = ForgeDirection.getOrientation(side);
            int x2 = x + dir.offsetX;
            int z2 = z + dir.offsetZ;
            if (world.isAirBlock(x2, y, z2) && BidsBlocks.dryingPegs.canBlockStay(world, x2, y, z2)) {
                return dir;
            }
        }

        return ForgeDirection.UNKNOWN;
    }

    public static boolean canPlayerDryItem(EntityPlayer player, ItemStack itemStack) {
        return hasCordage(player) && itemStack.getItem() instanceof ItemFreshSkin;
    }

    private static boolean hasCordage(EntityPlayer player) {
        return findCordage(player) != -1;
    }

    private static int findCordage(EntityPlayer player) {
        for (int i = 0; i < 9; i++) {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.stackSize >= CORDAGE_AMOUNT_REQUIRED && DryingRackHelper.findTyingEquipment(is) != null) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isBlockDryingPegAnchor(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        // By default, can attach to Pegs
        boolean canAttach = block instanceof BlockPeg;
        return BidsEventFactory.onDryingPegsAnchorAttach(world, x, y, z, block, canAttach);
    }

    public static AxisAlignedBB getBlockDryingPegAnchorBounds(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        // By default, this works for Pegs, and other blocks with static bounds that tightly match the model
        AxisAlignedBB knotBounds = AxisAlignedBB.getBoundingBox(block.getBlockBoundsMinX(), 0, block.getBlockBoundsMinZ(),
            block.getBlockBoundsMaxX(), 0, block.getBlockBoundsMaxZ());
        return BidsEventFactory.onDryingPegsAnchorBounds(world, x, y, z, block, knotBounds);
    }

}
