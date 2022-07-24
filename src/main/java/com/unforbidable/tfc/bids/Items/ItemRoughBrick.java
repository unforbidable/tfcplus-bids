package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemRoughBrick extends Item implements ISize {

    protected IIcon icons[];
    protected String[] names;

    public ItemRoughBrick() {
        super();
        setMaxStackSize(32);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public ItemRoughBrick setNames(String[] names) {
        this.names = names;
        return this;
    }

    public String[] getNames() {
        return names;
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (names != null && i < names.length) {
            return icons[i];
        } else
            return this.itemIcon;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < names.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public void registerIcons(IIconRegister iconRegisterer) {
        icons = new IIcon[names.length];
        for (int i = 0; i < names.length; i++) {
            icons[i] = iconRegisterer.registerIcon(Tags.MOD_ID + ":rocks/"
                    + names[i] + " Rough Brick");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return this.getUnlocalizedName() + "." + names[itemstack.getItemDamage()];
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.TINY;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.HEAVY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
