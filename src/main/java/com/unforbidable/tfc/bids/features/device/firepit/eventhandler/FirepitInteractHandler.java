package com.unforbidable.tfc.bids.features.device.firepit.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.Tools.ItemCustomShovel;
import com.dunk.tfc.TileEntities.TEFirepit;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.compat.tfc.meta.Powder;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitConfig;
import com.unforbidable.tfc.bids.features.device.firepit.tileentity.TileEntityNewFirepit;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class FirepitInteractHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (FirepitConfig.allowAshRemovalAlsoFromFirepitTFC) {
            if (!event.world.isRemote) {
                if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                    ItemStack heldItem = event.entityPlayer.getHeldItem();
                    if (heldItem != null && heldItem.getItem() instanceof ItemCustomShovel) {
                        TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
                        if (te instanceof TEFirepit && !(te instanceof TileEntityNewFirepit)) {
                            TEFirepit firepit = (TEFirepit) te;
                            if (firepit.ashNumber > 0 && firepit.fireTemp <= 1F) {
                                TFC_Core.giveItemToPlayer(new ItemStack(TFCItems.powder, firepit.ashNumber, Powder.ASH), event.entityPlayer);
                                firepit.ashNumber = 0;

                                heldItem.damageItem(1, event.entityPlayer);

                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }

}
