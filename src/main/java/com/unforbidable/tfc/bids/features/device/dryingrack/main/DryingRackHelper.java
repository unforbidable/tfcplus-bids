package com.unforbidable.tfc.bids.features.device.dryingrack.main;

import com.dunk.tfc.Items.ItemClothing;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackAnchorBlock;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import com.unforbidable.tfc.bids.features.device.dryingrack.block.BlockDryingRackSide;
import com.unforbidable.tfc.bids.features.device.dryingrack.tileentity.TileEntityDryingRack;
import com.unforbidable.tfc.bids.util.collision.CollisionHelper;
import com.unforbidable.tfc.bids.util.collision.CollisionInfo;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class DryingRackHelper {

    private static final int MAX_DRYING_RACK_SIDE_HEIGHT = 3;

    private static int getRequiredDryingRackSpan(World world, int x, int y, int z, ForgeDirection side, int max) {
        int span = 0;
        int x2 = x + side.offsetX;
        int z2 = z + side.offsetZ;

        while (span < max && world.isAirBlock(x2, y, z2)) {
            span++;
            x2 += side.offsetX;
            z2 += side.offsetZ;
        }

        if (span == 0) {
            Bids.LOG.debug("Not enough room to place drying rack here");
            return 0;
        }

        if (canPlaceDryingRackAt(world, x2, y, z2, side.getOpposite())) {
            Bids.LOG.debug("Drying rack can be placed spanning over " + span + " block(s)");
            return span;
        }

        Bids.LOG.debug("No sufficiently solid block on the opposite side to place drying rack here");

        return 0;
    }

    public static boolean canPlaceDryingRackAt(World world, int x, int y, int z, ForgeDirection side) {
        return side != ForgeDirection.UP && side != ForgeDirection.DOWN &&
            world.isAirBlock(x + side.offsetX, y, z + side.offsetZ) &&
            canDryingRackAttach(world, x, y, z, side);
    }

    private static boolean canDryingRackAttach(World world, int x, int y, int z, ForgeDirection side) {
        if (world.isSideSolid(x, y, z, side)) {
            return true;
        } else {
            Block block = world.getBlock(x, y, z);
            if (block instanceof DryingRackAnchorBlock) {
                return ((DryingRackAnchorBlock)block).canDryingRackAttach(world, x, y, z, side);
            }
        }

        return false;
    }

    public static void placeDryingRackFromItemsAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, ForgeDirection side) {
        int maxPoles = Math.min(stack.stackSize / 2, 3);
        int span = getRequiredDryingRackSpan(world, x, y, z, side, maxPoles);
        if (span > 0) {
            // 2 cordage per each non-solid wall attachment
            // 2 cordage per each section if span is longer than 1
            int firstExtraCordage = world.isSideSolid(x, y, z, side) ? 0 : 2;
            int lastExtraCordage = world.isSideSolid(x + side.offsetX * span, y, z + side.offsetZ * span, side.getOpposite()) ? 0 : 2;
            int totalCordageAmount = firstExtraCordage + lastExtraCordage + (span > 1 ? span * 2 : 0);

            ItemStack cordage = totalCordageAmount > 0 ? findCordageOnPlayer(player, totalCordageAmount) : null;

            if (cordage != null || totalCordageAmount == 0) {
                final int orientation = side.ordinal() / 2 - 1;

                for (int i = 1; i <= span; i++) {
                    int cordageAmountForSection = (span > 1 ? 2 : 0) +
                        (i == 1 ? firstExtraCordage : 0) +
                        (i == span ? lastExtraCordage : 0);

                    int x2 = x + side.offsetX * i;
                    int z2 = z + side.offsetZ * i;

                    world.setBlock(x2, y, z2, BidsBlocks.dryingRack, orientation % 4, 2);
                    TileEntityDryingRack te = (TileEntityDryingRack) world.getTileEntity(x2, y, z2);
                    te.setOrientation(orientation);
                    stack.stackSize -= 2;

                    if (cordage != null && cordageAmountForSection > 0) {
                        ItemStack cordageCopy = cordage.copy();
                        cordageCopy.stackSize = cordageAmountForSection;
                        te.setCordage(cordageCopy);

                        cordage.stackSize -= cordageAmountForSection;
                    }
                }

                if (cordage != null) {
                    player.inventoryContainer.detectAndSendChanges();
                }
            }
        }
    }

    private static ItemStack findCordageOnPlayer(EntityPlayer player, int num) {
        List<ItemStack> bindings = OreDictionary.getOres("materialBindingStrong", false);
        for (int i = 0; i < 9; i++) {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.stackSize >= num && isOreList(is, bindings)) {
                return is;
            }
        }

        return null;
    }

    private static boolean isOreList(ItemStack itemStack, List<ItemStack> ores) {
        for (ItemStack ore : ores) {
            if (ore.getItem() == itemStack.getItem()
                && (ore.getItemDamage() == itemStack.getItemDamage()
                || ore.getItemDamage() == OreDictionary.WILDCARD_VALUE)) {
                return true;
            }
        }

        return false;
    }

    public static MovingObjectPosition onDryingRackCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec,
            Vec3 endVec) {
        TileEntityDryingRack te = (TileEntityDryingRack) world.getTileEntity(x, y, z);
        startVec = startVec.addVector(-x, -y, -z);
        endVec = endVec.addVector(-x, -y, -z);

        CollisionInfo nearestCol = null;
        int nearestSection = -1;

        DryingRackBounds dryingRackBounds = DryingRackBounds.fromOrientation(te.getOrientation());
        AxisAlignedBB[] bounds = dryingRackBounds.sections;
        for (int i = 0; i < bounds.length; i++) {
            CollisionInfo col = CollisionHelper.rayTraceAABB(bounds[i], startVec, endVec);

            // When the item collides
            // Save if first or closer than the nearest so far
            if (col != null && (nearestCol == null || col.distance < nearestCol.distance)) {
                nearestCol = col;
                nearestSection = i;
            }
        }

        if (nearestSection != -1) {
            te.setSelectedSection(nearestSection);
        } else {
            te.clearSelectedSection();
        }

        return null;
    }

    public static boolean placeItemOnDryingRackAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y,
            int z, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && isItemValidDryingRackItem(itemStack)) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityDryingRack) {
                final TileEntityDryingRack dryingRack = (TileEntityDryingRack) te;
                final int section = getDryingRackSectionFromHit(dryingRack, hitX, hitY, hitZ);
                if (section != -1 && dryingRack.placeItem(section, player, itemStack)) {
                    itemStack.stackSize--;
                }
            }
        }

        return false;
    }

    public static boolean retrieveItemFromDryingRackAt(EntityPlayer player, World world, int x, int y, int z,
            float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileEntityDryingRack) {
                TileEntityDryingRack dryingRack = (TileEntityDryingRack) te;
                int section = getDryingRackSectionFromHit(dryingRack, hitX, hitY, hitZ);
                if (section != -1 && dryingRack.retrieveItem(section, player)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isItemValidDryingRackItem(ItemStack itemStack) {
        return DryingRackRegistry.recipes.findMatchingRecipe(itemStack) != null || itemStack.getItem() instanceof ItemClothing;
    }

    public static int getDryingRackSectionFromHit(TileEntityDryingRack dryingRack, float hitX, float hitY, float hitZ) {
        DryingRackBounds bounds = DryingRackBounds.fromOrientation(dryingRack.getOrientation());
        AxisAlignedBB near = AxisAlignedBB.getBoundingBox(hitX - 0.01f, hitY - 0.1f, hitZ - 0.01f,
                hitX + 0.01f, hitY + 0.1f, hitZ + 0.01f);

        for (int i = 0; i < bounds.sections.length; i++) {
            AxisAlignedBB check = bounds.sections[i];

            if (check.intersectsWith(near)) {
                return i;
            }
        }

        return -1;
    }

    public static DryingRackTyingEquipment findTyingEquipment(ItemStack item) {
        for (DryingRackTyingEquipment te : DryingRackRegistry.tyingEquipment) {
            if (te.item == item.getItem()) {
                return te;
            }
        }

        return null;
    }

    public static boolean canPlaceDryingRackSideOnTop(World world, int x, int y, int z) {
        if (!world.isAirBlock(x, y + 1, z)) {
            return false;
        }

        Block below = world.getBlock(x, y, z);
        return below.isSideSolid(world, x, y, z, ForgeDirection.UP) ||
            below instanceof BlockDryingRackSide && getDryingRackSideHeight(world, x, y, z) < MAX_DRYING_RACK_SIDE_HEIGHT;
    }

    private static int getDryingRackSideHeight(World world, int x, int y, int z) {
        Block below = world.getBlock(x, y - 1, z);
        if (below.isSideSolid(world, x, y - 1, z, ForgeDirection.UP)) {
            return 1;
        } else {
            return getDryingRackSideHeight(world, x, y - 1, z) + 1;
        }
    }

    public static void placeDryingRackSideFromItemsAt(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z) {
        if (itemStack.stackSize > 2) {
            int orientation = getPlacedDryingRackSideOrientation(player, world, x, y + 1, z);

            // Place one section
            world.setBlock(x, y + 1, z, BidsBlocks.dryingRackSide, orientation, 2);
        }
    }

    public static int getPlacedDryingRackSideOrientation(EntityPlayer player, World world, int x, int y, int z) {
        Block below = world.getBlock(x, y - 1, z);
        if (below instanceof BlockDryingRackSide) {
            // Use same orientation as drying rack side below
            return world.getBlockMetadata(x, y - 1, z) & 1;
        } else {
            return (int) Math.floor(player.rotationYaw * 4F / 360F + 0.5D) & 1;
        }
    }

    public static boolean canPlaceDryingRackCoverAbove(World world, int x, int y, int z) {
        return world.isAirBlock(x, y + 1, z) &&
            BidsBlocks.dryingRackCover.canBlockStay(world, x, y + 1, z);
    }

    public static void placeDryingRackCoverAbove(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z) {
        world.setBlock(x, y + 1, z, BidsBlocks.dryingRackCover);
        itemStack.stackSize--;
    }

}
