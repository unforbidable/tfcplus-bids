package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.Items.ItemClothing;
import com.dunk.tfc.Items.ItemCoat;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class ItemExtraCoat extends ItemCoat {

    ResourceLocation myTexture;
    boolean[][] clothingAlpha;
    ResourceLocation res;
    String resLoc1 = "";
    String resLoc2 = "";
    public IIcon[] emptySlotIcons;

    public ItemExtraCoat(ClothingType clothingType) {
        super(clothingType);

        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "armor/clothing/"
                + this.getUnlocalizedName().replace("item.", ""));

        this.myTexture = new ResourceLocation(resLoc1, resLoc2);

        res = new ResourceLocation(Tags.MOD_ID, "textures/items/armor/clothing/"
                + this.getUnlocalizedName().replace("item.", "Flat ") + ".png");

        clothingAlpha = TFC_Textures.loadClothingPattern(res);
    }

    @Override
    public ItemClothing setResourceLocation(String loc, String loc2) {
        resLoc1 = loc;
        resLoc2 = loc2;

        return super.setResourceLocation(loc, loc2);
    }

    @Override
    public boolean[][] getClothingAlpha() {
        return clothingAlpha;
    }

    @Override
    public ResourceLocation getClothingTexture(Entity entity, ItemStack itemstack, int num) {
        return myTexture;
    }

    @Override
    public ResourceLocation getFlatTexture() {
        return res;
    }

}
