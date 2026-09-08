package com.unforbidable.tfc.bids.features.crafting.cooking.main;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.util.food.BidsFoodHeatIndex;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

public class CookingFoodTicker {

    private final TileEntity tileEntity;
    private final int ticksPerCycle;

    public CookingFoodTicker(TileEntity tileEntity, int ticksPerCycle) {
        this.tileEntity = tileEntity;
        this.ticksPerCycle = ticksPerCycle;
    }

    public CookingFoodTickerResult tick(ItemStack inputItem, FluidStack inputFluid, CookingHeatLevel heatLevel, boolean steaming, boolean lid, boolean canProbablyCook) {
        if (inputItem != null && CookingHelper.isCookableFoodItemStack(inputItem)) {
            boolean lastCooked = Food.isCooked(inputItem);
            int lastCookedLevel = CookingHelper.getItemStackCookedLevel(inputItem);
            int requiredFluidAmount = CookingHelper.calculateRequiredCookingFluidAmount(Food.getWeight(inputItem), steaming, lastCookedLevel);

            // Cooking requires heat, input fluid (valid and sufficient amount), and lid when steaming
            // and cooking level cannot exceed the maximum
            boolean canCook = canProbablyCook &&
                heatLevel != CookingHeatLevel.NONE &&
                inputFluid != null && CookingHelper.isValidCookingFluid(inputFluid, steaming) &&
                (!steaming || lid) &&
                inputFluid.amount >= requiredFluidAmount &&
                lastCookedLevel < getMaxCookedLevel(inputItem, heatLevel, steaming);

            // Cooking down requires no heat
            boolean canCoolDown = heatLevel == CookingHeatLevel.NONE;

            if (canCook) {
                float temp = TFC_Climate.getHeightAdjustedTemp(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                handleItemStackHeating(inputItem, temp, heatLevel, steaming);
            } else if (canCoolDown) {
                float temp = TFC_Climate.getHeightAdjustedTemp(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                handleItemStackCooling(inputItem, temp);
            }

            ItemStack[] stacks = new ItemStack[1];
            stacks[0] = inputItem;
            TFC_Core.handleItemTicking(stacks, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 1, 1, canCook);

            if (canCook && tryToMorphStacks(stacks, steaming)) {
                TFC_Core.handleItemTicking(stacks, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 1, 1, true);
            }

            ItemStack cookedInputItem = stacks[0];

            // Make sure item has not decayed or burned
            if (cookedInputItem == null || cookedInputItem.stackSize == 0) {
                return new CookingFoodTickerResult(null, true);
            }

            if (lastCooked != Food.isCooked(cookedInputItem)) {
                // When the food is cooked for the first time alter the taste
                CookingHelper.setInputStackCookedNBT(cookedInputItem, inputFluid, steaming);
            }

            if (canCook && lastCookedLevel != CookingHelper.getItemStackCookedLevel(cookedInputItem)) {
                // Whenever the cooked level is raised some liquid is consumed
                inputFluid.amount -= requiredFluidAmount;

                Bids.LOG.debug("Consumed input liquid amount: " + requiredFluidAmount);

                return new CookingFoodTickerResult(cookedInputItem, true);
            }
        }

        return new CookingFoodTickerResult(inputItem, false);
    }

    private boolean tryToMorphStacks(ItemStack[] stacks, boolean steaming) {
        HeatIndex hi = HeatRegistry.getInstance().findMatchingIndex(stacks[0]);
        if (hi instanceof BidsFoodHeatIndex) {
            BidsFoodHeatIndex bidsFoodHeatIndex = (BidsFoodHeatIndex) hi;

            float temp = TFC_ItemHeat.getTemp(stacks[0]);
            if (temp > bidsFoodHeatIndex.meltTemp) {
                if (!steaming && bidsFoodHeatIndex.hasBoilingOutput()) {
                    bidsFoodHeatIndex.getBoilingOutput(stacks[0]);

                    // Boiling adds weight due to water absorption
                    if (stacks[0].getItem() instanceof ItemFoodTFC) {
                        float weight = Food.getWeight(stacks[0]);
                        float newWeight = Math.min(160f, weight * 1.2f);
                        Food.setWeight(stacks[0], newWeight);
                    }

                    return true;
                }

                if (steaming && bidsFoodHeatIndex.hasSteamingOutput()) {
                    bidsFoodHeatIndex.getSteamingOutput(stacks[0]);

                    return true;
                }
            }
        }

        return false;
    }

    private int getMaxCookedLevel(ItemStack itemStack, CookingHeatLevel heatLevel, boolean steaming) {
        int cookedTempIndex = ((ItemFoodTFC) itemStack.getItem()).cookTempIndex;
        if (heatLevel == CookingHeatLevel.LOW || steaming) {
            // Cooking with low heat or steaming - Medium / Light
            return cookedTempIndex == 0 ? 3 : 2;
        } else {
            // Cooking with medium or high heat (not steaming) - Well done / Medium
            return cookedTempIndex == 0 ? 5 : 3;
        }
    }

    private void handleItemStackCooling(ItemStack itemStack, float environmentTemp) {
        float temp = TFC_ItemHeat.getTemp(itemStack);
        if (temp == 0) {
            temp = environmentTemp;
        }

        int cookedTempIndex = ((ItemFoodTFC) itemStack.getItem()).cookTempIndex;
        float cookedTemp = Food.globalCookTemps[cookedTempIndex];

        if (Math.abs(environmentTemp - temp) < cookedTemp) {
            // Jump to target at a threshold
            temp = environmentTemp;
        } else {
            // Cool down fast in a liquid that is cooling down too
            float bonus = 10f;
            if (environmentTemp > temp) {
                temp += (TFC_ItemHeat.getTempIncrease(itemStack, environmentTemp) * ticksPerCycle * bonus);
                temp = Math.min(environmentTemp, temp);
            } else {
                temp -= (TFC_ItemHeat.getTempDecrease(itemStack, environmentTemp) * ticksPerCycle * bonus);
                temp = Math.max(environmentTemp, temp);
            }
        }

        TFC_ItemHeat.setTemp(itemStack, temp, true);
    }

    private void handleItemStackHeating(ItemStack itemStack, float environmentTemp, CookingHeatLevel heatLevel, boolean steaming) {
        float temp = TFC_ItemHeat.getTemp(itemStack);
        if (temp == 0) {
            temp = environmentTemp;
        }

        float targetTemp = getTargetTemp(heatLevel);

        // Steaming takes longer
        if (steaming) {
            targetTemp *= 0.75;
        }

        // Cooking a large stack of food with LOW heat level will still take a while though
        float bonus = 1 + (1 - Food.getWeight(itemStack) / Global.FOOD_MAX_WEIGHT) * 2;

        if (targetTemp > temp) {
            temp += (TFC_ItemHeat.getTempIncrease(itemStack, targetTemp) * ticksPerCycle * bonus);
        } else {
            temp -= (TFC_ItemHeat.getTempDecrease(itemStack, targetTemp) * ticksPerCycle);
        }

        TFC_ItemHeat.setTemp(itemStack, temp, true);
    }

    private float getTargetTemp(CookingHeatLevel level) {
        switch (level) {
            case HIGH:
                return 400;
            case MEDIUM:
                return 300;
            case LOW:
                return 250;
        }

        return 0;
    }

}
