package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.TileEntities.TEPottery;
import com.unforbidable.tfc.bids.api.Events.KilnEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.tileentity.TileEntity;

public class KilnPotteryFiringHandler {

    @SubscribeEvent
    public void onFireBlockCheck(KilnEvent.FireBlockCheck event) {
        // If another handler checked the block successfully
        // no need to check here
        if (!event.success) {
            TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
            if (te instanceof TEPottery) {
                event.success = true;
            }
        }
    }

    @SubscribeEvent
    public void onFireBlock(KilnEvent.FireBlock event) {
        // For TFC pottery
        // No firing is happening until the progress reaches 1
        // after that keep firing, so any molten metal remains heated
        if (event.progress >= 1) {
            TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
            if (te instanceof TEPottery) {
                TEPottery pottery = (TEPottery) te;
                pottery.cookItems();
            }
        }
    }

}
