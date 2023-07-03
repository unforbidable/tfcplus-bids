package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.Bids;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
        if (!(is.getItem() instanceof IWorldFluidFillable)) {
            Bids.LOG.warn("Fluid container " + is.getDisplayName() + " cannot be filled up form world, missing IWorldFluidFillable interface");

            return is;
        }

        double reachBase = player instanceof EntityPlayerMP ? ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance() : 5;
        int reach = (int) Math.round(reachBase * ((ISize)is.getItem()).getReach(is).multiplier);
        MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(world, player, true, reach);

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

                if (TFC_Core.isFreshWater(world.getBlock(x, y, z)))  {
                    if (player.capabilities.isCreativeMode)
                        return is;

                    return getContainerFilledWithFluid(is, world, player, TFCFluids.FRESHWATER);
                }

                if (TFC_Core.isSaltWater(world.getBlock(x, y, z)))  {
                    if (player.capabilities.isCreativeMode)
                        return is;

                    return getContainerFilledWithFluid(is, world, player, TFCFluids.SALTWATER);
                }

                if (TFC_Core.isTanninWater(world.getBlock(x, y, z)))  {
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
        IWorldFluidFillable fillable = (IWorldFluidFillable) is.getItem();
        ItemStack filledContainer = fillable.getWorldFluidFilledItem(is, world, player, fluid);

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
