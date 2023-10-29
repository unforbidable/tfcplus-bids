package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.Player.FoodStatsTFC;
import com.dunk.tfc.Food.ItemSandwich;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.Interfaces.IMoreSandwich;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMoreSandwich extends ItemSandwich implements IMoreSandwich {

    protected final float[] ingredientWeights;
    protected final float foodMinWeight;
    protected final float foodMaxWeight;

    public ItemMoreSandwich(float[] ingredientWeights, float foodMinWeight) {
        this.ingredientWeights = ingredientWeights;
        this.foodMinWeight = foodMinWeight;

        float total = 0;
        for (float w : ingredientWeights) {
            total += w;
        }
        this.foodMaxWeight = total;

        this.hasSubtypes = false;
        this.metaNames = null;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        if (this.metaNames == null) {
            if (this.iconString != null)
                this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "food/" + this.getIconString());
            else
                this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "food/" + this.getUnlocalizedName().replace("item.", ""));
        } else {
            metaIcons = new IIcon[metaNames.length];
            for (int i = 0; i < metaNames.length; i++) {
                metaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "food/" + metaNames[i]);
            }

            this.itemIcon = metaIcons[0];
        }
    }

    @Override
    protected float getFillingBoost() {
        return 1.1f;
    }

    @Override
    protected float[] getFoodWeights() {
        return getIngredientWeights();
    }

    @Override
    protected float getEatAmount(FoodStatsTFC fs, float amount) {
        float eatAmount = Math.min(amount, getAmountEatenPerCycle());
        float stomachDiff = fs.stomachLevel+eatAmount-fs.getMaxStomach(fs.player);
        if(stomachDiff > 0)
            eatAmount-=stomachDiff;

        return eatAmount;
    }

    @Override
    public float getFoodMaxWeight(ItemStack is) {
        return foodMaxWeight;
    }

    @Override
    public boolean renderDecay() {
        return true;
    }

    @Override
    public boolean renderWeight() {
        return true;
    }

    @Override
    public float[] getIngredientWeights() {
        return ingredientWeights;
    }

    @Override
    public float getFoodMinWeight() {
        return foodMinWeight;
    }

    @Override
    public int getDamageFromIngredients(ItemStack[] input) {
        return 0;
    }

    protected float getAmountEatenPerCycle() {
        return foodMaxWeight / 2;
    }

}
