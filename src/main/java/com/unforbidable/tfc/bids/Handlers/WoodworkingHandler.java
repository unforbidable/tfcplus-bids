package com.unforbidable.tfc.bids.Handlers;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Woodworking.WoodworkingHelper;
import com.unforbidable.tfc.bids.api.BidsGui;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class WoodworkingHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (!event.entityPlayer.isSneaking() && (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR ||
                event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && event.world.getTileEntity(event.x, event.y, event.z) == null)) {
                // Sneaking and right click air OR a non tile entity block
                // this allows opening containers and other interactions to work
                // even when a valid material is in hand
                ItemStack heldItem = event.entityPlayer.getHeldItem();
                if (heldItem != null && WoodworkingHelper.isValidWoodworkingMaterial(heldItem)) {
                    event.entityPlayer.openGui(Bids.instance, BidsGui.woodworkingGui, event.world, (int) event.entityPlayer.posX,
                        (int) event.entityPlayer.posY, (int) event.entityPlayer.posZ);

                    event.setCanceled(true);
                }
            }
        }
    }
}
