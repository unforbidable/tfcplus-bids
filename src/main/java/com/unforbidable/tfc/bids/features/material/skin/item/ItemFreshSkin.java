package com.unforbidable.tfc.bids.features.material.skin.item;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFreshSkin extends ItemSkin {

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        if (this == BidsItems.genericFur) {
            list.add(SkinHelper.createStack(this, 4, SkinTagAccess.STAGE_PRESERVED));
            list.add(SkinHelper.createStack(this, 4, SkinTagAccess.STAGE_PRESERVED, tag -> tag.setAnimal("deerTFC")));
            list.add(SkinHelper.createStack(this, 4, SkinTagAccess.STAGE_PRESERVED, tag -> tag.setAnimal("bisonTFC")));
        }

        String animal = this == BidsItems.genericFur ? "deerTFC" : (this == BidsItems.genericSkin ? "pigTFC" : null);

        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_SMALL, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_MEDIUM, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_FLESHED, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN, tag -> tag.setAnimal(animal).setSalted()));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_PRESERVED, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_PREPARED, tag -> tag.setAnimal(animal)));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_PREPARED, tag -> tag.setAnimal(animal).setFluid(TFCFluids.LIMEWATER.getName())));
    }

    @Override
    public ItemStack getSpecialCraftingItemStack(ItemStack itemStack) {
        if (SkinTag.of(itemStack).isStage(SkinTagAccess.STAGE_PRESERVED)) {
            return super.getSpecialCraftingItemStack(itemStack);
        }

        return null;
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int pass) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_FLESHED) || tag.isStage("")) {
            return 0xffeecc;
        }

        if (tag.isStage(SkinTagAccess.STAGE_PREPARED) || tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            return 0xccccaa;
        }

        return 0xffffff;
    }

    @Override
    public float getDecayRate(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_PRESERVED)) {
            return 0;
        }

        float saltedModifier = tag.isSalted() ? 0.25f : 1f;
        float stageModifier = getSkinProcessingStageDecayMultiplier(itemStack);
        float fluidModifier = getFluidDecayMultiplier(itemStack);

        return super.getDecayRate(itemStack) * saltedModifier * stageModifier * fluidModifier;
    }

    protected float getSkinProcessingStageDecayMultiplier(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if (tag.isStage(SkinTagAccess.STAGE_FLESHED)) {
            return 1f / 4;
        }
        if (tag.isStage(SkinTagAccess.STAGE_CLEAN) || tag.isStage(SkinTagAccess.STAGE_PREPARED) || tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) {
            return 1f / 8;
        }
        if (tag.isStage(SkinTagAccess.STAGE_TANNED)) {
            return 1f / 64;
        }

        return 1f;
    }

    protected float getFluidDecayMultiplier(ItemStack itemStack) {
        SkinTag tag = SkinTag.of(itemStack);
        if ((tag.isStage(SkinTagAccess.STAGE_PREPARED) || tag.isStage(SkinTagAccess.STAGE_DEHAIRED)) &&
            (tag.isFluid(TFCFluids.LIMEWATER.getName()) || tag.isFluid(BidsFluids.weakWoodAshLye.getName()))) {
            return 1f / 8;
        }

        return 1f;
    }

}
