package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemGlassBottle;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Metal;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemDrinkingCloth extends ItemGlassBottle {

    public ItemDrinkingCloth() {
        super();

        setMetal(Global.GARBAGE);
        setMaxStackSize(32);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    @Override
    public boolean canSmash(ItemStack item) {
        return false;
    }

    @Override
    public Metal getMetalType(ItemStack is) {
        return Global.GARBAGE;
    }

    @Override
    public short getMetalReturnAmount(ItemStack is) {
        return 0;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":armor/clothing/"
                + getUnlocalizedName().replace("item.", ""));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity) {
        return FluidHelper.fillContainerFromWorld(is, world, entity);
    }

}
