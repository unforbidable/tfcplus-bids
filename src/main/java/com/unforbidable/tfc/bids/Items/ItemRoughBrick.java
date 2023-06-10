package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;

public class ItemRoughBrick extends Item implements ISize {

    protected IIcon icons[];
    protected String[] names;
    protected List<Integer> metaWhitelist = null;
    protected String textureName;

    public ItemRoughBrick() {
        super();
        setMaxStackSize(32);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public ItemRoughBrick setMetaOnly(Integer ...metaOnly) {
        this.metaWhitelist = Arrays.asList(metaOnly);
        return this;
    }

    public ItemRoughBrick setNames(String[] names) {
        this.names = names;
        return this;
    }

    @Override
    public Item setTextureName(String textureName) {
        this.textureName = textureName;
        return super.setTextureName(textureName);
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
        for (int i = 0; i < names.length; i++) {
            if (metaWhitelist != null && !metaWhitelist.contains(i)) {
                continue;
            }

            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public void registerIcons(IIconRegister iconRegisterer) {
        icons = new IIcon[names.length];
        for (int i = 0; i < names.length; i++) {
            if (metaWhitelist != null && !metaWhitelist.contains(i)) {
                continue;
            }

            icons[i] = iconRegisterer.registerIcon(Tags.MOD_ID + ":rocks/"
                    + names[i] + " " + textureName);
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
