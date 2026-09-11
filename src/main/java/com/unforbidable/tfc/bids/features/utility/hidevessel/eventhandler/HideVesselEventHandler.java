package com.unforbidable.tfc.bids.features.utility.hidevessel.eventhandler;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingEvent;
import com.unforbidable.tfc.bids.api.meta.HideVesselMeta;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.item.ItemHideVessel;
import com.unforbidable.tfc.bids.features.utility.hidevessel.item.ItemHideVesselFluid;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fluids.Fluid;
import java.util.List;

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
        if (heldItem.getItem() == TFCItems.hideBag && heldItem.getItemDamage() < 20) {
            Fluid fluid = FluidHelper.getFluidFromWorld(world, player, mop);
            if (fluid != null) {
                ItemStack hideVessel = new ItemStack(BidsItems.hideVessel, 1, heldItem.getItemDamage());

                int amount = FluidHelper.getTotalContainerCapacity(hideVessel, fluid);
                ItemStack filledContainer = FluidHelper.getFilledContainer(hideVessel, fluid, amount);
                if (filledContainer != null) {
                    BidsEventFactory.onFillContainer(player, hideVessel, filledContainer);

                    FluidHelper.giveFilledContainerToPlayer(filledContainer, player);
                }
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onDryingItemCrafted(DryingEvent.ItemCrafted event) {
        if (event.input.getItem() instanceof ItemHideVessel) {
            // Pass on hide vessel damage to the dry bag
            int damage = event.input.getItemDamage();
            event.result.setItemDamage(damage);
        }
    }

    @SubscribeEvent
    public void onInventoryOpen(PlayerOpenContainerEvent event) {
        checkContainerForHideVessel(event.entityPlayer.openContainer);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer && TFC_Time.getTotalTicks() % 20 == 0) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            checkContainerForHideVessel(player.openContainer);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkContainerForHideVessel(Container inventory) {
        for (Slot slot : (List<Slot>) inventory.inventorySlots) {
            ItemStack itemStack = slot.getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemHideVesselFluid) {
                if (itemStack.getItemDamage() == HideVesselMeta.RUINED) {
                    slot.putStack(new ItemStack(BidsItems.hideVessel, 1, HideVesselMeta.RUINED));
                    Bids.LOG.debug("Fluid lost!");
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.itemStack.getItem() == TFCItems.hideBag) {
            // Add hint about filling hide bag with fluid
            String originalHint = StatCollector.translateToLocal("gui.PotteryBase.Inst0");
            for (int i = 0; i < event.toolTip.size(); i++) {
                if (originalHint.equals(event.toolTip.get(i))) {
                    event.toolTip.add(i + 1, StatCollector.translateToLocal("gui.Help.HideBag"));

                    break;
                }
            }
        }
    }

}
