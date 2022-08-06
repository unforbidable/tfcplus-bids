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

public class ItemKindling extends Item implements ISize {

    static String[] metaNames = new String[] { "Straw" };
    IIcon[] icons;

    public ItemKindling() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(1);
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < metaNames.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        icons = new IIcon[metaNames.length];
        for (int i = 0; i < metaNames.length; i++) {
            icons[i] = registerer.registerIcon(Tags.MOD_ID + ":wood/sticks/"
                    + getUnlocalizedName().replace("item.", "") + "." + metaNames[i]);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName() + "." + metaNames[itemStack.getItemDamage()];
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[damage];
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
