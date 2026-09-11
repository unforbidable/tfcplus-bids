package com.unforbidable.tfc.bids.features.crafting.cooking.main;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHost;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeCraftingResult;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.common.fluid.FluidDilutable;
import com.unforbidable.tfc.bids.util.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class CookingEngine {

    private static final int RECIPE_HANDLE_INTERVAL = 20;
    private static final int RECIPE_HANDLE_INTERVAL_AFTER_PARAMETER_CHANGE = 5;

    private final CookingHost host;

    private CookingRecipe cachedRecipe = null;
    private boolean isCachedRecipeValid = false;
    private CookingRecipeProgress recipeProgress = null;

    private boolean recipeCanPauseWhenParametersChange = true;

    private final Timer recipeHandleTimer = new Timer(RECIPE_HANDLE_INTERVAL);

    public CookingEngine(CookingHost host) {
        this.host = host;
    }

    public void onRecipeParametersChanged(boolean cancelRecipe) {
        Bids.LOG.debug("Recipe parameters changed - recipe cancellation: " + cancelRecipe);

        isCachedRecipeValid = false;

        // By default, recipes can pause when parameters change
        // unless a change occurred that cancels the recipe
        if (cancelRecipe) {
            recipeProgress = null;
        }

        // Handle the recipe progress after delay
        recipeHandleTimer.delay(RECIPE_HANDLE_INTERVAL_AFTER_PARAMETER_CHANGE);
    }

    public CookingRecipeProgress getRecipeProgress() {
        return recipeProgress;
    }

    private CookingRecipe getCachedRecipe() {
        if (!isCachedRecipeValid) {
            isCachedRecipeValid = true;
            cachedRecipe = null;

            CookingRecipeInputTemplate template = host.createRecipeTemplate();
            for (CookingRecipe recipe : CookingHelper.getRecipesMatchingTemplate(template)) {
                // Ensure host allows the recipe and
                // that the recipe can run - checking the amounts
                if (host.isValidCookingRecipe(recipe) && calculateTotalRecipeRuns(recipe) > 0) {
                    cachedRecipe = recipe;
                    break;
                }
            }
        }

        return cachedRecipe;
    }

    public boolean cancelRecipeProgress() {
        isCachedRecipeValid = false;
        recipeCanPauseWhenParametersChange = false;

        if (recipeProgress != null) {
            recipeProgress = null;

            return true;
        }

        return false;
    }

    public boolean update() {
        if (recipeHandleTimer.tick()) {
            return handleRecipeProgress();
        }

        return false;
    }

    public boolean handleRecipeProgress() {
        boolean update = false;

        CookingRecipe recipe = getCachedRecipe();

        if (recipeProgress == null) {
            // No recipe is in progress
            // see if we can start one
            if (recipe != null) {
                String displayText = CookingRecipeHelper.getRecipeOutputDisplayText(recipe, host.createRecipeTemplate());
                String hashString = CookingRecipeHelper.getRecipeHashString(recipe);
                int totalRuns = calculateTotalRecipeRuns(recipe);

                recipeProgress = new CookingRecipeProgress(displayText, hashString, totalRuns);
                update = true;

                Bids.LOG.debug("Recipe progress started: " + recipeProgress.getOutputHashString());
            }
        }

        if (recipeProgress != null) {
            // A recipe is in progress
            // see if the progress matches the current recipe
            if (recipe != null &&
                recipeProgress.getTotalRuns() == calculateTotalRecipeRuns(recipe) &&
                recipeProgress.getOutputHashString().equals(CookingRecipeHelper.getRecipeHashString(recipe))) {
                // Resume paused recipe if it has been paused
                if (recipeProgress.isProgressPaused()) {
                    Bids.LOG.debug("Recipe progress resumed: " + recipeProgress.getOutputHashString());

                    recipeProgress.setProgressPaused(false);

                    update = true;
                }

                int lastProgressRounded = recipeProgress.getProgressRounded();

                // Determine how much progress is made
                long totalRecipeTicks = (long) Math.ceil(recipe.getTime() * (recipe.isFixedTime() ? 1 : recipeProgress.getTotalRuns()));

                Bids.LOG.debug("runs: " + recipeProgress.getTotalRuns());
                Bids.LOG.debug("totalRecipeTicks: " + totalRecipeTicks);

                if (totalRecipeTicks > 0) {
                    long lastUpdateTicks = recipeProgress.getLastUpdateTicks();
                    long elapsedTicks = TFC_Time.getTotalTicks() - lastUpdateTicks;
                    float progress = (float) elapsedTicks / totalRecipeTicks;
                    float heatAdjustedProgress = adjustProgressForHeat(recipe, host.getHeatLevel(), progress);
                    recipeProgress.addProgress(heatAdjustedProgress);
                } else {
                    // For recipe without time the progress is completed immediately
                    recipeProgress.addProgress(1f);
                }

                if (recipeProgress.getProgress() == 1f) {
                    Bids.LOG.debug("Recipe progress completed: " + recipeProgress.getOutputHashString());

                    int runs = calculateTotalRecipeRuns(recipe);
                    CookingRecipeInputTemplate template = host.createRecipeTemplate();
                    CookingRecipeCraftingResult result = recipe.getCraftingResult(template);
                    handleRecipeOutput(recipe, template, result, runs);

                    recipeProgress = null;

                    // Update the cached recipe that may be invalid after completion
                    isCachedRecipeValid = false;
                    recipe = getCachedRecipe();

                    update = true;
                } else {
                    Bids.LOG.debug("Recipe progress updated: " + recipeProgress.getOutputHashString() + " - " + recipeProgress.getProgress());

                    // If output changes for any reason, update the display text
                    String displayText = CookingRecipeHelper.getRecipeOutputDisplayText(recipe, host.createRecipeTemplate());
                    recipeProgress.setOutputDisplayText(displayText);

                    // Only send update to the client when the rounded progress changes
                    if (lastProgressRounded != recipeProgress.getProgressRounded()) {
                        update = true;
                    }
                }
            } else {
                // When the recipe in progress does not match because
                // sometimes the progress can be paused, depending on the kind of changes
                // This prevents progress being lost, when heat changes or lid is manipulated,
                // when substantial progress has been made
                // Substantial progress means the recipe has run for a certain amount of time
                // This allows melting tallow to be cancelled even when almost done
                if (recipeCanPauseWhenParametersChange && TFC_Time.getTotalTicks() - recipeProgress.getStartTicks() > 500) {
                    if (!recipeProgress.isProgressPaused()) {
                        Bids.LOG.debug("Recipe progress paused: " + recipeProgress.getOutputHashString());

                        recipeProgress.setProgressPaused(true);

                        update = true;
                    }
                } else {
                    Bids.LOG.debug("Recipe progress cancelled: " + recipeProgress.getOutputHashString());

                    recipeProgress = null;

                    update = true;
                }
            }
        }

        // By default, pause recipe unless explicitly stated otherwise
        recipeCanPauseWhenParametersChange = true;

        return update;
    }

    private float adjustProgressForHeat(CookingRecipe recipe, CookingHeatLevel heatLevel, float progress) {
        if (recipe.getMinHeatLevel() == null || recipe.getMinHeatLevel() == CookingHeatLevel.NONE) {
            return progress;
        }

        switch (heatLevel) {
            case LOW:
                return progress * 0.6f;

            case MEDIUM:
                return progress * 1.2f;

            case HIGH:
                return progress * 1.8f;
        }

        return 0;
    }

    public int handleFluidMixing(ItemStack itemStack) {
        if (FluidContainerRegistry.isFilledContainer(itemStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);

            // Template just for mixing fluids
            CookingRecipeInputTemplate template = new CookingRecipeInputTemplate(
                host.getFluidStack(), fluidStack,
                null,
                null,
                null,
                CookingHeatLevel.NONE, CookingHeatLevel.HIGH);

            for (CookingRecipe recipe : CookingHelper.getRecipesMatchingTemplate(template)) {
                Bids.LOG.debug("Found mixing recipe: " + CookingRecipeHelper.getRecipeHashString(recipe));

                int primaryAmountRequired = recipe.getInputFluidStack().amount;
                int primaryAmountAvailable = host.getFluidStack().amount;

                float runs = (float) primaryAmountAvailable / primaryAmountRequired;
                int secondaryAmountRequired = Math.round(recipe.getSecondaryInputFluidStack().amount * runs);
                Bids.LOG.debug("secondaryAmountRequired: " + secondaryAmountRequired);

                int minAmountToDrainRequired = Math.round(secondaryAmountRequired * 0.6f);
                int maxAmountToDrainRequired = Math.round(secondaryAmountRequired * 1.3f);
                Bids.LOG.debug("minAmountToDrainRequired: " + minAmountToDrainRequired);
                Bids.LOG.debug("maxAmountToDrainRequired: " + maxAmountToDrainRequired);

                int drainedAmount = CookingHelper.canDrainAmount(itemStack, secondaryAmountRequired);
                Bids.LOG.debug("Input container will be drained for amount: " + drainedAmount);

                if (drainedAmount < minAmountToDrainRequired || drainedAmount > maxAmountToDrainRequired) {
                    Bids.LOG.debug("The container cannot be drained as required");
                    continue;
                }

                if (drainedAmount + host.getTotalLiquidVolume() > host.getMaxFluidVolume()) {
                    Bids.LOG.debug("The cooking pot cannot fit the drained amount");
                    continue;
                }

                // FluidContainerRegistry.getFluidForFilledItem will not produce the correct fluid amount
                // for partial containers such as glass bottles
                // The correct amount is needed to handle merging cooking mixes with other fluids
                template.getSecondaryInputFluidStack().amount = drainedAmount;

                handleRecipeOutput(recipe, template, recipe.getCraftingResult(template), calculateTotalRecipeRuns(recipe));

                return drainedAmount;
            }
        }

        return 0;
    }

    private int calculateTotalRecipeRuns(CookingRecipe recipe) {
        // Melting recipes (solid input changes and liquid output) are processed one set of ingredients at a time
        // Total time is one run of the recipe
        if (recipe.getInputItemStack() != null && recipe.getOutputItemStack() == null
            && recipe.getOutputFluidStack() != null && recipe.getInputFluidStack() == null) {
            // Except when input item is food with weight
            // then the amount of runs is given by the weight of the recipe input and the actual input weight
            if (recipe.getInputItemStack().getItem() instanceof ItemFoodTFC) {
                int inputFoodRuns = getRunsForInputFood(recipe);
                int outputFluidRunsToFit = getOutputFluidRunsToFit(recipe);
                return Math.min(inputFoodRuns, outputFluidRunsToFit);
            } else {
                return 1;
            }
        }

        // Total recipe time depends on both the solid and liquid input
        if (recipe.getInputItemStack() != null && recipe.getInputFluidStack() != null) {
            // Unless the recipe has an output liquid that is dilutable with the input liquid
            // Then any excess liquid is used to dilute the output liquid
            boolean dilutable = recipe.getOutputFluidStack() != null && recipe.getOutputFluidStack().getFluid() instanceof FluidDilutable &&
                ((FluidDilutable) recipe.getOutputFluidStack().getFluid()).dilutingFluid == recipe.getInputFluidStack().getFluid();

            int itemRuns = getRunsForInputItemStack(recipe);
            int fluidRuns = getRunsForInputFluidStack(recipe);

            // If recipe also has fluid output, ensure enough items
            // unless dilutable
            if (recipe.getOutputFluidStack() != null && itemRuns < fluidRuns && !dilutable) {
                Bids.LOG.debug("Not enough items for recipe");
                return 0;
            }

            // If recipe also has input output, ensure enough liquid
            if (recipe.getOutputItemStack() != null && fluidRuns < itemRuns) {
                Bids.LOG.debug("Not enough fluid for recipe");
                return 0;
            }

            return Math.min(itemRuns, fluidRuns);
        }

        // Total recipe time depends on the input item stack size
        if (recipe.getInputItemStack() != null) {
            return getRunsForInputItemStack(recipe);
        }

        // Total recipe time depends on the input fluid amount
        if (recipe.getInputFluidStack() != null) {
            return getRunsForInputFluidStack(recipe);
        }

        // Making things out of thin air?
        return 1;
    }

    private int getOutputFluidRunsToFit(CookingRecipe recipe) {
        FluidStack fluidStack = host.getFluidStack();
        if (fluidStack == null || fluidStack.isFluidEqual(recipe.getOutputFluidStack())) {
            int totalAmountToFit = host.getMaxFluidVolume() - host.getTotalLiquidVolume();
            return (int)Math.floor((float)totalAmountToFit / recipe.getOutputFluidStack().amount);
        } else {
            // Not compatible fluids present
            return 0;
        }
    }

    private int getRunsForInputFood(CookingRecipe recipe) {
        float recipeInputWeight = Food.getWeight(recipe.getInputItemStack());
        float actualInputWeight = Food.getWeight(host.getInputItemStack()) - Math.max(Food.getDecay(host.getInputItemStack()), 0);
        return (int)Math.ceil(actualInputWeight / recipeInputWeight);
    }

    private int getRunsForInputFluidStack(CookingRecipe recipe) {
        int recipeAmount = recipe.getInputFluidStack().amount;
        int actualInputAmount = host.getTotalLiquidVolume();

        // If the input stack is diluted, we only care for the undiluted amount
        if (host.getFluidStack().getFluid() instanceof FluidDilutable) {
            actualInputAmount = ((FluidDilutable) host.getFluidStack().getFluid()).getUndilutedAmount(host.getFluidStack());
        }

        // Should the output stack be diluted, consider minimal diluting amount of the fluid, and save it to be used to dilute the output
        if (recipe.getOutputFluidStack() != null && recipe.getOutputFluidStack().getFluid() instanceof FluidDilutable) {
            actualInputAmount -= ((FluidDilutable) recipe.getOutputFluidStack().getFluid()).minDilution;
        }

        return (int)Math.floor((float)actualInputAmount / recipeAmount);
    }

    private int getRunsForInputItemStack(CookingRecipe recipe) {
        if (host.getInputItemStack().getItem() instanceof ItemFoodTFC) {
            float recipeInputStackWeight = Food.getWeight(recipe.getInputItemStack());
            float actualInputStackWeight = Food.getWeight(host.getInputItemStack());
            return (int) Math.floor(actualInputStackWeight / recipeInputStackWeight);
        } else {
            int recipeStackSize = recipe.getInputItemStack().stackSize;
            int actualInputStackSize = host.getInputItemStack().stackSize;
            return (int) Math.floor((float) actualInputStackSize / recipeStackSize);
        }
    }

    public void handleRecipeOutput(CookingRecipe recipe, CookingRecipeInputTemplate template, CookingRecipeCraftingResult result, int runs) {
        if (result.getOutputItemStack() != null) {
            // When the recipe has output item stack,
            // any input stack is always replaced with the output item stack
            ItemStack output = result.getOutputItemStack().copy();
            if (output.getItem() instanceof ItemFoodTFC) {
                // Multiply the weight for foodstuff
                float weight = Food.getWeight(output);
                Food.setWeight(output, weight * runs);
                // Delay the start of decay of crafted food items
                Food.setDecayTimer(output, (int)TFC_Time.getTotalHours() + 1);
            } else {
                // Multiply the stack size for non foodstuff
                output.stackSize *= runs;
            }

            host.onItemChanged(output);
        } else if (recipe.getInputItemStack() != null) {
            // Otherwise consume appropriate size of the input item stack
            // or food weight
            ItemStack output = host.getInputItemStack().copy();
            if (recipe.getInputItemStack().getItem() instanceof ItemFoodTFC) {
                float availableWeight = Food.getWeight(host.getInputItemStack());
                float consumedWeight = Food.getWeight(recipe.getInputItemStack()) * runs;

                Bids.LOG.debug("availableWeight: " + availableWeight);
                Bids.LOG.debug("consumedWeight: " + consumedWeight);

                float remainingWeight = availableWeight - consumedWeight;
                if (remainingWeight > 0) {
                    Food.setWeight(output, remainingWeight);
                } else {
                    output.stackSize = 0;
                }
            } else {
                int consumedStackSize = recipe.getInputItemStack().stackSize * runs;

                Bids.LOG.debug("availableStackSize: " + output.stackSize);
                Bids.LOG.debug("consumedStackSize: " + consumedStackSize);

                output.stackSize -= consumedStackSize;
            }

            if (output.stackSize < 0) {
                Bids.LOG.warn("Recipe consumed more items than available");
                output.stackSize = 0;
            }

            if (output.stackSize > 0) {
                host.onItemChanged(output);
            } else {
                host.onItemChanged(null);
            }
        }

        if (result.getOutputFluidStack() != null) {
            // When the recipe has output fluid stack
            // any input fluid stack is always replaced
            // or added to the existing liquid
            if (host.getFluidStack() != null && recipe.getInputFluidStack() == null) {
                FluidStack output = host.getFluidStack().copy();
                output.amount += result.getOutputFluidStack().amount * runs;
                host.onFluidChanged(output, null);
            } else {
                FluidStack output = result.getOutputFluidStack().copy();

                if (recipe.getInputFluidStack() != null && recipe.getSecondaryInputFluidStack() != null) {
                    // For fluid mixing
                    // the volume of both input liquids is considered rather than number of runs
                    // to handle tolerance
                    int inputAmount = recipe.getInputFluidStack().amount + recipe.getSecondaryInputFluidStack().amount;
                    int outputAmount = recipe.getOutputFluidStack().amount;

                    // Always 1 for lossless and gain-less fluid mixing
                    float inputOutputRatio = (float)inputAmount / outputAmount;

                    int actualInputAmount = template.getInputFluidStack().amount + template.getSecondaryInputFluidStack().amount;
                    float actualOutputAmount = actualInputAmount / inputOutputRatio;

                    output.amount = Math.round(actualOutputAmount);
                } else {
                    output.amount *= runs;
                }

                if (recipe.getOutputFluidStack().getFluid() instanceof FluidDilutable) {
                    // Recipes with dilutable output fluid allow excess input fluid
                    // to dilute the output liquid
                    // Find out the amount if any
                    float actuallyConsumedInputAmount = recipe.getInputFluidStack().amount * runs;
                    float remainingInputAmount = template.getInputFluidStack().amount - actuallyConsumedInputAmount;
                    float remainingInputAmountRatio = remainingInputAmount / actuallyConsumedInputAmount;
                    // Ignore tiny amount, possibly from decay
                    if (remainingInputAmountRatio > 0.1f) {
                        output.amount += remainingInputAmount;
                        ((FluidDilutable)output.getFluid()).setDilutingAmount(output, Math.round(remainingInputAmount));
                    }
                }

                if (result.getSecondaryOutputFluidStack() != null) {
                    FluidStack secondaryOutput = result.getSecondaryOutputFluidStack().copy();
                    secondaryOutput.amount *= runs;

                    host.onFluidChanged(output, secondaryOutput);
                } else {
                    host.onFluidChanged(output, null);
                }
            }
        } else if (recipe.getInputFluidStack() != null) {
            // Otherwise consume appropriate amount of the input fluid stack
            int consumedAmount = recipe.getInputFluidStack().amount * runs;

            FluidStack output = host.getFluidStack();

            Bids.LOG.debug("availableAmount: " + output.amount);
            Bids.LOG.debug("consumedAmount: " + consumedAmount);

            output.amount -= consumedAmount;

            if (output.amount < 0) {
                Bids.LOG.warn("Recipe consumed more fluid than available");
                output.amount = 0;
            }

            if (output.amount > 0) {
                // If the entirety of the undiluted portion of dilutable fluid is consumed
                // or if the undiluted amount isn't enough to complete a single recipe run
                // the output becomes the remaining diluting fluid
                if (output.getFluid() instanceof FluidDilutable) {
                    FluidDilutable dilutable = (FluidDilutable) output.getFluid();
                    int dilutingAmount = dilutable.getDilutingAmount(output);
                    int undilutedAmount = output.amount - dilutingAmount;

                    if (undilutedAmount < 0) {
                        Bids.LOG.warn("Recipe consumed more undiluted fluid than available");
                    }

                    int oneRunAmount = recipe.getInputFluidStack().amount;
                    if (undilutedAmount == 0 || undilutedAmount < oneRunAmount) {
                        output = dilutable.getDilutingFluid(output);
                    }
                }

                // Floor the amount to a sip volume
                output.amount = (int)Math.floor(output.amount / 50f) * 50;

                host.onFluidChanged(output, null);
            } else {
                host.onFluidChanged(null, null);
            }
        }
    }

    public void writeDataToNBT(NBTTagCompound tag) {
        if (recipeProgress != null) {
            NBTTagCompound progressTag = new NBTTagCompound();
            recipeProgress.writeToNBT(progressTag);
            tag.setTag("progress", progressTag);
        }
    }

    public void readDataFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("progress")) {
            NBTTagCompound progressTag = tag.getCompoundTag("progress");
            recipeProgress = CookingRecipeProgress.readFromNBT(progressTag);
        } else {
            recipeProgress = null;
        }
    }

}
