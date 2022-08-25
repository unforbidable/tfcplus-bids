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
import com.unforbidable.tfc.bids.Core.Firepit.FirepitHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IFirepitFuelMaterial;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemKindling extends Item implements ISize, IFirepitFuelMaterial {

    private float fuelKindlingQuality = 1f;

    public ItemKindling() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(1);
    }

    public ItemKindling setFuelKindlingQuality(float fuelKindlingQuality) {
        this.fuelKindlingQuality = fuelKindlingQuality;

        return this;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (FirepitHelper.createFirepitAt(itemStack, player, world, x, y, z, side)) {
            return true;
        }

        return false;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":wood/sticks/"
                + this.getUnlocalizedName().replace("item.", ""));
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

        if (ItemHelper.showShiftInformation()) {
            list.add(StatCollector.translateToLocal("gui.Help"));
            list.add(StatCollector.translateToLocal("gui.Help.Kindling"));
            list.add(StatCollector.translateToLocal("gui.Help.Kindling2"));
        } else {
            list.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return fuelKindlingQuality;
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
