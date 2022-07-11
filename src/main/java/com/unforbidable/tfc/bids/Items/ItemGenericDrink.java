package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemDrink;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.DrinkOverlayHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemGenericDrink extends ItemDrink {

    final DrinkOverlayHandler overlay;

    public ItemGenericDrink(float volume, boolean isPottery, int... overlayParts) {
        super(volume);
        pottery = isPottery;
        setCreativeTab(BidsCreativeTabs.BidsDrinks);
        overlay = new DrinkOverlayHandler(this, isPottery, overlayParts);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        overlay.registerIcons(registerer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        return overlay.getIcon(stack, pass);
    }

}
