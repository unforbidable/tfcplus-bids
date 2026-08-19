package com.unforbidable.tfc.bids.features.material.skin.crafting;

import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class SkinSaltingRecipe implements IRecipe {

    private final ItemStack input;
    private final ItemStack output;
    private final ItemStack ingredient;

    private final SkinTag inputTag;

    public SkinSaltingRecipe(ItemStack input, ItemStack output, ItemStack ingredient) {
        this.input = input;
        this.inputTag = SkinTag.of(input);
        this.output = output;
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        int skinsFound = 0;
        int ingredientsFound = 0;
        for (int i = 0; i < 9; i++) {
            if (inventoryCrafting.getStackInSlot(i) != null) {
                ItemStack is = inventoryCrafting.getStackInSlot(i);
                if (is.getItem() == input.getItem()) {
                    SkinTag tag = SkinTag.of(is);
                    if (tag.isStage(inputTag.getStage())) {
                        skinsFound++;
                    } else {
                        // Wrong skin stage
                        return false;
                    }
                } else if (is.getItem() == ingredient.getItem() && is.getItemDamage() == ingredient.getItemDamage()) {
                    ingredientsFound++;
                } else {
                    // Something other than skin or knife
                    return false;
                }
            }
        }

        // just enough skins and knives?
        return (ingredientsFound == 1) && skinsFound == 1;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        for (int i = 0; i < 9; i++) {
            if (inventoryCrafting.getStackInSlot(i) != null) {
                ItemStack is = inventoryCrafting.getStackInSlot(i);
                if (is.getItem() instanceof ItemSkin) {
                    ItemStack result = output.copy();

                    SkinTag tag = SkinTag.of(result);
                    tag.setWeight(inputTag.getWeight());
                    tag.setStage(inputTag.getStage());

                    tag.setSalted();

                    return result;
                }
            }
        }

        return null;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack result = output.copy();

        SkinTag tag = SkinTag.of(result);
        tag.setWeight(inputTag.getWeight());
        tag.setStage(inputTag.getStage());
        tag.setSalted();

        return result;
    }

}
