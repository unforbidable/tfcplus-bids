package com.unforbidable.tfc.bids.features.material.heatingstone.item;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.meta.HeatingStoneMeta;
import com.unforbidable.tfc.bids.common.item.ItemCommonTerraMisc;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import java.util.List;

public class ItemHeatingStone extends ItemCommonTerraMisc {

    public ItemHeatingStone() {
        setTextureFolder("rocks");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(this, 1, HeatingStoneMeta.COLD));

        ItemStack is = new ItemStack(this, 1, HeatingStoneMeta.HOT);
        TFC_ItemHeat.setTemp(is, 500);
        is.stackTagCompound.setLong("heatedUntilStone", TFC_Time.getTotalTicks() + 2000);
        list.add(is);
    }

    @Override
    public boolean onUpdate(ItemStack is, World world, int x, int y, int z) {
        // Only called when item not being heated up
        // Can happen during heating, when the fuel is "reloading"
        if (is.hasTagCompound() && (is.getItemDamage() == HeatingStoneMeta.HOT ||
            is.getItemDamage() == HeatingStoneMeta.COLD && TFC_ItemHeat.hasTemp(is))) {
            // Cannot use TFC the hold temperature system
            // but doing something similar
            if (is.stackTagCompound.hasKey("lastHeated")) {
                // Rock is cooling, and it has just been taken off the heat
                // but wait some tick it case it is just fuel reloading
                long lastHeated = is.stackTagCompound.getLong("lastHeated");
                if (lastHeated + 10 < TFC_Time.getTotalTicks()) {
                    // Pausing cooling if glowing hot
                    if (is.getItemDamage() == HeatingStoneMeta.HOT) {
                        // set it to 2 hours, 2x as long as metal
                        is.stackTagCompound.setLong("heatedUntilStone", TFC_Time.getTotalTicks() + 2000L);
                    } else {
                        // make sure to remove any protection to allow normal cooling
                        is.stackTagCompound.removeTag("heatedUntilStone");
                    }

                    // Forget heating as not to trigger this again, until heated again
                    is.stackTagCompound.removeTag("lastHeated");
                }
            }

            if (is.getItemDamage() == HeatingStoneMeta.HOT && is.stackTagCompound.hasKey("heatedUntilStone")) {
                if (TFC_Time.getTotalTicks() < is.stackTagCompound.getLong("heatedUntilStone")) {
                    // Do not allow normal cooling
                    return true;
                } else {
                    // Clean up
                    is.stackTagCompound.removeTag("heatedUntilStone");
                }
            }

            if (is.getItemDamage() == HeatingStoneMeta.HOT && TFC_ItemHeat.getTemp(is) < 480) {
                // No longer glowing hot but continue cooling
                is.setItemDamage(HeatingStoneMeta.COLD);
            }

            if (is.getItemDamage() == HeatingStoneMeta.COLD && TFC_ItemHeat.getTemp(is) < 80) {
                // If only "Warming" temp, try to remove
                TFC_ItemHeat.removeTempTag(is);
            }
        }

        // Allow normal cooling
        return false;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.getEntityItem().getItemDamage() == HeatingStoneMeta.HOT ||
            entityItem.getEntityItem().getItemDamage() == HeatingStoneMeta.COLD && TFC_ItemHeat.hasTemp(entityItem.getEntityItem())) {
            if (entityItem.age > 20) {
                int xCoord = (int) Math.floor(entityItem.posX);
                int yCoord = (int) Math.floor(entityItem.posY);
                int zCoord = (int) Math.floor(entityItem.posZ);

                Block block = entityItem.worldObj.getBlock(xCoord, yCoord, zCoord);
                boolean atWetBlock = TFC_Core.isWater(block) ||
                    TFC_Core.isHotWater(block) ||
                    TFC_Core.isExposedToRain(entityItem.worldObj, xCoord, yCoord, zCoord);

                if (atWetBlock) {
                    // CURED stone changed to CURED as well, as a quick way to remove temperature and any other tags
                    entityItem.setEntityItemStack(new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.COLD));
                }
            }
        }

        return false;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getItemDamage() == HeatingStoneMeta.HOT ? 1 : super.getItemStackLimit(stack);
    }

}
