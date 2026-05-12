package com.unforbidable.tfc.bids.features.device.woodpile.eventhandler;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.features.device.woodpile.WoodpileConfig;
import com.unforbidable.tfc.bids.features.device.woodpile.main.WoodpileHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class WoodpilePlacementHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (event.entityPlayer.isSneaking()) {
                    ItemStack heldItem = event.entityPlayer.getHeldItem();
                    if (heldItem != null && canItemCreateWoodpile(heldItem)) {
                        if (WoodpileHelper.createWoodpileAt(heldItem, event.entityPlayer, event.world, event.x, event.y, event.z, event.face)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean canItemCreateWoodpile(ItemStack heldItem) {
        return heldItem.getItem() == TFCItems.logs && WoodpileConfig.enablePlacementUsingLogsTFC;
    }

}
