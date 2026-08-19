package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BarrelRecipe {

    public final BarrelRecipeType type;
    public final ItemStack inputItem;
    public final ItemStack outputItem;
    public final FluidStack inputFluid;
    public final FluidStack outputFluid;
    public final FluidStack secondaryInputFluid;
    public final int minTechLevel;
    public final int sealTime;
    public final boolean sealed;
    public final boolean requiresCooked;
    public final boolean removesLiquid;
    public final boolean keepStackSize;
    public final boolean allowAnyStack;

    public BarrelRecipe(BarrelRecipeType type,
                        ItemStack inputItem, ItemStack outputItem,
                        FluidStack inputFluid, FluidStack outputFluid, FluidStack secondaryInputFluid,
                        int minTechLevel, int sealTime,
                        boolean sealed, boolean requiresCooked, boolean removesLiquid, boolean keepStackSize, boolean allowAnyStack) {
        this.type = type;
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.secondaryInputFluid = secondaryInputFluid;
        this.minTechLevel = minTechLevel;
        this.sealTime = sealTime;
        this.sealed = sealed;
        this.requiresCooked = requiresCooked;
        this.removesLiquid = removesLiquid;
        this.keepStackSize = keepStackSize;
        this.allowAnyStack = allowAnyStack;
    }

    public static RegistryActor<BarrelRecipe> add(Consumer<BarrelRecipeBuilder> consumer) {
        return new RegistryAddingActor<>(BarrelRecipeStage.instance, buildRecipe(BarrelRecipeType.SIMPLE, consumer));
    }

    public static RegistryActor<BarrelRecipe> addAlcohol(Consumer<BarrelRecipeBuilder> consumer) {
        return new RegistryAddingActor<>(BarrelRecipeStage.instance, buildRecipe(BarrelRecipeType.ALCOHOL, consumer));
    }

    public static RegistryActor<BarrelRecipe> addItemDemanding(Consumer<BarrelRecipeBuilder> consumer) {
        return new RegistryAddingActor<>(BarrelRecipeStage.instance, buildRecipe(BarrelRecipeType.ITEM_DEMANDING, consumer));
    }

    public static RegistryActor<BarrelRecipe> addFoodHandling(Consumer<BarrelRecipeBuilder> consumer) {
        return new RegistryAddingActor<>(BarrelRecipeStage.instance, buildRecipe(BarrelRecipeType.FOOD_HANDLING, consumer));
    }

    public static RegistryActor<BarrelRecipe> addMultiItem(Consumer<BarrelRecipeBuilder> consumer) {
        return new RegistryAddingActor<>(BarrelRecipeStage.instance, buildRecipe(BarrelRecipeType.MULTI_ITEM, consumer));
    }

    public static RegistryActor<BarrelRecipe> addLiquidToLiquid(Consumer<BarrelRecipeBuilder> consumer) {
        return new RegistryAddingActor<>(BarrelRecipeStage.instance, buildRecipe(BarrelRecipeType.LIQUID_TO_LIQUID, consumer));
    }

    private static BarrelRecipe buildRecipe(BarrelRecipeType type, Consumer<BarrelRecipeBuilder> consumer) {
        BarrelRecipeBuilder b = BarrelRecipeBuilder.ofType(type);
        consumer.accept(b);
        return b.build();
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

}
