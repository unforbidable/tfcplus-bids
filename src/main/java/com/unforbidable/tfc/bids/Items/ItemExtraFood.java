package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Reference;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import com.unforbidable.tfc.bids.api.Interfaces.ICookingIngredientOverride;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemExtraFood extends ItemFoodTFC implements ICookingIngredientOverride {

    protected Item ingredientOverride;

    public ItemExtraFood(EnumFoodGroup fg, int sw, int so, int sa, int bi, int um) {
        super(fg, sw, so, sa, bi, um);

        setCreativeTab(BidsCreativeTabs.bidsFoodstuffs);
    }

    public ItemExtraFood(EnumFoodGroup fg, int sw, int so, int sa, int bi, int um, boolean edible, boolean usable) {
        super(fg, sw, so, sa, bi, um, edible, usable);

        setCreativeTab(BidsCreativeTabs.bidsFoodstuffs);
    }

    public ItemExtraFood(EnumFoodGroup fg, int sw, int so, int sa, int bi, int um, boolean edible, boolean usable, boolean poisonOnRaw, boolean guaranteedPoison) {
        super(fg, sw, so, sa, bi, um, edible, usable, poisonOnRaw, guaranteedPoison);

        setCreativeTab(BidsCreativeTabs.bidsFoodstuffs);
    }

    public ItemExtraFood setIngredientOverride(Item ingredientOverride) {
        this.ingredientOverride = ingredientOverride;

        return this;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":food/"
                + this.getUnlocalizedName().replace("item.", ""));

        if (hasCookedIcon) {
            this.cookedIcon = registerer.registerIcon(Tags.MOD_ID + ":food/"
                + this.getUnlocalizedName().replace("item.", "") + " Cooked");
        }
    }

    @Override
    public Item getIngredientOverride() {
        return ingredientOverride != null ? ingredientOverride : this;
    }

}
