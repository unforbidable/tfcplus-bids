package com.unforbidable.tfc.bids.features.device.woodpile.eventhandler;

import com.unforbidable.tfc.bids.api.features.kiln.KilnEvent;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.tileentity.TileEntity;

public class KilnWoodDryingHandler {

    @SubscribeEvent
    public void onFireBlockCheck(KilnEvent.FireBlockCheck event) {
        // If another handler checked the block successfully
        // no need to check here
        if (!event.success) {
            TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
            if (te instanceof TileEntityWoodpile) {
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
            if (te instanceof TileEntityWoodpile) {
                TileEntityWoodpile woodPile = (TileEntityWoodpile) te;
                woodPile.seasonItemsFully();
            }
        }
    }

}
