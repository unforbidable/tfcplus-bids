package com.unforbidable.tfc.bids.features.material.hide.eventhandler;

import com.dunk.tfc.Entities.EntityProjectileTFC;
import com.dunk.tfc.Items.ItemLeather;
import com.dunk.tfc.Items.ItemRawHide;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.material.skin.SkinConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class HideLivingDropsEventHandler {

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        // Very small rawhide drops not desirable when Skins are dropped
        if (!SkinConfig.enableAnimalSkinDropReplacement) {
            if (event.entityLiving instanceof IAnimal) {
                EntityPlayer butcherPlayer = getButcherPlayer(event);
                if (butcherPlayer != null) {
                    int tinyRawHideCount = getTinyRawHideCountForDrops(event.drops);
                    if (tinyRawHideCount > 0) {
                        ItemStack tinyRawHideItemStack = new ItemStack(BidsItems.moreHide, tinyRawHideCount, 0);
                        EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, tinyRawHideItemStack);
                        event.drops.add(entityItem);
                    }
                }
            }
        }
    }

    private static EntityPlayer getButcherPlayer(LivingDropsEvent event) {
        if (event.source.getSourceOfDamage() instanceof EntityPlayer) {
            return (EntityPlayer) event.source.getSourceOfDamage();
        } else if (event.source.getSourceOfDamage() instanceof EntityProjectileTFC) {
            EntityProjectileTFC proj = (EntityProjectileTFC) event.source.getSourceOfDamage();
            if (proj.shootingEntity instanceof EntityPlayer) {
                return (EntityPlayer) proj.shootingEntity;
            }
        }

        return null;
    }

    private int getTinyRawHideCountForDrops(ArrayList<EntityItem> drops) {
        int count = 0;
        for (EntityItem ei : drops) {
            // Each dropped hide, fur, or sheep skin contributes to tiny raw hide count
            // small = 0
            // medium = 1
            // large = 2
            if (ei.getEntityItem() != null) {
                Item item = ei.getEntityItem().getItem();

                if (item instanceof ItemLeather && ((ItemLeather) item).getHasSizes()
                    || item instanceof ItemRawHide) {
                    int damage = ei.getEntityItem().getItemDamage();
                    count += damage;
                }
            }
        }

        return count;
    }

}
