package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.Interfaces.ICookableFood;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.CookingPotBounds;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.EnumCookingPotPlacement;
import com.unforbidable.tfc.bids.Core.Cooking.CookingRecipeHelper;
import com.unforbidable.tfc.bids.Core.Cooking.CookingRecipeProgress;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.CookingManager;
import com.unforbidable.tfc.bids.api.Crafting.CookingRecipe;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingAccessory;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingLidUsage;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityCookingPot extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage>, IInventory {

    private static final int MAX_STORAGE = 3;
    private static final int MAX_FLUIDS = 3;

    private static final int SLOT_LID = 0;
    private static final int SLOT_ACCESSORY = 1;
    private static final int SLOT_INPUT = 2;

    private static final int FLUID_PRIMARY = 0;
    private static final int FLUID_TOP_LAYER = 1;
    private static final int FLUID_BEFORE_SEPARATION = 2;

    private static final int HEAT_CHECK_INTERVAL = 10;
    private static final int HEAT_CHECK_DELAY_AFTER_PLACEMENT_CHANGE = 50;
    private static final int RECIPE_HANDLE_INTERVAL = 20;
    private static final int RECIPE_HANDLE_INTERVAL_AFTER_PARAMETER_CHANGE = 5;
    private static final int ITEM_TICK_INTERVAL = 50;

    private final ItemStack[] storage = new ItemStack[MAX_STORAGE];
    private final FluidStack[] fluids = new FluidStack[MAX_FLUIDS];

    private EnumCookingPotPlacement placement = EnumCookingPotPlacement.GROUND;
    private CookingPotBounds cachedBounds = null;
    private EnumCookingHeatLevel heatLevel = EnumCookingHeatLevel.NONE;

    private CookingRecipe cachedRecipe = null;
    private boolean isCachedRecipeValid = false;

    private CookingRecipeProgress recipeProgress = null;

    boolean clientNeedToUpdate = false;
    boolean clientDataLoaded = false;
    boolean inputItemSelected = false;

    private final Timer heatCheckTimer = new Timer(HEAT_CHECK_INTERVAL);
    private final Timer recipeHandleTimer = new Timer(RECIPE_HANDLE_INTERVAL);
    private final Timer itemTickTimer = new Timer(ITEM_TICK_INTERVAL);

    private boolean recipeCanPauseWhenParametersChange = true;

    public CookingRecipeProgress getRecipeProgress() {
        return recipeProgress;
    }

    public EnumCookingPotPlacement getPlacement() {
        return placement;
    }

    public void setPlacement(EnumCookingPotPlacement placement) {
        this.placement = placement;

        cachedBounds = null;

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        clientNeedToUpdate = true;

        heatCheckTimer.delay(HEAT_CHECK_DELAY_AFTER_PLACEMENT_CHANGE);
    }

    public CookingPotBounds getCachedBounds() {
        if (cachedBounds == null) {
            cachedBounds = CookingPotBounds.getBoundsForPlacement(placement.getPlacement());
        }

        return cachedBounds;
    }

    public CookingRecipe getCachedRecipe() {
        if (!isCachedRecipeValid) {
            isCachedRecipeValid = true;
            cachedRecipe = null;

            CookingRecipe template = createRecipeTemplate();
            for (CookingRecipe recipe : CookingManager.getRecipesMatchingTemplate(template)) {
                // Ensure the recipe can run - checking the amounts
                if (calculateTotalRecipeRuns(recipe) > 0) {
                    cachedRecipe = recipe;
                    break;
                }
            }
        }

        return cachedRecipe;
    }

    public EnumCookingHeatLevel getHeatLevel() {
        return heatLevel;
    }

    public boolean placeItemStack(ItemStack itemStack) {
        // Lid can be placed when there is no lid already
        if (isValidLidItemStack(itemStack) && !hasLid()) {
            storage[SLOT_LID] = itemStack.copy();
            storage[SLOT_LID].stackSize = 1;
            itemStack.stackSize--;

            Bids.LOG.debug("Placed lid: " + storage[SLOT_LID].getDisplayName());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(false);

            return true;
        }

        // Accessory can be placed when there is no lid, input item or another accessory already
        if (isValidAccessoryItemStack(itemStack) && !hasLid() && !hasInputItem() && !hasAccessory()) {
            storage[SLOT_ACCESSORY] = itemStack.copy();
            storage[SLOT_ACCESSORY].stackSize = 1;
            itemStack.stackSize--;

            Bids.LOG.debug("Placed accessory: " + storage[SLOT_ACCESSORY].getDisplayName());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(true);

            return true;
        }

        // If not lid or accessory, try to match item to a recipe
        // When there is no lid and top layer fluid
        if (!hasLid() && !hasTopLayerFluid()) {
            // If there is already an input item, the items need to be able to form a stack
            if (hasInputItem()) {
                if (itemStack.getItem() instanceof ItemFoodTFC) {
                    // Food cannot be stacked
                    return false;
                } else if (!itemStack.isItemEqual(getInputItemStack()) || itemStack.getItemDamage() != getInputItemStack().getItemDamage()) {
                    // Different items cannot be stacked
                    return false;
                }
            }

            // For input items the stack size is determined based on the matching recipe
            int requiredStackSize = getInputItemStackSizeForRecipe(itemStack);
            if (requiredStackSize == 0) {
                // Stack size could not be determined based on the matching recipe,
                // or it was determined as 0
                // which means the item stack in fact cannot be placed
                return false;
            }
            Bids.LOG.debug("requiredStackSize: " + requiredStackSize);

            // Ensure the required stack size does not exceed max stack size
            int checkedRequiredStackSize = Math.min(requiredStackSize, itemStack.getMaxStackSize());
            Bids.LOG.debug("checkedRequiredStackSize: " + checkedRequiredStackSize);

            // Reduce target stack size by existing stack size
            int existingStackSize = hasInputItem() ? getInputItemStack().stackSize : 0;
            int targetStackSize = checkedRequiredStackSize - existingStackSize;
            Bids.LOG.debug("targetStackSize: " + targetStackSize);

            if (targetStackSize <= 0) {
                // The target stack size is already fulfilled
                return false;
            }

            // Consider the stack size that is available
            int consumedStackSize = Math.min(targetStackSize, itemStack.stackSize);
            Bids.LOG.debug("consumedStackSize: " + consumedStackSize);

            if (hasInputItem()) {
                storage[SLOT_INPUT].stackSize += consumedStackSize;
            } else {
                storage[SLOT_INPUT] = itemStack.copy();
                storage[SLOT_INPUT].stackSize = consumedStackSize;
            }

            itemStack.stackSize -= consumedStackSize;

            Bids.LOG.debug("Placed input item: " + storage[SLOT_INPUT].getDisplayName() + "[" + storage[SLOT_INPUT].stackSize + "]");

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(false);

            return true;
        }

        return false;
    }

    private int getInputItemStackSizeForRecipe(ItemStack itemStack) {
        if (isCookableFoodItemStack(itemStack)) {
            // Food items default to stack size of 1
            return 1;
        } else {
            // Look for all recipes matching specific input item
            // as we don't know the final parameters for the recipes
            // and manually ensure certain parameters such as steaming mesh and existing liquid
            for (CookingRecipe recipe : CookingManager.getRecipesMatchingInput(itemStack)) {
                // Ensure accessory matches
                if (recipe.getAccessory() == EnumCookingAccessory.NONE && hasAccessory() ||
                    recipe.getAccessory() == EnumCookingAccessory.STEAMING_MESH && !hasSteamingMesh()) {
                    continue;
                }

                if (recipe.getInputFluidStack() != null) {
                    // Ensure existing liquid matches recipe input liquid
                    if (hasFluid() && !recipe.getInputFluidStack().isFluidEqual(getPrimaryFluidStack())) {
                        continue;
                    }

                    // If recipe has secondary input liquid (e.g. fluid mixing)
                    // those recipes are completed instantly when item containers are used
                    if (recipe.getSecondaryInputFluidStack() != null) {
                        continue;
                    }

                    // If the recipe has both input and output item (e.g. soaking)
                    // we can only do one run
                    if (recipe.getOutputItemStack() != null) {
                        return 1;
                    }

                    // Recipes with fluid input (salt + fresh water)
                    // Stack size depends on how much input liquid is in the cooking pot
                    // If there is no input liquid (yet) assume max volume
                    int currentAmount = hasFluid() ? getPrimaryFluidStack().amount : getMaxFluidVolume();
                    float runs = (float)currentAmount / recipe.getInputFluidStack().amount;
                    int count = (int)Math.ceil(runs);
                    int stackSize = count * recipe.getInputItemStack().stackSize;

                    Bids.LOG.debug("Determined input item stack size: " + stackSize + " for runs: " + runs + " and input amount: " + currentAmount);

                    return stackSize;
                } else if (recipe.getOutputFluidStack() != null) {
                    // Ensure existing liquid matches recipe output liquid
                    if (hasFluid() && !recipe.getOutputFluidStack().isFluidEqual(getPrimaryFluidStack())) {
                        continue;
                    }

                    // If the recipe has both input and output item (creating liquid and changing solid item)
                    // we can only do one run
                    if (recipe.getOutputItemStack() != null) {
                        return 1;
                    }

                    // Recipes without fluid input (melting snow)
                    // Stack size depends on how much output liquid can fit in the cooking pot
                    // account for existing liquid
                    int currentAmount = hasFluid() ? getPrimaryFluidStack().amount : 0;
                    int outputAmount = getMaxFluidVolume() - currentAmount;
                    float runs = (float)outputAmount / recipe.getOutputFluidStack().amount;
                    int count = (int)Math.floor(runs);
                    int stackSize = count * recipe.getInputItemStack().stackSize;

                    Bids.LOG.debug("Determined input item stack size: " + stackSize + " for runs: " + runs + " and output amount: " + outputAmount);

                    return stackSize;
                } else {
                    // Recipes without input or output liquid (turning solid item into another solid item)
                    // always one item
                    return 1;
                }
            }

            // No valid recipe found for the input item
            return 0;
        }
    }

    private CookingRecipe createRecipeTemplate() {
        return new CookingRecipe(
            getPrimaryFluidStack(), null, null, null,
            getInputItemStack(), null,
            hasSteamingMesh() ? EnumCookingAccessory.STEAMING_MESH : EnumCookingAccessory.NONE,
            hasLid() ? EnumCookingLidUsage.ON : EnumCookingLidUsage.OFF,
            getHeatLevel(), getHeatLevel(), 0
        );
    }

    private CookingRecipe createRecipeTemplateWithSecondaryInputFluidStack(FluidStack secondaryInputFluidStack) {
        return new CookingRecipe(
            getPrimaryFluidStack(), secondaryInputFluidStack, null, null,
            null, null,
            hasSteamingMesh() ? EnumCookingAccessory.STEAMING_MESH : EnumCookingAccessory.NONE,
            hasLid() ? EnumCookingLidUsage.ON : EnumCookingLidUsage.OFF,
            getHeatLevel(), getHeatLevel(), 0
        );
    }

    private FluidStack getFluidStackFromItemStack(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof IFluidContainerItem) {
            IFluidContainerItem container = (IFluidContainerItem) itemStack.getItem();
            return container.getFluid(itemStack);
        } else {
            return null;
        }
    }

    public boolean retrieveItemStack(EntityPlayer player) {
        if (hasLid()) {
            Bids.LOG.debug("Retrieving lid: " + storage[SLOT_LID].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_LID]);
            storage[SLOT_LID] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(false);

            return true;
        } else if (hasInputItem()) {
            Bids.LOG.debug("Retrieving input item: " + storage[SLOT_INPUT].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_INPUT]);
            storage[SLOT_INPUT] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(true);

            return true;
        } else if (hasAccessory()) {
            Bids.LOG.debug("Retrieving accessory: " + storage[SLOT_ACCESSORY].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_ACCESSORY]);
            storage[SLOT_ACCESSORY] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(true);

            return true;
        } else {
            return false;
        }
    }

    public boolean addLiquid(FluidStack inFS) {
        if (inFS != null) {
            if (fluids[FLUID_PRIMARY] == null) {
                // Cannot fill up from larger amount
                if (inFS.amount > getMaxFluidVolume()) {
                    return false;
                }

                fluids[FLUID_PRIMARY] = inFS.copy();
                inFS.amount = 0;
            } else {
                // check if the container would overflow or if the fluid being added does
                // not match the contained liquid
                if (fluids[FLUID_PRIMARY].amount + inFS.amount > getMaxFluidVolume() || !fluids[FLUID_PRIMARY].isFluidEqual(inFS)) {
                    return false;
                }

                fluids[FLUID_PRIMARY].amount += inFS.amount;
                inFS.amount = 0;
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(true);

            return true;
        }

        return false;
    }

    public ItemStack addLiquid(ItemStack is) {
        if (is == null || is.stackSize > 1) {
            return is;
        }

        if (hasLid() || hasTopLayerFluid()) {
            return is;
        }

        if (FluidContainerRegistry.isFilledContainer(is)) {
            ItemStack draining = is.copy();
            boolean drained = false;
            while (FluidContainerRegistry.isFilledContainer(draining)) {
                FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(draining);
                if (addLiquid(fs)) {
                    drained = true;
                    draining = FluidContainerRegistry.drainFluidContainer(draining);
                    Bids.LOG.debug("Draining item stack: " + draining.getDisplayName() + "[" + draining.getItemDamage() + "]");
                } else {
                    break;
                }
            }

            if (drained) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
            return draining;
        } else if (is.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
            if (addLiquid(isfs)) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                ((IFluidContainerItem) is.getItem()).drain(is, is.getMaxDamage(), true);
                return is;
            }
        }

        return is;
    }

    public ItemStack mixLiquids(ItemStack itemStack) {
        if (hasFluid() && FluidContainerRegistry.isFilledContainer(itemStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
            CookingRecipe template = createRecipeTemplateWithSecondaryInputFluidStack(fluidStack);
            for (CookingRecipe recipe : CookingManager.getRecipesMatchingTemplate(template)) {
                Bids.LOG.debug("Found mixing recipe: " + CookingRecipeHelper.getRecipeHashString(recipe));

                int primaryAmountRequired = recipe.getInputFluidStack().amount;
                int primaryAmountAvailable = getTopFluidStack().amount;

                // Ensure the primary amount is divisible by required amount precisely
                if (primaryAmountAvailable % primaryAmountRequired != 0) {
                    Bids.LOG.debug("Input liquid amount must be multiple of recipe input");
                    continue;
                }

                int runs = primaryAmountAvailable / primaryAmountRequired;
                int secondaryAmountRequired = recipe.getSecondaryInputFluidStack().amount * runs;
                int secondaryAmountAvailable = fluidStack.amount;

                Bids.LOG.debug("secondaryAmountRequired: " + secondaryAmountRequired);
                Bids.LOG.debug("secondaryAmountAvailable: " + secondaryAmountAvailable);

                int drains = getDrainCountForAmount(itemStack, secondaryAmountRequired);
                if (drains == 0) {
                    Bids.LOG.debug("The container cannot be drained as required");
                    continue;
                }

                Bids.LOG.debug("Input container will be drained times: " + drains);

                ItemStack drainingItemStack = itemStack;
                for (int i = 0; i < drains; i++) {
                    drainingItemStack = FluidContainerRegistry.drainFluidContainer(drainingItemStack);
                }

                handleRecipeOutput(recipe);

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;

                return drainingItemStack;
            }
        }

        return itemStack;
    }

    private int getDrainCountForAmount(ItemStack itemStack, int requiredAmount) {
        int drainedAmount = 0;
        int drainedCount = 0;
        ItemStack drainedItemStack = itemStack;
        FluidStack originalFluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
        while (drainedAmount < requiredAmount && FluidContainerRegistry.isFilledContainer(drainedItemStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(drainedItemStack);
            // Ensure the fluid is still the same
            if (!originalFluidStack.isFluidEqual(fluidStack)) {
                break;
            }

            int availableAmount = fluidStack.amount;
            drainedItemStack = FluidContainerRegistry.drainFluidContainer(drainedItemStack);
            drainedAmount += availableAmount;
            drainedCount++;
        }

        // Ensure the exact amount was drained
        if (drainedAmount == requiredAmount) {
            return drainedCount;
        } else {
            return 0;
        }
    }

    public ItemStack removeLiquid(ItemStack is) {
        if (is == null || is.stackSize > 1) {
            return is;
        }

        if (hasLid() || !hasFluid()) {
            return is;
        }

        FluidStack topFluidStack = getTopFluidStack();

        if (FluidContainerRegistry.isEmptyContainer(is)) {
            ItemStack out = FluidContainerRegistry.fillFluidContainer(topFluidStack, is);
            if (out != null) {
                removeLiquidAmount(FluidContainerRegistry.getFluidForFilledItem(out).amount);

                return out;
            }
        } else if (is.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
            if (isfs == null || topFluidStack.isFluidEqual(isfs)) {
                removeLiquidAmount(((IFluidContainerItem) is.getItem()).fill(is, topFluidStack, true));

                return is;
            }
        }

        return is;
    }

    private void removeLiquidAmount(int amount) {
        // Remove liquid from the top layer first
        if (hasTopLayerFluid()) {
            fluids[FLUID_TOP_LAYER].amount -= amount;
            if (fluids[FLUID_TOP_LAYER].amount == 0) {
                fluids[FLUID_TOP_LAYER] = null;
            }
        } else {
            fluids[FLUID_PRIMARY].amount -= amount;
            if (fluids[FLUID_PRIMARY].amount == 0) {
                fluids[FLUID_PRIMARY] = null;
            }
        }

        // Removing any liquid, in this case the liquid is separated,
        // removes the option to return the liquid to what it was before separation,
        // by picking up the container
        preventRemixingSeparatedLiquids();

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        clientNeedToUpdate = true;

        onRecipeParametersChanged(true);
    }

    public void drainTopLiquid() {
        if (hasFluid()) {
            if (hasTopLayerFluid()) {
                fluids[FLUID_TOP_LAYER] = null;
            } else {
                fluids[FLUID_PRIMARY] = null;
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            onRecipeParametersChanged(true);
        }
    }

    public void setLiquidEmptyNoUpdate() {
        fluids[FLUID_PRIMARY] = null;
        fluids[FLUID_TOP_LAYER] = null;
        fluids[FLUID_BEFORE_SEPARATION] = null;
    }

    public boolean hasTopLayerFluid() {
        return fluids[FLUID_TOP_LAYER] != null;
    }

    public FluidStack getTopLayerFluidStack() {
        return fluids[FLUID_TOP_LAYER];
    }

    public FluidStack getPrimaryFluidStack() {
        return fluids[FLUID_PRIMARY];
    }

    private void giveItemStackToPlayer(EntityPlayer player, ItemStack itemStack) {
        final EntityItem ei = new EntityItem(worldObj, player.posX, player.posY, player.posZ, itemStack);
        worldObj.spawnEntityInWorld(ei);
    }

    protected boolean isValidAccessoryItemStack(ItemStack itemStack) {
        for (ItemStack is : OreDictionary.getOres("itemCookingPotAccessory")) {
            if (is.getItem() == itemStack.getItem() && (is.getItemDamage() == OreDictionary.WILDCARD_VALUE || is.getItemDamage() == itemStack.getItemDamage())) {
                return true;
            }
        }

        return false;
    }

    protected boolean isValidAccessorySteamingMeshItemStack(ItemStack itemStack) {
        for (ItemStack is : OreDictionary.getOres("itemCookingPotAccessorySteamingMesh")) {
            if (is.getItem() == itemStack.getItem() && (is.getItemDamage() == OreDictionary.WILDCARD_VALUE || is.getItemDamage() == itemStack.getItemDamage())) {
                return true;
            }
        }

        return false;
    }

    protected boolean isValidLidItemStack(ItemStack itemStack) {
        return Block.getBlockFromItem(itemStack.getItem()) == BidsBlocks.cookingPotLid && itemStack.getItemDamage() == 1;
    }

    public boolean hasInputItem() {
        return storage[SLOT_INPUT] != null;
    }

    public boolean hasLid() {
        return storage[SLOT_LID] != null;
    }

    public boolean hasAccessory() {
        return storage[SLOT_ACCESSORY] != null;
    }

    public boolean hasSteamingMesh() {
        return hasAccessory() && isValidAccessorySteamingMeshItemStack(storage[SLOT_ACCESSORY]);
    }

    public boolean hasFluid() {
        return fluids[FLUID_PRIMARY] != null;
    }

    public ItemStack getInputItemStack() {
        return storage[SLOT_INPUT];
    }

    public ItemStack getAccessoryItemStack() {
        return storage[SLOT_ACCESSORY];
    }

    public int getMaxFluidVolume() {
        return 5000;
    }

    public int getTotalLiquidVolume() {
        int total = 0;

        if (fluids[FLUID_PRIMARY] != null) {
            total += fluids[FLUID_PRIMARY].amount;
        }

        if (fluids[FLUID_TOP_LAYER] != null) {
            total += fluids[FLUID_TOP_LAYER].amount;
        }

        return total;
    }

    public FluidStack getTopFluidStack() {
        if (fluids[FLUID_TOP_LAYER] != null) {
            return fluids[FLUID_TOP_LAYER];
        }

        if (fluids[FLUID_PRIMARY] != null) {
            return fluids[FLUID_PRIMARY];
        }

        return null;
    }

    public void onBreakBlock() {
        // Reset placement and heat level
        placement = EnumCookingPotPlacement.GROUND;
        heatLevel = EnumCookingHeatLevel.NONE;

        // Cancel recipe progress
        recipeProgress = null;

        if (fluids[FLUID_BEFORE_SEPARATION] != null) {
            // When block is broken - that is picked up -
            // any separated liquid is mixed together again
            remixSeparatedFluids();
        }
    }

    private void onHeatLevelChanged() {
        Bids.LOG.debug("Heat level changed: " + heatLevel);

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        clientNeedToUpdate = true;

        onRecipeParametersChanged(false);
    }

    private void onRecipeParametersChanged(boolean cancelRecipe) {
        Bids.LOG.debug("Recipe parameters changed - recipe cancellation: " + cancelRecipe);

        isCachedRecipeValid = false;

        // By default, recipes can pause when parameters change
        // unless a change occurred that cancels the recipe
        if (cancelRecipe) {
            recipeCanPauseWhenParametersChange = false;
        }

        // Handle the recipe progress after delay
        recipeHandleTimer.delay(RECIPE_HANDLE_INTERVAL_AFTER_PARAMETER_CHANGE);
    }

    private void remixSeparatedFluids() {
        fluids[FLUID_PRIMARY] = fluids[FLUID_BEFORE_SEPARATION];
        fluids[FLUID_TOP_LAYER] = null;
        fluids[FLUID_BEFORE_SEPARATION] = null;
    }

    private void preventRemixingSeparatedLiquids() {
        fluids[FLUID_BEFORE_SEPARATION] = null;
    }

    private void handleRecipeProgress() {
        boolean update = false;

        CookingRecipe recipe = getCachedRecipe();

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
                }

                // Determine how much progress is made
                long lastUpdateTicks = recipeProgress.getLastUpdateTicks();
                long elapsedTicks = TFC_Time.getTotalTicks() - lastUpdateTicks;
                long totalRecipeTicks = recipe.getTime() * recipeProgress.getTotalRuns();
                float progress = (float)elapsedTicks / totalRecipeTicks;
                float heatAdjustedProgress = adjustProgressForHeat(recipe, getHeatLevel(), progress);

                int lastProgressRounded = recipeProgress.getProgressRounded();
                recipeProgress.addProgress(heatAdjustedProgress);

                if (recipeProgress.getProgress() == 1f) {
                    Bids.LOG.debug("Recipe progress completed: " + recipeProgress.getOutputHashString());

                    handleRecipeOutput(recipe);

                    recipeProgress = null;

                    // Update the cached recipe that may be invalid after completion
                    isCachedRecipeValid = false;
                    recipe = getCachedRecipe();

                    update = true;
                } else {
                    Bids.LOG.debug("Recipe progress updated: " + recipeProgress.getOutputHashString() + " - " + recipeProgress.getProgress());

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
                if (recipeCanPauseWhenParametersChange && recipeProgress.getProgress() > 0.1f) {
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

        if (recipeProgress == null) {
            // No recipe is in progress
            // see if we can start one
            if (recipe != null) {
                String displayText = CookingRecipeHelper.getRecipeOutputDisplayText(recipe);
                String hashString = CookingRecipeHelper.getRecipeHashString(recipe);
                int totalRuns = calculateTotalRecipeRuns(recipe);

                recipeProgress = new CookingRecipeProgress(displayText, hashString, totalRuns);
                update = true;

                Bids.LOG.debug("Recipe progress started: " + recipeProgress.getOutputHashString());
            }
        }

        if (update) {
            // Sync data with the client
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;
        }

        // By default, pause recipe unless explicitly stated otherwise
        recipeCanPauseWhenParametersChange = true;
    }

    private void handleRecipeOutput(CookingRecipe recipe) {
        int runs = calculateTotalRecipeRuns(recipe);

        if (recipe.getOutputItemStack() != null) {
            // When the recipe has output item stack,
            // any input stack is always replaced with the output item stack
            storage[SLOT_INPUT] = recipe.getOutputItemStack().copy();
            if (storage[SLOT_INPUT].getItem() instanceof ItemFoodTFC) {
                // Multiply the weight for foodstuff
                float weight = Food.getWeight(storage[SLOT_INPUT]);
                Bids.LOG.info("weight: " + weight);
                Food.setWeight(storage[SLOT_INPUT], weight * runs);
                Bids.LOG.info("weight after: " + Food.getWeight(storage[SLOT_INPUT]));
            } else {
                // Multiply the stack size for non foodstuff
                storage[SLOT_INPUT].stackSize *= runs;
            }
        } else if (recipe.getInputItemStack() != null) {
            // Otherwise consume appropriate size of the input item stack
            int consumedStackSize = recipe.getInputItemStack().stackSize * runs;

            Bids.LOG.debug("availableStackSize: " + storage[SLOT_INPUT].stackSize);
            Bids.LOG.debug("consumedStackSize: " + consumedStackSize);

            storage[SLOT_INPUT].stackSize -= consumedStackSize;

            if (storage[SLOT_INPUT].stackSize < 0) {
                Bids.LOG.warn("Recipe consumed more items than available");
                storage[SLOT_INPUT].stackSize = 0;
            }

            if (storage[SLOT_INPUT].stackSize == 0) {
                storage[SLOT_INPUT] = null;
            }
        }

        if (recipe.getOutputFluidStack() != null) {
            // When the recipe has output fluid stack
            // any input fluid stack is always replaced
            // or merged with existing liquid
            if (hasFluid() && !hasTopLayerFluid() && recipe.getInputFluidStack() == null) {
                fluids[FLUID_PRIMARY].amount += recipe.getOutputFluidStack().amount * runs;
            } else {
                fluids[FLUID_PRIMARY] = recipe.getOutputFluidStack().copy();
                fluids[FLUID_PRIMARY].amount *= runs;

                // When two liquids are created, the secondary one goes to the top
                if (recipe.getSecondaryOutputFluidStack() != null) {
                    fluids[FLUID_TOP_LAYER] = recipe.getSecondaryOutputFluidStack();
                    fluids[FLUID_TOP_LAYER].amount *= runs;
                }
            }
        } else if (recipe.getInputFluidStack() != null) {
            // Otherwise consume appropriate amount of the input fluid stack
            int consumedAmount = recipe.getInputFluidStack().amount * runs;

            Bids.LOG.debug("availableAmount: " + fluids[FLUID_PRIMARY].amount);
            Bids.LOG.debug("consumedAmount: " + consumedAmount);

            fluids[FLUID_PRIMARY].amount -= consumedAmount;

            if (fluids[FLUID_PRIMARY].amount < 0) {
                Bids.LOG.warn("Recipe consumed more fluid than available");
                fluids[FLUID_PRIMARY].amount = 0;
            }

            if (fluids[FLUID_PRIMARY].amount == 0) {
                fluids[FLUID_PRIMARY] = null;
            }
        }
    }

    private float adjustProgressForHeat(CookingRecipe recipe, EnumCookingHeatLevel heatLevel, float progress) {
        if (recipe.getMinHeatLevel() == null || recipe.getMinHeatLevel() == EnumCookingHeatLevel.NONE) {
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

    private int calculateTotalRecipeRuns(CookingRecipe recipe) {
        // Melting recipes (solid input changes and liquid output) are processed one set of ingredients at a time
        // Total time is one run of the recipe
        if (recipe.getInputItemStack() != null && recipe.getOutputItemStack() == null
            && recipe.getOutputFluidStack() != null && recipe.getInputFluidStack() == null) {
            return 1;
        }

        // Total recipe time depends on both the solid and liquid input
        if (recipe.getInputItemStack() != null && recipe.getInputFluidStack() != null) {
            int itemRuns = getRunsForInputItemStack(recipe);
            int fluidRuns = getRunsForInputFluidStack(recipe);

            // If recipe also has fluid output, ensure enough items
            if (recipe.getOutputFluidStack() != null && itemRuns < fluidRuns) {
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

    private int getRunsForInputFluidStack(CookingRecipe recipe) {
        int recipeAmount = recipe.getInputFluidStack().amount;
        int actualInputAmount = getTotalLiquidVolume();
        return (int)Math.floor((float)actualInputAmount / recipeAmount);
    }

    private int getRunsForInputItemStack(CookingRecipe recipe) {
        int recipeStackSize = recipe.getInputItemStack().stackSize;
        int actualInputStackSize = getInputItemStack().stackSize;
        return (int)Math.floor((float)actualInputStackSize / recipeStackSize);
    }

    private void handleInputItemTicking() {
        if (getInputItemStack() != null && isCookableFoodItemStack(getInputItemStack())) {
            boolean lastCooked = Food.isCooked(getInputItemStack());
            int lastCookedLevel = CookingHelper.getItemStackCookedLevel(getInputItemStack());
            int requiredFluidAmount = CookingHelper.calculateRequiredCookingFluidAmount(Food.getWeight(getInputItemStack()), hasSteamingMesh(), lastCookedLevel);

            // Cooking requires heat, input fluid (valid and sufficient amount), and lid when steaming
            boolean canCook = getHeatLevel() != EnumCookingHeatLevel.NONE &&
                hasFluid() && !hasTopLayerFluid() &&
                CookingHelper.isValidCookingFluid(getPrimaryFluidStack(), hasSteamingMesh()) &&
                getPrimaryFluidStack().amount >= requiredFluidAmount &&
                (!hasSteamingMesh() || hasLid());

            // Cooking down requires no heat
            boolean canCoolDown = getHeatLevel() == EnumCookingHeatLevel.NONE;

            if (canCook) {
                handleItemStackHeating(getInputItemStack());
            } else if (canCoolDown) {
                handleItemStackCooling(getInputItemStack());
            }

            TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, canCook);

            if (lastCooked != Food.isCooked(getInputItemStack())) {
                // When the food is cooked for the first time alter the taste
                CookingHelper.setInputStackCookedNBT(getInputItemStack(), getPrimaryFluidStack(), hasSteamingMesh());
            }

            if (canCook && lastCookedLevel != CookingHelper.getItemStackCookedLevel(getInputItemStack())) {
                // Whenever the cooked level is raised some liquid is consumed
                fluids[FLUID_PRIMARY].amount -= requiredFluidAmount;
                if (fluids[FLUID_PRIMARY].amount == 0) {
                    fluids[FLUID_PRIMARY] = null;
                }

                Bids.LOG.debug("Consumed input liquid amount: " + requiredFluidAmount);

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;
            }
        }
    }

    private boolean isCookableFoodItemStack(ItemStack itemStack) {
        if ( itemStack.getItem() instanceof ItemFoodTFC
            && itemStack.getItem() instanceof ICookableFood) {

            // Check heat registry to ensure there is no output or meltTemp lower than 160
            // The assumption is that the output is for baking (cooking directly in the fire), such as bread
            // which should not be possible with boiling or steaming in a cooking pot
            // Therefore food that turns into another item when cooked, cannot be boiled
            // Grain based food can still be cooked, such pasta, as long as it's the same item
            // See weed can be cooked, even though it turns into soda ash
            // because cooking pot cannot reach the temperate at which it burns
            HeatIndex hi = HeatRegistry.getInstance().findMatchingIndex(itemStack);
            return !hi.hasOutput() || hi.meltTemp > 150;
        }

        return false;
    }

    private void handleItemStackCooling(ItemStack itemStack) {
        float temp = TFC_ItemHeat.getTemp(itemStack);
        if (temp == 0) {
            temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);
        }

        int cookedTempIndex = ((ItemFoodTFC) itemStack.getItem()).cookTempIndex;
        float cookedTemp = Food.globalCookTemps[cookedTempIndex];
        float targetTemp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);

        if (Math.abs(targetTemp - temp) < cookedTemp) {
            // Jump to target at a threshold
            temp = targetTemp;
        } else {
            // Cool down fast in a liquid that is cooling down too
            float bonus = 10f;
            if (targetTemp > temp) {
                temp += (TFC_ItemHeat.getTempIncrease(itemStack, targetTemp) * ITEM_TICK_INTERVAL * bonus);
                temp = Math.min(targetTemp, temp);
            } else {
                temp -= (TFC_ItemHeat.getTempDecrease(itemStack, targetTemp) * ITEM_TICK_INTERVAL * bonus);
                temp = Math.max(targetTemp, temp);
            }
        }

        TFC_ItemHeat.setTemp(itemStack, temp, true);
    }

    private void handleItemStackHeating(ItemStack itemStack) {
        float temp = TFC_ItemHeat.getTemp(itemStack);
        if (temp == 0) {
            temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);
        }

        int cookedTempIndex = ((ItemFoodTFC) itemStack.getItem()).cookTempIndex;
        float cookedTemp = Food.globalCookTemps[cookedTempIndex];
        float targetTemp = cookedTemp * getMaxCookedTempMultiplier(cookedTempIndex);

        // Because food is boiled and steamed differently in a cooking pot
        // compared to roasting in the fire - that is, at lower temperatures
        // a bonus is provided so food cooking does not take forever
        // Cooking a large stack of food with LOW heat level will still take a while though
        float bonus = getBaseCookingBonus();
        bonus += (1 - Food.getWeight(itemStack) / Global.FOOD_MAX_WEIGHT) * 2;

        // Steaming takes 2x as long
        if (hasSteamingMesh()) {
            bonus /= 2;
        }

        if (targetTemp > temp) {
            // Also the targetTemp is increased for the calculation of speeding up heating
            temp += (TFC_ItemHeat.getTempIncrease(itemStack, targetTemp * 1.5f) * ITEM_TICK_INTERVAL * bonus);
        } else {
            temp -= (TFC_ItemHeat.getTempDecrease(itemStack, targetTemp) * ITEM_TICK_INTERVAL);
        }

        TFC_ItemHeat.setTemp(itemStack, temp, true);
    }

    private float getBaseCookingBonus() {
        switch (getHeatLevel()) {
            case HIGH:
                return 2f;
            case MEDIUM:
                return 1.25f;
            case LOW:
                return 1f;
        }

        return 0;
    }

    private float getMaxCookedTempMultiplier(int cookedTempIndex) {
        switch (getHeatLevel()) {
            case HIGH:
            case MEDIUM:
                // up to well done / medium
                return cookedTempIndex == 0 ? 1.9f : 1.4f;
            case LOW:
                // up to medium / light
                return cookedTempIndex == 0 ? 1.5f : 1.25f;
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)
    public boolean isClientDataLoaded() {
        return clientDataLoaded;
    }

    @SideOnly(Side.CLIENT)
    public boolean isInputItemSelected() {
        return inputItemSelected;
    }

    @SideOnly(Side.CLIENT)
    public void setInputItemSelected(boolean inputItemSelected) {
        this.inputItemSelected = inputItemSelected;
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            // When inventory content changes
            if (clientNeedToUpdate) {
                sendUpdateMessage(worldObj, xCoord, yCoord, zCoord);

                clientNeedToUpdate = false;
            }

            if (heatCheckTimer.tick()) {
                EnumCookingHeatLevel currentHeatLevel = placement.getPlacement().getHeatLevel(worldObj, xCoord, yCoord, zCoord);
                if (heatLevel != currentHeatLevel) {
                    heatLevel = currentHeatLevel;
                    onHeatLevelChanged();
                }
            }

            if (recipeHandleTimer.tick()) {
                handleRecipeProgress();
            }

            if (itemTickTimer.tick()) {
                handleInputItemTicking();
            }
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDataToNBT(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();

        // This forces client render after NBT data is loaded
        // until then the cooking pot cannot be fully rendered
        if (worldObj.isRemote && !clientDataLoaded) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientDataLoaded = true;
        }

        readDataFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readDataFromNBT(tag);
    }

    public void writeDataToNBT(NBTTagCompound tag) {
        if (placement != EnumCookingPotPlacement.GROUND) {
            tag.setString("placement", placement.name());
        }

        if (heatLevel != EnumCookingHeatLevel.NONE) {
            tag.setString("heatLevel", heatLevel.name());
        }

        if (hasLid() || hasAccessory() || hasInputItem()) {
            NBTTagList itemTagList = new NBTTagList();
            for (int i = 0; i < MAX_STORAGE; i++) {
                if (storage[i] != null) {
                    NBTTagCompound itemTag = new NBTTagCompound();
                    itemTag.setInteger("slot", i);
                    storage[i].writeToNBT(itemTag);
                    itemTagList.appendTag(itemTag);
                }
            }
            tag.setTag("storage", itemTagList);
        }

        if (hasFluid()) {
            NBTTagList fluidTagList = new NBTTagList();
            for (int i = 0; i < MAX_FLUIDS; i++) {
                if (fluids[i] != null) {
                    NBTTagCompound fluidTag = new NBTTagCompound();
                    fluidTag.setInteger("slot", i);
                    fluids[i].writeToNBT(fluidTag);
                    fluidTagList.appendTag(fluidTag);
                }
            }
            tag.setTag("fluids", fluidTagList);
        }

        if (recipeProgress != null) {
            NBTTagCompound progressTag = new NBTTagCompound();
            recipeProgress.writeToNBT(progressTag);
            tag.setTag("progress", progressTag);
        }
    }

    public void readDataFromNBT(NBTTagCompound tag) {
        try {
            placement = EnumCookingPotPlacement.valueOf(tag.getString("placement"));
        } catch (IllegalArgumentException e) {
            placement = EnumCookingPotPlacement.GROUND;
        }
        cachedBounds = null;

        try {
            heatLevel = EnumCookingHeatLevel.valueOf(tag.getString("heatLevel"));
        } catch (IllegalArgumentException e) {
            heatLevel = EnumCookingHeatLevel.NONE;
        }

        {
            for (int i = 0; i < MAX_STORAGE; i++) {
                storage[i] = null;
            }

            NBTTagList itemTagList = tag.getTagList("storage", 10);
            for (int i = 0; i < itemTagList.tagCount(); i++) {
                NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
                final int slot = itemTag.getInteger("slot");
                storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }

        {
            for (int i = 0; i < MAX_FLUIDS; i++) {
                fluids[i] = null;
            }

            NBTTagList fluidTagList = tag.getTagList("fluids", 10);
            for (int i = 0; i < fluidTagList.tagCount(); i++) {
                NBTTagCompound fluidTag = fluidTagList.getCompoundTagAt(i);
                final int slot = fluidTag.getInteger("slot");
                fluids[slot] = FluidStack.loadFluidStackFromNBT(fluidTag);
            }
        }

        if (tag.hasKey("progress")) {
            NBTTagCompound progressTag = tag.getCompoundTag("progress");
            recipeProgress = CookingRecipeProgress.readFromNBT(progressTag);
        } else {
            recipeProgress = null;
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return storage[SLOT_INPUT];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        if (itemStack == null) {
            if (storage[SLOT_INPUT] != null) {
                // Item has decayed out of existence
                storage[SLOT_INPUT] = null;

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;
            }
        } else {
            storage[SLOT_INPUT] = itemStack;
        }
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public void onTileEntityMessage(TileEntityUpdateMessage message) {
        worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
        Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
            + message.getZCoord());
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new TileEntityUpdateMessage(x, y, z, 0), tp);
        Bids.LOG.debug("Sent update message");
    }

}
