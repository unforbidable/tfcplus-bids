package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Items.ItemGlassBottle;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.Drinks.DrinkHelper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDrinkingGlass extends ItemGlassBottle {

    short glassReturnAmount = 0;

    public ItemDrinkingGlass() {
        super();
        setMetal(Global.GLASS);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    public ItemDrinkingGlass setGlassReturnAmount(int glassReturnAmount) {
        this.glassReturnAmount = (short) glassReturnAmount;
        return this;
    }

    @Override
    public Metal getMetalType(ItemStack is) {
        return Global.GLASS;
    }

    @Override
    public short getMetalReturnAmount(ItemStack is) {
        return glassReturnAmount;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":glassware/"
                + getUnlocalizedName().replace("item.", ""));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity) {
        return DrinkHelper.getFreshWaterDrink(is, world, entity,
                getMovingObjectPositionFromPlayer(world, entity, true));
    }

}
