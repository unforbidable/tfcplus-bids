package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSmallStickBundle extends Item implements ISize, IFirepitFuelMaterial {

    public ItemSmallStickBundle() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(32);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":wood/sticks/"
                + getUnlocalizedName().replace("item.", ""));
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
        return 0.25f;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return (int) (EnumFuelMaterial.STICK.burnTimeMax * 1.5f);
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return (int) (EnumFuelMaterial.STICK.burnTempMax * 1.2f);
    }

    @Override
    public EnumFuelMaterial getFuelTasteProfile(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE;
    }

}
