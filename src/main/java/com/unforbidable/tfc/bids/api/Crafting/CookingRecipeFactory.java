package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Enums.EnumCookingAccessory;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingLidUsage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipeFactory {

    public static CookingRecipe createCookingRecipe(FluidStack inputFluidStack, FluidStack outputFluidStack, boolean lidUsed, EnumCookingHeatLevel minHeatLevel, EnumCookingHeatLevel maxHeatLevel, float time) {
        return new CookingRecipe(inputFluidStack, outputFluidStack, null, null, null, EnumCookingAccessory.NONE, lidUsed ? EnumCookingLidUsage.ON : EnumCookingLidUsage.OFF, minHeatLevel, maxHeatLevel, time);
    }

    public static CookingRecipe createCookingRecipe(FluidStack inputFluidStack, FluidStack outputFluidStack, boolean lidUsed, EnumCookingHeatLevel heatLevel, float time) {
        return new CookingRecipe(inputFluidStack, outputFluidStack, null, null, null, EnumCookingAccessory.NONE, lidUsed ? EnumCookingLidUsage.ON : EnumCookingLidUsage.OFF, heatLevel, heatLevel, time);
    }

    public static CookingRecipe createBoilingRecipe(FluidStack inputFluidStack, ItemStack inputItemStack, ItemStack outputItemStack, float time) {
        return new CookingRecipe(inputFluidStack, null, null, inputItemStack, outputItemStack, EnumCookingAccessory.NONE, null, EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.HIGH, time);
    }

    public static CookingRecipe createSteamingRecipe(FluidStack inputFluidStack, ItemStack inputItemStack, ItemStack outputItemStack, float time) {
        return new CookingRecipe(inputFluidStack, null, null, inputItemStack, outputItemStack, EnumCookingAccessory.STEAMING_MESH, EnumCookingLidUsage.ON, EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.HIGH, time);
    }

    public static CookingRecipe createSoakingRecipe(FluidStack inputFluidStack, ItemStack inputItemStack, ItemStack outputItemStack, float time) {
        return new CookingRecipe(inputFluidStack, null, null, inputItemStack, outputItemStack, EnumCookingAccessory.NONE, null, null, null, time);
    }

    public static CookingRecipe createDissolvingRecipe(FluidStack inputFluidStack, FluidStack outputFluidStack, ItemStack inputItemStack, float time) {
        return new CookingRecipe(inputFluidStack, outputFluidStack, null, inputItemStack, null, EnumCookingAccessory.NONE, null, null, null, time);
    }

    public static CookingRecipe createMeltingRecipe(FluidStack outputFluidStack, ItemStack inputItemStack, float time) {
        return new CookingRecipe(null, outputFluidStack, null, inputItemStack, null, EnumCookingAccessory.NONE, null, EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.HIGH, time);
    }

    public static CookingRecipe createEvaporatingRecipe(FluidStack inputFluidStack, ItemStack outputItemStack, float time) {
        return new CookingRecipe(inputFluidStack, null, null, null, outputItemStack, EnumCookingAccessory.NONE, EnumCookingLidUsage.OFF, EnumCookingHeatLevel.LOW, EnumCookingHeatLevel.HIGH, time);
    }

    public static CookingRecipe createSolidifyingRecipe(FluidStack inputFluidStack, ItemStack outputItemStack, float time) {
        return new CookingRecipe(inputFluidStack, null, null, null, outputItemStack, EnumCookingAccessory.NONE, null, null, null, time);
    }

    public static CookingRecipe createFluidSeparatingRecipe(FluidStack inputFluidStack, FluidStack heavyOutputFluidStack, FluidStack lightOutputFluidStack, float time) {
        return new CookingRecipe(inputFluidStack, heavyOutputFluidStack, lightOutputFluidStack, null, null, EnumCookingAccessory.NONE, null, null, null, time);
    }

}
