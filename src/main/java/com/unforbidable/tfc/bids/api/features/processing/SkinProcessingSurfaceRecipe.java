package com.unforbidable.tfc.bids.api.features.processing;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import net.minecraft.item.ItemStack;

public class SkinProcessingSurfaceRecipe extends ProcessingSurfaceRecipe {

    public SkinProcessingSurfaceRecipe(ItemStack input, ItemStack output, String toolOreName, String surfaceBlockOreName, float effort) {
        super(input, output, toolOreName, surfaceBlockOreName, effort);
    }

    @Override
    public boolean matchesInput(ItemStack ingredient) {
        return super.matchesInput(ingredient) &&
            matchesSkin(ingredient);
    }

    private boolean matchesSkin(ItemStack ingredient) {
        SkinTag ingredientTag = SkinTag.of(ingredient);
        SkinTag inputTag = SkinTag.of(getInput());

        return ingredientTag.isStage(inputTag.getStage());
    }

    @Override
    public float getEffort(ItemStack itemStack) {
        return super.getEffort(itemStack) * SkinTag.of(itemStack).getWeight() / 16 * getSoakedEffortMultiplier(itemStack);
    }

    private float getSoakedEffortMultiplier(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_PREPARED)) {
            if (tag.isFluid(TFCFluids.LIMEWATER.getName()) || tag.isFluid(BidsFluids.weakWoodAshLye.getName())) {
                return 0.25f;
            }
        }

        return 1f;
    }

    @Override
    public ItemStack getResult(ItemStack ingredient) {
        SkinTag ingredientTag = SkinTag.of(ingredient);

        ItemStack result = super.getResult(ingredient);
        SkinTag resultTag = SkinTag.of(result);

        if (ingredientTag.hasAnimal()) {
            // Only set the animal if source skin has it
            // otherwise keep result animal if any
            // This is important for dehairing skins of specific animals into generic dehaired skins
            resultTag.setAnimal(ingredientTag.getAnimal());
        }

        resultTag.setFluid(ingredientTag.getFluid());
        resultTag.setWeight(ingredientTag.getWeight());
        resultTag.setDecay(ingredientTag.getDecay());
        resultTag.setDecayTimer(ingredientTag.getDecayTimer());

        return result;
    }

}
