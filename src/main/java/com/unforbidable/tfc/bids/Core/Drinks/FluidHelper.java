package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Network.Messages.FillContainerFromWorldMessage;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
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
        if (!FluidContainerRegistry.isEmptyContainer(is)) {
            Bids.LOG.warn("Fluid container " + is.getDisplayName() + " is not a registered fluid container");

            return is;
        }

        if (world.isRemote) {
            if (tryToFillContainedFromWorld(is, world, player)) {
                return is;
            }
        }

        return is;
    }

    @SideOnly(Side.CLIENT)
    private static boolean tryToFillContainedFromWorld(ItemStack is, World world, EntityPlayer player) {
        MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;

        if (mop != null) {
            if (mop.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
                Bids.network.sendToServer(new FillContainerFromWorldMessage(is, player, mop));
                Bids.LOG.debug("Send FillContainerFromWorldMessage message");

                return true;
            }
        }

        return false;
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
        }

        if (returned != null && !ItemStack.areItemStacksEqual(returned, empty)) {
            player.inventory.setInventorySlotContents(slot, returned);
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    private static ItemStack fillContainerFromBlock(ItemStack is, World world, EntityPlayer player, MovingObjectPosition mop) {
        if (mop != null) {
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
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

                if (TFC_Core.isFreshWater(world.getBlock(x, y, z))) {
                    if (player.capabilities.isCreativeMode)
                        return is;

                    return getContainerFilledWithFluid(is, world, player, TFCFluids.FRESHWATER);
                }

                if (TFC_Core.isSaltWater(world.getBlock(x, y, z))) {
                    if (player.capabilities.isCreativeMode)
                        return is;

                    return getContainerFilledWithFluid(is, world, player, TFCFluids.SALTWATER);
                }

                if (TFC_Core.isTanninWater(world.getBlock(x, y, z))) {
                    if (player.capabilities.isCreativeMode)
                        return is;

                    return getContainerFilledWithFluid(is, world, player, TFCFluids.TANNIN);
                }

                // Handle flowing water
                int flowX = x;
                int flowY = y;
                int flowZ = z;
                switch (mop.sideHit) {
                    case 0:
                        flowY = y - 1;
                        break;
                    case 1:
                        flowY = y + 1;
                        break;
                    case 2:
                        flowZ = z - 1;
                        break;
                    case 3:
                        flowZ = z + 1;
                        break;
                    case 4:
                        flowX = x - 1;
                        break;
                    case 5:
                        flowX = x + 1;
                        break;
                }

                if (TFC_Core.isFreshWater(world.getBlock(flowX, flowY, flowZ))) {
                    return getContainerFilledWithFluid(is, world, player, TFCFluids.FRESHWATER);
                }

                if (TFC_Core.isSaltWater(world.getBlock(flowX, flowY, flowZ))) {
                    return getContainerFilledWithFluid(is, world, player, TFCFluids.SALTWATER);
                }

                if (TFC_Core.isTanninWater(world.getBlock(flowX, flowY, flowZ))) {
                    return getContainerFilledWithFluid(is, world, player, TFCFluids.TANNIN);
                }
            }
        }

        return is;
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

            Bids.LOG.info("Filled: " + temp.getDisplayName() + "[" + temp.getItemDamage() + "]");
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
