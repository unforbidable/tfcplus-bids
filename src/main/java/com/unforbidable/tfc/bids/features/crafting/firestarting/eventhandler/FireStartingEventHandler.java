package com.unforbidable.tfc.bids.features.crafting.firestarting.eventhandler;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.features.firestarting.FireStartingEvent;
import com.unforbidable.tfc.bids.features.crafting.firestarting.FireStartingRegistry;
import com.unforbidable.tfc.bids.features.crafting.firestarting.item.ItemTinder;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHandler;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class FireStartingEventHandler {

    @SubscribeEvent
    public void onInventoryOpen(PlayerOpenContainerEvent event) {
        checkContainerForTinder(event.entityPlayer.openContainer);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer && TFC_Time.getTotalTicks() % 20 == 0) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            checkContainerForTinder(player.openContainer);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkContainerForTinder(Container inventory) {
        for (Slot slot : (List<Slot>)inventory.inventorySlots) {
            ItemStack itemStack = slot.getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemTinder) {
                if (TFC_ItemHeat.hasTemp(itemStack)) {
                    if (FireStartingHelper.isTinderSpent(itemStack)){
                        slot.putStack(null);
                        Bids.LOG.info("Tinder is spent!");
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onFireStartingStart(FireStartingEvent event) {
        for (FireStartingHandler handler : FireStartingRegistry.handlers.stream()
            .sorted(Comparator.comparing(h -> h.priority))
            .collect(Collectors.toList())) {
            if (!event.result && !event.isCanceled()) {
                if (event.stage == FireStartingEvent.Stage.START) {
                    event.result = handler.start(event.entityPlayer, event.world, event.x, event.y, event.z, event.side);
                } else if (event.stage == FireStartingEvent.Stage.COMPLETE) {
                    event.result = handler.complete(event.entityPlayer, event.world, event.x, event.y, event.z, event.side);
                } else if (event.stage == FireStartingEvent.Stage.IGNITE) {
                    event.result = handler.ignite(event.entityPlayer, event.world, event.x, event.y, event.z, event.side);
                } else if (event.stage == FireStartingEvent.Stage.PROPAGATE) {
                    event.result = handler.propagate(event.entityPlayer, event.world, event.x, event.y, event.z, event.side);
                }

                Bids.LOG.info("Fire starting stage {} result: {} ({})", event.stage, event.result, handler.getClass().getCanonicalName());
            }
        }

    }

}
