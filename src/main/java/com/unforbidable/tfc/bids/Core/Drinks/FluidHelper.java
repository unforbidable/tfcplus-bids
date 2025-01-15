package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.Bids;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidHelper {

    public static void registerPartialFluidContainer(Fluid fluid, Item emptyContainerItem, int emptyDmg, Item filledContainerItem, int sip, int volume) {
        int count = volume / sip;

        FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, sip),
            new ItemStack(filledContainerItem, 1, count - 1), new ItemStack(emptyContainerItem, 1, emptyDmg));

        for (int i = 0; i < count - 1; i++) {
            FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, sip),
                new ItemStack(filledContainerItem, 1, i), new ItemStack(filledContainerItem, 1, i + 1));
        }
    }

    public static ItemStack fillContainerFromWorld(ItemStack is, World world, EntityPlayer player) {
        double reachBase = player instanceof EntityPlayerMP ? ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance() : 5;
        int reach = (int) Math.round(reachBase * ((ISize)is.getItem()).getReach(is).multiplier);
        MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(world, player, true, reach);

        if (mop != null) {
            Fluid fluid = getFluidFromWorld(world, player, mop);
            if (fluid != null) {
                int amount = getTotalContainerCapacity(is, fluid);
                ItemStack filledContainer = getFilledContainer(is, fluid, amount);
                if (filledContainer != null) {
                    return giveFilledContainerToPlayer(filledContainer, player);
                }
            }
        }

        return is;
    }

    private static Fluid getFluidFromWorld(World world, EntityPlayer player, MovingObjectPosition mop) {
        int x = mop.blockX;
        int y = mop.blockY;
        int z = mop.blockZ;

        if (!world.canMineBlock(player, x, y, z))
            return null;

        if (!player.canPlayerEdit(x, y, z, mop.sideHit, player.inventory.getCurrentItem()))
            return null;

        Fluid fluid = getFluidFromBlockAt(world, x, y, z);
        if (fluid != null) {
            return fluid;
        }

        // Handle flowing water
        ForgeDirection d = ForgeDirection.getOrientation(mop.sideHit);
        Fluid flowFluid = getFluidFromBlockAt(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        if (flowFluid != null) {
            return flowFluid;
        }

        return null;
    }

    private static Fluid getFluidFromBlockAt(World world, int x, int y, int z) {
        if (TFC_Core.isFreshWater(world.getBlock(x, y, z))) {
            return TFCFluids.FRESHWATER;
        }

        if (TFC_Core.isSaltWater(world.getBlock(x, y, z))) {
            return TFCFluids.SALTWATER;
        }

        if (TFC_Core.isTanninWater(world.getBlock(x, y, z))) {
            return TFCFluids.TANNIN;
        }

        return null;
    }

    private static int getTotalContainerCapacity(ItemStack is, Fluid fluid) {
        FluidStack fs = new FluidStack(fluid, 1);
        int total = 0;

        while (FluidContainerRegistry.isEmptyContainer(is)) {
            int capacity = FluidContainerRegistry.getContainerCapacity(fs, is);
            fs.amount = capacity;
            total += capacity;
            is = FluidContainerRegistry.fillFluidContainer(fs, is);
        }

        return total;
    }

    public static ItemStack getFilledContainer(ItemStack container, Fluid fluid, int amount) {
        if (!FluidContainerRegistry.isEmptyContainer(container)) {
            Bids.LOG.warn("Item " + container + " is not a registered fluid container");

            return null;
        }

        FluidStack fs = new FluidStack(fluid, 1);
        ItemStack temp = container.copy();

        if (FluidContainerRegistry.getContainerCapacity(fs, container) == 0) {
            Bids.LOG.warn("Item " + container + " does not accept fluid " + fluid);

            return null;
        }

        int amountFilled = 0;
        while (FluidContainerRegistry.isEmptyContainer(temp) && amountFilled < amount) {
            fs.amount = FluidContainerRegistry.getContainerCapacity(fs, temp);
            amountFilled += fs.amount;

            temp = FluidContainerRegistry.fillFluidContainer(fs, temp);
            if (temp == null) {
                Bids.LOG.warn("Container could not be filled");
                return null;
            }

            Bids.LOG.debug("Filled: " + temp);
        }

        if (amountFilled < amount) {
            Bids.LOG.warn("Item " + container + " could not accept total amount of " + amount);

            return null;
        }

        return temp;
    }

    public static ItemStack giveFilledContainerToPlayer(ItemStack filledContainer, EntityPlayer player) {
        int slot = player.inventory.currentItem;
        ItemStack emptyContainer = player.inventory.getStackInSlot(slot);

        if (emptyContainer.stackSize > 1) {
            --emptyContainer.stackSize;

            if (!player.inventory.addItemStackToInventory(filledContainer)) {
                player.dropPlayerItemWithRandomChoice(filledContainer, false);
            }
            player.inventoryContainer.detectAndSendChanges();

            return emptyContainer;
        } else {
            player.inventory.setInventorySlotContents(slot, filledContainer);

            return filledContainer;
        }
    }

    public static boolean fillContainerOnEntityInteractEvent(EntityPlayer player, Entity entity) {
        int slot = player.inventory.currentItem;
        ItemStack emptyContainer = player.inventory.getStackInSlot(slot);
        Fluid fluid = EntityFluidHelper.getFluidFromEntity(entity, player);
        if (fluid != null) {
            int capacity = getTotalContainerCapacity(emptyContainer, fluid);
            int amount = Math.min(capacity, 1000);

            ItemStack filledContainer = getFilledContainer(emptyContainer, fluid, amount);
            if (filledContainer != null) {

                EntityFluidHelper.drainEntityFluid(entity, player, amount);

                giveFilledContainerToPlayer(filledContainer, player);

                return true;
            }
        }

        return false;
    }

}
