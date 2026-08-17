package com.unforbidable.tfc.bids.features.material.skin.crafting.action;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.core.crafting.CraftingContext;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.ItemStack;
import java.util.function.Consumer;

public class ShearSkin {

    private final ItemStack drop;
    private final boolean single;

    public ShearSkin(ItemStack drop, boolean single) {
        this.drop = drop;
        this.single = single;
    }

    public static Consumer<CraftingContext> shearSkin(ItemStack drop, boolean single) {
        return context -> new ShearSkin(drop, single)
            .onItemCrafted(context);
    }

    public static Consumer<CraftingContext> shearSkin(ItemStack drop) {
        return context -> new ShearSkin(drop, false)
            .onItemCrafted(context);
    }

    protected void onItemCrafted(CraftingContext context) {
        findCutAndIncreaseItemStackSize(context.event);
    }

    private void findCutAndIncreaseItemStackSize(PlayerEvent.ItemCraftedEvent event) {
        if (single) {
            // Always one item no matter the weight
            TFC_Core.giveItemToPlayer(drop.copy(), event.player);
        } else {
            for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                if (event.craftMatrix.getStackInSlot(i) != null) {
                    ItemStack is = event.craftMatrix.getStackInSlot(i);
                    if (is.getItem() instanceof ItemSkin) {
                        // Stack size depends on weight, 1 per small skin size
                        // which can be 0 if the skin is too small
                        float weight = SkinTag.of(is).getWeight();
                        int stackSize = Math.round(weight / SkinHelper.WEIGHT_SMALL);
                        if (stackSize > 0) {
                            ItemStack dropMore = drop.copy();
                            dropMore.stackSize = stackSize;
                            TFC_Core.giveItemToPlayer(dropMore, event.player);
                        }

                        break;
                    }
                }
            }
        }
    }

}
