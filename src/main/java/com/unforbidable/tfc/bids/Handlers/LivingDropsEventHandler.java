package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Entities.EntityProjectileTFC;
import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.Entities.Mobs.EntitySheepTFC;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemLeather;
import com.dunk.tfc.Items.ItemRawHide;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Entities.IAnimal;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.api.BidsItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.Random;

public class LivingDropsEventHandler {

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.entityLiving instanceof IAnimal) {
            IAnimal animal = (IAnimal) event.entityLiving;
            float ageMod = TFC_Core.getPercentGrown(animal);
            float sizeMod = animal.getSizeMod();

            EntityPlayer butcherPlayer = getButcherPlayer(event);
            if (butcherPlayer != null) {
                int suetBaseWeight = getSuetBaseWeightForAnimal(animal);
                if (suetBaseWeight > 0) {
                    float suetWeight = ageMod * (sizeMod * suetBaseWeight);
                    dropButcheredMeat(event, butcherPlayer, BidsItems.suet, suetWeight);
                }

                int tinyRawHideCount = getTinyRawHideCountForDrops(event.drops);
                if (tinyRawHideCount > 0) {
                    ItemStack tinyRawHideItemStack = new ItemStack(BidsItems.moreHide, tinyRawHideCount, 0);
                    EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, tinyRawHideItemStack);
                    event.drops.add(entityItem);
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

    private static void dropButcheredMeat(LivingDropsEvent event, EntityPlayer butcherPlayer, Item item, float weight) {
        ItemStack foodItemStack = ItemFoodTFC.createTag(new ItemStack(item, 1), 160);
        Random r = new Random(event.entityLiving.getUniqueID().getLeastSignificantBits() + event.entityLiving.getUniqueID().getMostSignificantBits());
        Food.adjustFlavor(foodItemStack, r);

        float weightForSkill = weight * ((TFC_Core.getSkillStats(butcherPlayer).getSkillMultiplier(Global.SKILL_BUTCHERING) + 0.01f) * 0.5f + 0.5f);
        while (weightForSkill > 0) {
            ItemStack portionItemStack = foodItemStack.copy();
            if (weightForSkill < 160) {
                Food.setWeight(portionItemStack, weightForSkill);
            }
            weightForSkill -= 160;

            EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, portionItemStack);
            event.drops.add(entityItem);
        }
    }

    private int getSuetBaseWeightForAnimal(IAnimal animal) {
        // 10% of meat weight of cow, goat and sheep
        if (animal instanceof EntityCowTFC) {
            return 400;
        } else if (animal instanceof EntitySheepTFC) {
            return 64;
        } else if (animal instanceof EntityGoat) {
            return 64;
        } else {
            return 0;
        }
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
