package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.Player.FoodStatsTFC;
import com.dunk.tfc.Food.ItemSandwich;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemStuffedPepper extends ItemSandwich {

    public static final float[] STUFFED_PEPPER_WEIGHTS = new float[] { 3, 6, 4, 2, 1 };
    public static final float STUFFED_PEPPER_MIN_WEIGHT = 10;
    public static final float STUFFED_PEPPER_MAX_WEIGHT = 16;

    public ItemStuffedPepper() {
        this.hasSubtypes = true;
        this.metaNames = new String[]{"Stuffed Pepper.Green", "Stuffed Pepper.Yellow", "Stuffed Pepper.Red"};
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        metaIcons = new IIcon[metaNames.length];
        for (int i = 0; i < metaNames.length; i++) {
            metaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "food/" + metaNames[i]);
        }

        this.itemIcon = metaIcons[0];
    }

    @Override
    protected float getFillingBoost() {
        return 1.1f;
    }

    @Override
    protected float[] getFoodWeights() {
        return STUFFED_PEPPER_WEIGHTS;
    }

    @Override
    protected float getEatAmount(FoodStatsTFC fs, float amount) {
        float eatAmount = Math.min(amount, STUFFED_PEPPER_MAX_WEIGHT / 2);
        float stomachDiff = fs.stomachLevel+eatAmount-fs.getMaxStomach(fs.player);
        if(stomachDiff > 0)
            eatAmount-=stomachDiff;

        return eatAmount;
    }

    @Override
    public float getFoodMaxWeight(ItemStack is) {
        return STUFFED_PEPPER_MAX_WEIGHT;
    }

    @Override
    public boolean renderDecay() {
        return true;
    }

    @Override
    public boolean renderWeight() {
        return true;
    }

}
