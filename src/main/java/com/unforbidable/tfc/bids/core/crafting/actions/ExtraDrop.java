package com.unforbidable.tfc.bids.core.crafting.actions;

import com.unforbidable.tfc.bids.core.crafting.CraftingContext;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.function.Consumer;

public class ExtraDrop  {

    protected final ItemStack item;
    protected final float chance;

    protected ExtraDrop(ItemStack item, float chance) {
        this.item = item;
        this.chance = chance;
    }

    public static Consumer<CraftingContext> extraDrop(ItemStack item) {
        return extraDrop(item, 1f);
    }

    public static Consumer<CraftingContext> extraDrop(ItemStack item, float chance) {
        return context -> new ExtraDrop(item, chance)
            .onItemCrafted(context);
    }

    protected void onItemCrafted(CraftingContext context) {
        ItemCraftedEvent event = context.event;
        if (!event.player.worldObj.isRemote) {
            if (event.player.worldObj.rand.nextDouble() < chance) {
                if (item.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    final int dmg = event.crafting.getItemDamage();
                    final ItemStack is = new ItemStack(item.getItem(), item.stackSize, dmg);
                    giveItemToPlayer(event.player, is);
                } else {
                    giveItemToPlayer(event.player, item.copy());
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
