package com.unforbidable.tfc.bids.features.utility.hidevessel.eventhandler;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingEvent;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.item.ItemHideVessel;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.HideVesselHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.nbt.HideVesselTag;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class HideVesselEventHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.entityPlayer.isSneaking() && event.entityPlayer.getHeldItem() != null) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (!event.world.isRemote) {
                    MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(event.world, event.entityPlayer, true);
                    if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        if (tryToFillItemContainer(event.world, mop, event.entityPlayer)) {
                            event.setCanceled(true);
                        }
                    }
                }
            } else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
                // Prevent right-clicking with the new stack
                // this event needs to be cancelled too
                if (event.world.isRemote) {
                    MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(event.world, event.entityPlayer, true);
                    if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        if (tryToFillItemContainer(event.world, mop, event.entityPlayer)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean tryToFillItemContainer(World world, MovingObjectPosition mop, EntityPlayer player) {
        ItemStack heldItem = player.getHeldItem();
        if (heldItem.getItem() == TFCItems.hideBag) {
            Fluid fluid = FluidHelper.getFluidFromWorld(world, player, mop);
            if (fluid != null && HideVesselHelper.isValidHideVesselFluid(fluid)) {
                if (!world.isRemote) {
                    ItemStack hideVessel = new ItemStack(BidsItems.hideVessel);

                    FluidStack fluidStack = new FluidStack(fluid, HideVesselHelper.HIDE_VESSEL_CAPACITY);
                    HideVesselTag.of(hideVessel)
                        .setFluid(fluidStack)
                        .setDamage(heldItem.getItemDamage());

                    FluidHelper.giveFilledContainerToPlayer(hideVessel, player);
                }

                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onDryingItemCrafted(DryingEvent.ItemCrafted event) {
        if (event.input.getItem() instanceof ItemHideVessel) {
            // Pass on hide vessel damage to the dried bag
            int damage = HideVesselTag.of(event.input).getDamage();
            event.result.setItemDamage(damage);
        }
    }



}
