package com.unforbidable.tfc.bids.Core.Recipes.Actions;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.Core.Recipes.RecipeAction;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ActionExtraDrop extends RecipeAction {

    private List<RandomItemStack> extraDropList = new ArrayList<RandomItemStack>();

    public ActionExtraDrop addExtraDrop(ItemStack extraDrop) {
        extraDropList.add(new RandomItemStack(extraDrop, 1f));
        return this;
    }

    public ActionExtraDrop addExtraDrop(ItemStack extraDrop, float chance) {
        extraDropList.add(new RandomItemStack(extraDrop, chance));
        return this;
    }

    @Override
    public void onItemCrafted(ItemCraftedEvent event) {
        super.onItemCrafted(event);

        if (!event.player.worldObj.isRemote) {
            for (RandomItemStack extraDrop : extraDropList) {
                if (event.player.worldObj.rand.nextDouble() < extraDrop.chance) {
                    if (extraDrop.item.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        final int dmg = event.crafting.getItemDamage();
                        final ItemStack is = new ItemStack(extraDrop.item.getItem(), extraDrop.item.stackSize, dmg);
                        giveItemToPlayer(event.player, is);
                    } else {
                        giveItemToPlayer(event.player, extraDrop.item.copy());
                    }
                }
            }
        }
    }

    protected void giveItemToPlayer(EntityPlayer player, ItemStack itemStack) {
        EntityItem ei = new EntityItem(player.worldObj, player.posX, player.posY + 1, player.posZ, itemStack);
        player.worldObj.spawnEntityInWorld(ei);
        player.onItemPickup(ei, 0);
    }

    class RandomItemStack {

        public final ItemStack item;
        public final float chance;

        public RandomItemStack(ItemStack item, float chance) {
            this.item = item;
            this.chance = chance;
        }

    }

}
