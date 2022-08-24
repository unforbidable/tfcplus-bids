package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.Items.ItemLeatherBag;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class ItemExtraBag extends ItemLeatherBag {

    boolean[][] clothingAlpha;
    ResourceLocation res;

    public ItemExtraBag() {
        super(new String[] {});

        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "armor/clothing/"
                + getUnlocalizedName().replace("item.", ""));

        res = new ResourceLocation(Tags.MOD_ID, "textures/items/armor/clothing/"
                + getUnlocalizedName().replace("item.", "Flat ") + ".png");

        clothingAlpha = TFC_Textures.loadClothingPattern(res);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return itemIcon;
    }

    @Override
    public ResourceLocation getFlatTexture() {
        return res;
    }

    @Override
    public boolean[][] getClothingAlpha() {
        return this.clothingAlpha;
    }

}
