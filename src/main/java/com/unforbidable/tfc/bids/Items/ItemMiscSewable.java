package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISewable;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemMiscSewable extends Item implements ISize, ISewable {

    boolean[][] clothingAlpha;
    ResourceLocation res;

    public ItemMiscSewable() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "armor/clothing/"
                + this.getUnlocalizedName().replace("item.", ""));

        res = new ResourceLocation(Tags.MOD_ID, "textures/items/armor/clothing/"
                + getUnlocalizedName().replace("item.", "Flat ") + ".png");

        clothingAlpha = TFC_Textures.loadClothingPattern(res);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

    @Override
    public ResourceLocation getFlatTexture() {
        return res;
    }

    @Override
    public boolean[][] getClothingAlpha() {
        return this.clothingAlpha;
    }

    @Override
    public int getRepairCost() {
        return 0;
    }

    @Override
    public Item setRepairCost(int arg0) {
        return this;
    }

}
