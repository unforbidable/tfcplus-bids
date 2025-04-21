package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileHelper;
import com.unforbidable.tfc.bids.api.BidsOptions;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class WoodPileHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (event.entityPlayer.isSneaking()) {
                    ItemStack heldItem = event.entityPlayer.getHeldItem();
                    if (heldItem != null && canItemCreateWoodPile(heldItem)) {
                        if (WoodPileHelper.createWoodPileAt(heldItem, event.entityPlayer, event.world, event.x, event.y, event.z, event.face)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean canItemCreateWoodPile(ItemStack heldItem) {
        return heldItem.getItem() == TFCItems.logs && BidsOptions.WoodPile.enablePlacementUsingLogsTFC;
    }

}
