package com.unforbidable.tfc.bids.features.material.skin.main;

import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import com.unforbidable.tfc.bids.util.nbt.ItemTag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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


    public static ItemStack createTag(ItemStack itemStack, Consumer<SkinTag> apply) {
        return ItemTag.create(itemStack, SkinTag::new, apply);
    }

    public static ItemStack createTag(ItemStack itemStack) {
        return ItemTag.create(itemStack, SkinTag::new, tag -> {});
    }

    public static ItemStack createStack(Item item) {
        return ItemTag.create(new ItemStack(item), SkinTag::new, tag -> {});
    }

    public static ItemStack createStack(Item item, float weight) {
        return ItemTag.create(new ItemStack(item), SkinTag::new, tag -> tag.setWeight(weight));
    }

    public static ItemStack createStack(Item item, float weight, Consumer<SkinTag> apply) {
        return ItemTag.create(new ItemStack(item), SkinTag::new, tag -> { tag.setWeight(weight); apply.accept(tag); });
    }

    public static ItemStack createStack(Item item, float weight, String stage) {
        return ItemTag.create(new ItemStack(item), SkinTag::new, tag -> tag.setStage(stage).setWeight(weight));
    }

    public static ItemStack createStack(Item item, float weight, String stage, Consumer<SkinTag> apply) {
        return ItemTag.create(new ItemStack(item), SkinTag::new, tag -> { tag.setStage(stage).setWeight(weight); apply.accept(tag); });
    }

    public static ItemStack createStack(Item item, String stage) {
        return ItemTag.create(new ItemStack(item), SkinTag::new, tag -> tag.setStage(stage));
    }

    public static ItemStack createStack(Item item, String stage, Consumer<SkinTag> apply) {
        return ItemTag.create(new ItemStack(item), SkinTag::new, tag -> { tag.setStage(stage); apply.accept(tag); });
    public static boolean areItemStacksEqual(ItemStack result, ItemStack output) {
        if (result.getItem() instanceof ItemSkin) {
            SkinTag resultTag = SkinTag.of(result);
            SkinTag outputTag = SkinTag.of(output);
            return result.getItem() == output.getItem() &&
                result.getItemDamage() == output.getItemDamage() &&
                resultTag.isStage(outputTag.getStage());
        } else {
            return ItemStack.areItemStacksEqual(result, output);
        }
    }

}
