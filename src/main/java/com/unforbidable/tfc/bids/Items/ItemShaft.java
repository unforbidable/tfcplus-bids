package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemShaft extends Item implements ISize {

    private IIcon[] icons;
    protected String[] names;

    public ItemShaft() {
        super();
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(32);
    }

    public ItemShaft setNames(String[] names) {
        this.names = names;
        return this;
    }

    public String[] getNames() {
        return names;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        icons = new IIcon[names.length];
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.items.hasShaft()) {
                icons[wood.index] = registerer.registerIcon(Tags.MOD_ID + ":" + "wood/"
                    + names[wood.index] + " "
                    + getUnlocalizedName().replace("item.", ""));
            }
        }
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (names != null && i < names.length && icons[i] != null) {
            return icons[i];
        } else {
            return this.itemIcon;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return this.getUnlocalizedName() + "." + names[itemstack.getItemDamage()];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list) {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.items.hasShaft()) {
                list.add(wood.items.getShaft());
            }
        }
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.MEDIUM;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
        SeasoningHelper.addSeasoningInformation(is, list);
    }

}
