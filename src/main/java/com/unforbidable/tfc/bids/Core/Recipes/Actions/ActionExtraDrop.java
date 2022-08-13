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

    private List<ItemStack> extraDropList = new ArrayList<ItemStack>();

    public ActionExtraDrop addExtraDrop(ItemStack extraDrop) {
        extraDropList.add(extraDrop);
        return this;
    }

    @Override
    public void onItemCrafted(ItemCraftedEvent event) {
        super.onItemCrafted(event);

        if (!event.player.worldObj.isRemote) {
            for (ItemStack extraDrop : extraDropList) {
                if (extraDrop.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    final int dmg = event.crafting.getItemDamage();
                    final ItemStack is = new ItemStack(extraDrop.getItem(), extraDrop.stackSize, dmg);
                    giveItemToPlayer(event.player, is);
                } else {
                    giveItemToPlayer(event.player, extraDrop.copy());
                }
            }
        }
    }

    protected void giveItemToPlayer(EntityPlayer player, ItemStack itemStack) {
        EntityItem ei = new EntityItem(player.worldObj, player.posX, player.posY + 1, player.posZ, itemStack);
        player.worldObj.spawnEntityInWorld(ei);
        player.onItemPickup(ei, 0);
    }

}
