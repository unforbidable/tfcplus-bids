package com.unforbidable.tfc.bids.features.utility.card.eventhandler;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Tools.IKnife;
import com.unforbidable.tfc.bids.api.BidsItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class CardEventHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.world.isRemote) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                ItemStack heldItem = event.entityPlayer.getHeldItem();
                if (heldItem != null && heldItem.getItem() instanceof IKnife) {
                    Block block = event.world.getBlock(event.x, event.y, event.z);
                    if ((block == TFCBlocks.shrub || block == TFCBlocks.undergrowthPalm || block == TFCBlocks.undergrowth) && !event.entityPlayer.isSneaking()) {
                        // This action is also used in TFC+ to harvest resin from shrubs
                        // so the knife already takes damage
                        if (event.world.rand.nextInt(16) == 0) {
                            ItemStack is = new ItemStack(BidsItems.thornBunch);
                            EntityItem ei = new EntityItem(event.world, event.x, event.y, event.z, is);
                            event.world.spawnEntityInWorld(ei);
                            event.world.playSoundEffect(event.x, event.y, event.z, "dig.wood",
                                0.4F + (event.world.rand.nextFloat() / 2), 0.7F + event.world.rand.nextFloat());
                        }
                    }
                }
            }
        }
    }

}
