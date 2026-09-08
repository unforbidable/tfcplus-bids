package com.unforbidable.tfc.bids.features.material.heatingstone.eventhandler;

import com.dunk.tfc.Blocks.Terrain.BlockGravel;
import com.dunk.tfc.Items.ItemBlocks.ItemSoil;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Events.ItemCookEvent;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.meta.HeatingStoneMeta;
import com.unforbidable.tfc.bids.api.meta.StoneMeta;
import com.unforbidable.tfc.bids.features.material.heatingstone.item.ItemHeatingStone;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;

public class HeatingStoneEventHandler {

    @SubscribeEvent
    public void onItemCook(ItemCookEvent event) {
        // Handle NBT transfer here instead of keepNBT which for some reason sets the temp to 90%
        if (event.input1.getItem() instanceof ItemHeatingStone) {
            float temp = TFC_ItemHeat.getTemp(event.input1);
            TFC_ItemHeat.setTemp(event.result, temp, true);
        }
    }

    @SubscribeEvent
    public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.block instanceof BlockGravel && event.harvester.getHeldItem() == null) {
            // Check elevation as a quick guess if the gravel might be near water
            if (event.y > Global.SEALEVEL + 3) {
                // Find gravel block to be replaced
                // If a flint is dropping by chance instead, do nothing
                for (int i = 0; i < event.drops.size(); i++) {
                    ItemStack drop = event.drops.get(i);
                    if (drop.getItem() instanceof ItemSoil) {
                        Block dropBlock = Block.getBlockFromItem(drop.getItem());
                        if (dropBlock instanceof BlockGravel) {
                            // Dropped soil item block is that of Gravel
                            // just need to find out which and get the whole meta
                            int damage = drop.getItemDamage() + (dropBlock == TFCBlocks.gravel2 ? 16 : 0);

                            // Certain blocks have an increased chance to drop a heating stone
                            int rockTypeAffinity = getRockTypeAffinity(damage);

                            // Otherwise handling fortune in the same way TFC handles flint drop
                            int fortune = Math.min(3, event.fortuneLevel + rockTypeAffinity);

                            if (event.world.rand.nextInt(10 - fortune * 3) == 0) {
                                // Higher elevation gravel blocks are more likely to drop heating stone that is already cured
                                int curedFortune = Math.min(8, Math.max(0, (event.y - Global.SEALEVEL - 6) / 4));
                                int meta = event.world.rand.nextInt(10 - curedFortune) == 0 ? HeatingStoneMeta.CURED : HeatingStoneMeta.FRESH;

                                Bids.LOG.info("{}, {} -> {}", event.y, curedFortune, meta);

                                // Replace the dropped gravel block item stack
                                ItemStack newDrop = new ItemStack(BidsItems.heatingStone, 1, meta);
                                event.drops.set(i, newDrop);
                            }
                        }
                    }
                }
            }
        }
    }

    private int getRockTypeAffinity(int damage) {
        switch (damage) {
            case StoneMeta.QUARTZITE:
                return 2;

            case StoneMeta.BASALT:
            case StoneMeta.GABBRO:
            case StoneMeta.ANDESITE:
            case StoneMeta.DACITE:
            case StoneMeta.DIORITE:
            case StoneMeta.GRANITE:
            case StoneMeta.GNEISS:
                return 1;
        }

        return 0;
    }

}
