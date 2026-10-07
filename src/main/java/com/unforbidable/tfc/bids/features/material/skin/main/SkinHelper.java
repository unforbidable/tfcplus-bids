package com.unforbidable.tfc.bids.features.material.skin.main;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import com.unforbidable.tfc.bids.util.nbt.ItemTag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import java.util.function.Consumer;

public class SkinHelper {

    public static final float WEIGHT_SMALL = 16f;
    public static final float WEIGHT_MEDIUM = WEIGHT_SMALL * 2f;
    public static final float WEIGHT_LARGE = WEIGHT_SMALL * 4f;
    public static final float WEIGHT_VERY_SMALL = WEIGHT_SMALL * 0.5f;
    public static final float WEIGHT_TINY = WEIGHT_SMALL / 16f;

    public static final float SKIN_MIN_SIZE_BONUS = 1.25f;
    public static final float SKIN_MAX_SIZE_BONUS = 1.75f;

    public static final float SKIN_MAX_WEIGHT = 160;

    public static final float BASE_DECAY_RATE = 16f;

    public static final float WEIGHT_CUT_REQUIRED = 15 / 16f;


    public static ItemStack createTag(ItemStack itemStack, Consumer<SkinTag> apply) {
        return ItemTag.create(itemStack, SkinTag::new, tag -> { tag.setWeight(SKIN_MAX_WEIGHT); apply.accept(tag); });
    }

    public static ItemStack createTag(ItemStack itemStack) {
        return createTag(itemStack, tag -> {});
    }

    public static ItemStack createStack(Item item) {
        return createTag(new ItemStack(item));
    }

    public static ItemStack createStack(Item item, Consumer<SkinTag> apply) {
        return createTag(new ItemStack(item), apply);
    }

    public static ItemStack createStack(Item item, float weight) {
        return createTag(new ItemStack(item), tag -> tag.setWeight(weight));
    }

    public static ItemStack createStack(Item item, float weight, Consumer<SkinTag> apply) {
        return createTag(new ItemStack(item), tag -> { tag.setWeight(weight); apply.accept(tag); });
    }

    public static ItemStack createStack(Item item, float weight, String stage) {
        return createTag(new ItemStack(item), tag -> tag.setStage(stage).setWeight(weight));
    }

    public static ItemStack createStack(Item item, float weight, String stage, Consumer<SkinTag> apply) {
        return createTag(new ItemStack(item), tag -> { tag.setStage(stage).setWeight(weight); apply.accept(tag); });
    }

    public static ItemStack createStack(Item item, String stage) {
        return createTag(new ItemStack(item), tag -> tag.setStage(stage));
    }

    public static ItemStack createStack(Item item, String stage, Consumer<SkinTag> apply) {
        return createTag(new ItemStack(item), tag -> { tag.setStage(stage); apply.accept(tag); });
    }

    public static CookingRecipe adaptSkinSoakingRecipeForCookingPot(SoakingRecipe source) {
        if (source.input != null && source.input.getItem() instanceof ItemSkin &&
            source.output != null && source.output.getItem() instanceof ItemSkin) {

            // Cooking recipes are scaled
            ItemStack input = source.input.copy();
            SkinTag.of(input).setWeight(1);
            ItemStack output = source.output.copy();
            SkinTag.of(output).setWeight(1);
            FluidStack fluid = new FluidStack(source.fluid.getFluid(), 10);

            return CookingRecipe.builder()
                .consumes(input)
                .consumes(fluid)
                .produces(output)
                .inFixedTime(source.ticks)
                .build();
        }

        return null;
    }

}
