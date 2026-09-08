package com.unforbidable.tfc.bids.features.utility.hidevessel.main;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.nbt.HideVesselTag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class HideVesselHelper {

    public static final int HIDE_VESSEL_CAPACITY = 1000;
    public static final long HIDE_VESSEL_EMPTY_DAMAGE_INTERVAL = TFC_Time.HOUR_LENGTH * 16;
    public static final long HIDE_VESSEL_FILLED_DAMAGE_INTERVAL = TFC_Time.HOUR_LENGTH * 8;
    public static final int HIDE_VESSEL_MAX_DAMAGE = 20;

    public static boolean isValidHideVesselFluid(Fluid fluid) {
        return fluid == TFCFluids.FRESHWATER;
    }

    public static ItemStack fillFromWorld(ItemStack is, World world, EntityPlayer player) {
        double reachBase = player instanceof EntityPlayerMP ? ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance() : 5;
        int reach = (int) Math.round(reachBase * ((ISize)is.getItem()).getReach(is).multiplier);
        MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(world, player, true, reach);

        if (mop != null) {
            Fluid fluid = FluidHelper.getFluidFromWorld(world, player, mop);
            if (fluid != null && isValidHideVesselFluid(fluid)) {
                ItemStack filledContainer = is.copy();
                HideVesselTag.of(filledContainer)
                    .setFluid(new FluidStack(fluid, HIDE_VESSEL_CAPACITY));

                BidsEventFactory.onFillContainer(player, is, filledContainer);

                return FluidHelper.giveFilledContainerToPlayer(filledContainer, player);
            }
        }

        return is;
    }

}
