package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Items.ItemDrink;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Items.ItemDrinkingCloth;
import com.unforbidable.tfc.bids.Items.ItemDrinkingGlass;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class DrinkOverlayHandler {

    final ItemDrink item;
    final boolean isPottery;

    final FluidOverlayMap overlays;

    public DrinkOverlayHandler(ItemDrink item, boolean isPottery, int[] parts) {
        super();
        this.item = item;
        this.isPottery = isPottery;
        this.overlays = new FluidOverlayMap(parts);
    }

    public void registerIcons(IIconRegister registerer) {
        overlays.registerOverlayIcons(registerer,
            getOverlayFolder(),
            ((Item) item).getContainerItem().getUnlocalizedName().replace("item.", ""));
    }

    private String getOverlayFolder() {
        if (isPottery) {
            return "pottery";
        } else if (item.getContainerItem() instanceof ItemDrinkingGlass || item.getContainerItem() == TFCItems.glassBottle) {
            return "glassware";
        } else if (item.getContainerItem() instanceof ItemDrinkingCloth) {
            return "armor/clothing";
        }

        return "";
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return isPottery
                    ? item.getContainerItem().getIcon(item.getContainerItem(stack), pass)
                    : item.getContainerItem().getIcon(stack, pass);
        } else if (pass == 1) {
            return overlays.getOverlayIcon(stack.getItemDamage(), (int) item.getVolume());
        }

        return null;
    }

}
