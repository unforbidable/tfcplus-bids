package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.Interfaces.ISmeltable;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IExtraSmeltable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class ItemGlassLump extends Item implements ISize, ISmeltable, IExtraSmeltable {

    public ItemGlassLump() {
        this.setCreativeTab(BidsCreativeTabs.BidsDefault);
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
        return EnumSize.TINY;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.HEAVY;
    }

    @Override
    public int getItemStackLimit(ItemStack arg0) {
        return 1;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        ItemStack is = new ItemStack(this, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("volume", 100);
        is.setTagCompound(tag);
        list.add(is);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":"
                + this.getUnlocalizedName().replace("item.", ""));
    }

    @Override
    public short getMetalReturnAmount(ItemStack is) {
        if (is.hasTagCompound()) {
            return (short) is.getTagCompound().getInteger("volume");
        }
        return 0;
    }

    @Override
    public Metal getMetalType(ItemStack is) {
        return Global.GLASS;
    }

    @Override
    public EnumTier getSmeltTier(ItemStack is) {
        return EnumTier.TierI;
    }

    @Override
    public boolean isSmeltable(ItemStack is) {
        return true;
    }

    @Override
    public float getPurity(ItemStack is) {
        return 0.75f;
    }

    @Override
    public boolean isNativeOre(ItemStack is) {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);

        if (getMetalType(is) != null) {
            if (ItemHelper.showShiftInformation()) {
                list.add(StatCollector.translateToLocal("gui.Units") + ": " + getMetalReturnAmount(is));
            } else {
                list.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }

    }

}
