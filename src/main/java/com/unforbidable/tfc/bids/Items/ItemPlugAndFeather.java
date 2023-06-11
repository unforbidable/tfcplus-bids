package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;

import com.unforbidable.tfc.bids.api.Enums.EnumQuarryEquipmentTier;
import com.unforbidable.tfc.bids.api.Interfaces.IPlugAndFeather;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPlugAndFeather extends Item implements ISize, IPlugAndFeather {

    public ItemPlugAndFeather() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    @Override
    public EnumQuarryEquipmentTier getPlugAndFeatherQuarryEquipmentTier(ItemStack itemStack) {
        return EnumQuarryEquipmentTier.STONE;
    }

    @Override
    public float getPlugAndFeatherDropRate(ItemStack itemStack) {
        return 0.6f;
    }

    @Override
    public Block getPlugAndFeatherRenderBlock(ItemStack itemStack) {
        return TFCBlocks.woodSupportV;
    }

    @Override
    public int getPlugAndFeatherRenderBlockMetadata(ItemStack itemStack) {
        return 5;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":"
                + this.getUnlocalizedName().replace("item.", ""));
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
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @Override
    public int getItemStackLimit(ItemStack arg0) {
        return 64;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
