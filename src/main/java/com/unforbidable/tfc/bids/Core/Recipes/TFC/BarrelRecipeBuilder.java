package com.unforbidable.tfc.bids.Core.Recipes.TFC;

import com.dunk.tfc.api.Crafting.BarrelAlcoholRecipe;
import com.dunk.tfc.api.Crafting.BarrelLiquidToLiquidRecipe;
import com.dunk.tfc.api.Crafting.BarrelMultiItemRecipe;
import com.dunk.tfc.api.Crafting.BarrelRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class BarrelRecipeBuilder {
    private final BarrelRecipeType type;
    private ItemStack inputItem;
    private ItemStack outputItem;
    private FluidStack inputFluid;
    private FluidStack outputFluid;
    private FluidStack secondaryInputFluid;
    private int minTechLevel = 1;
    private int sealTime = 8;
    private boolean sealed = true;
    private boolean requiresCooked = false;
    private boolean removesLiquid = true;
    private boolean keepStackSize = true;
    private boolean allowAnyStack = true;

    public static BarrelRecipeBuilder asSimple() {
        return new BarrelRecipeBuilder(BarrelRecipeType.SIMPLE);
    }

    public static BarrelRecipeBuilder asAlcohol() {
        return new BarrelRecipeBuilder(BarrelRecipeType.ALCOHOL);
    }

    public static BarrelRecipeBuilder asLiquidToLiquid() {
        return new BarrelRecipeBuilder(BarrelRecipeType.LIQUID_TO_LIQUID);
    }

    public static BarrelRecipeBuilder asMultiItem() {
        return new BarrelRecipeBuilder(BarrelRecipeType.MULTI_ITEM);
    }

    public static BarrelRecipeBuilder asItemDemanding() {
        return new BarrelRecipeBuilder(BarrelRecipeType.ITEM_DEMANDING);
    }

    private BarrelRecipeBuilder(BarrelRecipeType type) {
        this.type = type;
    }

    public BarrelRecipeBuilder consumes(ItemStack input, FluidStack inputFluid) {
        return consumes(input).consumes(inputFluid);
    }

    public BarrelRecipeBuilder consumes(ItemStack input) {
        this.inputItem = input;

        return this;
    }

    public BarrelRecipeBuilder consumes(FluidStack inputFluid) {
        this.inputFluid = inputFluid;

        return this;
    }

    public BarrelRecipeBuilder consumes(FluidStack inputFluid, FluidStack secondaryInputFluid) {
        this.inputFluid = inputFluid;
        this.secondaryInputFluid = secondaryInputFluid;

        return this;
    }

    public BarrelRecipeBuilder produces(ItemStack output, FluidStack outputFluid) {
        return this.produces(output).produces(outputFluid);
    }

    public BarrelRecipeBuilder produces(ItemStack output) {
        this.outputItem = output;

        return this;
    }

    public BarrelRecipeBuilder produces(FluidStack outputFluid) {
        this.outputFluid = outputFluid;

        return this;
    }

    public BarrelRecipeBuilder withMinTechLevel(int minTechLevel) {
        this.minTechLevel = minTechLevel;

        return this;
    }

    public BarrelRecipeBuilder withSealTime(int sealTime) {
        this.sealTime = sealTime;

        return this;
    }

    public BarrelRecipeBuilder beingSealed(boolean sealed) {
        this.sealed = sealed;

        return this;
    }

    public BarrelRecipeBuilder requiringCooked(boolean requiresCooked) {
        this.requiresCooked = requiresCooked;

        return this;
    }

    public BarrelRecipeBuilder removingLiquid(boolean removesLiquid) {
        this.removesLiquid = removesLiquid;

        return this;
    }

    public BarrelRecipeBuilder keepingStackSize(boolean keepStackSize) {
        this.keepStackSize = keepStackSize;

        return this;
    }

    public BarrelRecipeBuilder allowingAnyStack(boolean allowAnyStack) {
        this.allowAnyStack = allowAnyStack;

        return this;
    }

    public BarrelRecipe build() {
        return getRecipeInstance()
            .setMinTechLevel(minTechLevel)
            .setSealedRecipe(sealed)
            .setSealTime(sealTime)
            .setRequiresCooked(requiresCooked)
            .setRemovesLiquid(removesLiquid)
            .setAllowAnyStack(allowAnyStack);
    }

    private BarrelRecipe getRecipeInstance() {
        switch (type) {
            case ALCOHOL:
                return new BarrelAlcoholRecipe(inputItem, inputFluid, outputItem, outputFluid);
            case LIQUID_TO_LIQUID:
                return new BarrelLiquidToLiquidRecipe(inputFluid, secondaryInputFluid, outputFluid);
            case MULTI_ITEM:
                return new BarrelMultiItemRecipe(inputItem, inputFluid, outputItem, outputFluid)
                    .setKeepStackSize(keepStackSize);
            case ITEM_DEMANDING:
                return new BarrelItemDemandingRecipe(inputItem, inputFluid, outputItem, outputFluid);
            case SIMPLE:
            default:
                return new BarrelRecipe(inputItem, inputFluid, outputItem, outputFluid);
        }
    }

    @Override
    public String toString() {
        List<String> inputs = new ArrayList<String>();
        if (inputItem != null)
            inputs.add(inputItem.getDisplayName());
        if (inputFluid != null)
            inputs.add(inputFluid.getFluid().getLocalizedName(inputFluid));
        if (secondaryInputFluid != null)
            inputs.add(secondaryInputFluid.getFluid().getLocalizedName(secondaryInputFluid));

        List<String> outputs = new ArrayList<String>();
        if (outputItem != null)
            outputs.add(outputItem.getDisplayName());
        if (outputFluid != null)
            outputs.add(outputFluid.getFluid().getLocalizedName(outputFluid));

        return String.join(" + ", inputs) + " => " + String.join(" + ", outputs);
    }

    private enum BarrelRecipeType {
        SIMPLE,
        ALCOHOL,
        LIQUID_TO_LIQUID,
        MULTI_ITEM,
        ITEM_DEMANDING,
    }
}
