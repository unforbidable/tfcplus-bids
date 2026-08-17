package com.unforbidable.tfc.bids.features.material.skin.crafting;

import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.List;

public class SkinMergingRecipe implements IRecipe {

    private final ItemStack input;
    private final String toolOreName;
    private final float maxWeight;

    private final SkinTag inputTag;

    public SkinMergingRecipe(ItemStack input, String toolOreName, float maxWeight) {
        this.input = input;
        this.inputTag = SkinTag.of(input);
        this.toolOreName = toolOreName;
        this.maxWeight = maxWeight;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        int skinsFound = 0;
        int toolsFound = 0;
        float totalWeight = 0;
        for (int i = 0; i < 9; i++) {
            if (inventoryCrafting.getStackInSlot(i) != null) {
                ItemStack is = inventoryCrafting.getStackInSlot(i);
                if (is.getItem() == input.getItem()) {
                    SkinTag tag = SkinTag.of(is);
                    if (tag.isStage(inputTag.getStage())) {
                        skinsFound++;
                        totalWeight += tag.getWeight();
                    } else {
                        // Wrong skin stage or size
                        return false;
                    }
                } else if (toolOreName != null && OreDictionaryHelper.itemStackIsOre(is, toolOreName)) {
                    toolsFound++;
                } else {
                    // Something other than skin or knife
                    return false;
                }
            }
        }

        // just enough skins and knives?
        return (toolOreName == null || toolsFound == 1) && skinsFound > 1 && (maxWeight == SkinHelper.SKIN_MAX_WEIGHT || totalWeight <= maxWeight);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        float totalWeight = 0;
        ItemStack first = null;
        List<String> animals = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (inventoryCrafting.getStackInSlot(i) != null) {
                ItemStack is = inventoryCrafting.getStackInSlot(i);
                if (is.getItem() instanceof ItemSkin) {
                    float weight = SkinTag.of(is).getWeight();
                    totalWeight += weight;

                    if (first == null) {
                        first = is;
                    }

                    animals.add(SkinTag.of(is).getAnimal());
                }
            }
        }

        if (first != null) {
            ItemStack result = first.copy();

            SkinTag.of(result)
                .setAnimal(getSingleAnimalMatchingAllFromList(animals))
                .setWeight(Math.min(totalWeight, SkinHelper.SKIN_MAX_WEIGHT));

            return result;
        }

        return null;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return input.copy();
    }

    private String getSingleAnimalMatchingAllFromList(List<String> animals) {
        if (animals.size() > 0) {
            String first = animals.get(0);
            for (int i = 1; i < animals.size(); i++) {
                // Not matching animal tags cause any animal tag to be removed
                if (!first.equals(animals.get(i))) {
                    return null;
                }
            }

            return first;
        }

        return null;
    }

}
