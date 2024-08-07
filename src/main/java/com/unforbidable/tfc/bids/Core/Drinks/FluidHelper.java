package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.Messages.FillContainerFromWorldMessage;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.FillBucketEvent;
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
        return fillContainerFromWorld(is, world, player, false);
    }

    public static ItemStack fillContainerFromWorld(ItemStack is, World world, EntityPlayer player, boolean includeEntities) {
        if (!FluidContainerRegistry.isEmptyContainer(is)) {
            Bids.LOG.warn("Fluid container " + is.getDisplayName() + " is not a registered fluid container");

            return is;
        }

        if (includeEntities) {
            if (world.isRemote) {
                // Entities can only be detected on the client
                // So container filling is done asynchronously
                // after the server receives mop from the client
                fillContainedFromWorldClient(is, world, player);
            }
        } else {
            double reachBase = player instanceof EntityPlayerMP ? ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance() : 5;
            int reach = (int) Math.round(reachBase * ((ISize)is.getItem()).getReach(is).multiplier);
            MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(world, player, true, reach);

            if (mop != null) {
                return fillContainerFromBlock(is, world, player, mop);
            }
        }

        return is;
    }

    @SideOnly(Side.CLIENT)
    private static void fillContainedFromWorldClient(ItemStack is, World world, EntityPlayer player) {
        MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
        if (mop != null) {
            if (mop.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                Bids.network.sendToServer(new FillContainerFromWorldMessage(is, player, mop));
                Bids.LOG.debug("Send FillContainerFromWorldMessage message");
            }
        }
    }

    public static void onContainerFilledFromWorld(ItemStack is, EntityPlayer player, MovingObjectPosition mop) {
        int slot = player.inventory.currentItem;
        ItemStack empty = player.inventory.getStackInSlot(slot);
        if (!ItemStack.areItemStacksEqual(empty, is)) {
            Bids.LOG.warn("Player current item changed or out of sync");
            return;
        }

        ItemStack returned = null;
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            returned = fillContainerFromBlock(empty, player.worldObj, player, mop);
        } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            returned = fillContainerFromEntity(empty, player.worldObj, player, mop);
        }

        if (returned != null && !ItemStack.areItemStacksEqual(returned, empty)) {
            player.inventory.setInventorySlotContents(slot, returned);
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    private static ItemStack fillContainerFromEntity(ItemStack is, World world, EntityPlayer player, MovingObjectPosition mop) {
        Fluid fluid = EntityFluidHelper.getFluidFromEntity(mop.entityHit, player);
        if (fluid == null) {
            Bids.LOG.debug("Entity is unable to provide fluid");
            return is;
        }

        int totalCapacity = getTotalContainerCapacity(is, fluid);
        if (totalCapacity == 0) {
            Bids.LOG.debug("Container does not accept fluid from entity: " + fluid.getUnlocalizedName());
            return is;
        }

        if (EntityFluidHelper.drainEntityFluid(mop.entityHit, player, totalCapacity)) {
            return getContainerFilledWithFluid(is, world, player, fluid);
        }

        return is;
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

    private static ItemStack fillContainerFromBlock(ItemStack is, World world, EntityPlayer player, MovingObjectPosition mop) {
        int x = mop.blockX;
        int y = mop.blockY;
        int z = mop.blockZ;

        if (!world.canMineBlock(player, x, y, z))
            return is;

        if (!player.canPlayerEdit(x, y, z, mop.sideHit, is))
            return is;

        FillBucketEvent event = new FillBucketEvent(player, is, world, mop);
        if (MinecraftForge.EVENT_BUS.post(event) || event.isCanceled())
            return is;

        if (event.getResult() == Event.Result.ALLOW)
            return event.result;

        Fluid fluid = getFluidFromBlockAt(world, x, y, z);
        if (fluid != null) {
            return getContainerFilledWithFluid(is, world, player, fluid);
        }

        // Handle flowing water
        ForgeDirection d = ForgeDirection.getOrientation(mop.sideHit);
        Fluid flowFluid = getFluidFromBlockAt(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ);
        if (flowFluid != null) {
            return getContainerFilledWithFluid(is, world, player, flowFluid);
        }

        return is;
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

    private static ItemStack getContainerFilledWithFluid(ItemStack is, World world, EntityPlayer player, Fluid fluid) {
        //if (isPottery && random.nextInt(80) == 0)  {
        //    world.playSoundAtEntity(player, TFC_Sounds.CERAMICBREAK, 0.7f,
        //        player.worldObj.rand.nextFloat() * 0.2F + 0.8F);
        //    return new ItemStack(TFCItems.rope, 1, 0);
        //}

        FluidStack fs = new FluidStack(fluid, 1);
        ItemStack temp = is.copy();

        if (FluidContainerRegistry.getContainerCapacity(fs, is) == 0) {
            // Container does not accept this fluid
            return is;
        }

        while (FluidContainerRegistry.isEmptyContainer(temp)) {
            fs.amount = FluidContainerRegistry.getContainerCapacity(fs, is);

            temp = FluidContainerRegistry.fillFluidContainer(fs, temp);
            if (temp == null) {
                Bids.LOG.warn("Container could not be filled");
                return is;
            }

            Bids.LOG.debug("Filled: " + temp.getDisplayName() + "[" + temp.getItemDamage() + "]");
        }

        ItemStack filledContainer = temp;

        if (is.stackSize > 1) {
            --is.stackSize;

            if (!player.inventory.addItemStackToInventory(filledContainer)) {
                player.dropPlayerItemWithRandomChoice(filledContainer, false);
            }

            return is;
        } else {
            return filledContainer;
        }
    }

}
