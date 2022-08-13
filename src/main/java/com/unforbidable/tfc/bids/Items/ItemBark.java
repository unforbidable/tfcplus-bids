package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Firepit.FirepitHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBark extends Item implements ISize, IFirepitFuelMaterial {

    private IIcon[] icons;
    protected String[] names;

    public ItemBark() {
        super();
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(16);
    }

    public ItemBark setNames(String[] names) {
        this.names = names;
        return this;
    }

    public String[] getNames() {
        return names;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        icons = new IIcon[Global.WOOD_ALL.length];
        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            if (subItemExists(i)) {
                icons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "wood/"
                        + Global.WOOD_ALL[i] + " "
                        + getUnlocalizedName().replace("item.", ""));
            }
        }
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (names != null && i < names.length && subItemExists(i)) {
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
        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            if (subItemExists(i)) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public boolean subItemExists(int meta) {
        return WoodHelper.canPeelLog(meta);
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
        return EnumSize.MEDIUM;
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

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        // Birch bark can be used as kindling
        return itemStack.getItemDamage() == 2 ? 1.25f : 0;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return (int) (FirepitHelper.getEnumFuelMaterial(itemStack).burnTimeMax * 0.1f);
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return (int) (FirepitHelper.getEnumFuelMaterial(itemStack).burnTempMax * 0.5f);
    }

    @Override
    public EnumFuelMaterial getFuelTasteProfile(ItemStack itemStack) {
        return FirepitHelper.getEnumFuelMaterial(itemStack);
    }

}
