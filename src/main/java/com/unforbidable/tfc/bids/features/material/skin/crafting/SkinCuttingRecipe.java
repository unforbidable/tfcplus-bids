package com.unforbidable.tfc.bids.features.material.skin.crafting;

import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class SkinCuttingRecipe implements IRecipe {

    private final ItemStack input;
    private final ItemStack largeOutput;
    private final ItemStack mediumOutput;
    private final ItemStack smallOutput;
    private final ItemStack verySmallOutput;
    private final ItemStack tinyOutput;
    private final String toolOreName;

    private final SkinTag inputTag;
    private final float minWeight;

    public SkinCuttingRecipe(ItemStack input, String toolOreName, Item regular, ItemStack verySmall, ItemStack tiny) {
        this(input, toolOreName,
            new ItemStack(regular, 1, 2),
            new ItemStack(regular, 1, 1),
            new ItemStack(regular, 1, 0),
            verySmall,
            tiny);
    }

    public SkinCuttingRecipe(ItemStack input, String toolOreName, ItemStack... output) {
        this.input = input;
        this.inputTag = SkinTag.of(input);
        this.toolOreName = toolOreName;

        if (output.length != 5) {
            throw new RuntimeException("SkinCuttingRecipe requires 5 output item stacks");
        }

        this.largeOutput = output[0];
        this.mediumOutput = output[1];
        this.smallOutput = output[2];
        this.verySmallOutput = output[3];
        this.tinyOutput = output[4];

        if (tinyOutput != null) {
            this.minWeight = 1;
        } else if (verySmallOutput != null) {
            this.minWeight = SkinHelper.WEIGHT_VERY_SMALL;
        } else {
            this.minWeight = SkinHelper.WEIGHT_SMALL;
        }
    }

    public boolean hasLargeOutput() {
        return largeOutput != null;
    }

    public boolean hasMediumOutput() {
        return mediumOutput != null;
    }

    public boolean hasSmallOutput() {
        return smallOutput != null;
    }

    public boolean hasTinyOutput() {
        return verySmallOutput != null;
    }

    public boolean hasRepairPatchOutput() {
        return tinyOutput != null;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        int skinsFound = 0;
        int toolsFound = 0;
        for (int i = 0; i < 9; i++) {
            if (inventoryCrafting.getStackInSlot(i) != null) {
                ItemStack is = inventoryCrafting.getStackInSlot(i);
                if (is.getItem() == input.getItem()) {
                    SkinTag tag = SkinTag.of(is);
                    if (tag.isStage(inputTag.getStage()) && tag.getWeight() >= minWeight) {
                        skinsFound++;
                    } else {
                        // Wrong skin stage or not enough weight
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
        return (toolOreName == null || toolsFound == 1) && skinsFound == 1;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        for (int i = 0; i < 9; i++) {
            if (inventoryCrafting.getStackInSlot(i) != null) {
                ItemStack is = inventoryCrafting.getStackInSlot(i);
                if (is.getItem() instanceof ItemSkin) {
                    float weight = SkinTag.of(is).getWeight();

                    if (largeOutput != null && weight >= SkinHelper.WEIGHT_LARGE) {
                        return largeOutput.copy();
                    } else if (mediumOutput != null && weight >= SkinHelper.WEIGHT_MEDIUM) {
                        return mediumOutput.copy();
                    } else if (smallOutput != null && weight >= SkinHelper.WEIGHT_SMALL) {
                        return smallOutput.copy();
                    } else if (verySmallOutput != null && weight >= SkinHelper.WEIGHT_VERY_SMALL) {
                        return verySmallOutput.copy();
                    } else if (tinyOutput != null) {
                        ItemStack repairPatches = tinyOutput.copy();
                        repairPatches.stackSize = Math.round(weight / SkinHelper.WEIGHT_TINY);
                        return repairPatches;
                    }

                    return null;
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
        if (largeOutput != null) {
            return largeOutput.copy();
        } else if (mediumOutput != null) {
            return mediumOutput.copy();
        } else if (smallOutput != null) {
            return smallOutput.copy();
        } else if (verySmallOutput != null) {
            return verySmallOutput.copy();
        } else if (tinyOutput != null) {
            return tinyOutput.copy();
        }

        return null;
    }

}
