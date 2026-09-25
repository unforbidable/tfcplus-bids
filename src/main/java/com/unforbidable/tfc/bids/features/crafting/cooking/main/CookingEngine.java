package com.unforbidable.tfc.bids.features.crafting.cooking.main;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemBlocks.ItemBarrels;
import com.dunk.tfc.Items.ItemBlocks.ItemLargeVessel;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.api.features.cooking.CookedMeal;
import com.unforbidable.tfc.bids.api.features.cooking.CookingAccessory;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHost;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixture;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixtureFluid;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixtureItem;
import com.unforbidable.tfc.bids.api.features.cooking.CookingPotPlayerEvent;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeCraftingResult;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.util.Timer;
import com.unforbidable.tfc.bids.util.fluid.DualFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

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
                // that recipe has valid fluids and
                // that the recipe can run - checking the amounts
                if (host.isValidCookingRecipe(recipe) && hasRecipeValidFluids(recipe) && calculateTotalRecipeRuns(recipe) > 0) {
                    cachedRecipe = recipe;
                    break;
                }
            }
        }

        return cachedRecipe;
    }

    private boolean hasRecipeValidFluids(CookingRecipe recipe) {
        // - Must have output fluid that is valid OR input fluid must be diluted with a valid fluid
        // This allows melting snow, which does not need fresh water to start
        // Snow can melt slowly without fire (assuming cooking pit is in cave or some tent, near a firepit, something)
        // To melt with boiling stones, without a fluid no heating is possible, so it just works out
        // So first batch of snow needs to melt on its own, and once there is any water heat can be provided by boiling stones
        return (recipe.getOutputFluidStack() == null || isValidRecipeFluidStack(recipe.getOutputFluidStack()));
    }

    private boolean isValidRecipeFluidStack(FluidStack fluidStack) {
        // Match against the secondary (diluting) fluid for dual fluids
        if (DualFluid.isDualFluidStack(fluidStack)) {
            DualFluid dual = DualFluid.of(fluidStack);
            return host.isValidFluid(dual.getSecondaryFluid().getFluid());
        } else {
            return host.isValidFluid(fluidStack.getFluid());
        }
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

            // TODO ensure progress is saved (chunk is set dirty)
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

    public boolean handleItemInteraction(ItemStack equippedItem, EntityPlayer player) {
        return tryToHandleCookingMixes(equippedItem, player) ||
            tryToPlaceItemStack(equippedItem, player) ||
            tryToDrainItemStack(equippedItem, player) ||
            tryToFillItemStack(equippedItem, player) ||
            tryToHandleBarrel(equippedItem, player);
    }

    private boolean tryToHandleCookingMixes(ItemStack itemStack, EntityPlayer player) {
        // Cooking mixes can be added if there is no lid and accessory
        if (itemStack.getItem() instanceof CookingMixtureItem) {
            CookingMixtureItem cookingMix = (CookingMixtureItem) itemStack.getItem();
            FluidStack cookingMixFluidStack = cookingMix.getCookingFluid(itemStack);
            ItemStack emptyContainer = cookingMix.getEmptyContainer(itemStack);
            CookingMixtureFluid cookingFluid = (CookingMixtureFluid) cookingMixFluidStack.getFluid();

            if (canPlaceCookingMix(cookingMixFluidStack)) {
                float decay = Math.max(0, Food.getDecay(itemStack));
                float weight = Food.getWeight(itemStack);
                float decayRatio = decay / weight;
                if (decayRatio < 10) {
                    if (host.getFluidStack() != null) {
                        FluidStack output = host.getFluidStack();
                        FluidStack original = output.copy();
                        output.amount += cookingMixFluidStack.amount;
                        cookingFluid.onCookingFluidCombined(output, original, cookingMixFluidStack);

                        host.onFluidChanged(output, null);
                    } else {
                        cookingFluid.onCookingFluidPlaced(cookingMixFluidStack);

                        host.onFluidChanged(cookingMixFluidStack, null);
                    }
                } else {
                    // Decay is too great and cooking mix is unusable
                    TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.cooking.cookingMixDecayed"));
                }

                // Consume and return empty container even when spoiled
                itemStack.stackSize--;
                TFC_Core.giveItemToPlayer(emptyContainer, player);

                onRecipeParametersChanged(true);

                return true;
            }
        }

        // Cooked meals can be created from finished cooking mixes
        // into valid meal containers
        if (host.getFluidStack() != null && host.getFluidStack().getFluid() instanceof CookingMixtureFluid) {
            FluidStack currentFluidStack = host.getFluidStack();
            CookingMixtureFluid cookingFluid = (CookingMixtureFluid) currentFluidStack.getFluid();
            CookingMixture mixture = CookingMixtureHelper.getCookingMixture(currentFluidStack);
            if (mixture != null && mixture.isReady() && cookingFluid.isValidCookedMealContainer(currentFluidStack, itemStack)) {
                ItemStack cookedMeal = cookingFluid.retrieveCookedMeal(currentFluidStack, itemStack, player, true);

                if (cookedMeal != null) {
                    if (cookedMeal.getItem() instanceof CookedMeal) {
                        ((CookedMeal)cookedMeal.getItem()).onCookedMealCreated(cookedMeal, player);
                    }

                    CookingPotPlayerEvent event = new CookingPotPlayerEvent(player, host, CookingPotPlayerEvent.Action.RETRIEVE_COOKED_MEAL, cookedMeal);
                    MinecraftForge.EVENT_BUS.post(event);

                    if (!event.isCanceled()) {
                        if (currentFluidStack.amount <= 0) {
                            host.onFluidChanged(null, null);
                        }

                        itemStack.stackSize--;
                        TFC_Core.giveItemToPlayer(cookedMeal, player);

                        onRecipeParametersChanged(true);

                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean canPlaceCookingMix(FluidStack cookingMixFluidStack) {
        if (host.getFluidStack() != null) {
            FluidStack currentFluidStack = host.getFluidStack();
            return currentFluidStack.amount + cookingMixFluidStack.amount <= host.getMaxFluidVolume() &&
                CookingMixtureHelper.canCombineCookingFluids(currentFluidStack, cookingMixFluidStack);
        } else {
            return cookingMixFluidStack.amount <= host.getMaxFluidVolume();
        }
    }

    private boolean tryToPlaceItemStack(ItemStack equippedItem, EntityPlayer player) {
        ItemStack inputItem = host.getInputItemStack();

        // If there is already an input item, the items need to be able to form a stack
        if (inputItem != null) {
            if (inputItem.getItem() instanceof ItemFoodTFC) {
                // Food cannot be stacked
                return false;
            } else if (!equippedItem.isItemEqual(inputItem) || equippedItem.getItemDamage() != inputItem.getItemDamage()) {
                // Different items cannot be stacked
                return false;
            }
        }

        // Placed stack will include any existing amount so it needs to be included in the calculation
        int existingStackSize = inputItem != null ? inputItem.stackSize : 0;
        ItemStack placing = equippedItem.copy();
        placing.stackSize += existingStackSize;

        // For input items the stack size is determined based on the matching recipe
        int requiredStackSize = getInputItemStackSizeForRecipe(placing);
        if (requiredStackSize == 0) {
            // Stack size could not be determined based on the matching recipe,
            // or it was determined as 0
            // which means the item stack in fact cannot be placed
            return false;
        }
        Bids.LOG.debug("requiredStackSize: " + requiredStackSize);

        // Ensure the required stack size does not exceed max stack size
        int checkedRequiredStackSize = Math.min(requiredStackSize, equippedItem.getMaxStackSize());
        Bids.LOG.debug("checkedRequiredStackSize: " + checkedRequiredStackSize);

        // Reduce target stack size by existing stack size
        int targetStackSize = checkedRequiredStackSize - existingStackSize;
        Bids.LOG.debug("targetStackSize: " + targetStackSize);

        if (targetStackSize <= 0) {
            // The target stack size is already fulfilled
            return false;
        }

        // Consider the stack size that is available
        int consumedStackSize = Math.min(targetStackSize, equippedItem.stackSize);
        Bids.LOG.debug("consumedStackSize: " + consumedStackSize);

        ItemStack outputItem;
        if (inputItem != null) {
            outputItem = inputItem.copy();
            outputItem.stackSize += consumedStackSize;
        } else {
            outputItem = equippedItem.copy();
            outputItem.stackSize = consumedStackSize;
        }

        host.onItemChanged(outputItem);

        equippedItem.stackSize -= consumedStackSize;

        Bids.LOG.debug("Placed input item: " + outputItem.getDisplayName() + "[" + outputItem.stackSize + "]");

        // Cancel any recipe that may be in progress
        // otherwise instant recipes returning output immediately will not work
        cancelRecipeProgress();

        // Process recipe right after item is placed
        // and remember the input item
        ItemStack before = host.getInputItemStack();

        handleRecipeProgress();

        ItemStack after = host.getInputItemStack();

        if (after != null && !ItemStack.areItemStacksEqual(before, after)) {
            // If the input item changed
            // the recipe completed immediately
            // so return the crafted item immediately
            TFC_Core.giveItemToPlayer(after, player);

            host.onItemChanged(null);

            onRecipeParametersChanged(true);
        }

        return true;
    }

    private int getInputItemStackSizeForRecipe(ItemStack itemStack) {
        if (CookingHelper.isCookableFoodItemStack(itemStack)) {
            // Food items default to stack size of 1
            return 1;
        } else {
            FluidStack fluidStack = host.getFluidStack();

            // Look for all recipes matching specific input item
            // as we don't know the final parameters for the recipes
            // and manually ensure certain parameters such as steaming mesh and existing liquid
            for (CookingRecipe recipe : CookingHelper.getRecipesMatchingInput(itemStack)) {
                // Ensure accessory matches
                if (recipe.getAccessory() != CookingAccessory.NONE) {
                    continue;
                }

                float runs = calculateTotalRecipeRuns(recipe, itemStack, fluidStack, true);
                if (runs > 0) {
                    return (int) Math.floor(runs) * recipe.getInputItemStack().stackSize;
                }
            }

            // No valid recipe found for the input item
            return 0;
        }
    }

    private boolean tryToDrainItemStack(ItemStack equippedItem, EntityPlayer player) {
        if ((FluidContainerRegistry.isFilledContainer(equippedItem)
            || equippedItem.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem) equippedItem.getItem()).getFluid(equippedItem) != null)) {
            ItemStack tmp = equippedItem.copy();
            tmp.stackSize = 1;

            ItemStack is = handleFluidAdding(tmp);

            if (ItemStack.areItemStacksEqual(equippedItem, is)) {
                // Fluid could not be added, so let's try mixing instead
                is = tryMixingFluids(equippedItem);
                if (ItemStack.areItemStacksEqual(equippedItem, is)) {
                    return true;
                }
            }

            equippedItem.stackSize--;

            if (equippedItem.stackSize == 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }

            if (equippedItem.stackSize == 0 && (is.getMaxStackSize() == 1 || !player.inventory.hasItemStack(is))) {
                // put empty container in the slot you used them from.
                player.inventory.setInventorySlotContents(player.inventory.currentItem, is);
            } else {
                if (!player.inventory.addItemStackToInventory(is)) {
                    // if the new item doesn't fit, drop it.
                    player.dropPlayerItemWithRandomChoice(is, false);
                }
            }

            if (player.inventoryContainer != null) {
                // for some reason the players inventory won't update without this.
                player.inventoryContainer.detectAndSendChanges();
            }

            return true;
        }

        return false;
    }

    private boolean tryToFillItemStack(ItemStack equippedItem, EntityPlayer player) {
        if (FluidContainerRegistry.isEmptyContainer(equippedItem) || equippedItem.getItem() instanceof IFluidContainerItem) {
            ItemStack singleStack = equippedItem.copy();
            singleStack.stackSize = 1;
            ItemStack fillingStack = drainFluid(singleStack);

            if (ItemStack.areItemStacksEqual(equippedItem, fillingStack)) {
                // Could not fill
                return true;
            }

            boolean finished = false;
            while (!finished) {
                ItemStack copy = fillingStack.copy();
                fillingStack = drainFluid(fillingStack);

                // Keep filling up partial containers
                finished = ItemStack.areItemStacksEqual(copy, fillingStack);
            }

            BidsEventFactory.onFillContainer(player, equippedItem, fillingStack);

            equippedItem.stackSize--;

            if (equippedItem.stackSize == 0) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }

            if (equippedItem.stackSize == 0 && (fillingStack.getMaxStackSize() == 1 || !player.inventory.hasItemStack(fillingStack))) { // put buckets in the slot you used them from.
                player.inventory.setInventorySlotContents(player.inventory.currentItem, fillingStack);
            } else {
                if (!player.inventory.addItemStackToInventory(fillingStack))
                    player.dropPlayerItemWithRandomChoice(fillingStack, false); // if the new item dosn't fit, drop it.
            }

            if (player.inventoryContainer != null) {
                // for some reason the players inventory won't update without this.
                player.inventoryContainer.detectAndSendChanges();
            }

            return true;
        }

        return false;
    }

    private boolean tryToHandleBarrel(ItemStack equippedItem, EntityPlayer player) {
        if (equippedItem.getItem() instanceof ItemBarrels || equippedItem.getItem() instanceof ItemLargeVessel) {
            Bids.LOG.debug("Cooking pot activated with barrel/large vessel: " + equippedItem.getDisplayName());

            ItemStack is = equippedItem.copy();
            is.stackSize = 1;

            if (equippedItem.hasTagCompound()) {
                if (equippedItem.getTagCompound().hasKey("fluidNBT") && !equippedItem.getTagCompound().hasKey("Items")) {
                    boolean isNotFull = host.getFluidStack() == null || host.getFluidStack().amount < host.getMaxFluidVolume();
                    if (isNotFull) {
                        FluidStack fs = FluidStack.loadFluidStackFromNBT(equippedItem.getTagCompound().getCompoundTag("fluidNBT"));
                        if (addFluid(fs)) {
                            if (fs.amount == 0) {
                                equippedItem.getTagCompound().removeTag("Sealed");
                                equippedItem.getTagCompound().removeTag("fluidNBT");
                                if (equippedItem.getTagCompound().hasNoTags())
                                    equippedItem.setTagCompound(null);
                            } else {
                                fs.writeToNBT(equippedItem.getTagCompound().getCompoundTag("fluidNBT"));
                            }

                            return true;
                        }
                    }
                }
            } else {
                if (host.getFluidStack() != null && isTransferableFluid(host.getFluidStack())) {
                    if (is.getItem() instanceof ItemBarrels) {
                        FluidStack fs = host.getFluidStack();

                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setTag("fluidNBT", fs.writeToNBT(new NBTTagCompound()));
                        nbt.setBoolean("Sealed", true);
                        is.setTagCompound(nbt);
                        equippedItem.stackSize--;
                        TFC_Core.giveItemToPlayer(is, player);

                        emptyFluid();
                    } else if (is.getItem() instanceof ItemLargeVessel) {
                        if (is.getItemDamage() == 0) {
                            return false;
                        }

                        FluidStack fs = host.getFluidStack();

                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setTag("fluidNBT", fs.writeToNBT(new NBTTagCompound()));
                        nbt.setBoolean("Sealed", true);
                        is.setTagCompound(nbt);
                        equippedItem.stackSize--;
                        TFC_Core.giveItemToPlayer(is, player);

                        emptyFluid();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private boolean isTransferableFluid(FluidStack fluidStack) {
        return !(fluidStack.getFluid() instanceof CookingMixtureFluid) &&
            !DualFluid.isDualFluidStack(fluidStack);
    }

    private void emptyFluid() {
        if (host.getFluidStack() != null) {
            host.onFluidChanged(null, null);

            onRecipeParametersChanged(true);
        }
    }

    private ItemStack handleFluidAdding(ItemStack is) {
        if (is == null || is.stackSize > 1) {
            return is;
        }

        if (FluidContainerRegistry.isFilledContainer(is)) {
            ItemStack draining = is.copy();
            while (FluidContainerRegistry.isFilledContainer(draining)) {
                FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(draining);
                if (addFluid(fs)) {
                    draining = FluidContainerRegistry.drainFluidContainer(draining);
                    Bids.LOG.debug("Draining item stack: " + draining.getDisplayName() + "[" + draining.getItemDamage() + "]");
                } else {
                    break;
                }
            }
            return draining;
        } else if (is.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
            if (addFluid(isfs)) {
                ((IFluidContainerItem) is.getItem()).drain(is, is.getMaxDamage(), true);
                return is;
            }
        }

        return is;
    }

    public ItemStack tryMixingFluids(ItemStack itemStack) {
        if (host.getFluidStack() != null) {
            int drainedAmount = handleFluidMixing(itemStack);
            if (drainedAmount > 0) {
                ItemStack drainingItemStack = CookingHelper.doDrainAmount(itemStack, drainedAmount);

                // Mixing liquids resets recipe parameters
                onRecipeParametersChanged(true);

                return drainingItemStack;
            }
        }

        return itemStack;
    }

    public int handleFluidMixing(ItemStack itemStack) {
        if (FluidContainerRegistry.isFilledContainer(itemStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);

            // Template just for mixing fluids
            CookingRecipeInputTemplate hostTemplate = host.createRecipeTemplate();
            CookingRecipeInputTemplate template = new CookingRecipeInputTemplate(
                host.getFluidStack(), fluidStack,
                null,
                hostTemplate.getAccessory(),
                hostTemplate.getLidUsage(),
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

    public boolean addFluid(FluidStack inFS) {
        if (inFS != null && host.isValidFluid(inFS.getFluid())) {
            FluidStack fluidStack = host.getFluidStack();
            if (fluidStack == null) {
                // Cannot fill up from larger amount
                if (inFS.amount > host.getMaxFluidVolume()) {
                    return false;
                }

                host.onFluidChanged(inFS.copy(), null);
                inFS.amount = 0;
            } else {
                // check if the container would overflow or if the fluid being added does
                // not match the contained liquid
                if (fluidStack.amount + inFS.amount > host.getMaxFluidVolume() || !fluidStack.isFluidEqual(inFS)) {
                    return false;
                }

                fluidStack.amount += inFS.amount;
                host.onFluidChanged(fluidStack, null);

                inFS.amount = 0;
            }

            onRecipeParametersChanged(true);

            return true;
        }

        return false;
    }

    public ItemStack drainFluid(ItemStack container) {
        if (container == null || container.stackSize > 1) {
            return container;
        }

        if (host.getFluidStack() == null) {
            return container;
        }

        FluidStack fluidStack = host.getFluidStack();

        // Work with the primary fluid of diluted fluids
        FluidStack primaryFluidStack = DualFluid.isDualFluidStack(fluidStack) ? DualFluid.of(fluidStack).getPrimaryFluid() : fluidStack;

        if (FluidContainerRegistry.isEmptyContainer(container)) {
            ItemStack empty = FluidContainerRegistry.fillFluidContainer(primaryFluidStack, container);
            if (empty != null) {
                int amount = FluidContainerRegistry.getFluidForFilledItem(empty).amount;
                doDrainFluidAmount(amount);

                return empty;
            }
        } else if (container.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) container.getItem()).getFluid(container);
            if (isfs == null || primaryFluidStack.isFluidEqual(isfs)) {
                int amount = ((IFluidContainerItem) container.getItem()).fill(container, primaryFluidStack, true);
                doDrainFluidAmount(amount);
            }

            return container;
        }

        return container;
    }

    private void doDrainFluidAmount(int amount) {
        FluidStack fluidStack = host.getFluidStack();

        if (DualFluid.isDualFluidStack(fluidStack)) {
            DualFluid dual = DualFluid.of(fluidStack);
            FluidStack primaryFluidStack = dual.getPrimaryFluid();
            if (primaryFluidStack.amount == amount) {
                // All the primary fluid of the diluted fluid was used up
                // so the fluid changes to the diluting fluid
                host.onFluidChanged(dual.getSecondaryFluid(), null);
            } else {
                // Otherwise just consume part of the primary fluid
                fluidStack.amount -= amount;
                host.onFluidChanged(fluidStack, null);
            }
        } else {
            // Remove liquid from the top layer first
            fluidStack.amount -= amount;
            if (fluidStack.amount == 0) {
                host.onFluidChanged(null, null);
            } else {
                host.onFluidChanged(fluidStack, null);
            }
        }

        onRecipeParametersChanged(true);
    }

    private int calculateTotalRecipeRuns(CookingRecipe recipe) {
        return calculateTotalRecipeRuns(recipe, host.getInputItemStack(), host.getFluidStack(), false);
    }

    private int calculateTotalRecipeRuns(CookingRecipe recipe, ItemStack inputItemStack, FluidStack inputFluidStack, boolean greedy) {
        // Soaking recipes are always run one item at a time at most if there is enough input fluid
        if (recipe.getInputItemStack() != null && recipe.getOutputItemStack() != null && recipe.getInputFluidStack() != null) {
            int fluidRuns = getRunsForInputFluidStack(recipe, inputFluidStack);
            return Math.min(1, fluidRuns);
        }

        // Melting and dissolving recipes (solid input changes into liquid output or merges with existing input liquid)
        // are processed one set of ingredients at a time
        // Total time is one run of the recipe
        if (recipe.getInputItemStack() != null && recipe.getOutputItemStack() == null) {
            // Except when input item is food with weight
            // then the amount of runs is given by the weight of the recipe input and the actual input weight
            if (recipe.getInputItemStack().getItem() instanceof ItemFoodTFC) {
                int inputFoodRuns = getRunsForInputFood(recipe, inputItemStack);
                int outputFluidRunsToFit = getOutputFluidRunsToFit(recipe, inputFluidStack);
                return Math.min(inputFoodRuns, outputFluidRunsToFit);
            } else if (!greedy) {
                // Unless runs are calculated for placing item stack then return greedy amount using
                return 1;
            }
        }

        // Total recipe time depends on both the solid and liquid input
        if (recipe.getInputItemStack() != null && recipe.getInputFluidStack() != null) {
            // Unless the recipe has an output liquid that is dilutable with the input liquid
            // Then any excess liquid is used to dilute the output liquid
            boolean dilutable = recipe.getOutputFluidStack() != null &&
                isOutputFluidDilutableByInputFluid(recipe.getOutputFluidStack(), recipe.getInputFluidStack());

            int itemRuns = getRunsForInputItemStack(recipe, inputItemStack);
            int fluidRuns = getRunsForInputFluidStack(recipe, inputFluidStack);
            int outputFluidRunsToFit = getOutputFluidRunsToFit(recipe, inputFluidStack);

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

            return Math.min(Math.min(itemRuns, fluidRuns), outputFluidRunsToFit);
        }

        // Total recipe time depends on the input item stack size limited by output capacity if there is liquid output
        if (recipe.getInputItemStack() != null) {
            int runsForInputItemStack = getRunsForInputItemStack(recipe, inputItemStack);
            if (recipe.getOutputFluidStack() != null) {
                int outputFluidRunsToFit = getOutputFluidRunsToFit(recipe, inputFluidStack);
                return Math.min(runsForInputItemStack, outputFluidRunsToFit);
            } else {
                return runsForInputItemStack;
            }
        }

        // Total recipe time depends on the input fluid amount
        if (recipe.getInputFluidStack() != null) {
            return getRunsForInputFluidStack(recipe, inputFluidStack);
        }

        // Making things out of thin air?
        return 0;
    }

    private boolean isOutputFluidDilutableByInputFluid(FluidStack outputFluidStack, FluidStack inputFluidStack) {
        if (DualFluid.isDualFluidStack(outputFluidStack)) {
            return DualFluid.of(outputFluidStack)
                .isSecondaryFluidEqual(inputFluidStack);
        }

        return false;
    }

    private int getOutputFluidRunsToFit(CookingRecipe recipe, FluidStack fluidStack) {
        if (recipe.getInputFluidStack() != null && recipe.getOutputFluidStack() != null &&
            recipe.getInputFluidStack().amount >= recipe.getOutputFluidStack().amount) {
            // Infinite runs will fit if recipe consumed equal or greater amount of input fluid that it produces
            return Integer.MAX_VALUE;
        }

        if (fluidStack == null || fluidStack.isFluidEqual(recipe.getOutputFluidStack()) ||
            DualFluid.isDualFluidStack(fluidStack) && DualFluid.of(fluidStack).isSecondaryFluidEqual(fluidStack)) {
            int totalAmountToFit = host.getMaxFluidVolume() - host.getTotalLiquidVolume();
            return (int)Math.floor((float)totalAmountToFit / recipe.getOutputFluidStack().amount);
        } else {
            // Not compatible fluids present
            return 0;
        }
    }

    private int getRunsForInputFood(CookingRecipe recipe, ItemStack inputItemStack) {
        float recipeInputWeight = Food.getWeight(recipe.getInputItemStack());
        float actualInputWeight = Food.getWeight(inputItemStack) - Math.max(Food.getDecay(inputItemStack), 0);
        return (int)Math.ceil(actualInputWeight / recipeInputWeight);
    }

    private int getRunsForInputFluidStack(CookingRecipe recipe, FluidStack fluidStack) {
        if (fluidStack == null) {
            // No fluid, no runs
            return 0;
        }

        int recipeAmount = recipe.getInputFluidStack().amount;
        int actualInputAmount = host.getTotalLiquidVolume();

        // If the input stack is diluted, the actual amount depends on which of the liquids matches the recipe
        if (DualFluid.isDualFluidStack(fluidStack)) {
            DualFluid dual = DualFluid.of(fluidStack);
            if (dual.isPrimaryFluidEqual(recipe.getInputFluidStack())) {
                FluidStack primary = dual.getPrimaryFluid();
                actualInputAmount = primary.amount;
            } else if (dual.isSecondaryFluidEqual(recipe.getInputFluidStack())) {
                FluidStack secondary = dual.getSecondaryFluid();
                actualInputAmount = secondary.amount;
            }
        }

        // Should the output stack be diluted, consider minimal diluting amount of the fluid, and save it to be used to dilute the output
        if (recipe.getOutputFluidStack() != null && DualFluid.isDualFluidStack(recipe.getOutputFluidStack())) {
            DualFluid dual = DualFluid.of(recipe.getOutputFluidStack());
            FluidStack secondary = dual.getSecondaryFluid();
            actualInputAmount -= secondary.amount;
        }

        return (int)Math.floor((float)actualInputAmount / recipeAmount);
    }

    private int getRunsForInputItemStack(CookingRecipe recipe, ItemStack inputItemStack) {
        if (inputItemStack.getItem() instanceof ItemFoodTFC) {
            float recipeInputStackWeight = Food.getWeight(recipe.getInputItemStack());
            float actualInputStackWeight = Food.getWeight(inputItemStack);
            return (int) Math.floor(actualInputStackWeight / recipeInputStackWeight);
        } else {
            int recipeStackSize = recipe.getInputItemStack().stackSize;
            int actualInputStackSize = inputItemStack.stackSize;
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

                if (template.getInputFluidStack() != null && DualFluid.isDualFluidStack(recipe.getOutputFluidStack())) {
                    // Recipes with dilutable output fluid allow excess input fluid
                    // to dilute the output liquid
                    // Find out the amount if any
                    float existingInputAmount = template.getInputFluidStack().amount;

                    // If existing fluid is already diluted
                    // the existing diluting amount is used as input liquid
                    // and the existing undiluted amount is carried over to the new output amount
                    if (DualFluid.isDualFluidStack(template.getInputFluidStack())) {
                        DualFluid dual = DualFluid.of(template.getInputFluidStack());
                        existingInputAmount = dual.getSecondaryFluid().amount;
                        output.amount += dual.getPrimaryFluid().amount;
                    }

                    float actuallyConsumedInputAmount = recipe.getInputFluidStack().amount * runs;
                    float remainingInputAmount = existingInputAmount - actuallyConsumedInputAmount;

                    // Floor the amount of diluting fluid to a sip volume
                    // Tiny amount, possibly from decay and rounding errors, is ignored
                    int remainingInputAmountFloored = (int)Math.floor(remainingInputAmount / 50f) * 50;
                    if (remainingInputAmountFloored > 0) {
                        output.amount += remainingInputAmountFloored;
                        Fluid dilutingFluid = recipe.getInputFluidStack().getFluid();
                        DualFluid.of(output)
                            .setSecondaryFluid(new FluidStack(dilutingFluid, remainingInputAmountFloored));
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
                if (DualFluid.isDualFluidStack(output)) {
                    DualFluid dualOutput = DualFluid.of(output);

                    int undilutedAmount = dualOutput.getPrimaryFluid().amount;
                    if (undilutedAmount < 0) {
                        Bids.LOG.warn("Recipe consumed more undiluted fluid than available");
                    }

                    int oneRunAmount = recipe.getInputFluidStack().amount;
                    if (undilutedAmount <= 0 || undilutedAmount < oneRunAmount) {
                        output = dualOutput.getSecondaryFluid();
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
