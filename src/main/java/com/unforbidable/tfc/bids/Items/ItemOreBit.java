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
import com.unforbidable.tfc.bids.TFC.ItemTerra;
import com.unforbidable.tfc.bids.api.Interfaces.IExtraSmeltable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemOreBit extends Item implements ISize, ISmeltable, IExtraSmeltable {

    IIcon[] metaIcons = new IIcon[Global.ORE_METAL.length];

    public ItemOreBit() {
        this.setCreativeTab(BidsCreativeTabs.BidsDefault);
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

    @Override
    public int getItemStackLimit(ItemStack arg0) {
        return 64;
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (metaIcons != null && i < metaIcons.length) {
            return metaIcons[i];
        } else
            return this.itemIcon;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < Global.ORE_METAL.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        metaIcons = new IIcon[Global.ORE_METAL.length];
        for (int i = 0; i < Global.ORE_METAL.length; i++) {
            metaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + Global.ORE_METAL[i] + " Ore Bit");
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return getUnlocalizedName().concat("." + Global.ORE_METAL[itemstack.getItemDamage()]);
    }

    @Override
    public short getMetalReturnAmount(ItemStack is) {
        return 5;
    }

    @Override
    public Metal getMetalType(ItemStack is) {
        int dam = is.getItemDamage();
        switch (dam) {
            case 0:
            case 9:
            case 13:
            case 16:
            case 17:
                return Global.COPPER;
            case 1:
                return Global.GOLD;
            case 2:
                return Global.PLATINUM;
            case 4:
                return Global.SILVER;
            case 5:
                return Global.TIN;
            case 6:
                return Global.LEAD;
            case 7:
                return Global.BISMUTH;
            case 8:
                return Global.NICKEL;
            case 3:
            case 10:
            case 11:
                return Global.PIGIRON;
            case 12:
                return Global.ZINC;
        }

        return null;
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
    public float getPurity() {
        return 0.5f;
    }

    @Override
    public float getGranuality() {
        return 0.9f;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemTerra.addSizeInformation(is, list);

        if (getMetalType(is) != null) {
            if (ItemHelper.showShiftInformation()) {
                list.add(StatCollector.translateToLocal("gui.Units") + ": " + getMetalReturnAmount(is));
            } else {
                list.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }

    }

}
