package com.unforbidable.tfc.bids.common.item;

import com.dunk.tfc.Items.ItemAlcohol;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.core.drink.DrinkOverlayHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemCommonAlcohol extends ItemAlcohol {

    final DrinkOverlayHandler overlay;

    public ItemCommonAlcohol(float volume, boolean isPottery, int... overlayParts) {
        super(volume);
        pottery = isPottery;
        setCreativeTab(BidsCreativeTabs.bidsFoodstuffs);
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
