package com.unforbidable.tfc.bids.features.device.cookingpot.tileentity;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.cooking.CookedMeal;
import com.unforbidable.tfc.bids.api.features.cooking.CookingAccessory;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHost;
import com.unforbidable.tfc.bids.api.features.cooking.CookingLidUsage;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixture;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixtureFluid;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixtureItem;
import com.unforbidable.tfc.bids.api.features.cooking.CookingPotPlayerEvent;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingEngine;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTicker;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTickerResult;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingMixtureHelper;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingRecipeProgress;
import com.unforbidable.tfc.bids.features.device.cookingpot.main.CookingPotBounds;
import com.unforbidable.tfc.bids.features.device.cookingpot.main.CookingPotPlacement;
import com.unforbidable.tfc.bids.util.Timer;
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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityCookingPot extends TileEntity implements CookingHost, PacketHandler<SimpleUpdatePacket>, IInventory {

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
    private static final int ITEM_TICK_INTERVAL = 50;

    private final ItemStack[] storage = new ItemStack[MAX_STORAGE];
    private final FluidStack[] fluids = new FluidStack[MAX_FLUIDS];

    private final CookingEngine cookingEngine = new CookingEngine(this);
    private final CookingFoodTicker cookingFoodTicker = new CookingFoodTicker(this, ITEM_TICK_INTERVAL);

    private CookingPotPlacement placement = CookingPotPlacement.GROUND;
    private CookingPotBounds cachedBounds = null;
    private CookingHeatLevel heatLevel = CookingHeatLevel.NONE;

    boolean clientNeedToUpdate = false;
    boolean clientDataLoaded = false;
    boolean inputItemSelected = false;

    private final Timer heatCheckTimer = new Timer(HEAT_CHECK_INTERVAL);
    private final Timer itemTickTimer = new Timer(ITEM_TICK_INTERVAL);

    private int accessoryDamage = 0;

    public CookingRecipeProgress getRecipeProgress() {
        return cookingEngine.getRecipeProgress();
    }

    public CookingPotPlacement getPlacement() {
        return placement;
    }

    public void setPlacement(CookingPotPlacement placement) {
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

    @Override
    public CookingHeatLevel getHeatLevel() {
        return heatLevel;
    }

    public boolean placeItemStack(ItemStack itemStack, EntityPlayer player) {
        // Lid can be placed when there is no lid already
        if (isValidLidItemStack(itemStack) && !hasLid()) {
            storage[SLOT_LID] = itemStack.copy();
            storage[SLOT_LID].stackSize = 1;
            itemStack.stackSize--;

            Bids.LOG.debug("Placed lid: " + storage[SLOT_LID].getDisplayName());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(false);

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

            cookingEngine.onRecipeParametersChanged(false);

            return true;
        }

        // Cooking mixes can be added if there is no lid and accessory
        if (!hasLid() && !hasAccessory() && !hasTopLayerFluid() && itemStack.getItem() instanceof CookingMixtureItem) {
            CookingMixtureItem cookingMix = (CookingMixtureItem) itemStack.getItem();
            FluidStack fluidStack = cookingMix.getCookingFluid(itemStack);
            ItemStack emptyContainer = cookingMix.getEmptyContainer(itemStack);
            CookingMixtureFluid cookingFluid = (CookingMixtureFluid) fluidStack.getFluid();

            if (canPlaceCookingMix(fluidStack)) {
                float decay = Math.max(0, Food.getDecay(itemStack));
                float weight = Food.getWeight(itemStack);
                float decayRatio = decay / weight;
                if (decayRatio < 10) {
                    if (hasFluid()) {
                        FluidStack original = fluids[FLUID_PRIMARY].copy();
                        fluids[FLUID_PRIMARY].amount += fluidStack.amount;
                        cookingFluid.onCookingFluidCombined(fluids[FLUID_PRIMARY], original, fluidStack);
                    } else {
                        fluids[FLUID_PRIMARY] = fluidStack;
                        cookingFluid.onCookingFluidPlaced(fluids[FLUID_PRIMARY]);
                    }
                } else {
                    // Decay is too great and cooking mix is unusable
                    TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.cooking.cookingMixDecayed"));
                }

                // Consume and return empty container even when spoiled
                itemStack.stackSize--;
                giveItemStackToPlayer(player, emptyContainer);

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;

                cookingEngine.onRecipeParametersChanged(true);

                return true;
            }
        }

        // Cooked meals can be created from finished cooking mixes
        // into valid meal containers
        if (!hasLid() && !hasAccessory() && hasFluid() && getPrimaryFluidStack().getFluid() instanceof CookingMixtureFluid) {
            CookingMixtureFluid cookingFluid = (CookingMixtureFluid) getPrimaryFluidStack().getFluid();
            CookingMixture mixture = CookingMixtureHelper.getCookingMixture(getPrimaryFluidStack());
            if (mixture != null && mixture.isReady() && cookingFluid.isValidCookedMealContainer(getPrimaryFluidStack(), itemStack)) {
                ItemStack cookedMeal = cookingFluid.retrieveCookedMeal(getPrimaryFluidStack(), itemStack, player, true);

                if (cookedMeal != null) {
                    if (cookedMeal.getItem() instanceof CookedMeal) {
                        ((CookedMeal)cookedMeal.getItem()).onCookedMealCreated(cookedMeal, player);
                    }

                    CookingPotPlayerEvent event = new CookingPotPlayerEvent(player, this, CookingPotPlayerEvent.Action.RETRIEVE_COOKED_MEAL, cookedMeal);
                    MinecraftForge.EVENT_BUS.post(event);

                    if (!event.isCanceled()) {
                        if (getPrimaryFluidStack().amount <= 0) {
                            fluids[FLUID_PRIMARY] = null;
                        }

                        itemStack.stackSize--;
                        giveItemStackToPlayer(player, cookedMeal);

                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        clientNeedToUpdate = true;

                        cookingEngine.onRecipeParametersChanged(true);

                        return true;
                    }
                }
            }
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

            // Cancel any recipe that may be in progress
            // otherwise instant recipes returning output immediately will not work
            if (cookingEngine.cancelRecipeProgress()) {
                // Sync data with the client
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;
            }

            // Process recipe right after item is placed
            // and remember the input item
            ItemStack before = storage[SLOT_INPUT].copy();
            cookingEngine.handleRecipeProgress();

            if (hasInputItem() && !ItemStack.areItemStacksEqual(before, storage[SLOT_INPUT])) {
                // If the input item changed
                // the recipe completed immediately
                // so return the crafted item immediately
                giveItemStackToPlayer(player, storage[SLOT_INPUT]);
                storage[SLOT_INPUT] = null;

                cookingEngine.onRecipeParametersChanged(true);
            }

            return true;
        }

        return false;
    }

    private boolean canPlaceCookingMix(FluidStack fluidStack) {
        if (hasFluid()) {
            return getPrimaryFluidStack().amount + fluidStack.amount <= getMaxCombinedCookingFluidVolume() &&
                CookingMixtureHelper.canCombineCookingFluids(getPrimaryFluidStack(), fluidStack);
        } else {
            return fluidStack.amount <= getMaxCombinedCookingFluidVolume();
        }
    }

    private int getInputItemStackSizeForRecipe(ItemStack itemStack) {
        if (CookingHelper.isCookableFoodItemStack(itemStack)) {
            // Food items default to stack size of 1
            return 1;
        } else {
            // Look for all recipes matching specific input item
            // as we don't know the final parameters for the recipes
            // and manually ensure certain parameters such as steaming mesh and existing liquid
            for (CookingRecipe recipe : CookingHelper.getRecipesMatchingInput(itemStack)) {
                // Ensure accessory matches
                if (recipe.getAccessory() == CookingAccessory.NONE && hasAccessory() ||
                    recipe.getAccessory() == CookingAccessory.STEAMING_MESH && !hasSteamingMesh()) {
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

                    // If input item is food
                    // we take the whole stack to be processed fully or partially
                    if (recipe.getInputItemStack().getItem() instanceof ItemFoodTFC) {
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

    @Override
    public CookingRecipeInputTemplate createRecipeTemplate() {
        return new CookingRecipeInputTemplate(
            getPrimaryFluidStack(), null,
            getInputItemStack(),
            hasSteamingMesh() ? CookingAccessory.STEAMING_MESH : CookingAccessory.NONE,
            hasLid() ? CookingLidUsage.ON : CookingLidUsage.OFF,
            getHeatLevel(), getHeatLevel());
    }

    @Override
    public boolean isValidCookingRecipe(CookingRecipe recipe) {
        return true;
    }

    @Override
    public boolean isValidFluidStack(FluidStack inFS) {
        return true;
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

            cookingEngine.onRecipeParametersChanged(false);

            return true;
        } else if (hasInputItem()) {
            Bids.LOG.debug("Retrieving input item: " + storage[SLOT_INPUT].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_INPUT]);
            storage[SLOT_INPUT] = null;

            applyAccessoryDamage();

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(true);

            return true;
        } else if (hasAccessory()) {
            Bids.LOG.debug("Retrieving accessory: " + storage[SLOT_ACCESSORY].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_ACCESSORY]);
            storage[SLOT_ACCESSORY] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(true);

            return true;
        } else {
            return false;
        }
    }

    private void applyAccessoryDamage() {
        // When steaming mesh is used, and the accumulated damage exceeds the durability
        // this is when the item stack is destroyed
        if (accessoryDamage > 0 && hasAccessory() && getAccessoryItemStack().getMaxDamage() > 0) {
            Bids.LOG.debug("accessoryDamage (applied): " + accessoryDamage);
            Bids.LOG.debug("getAccessoryItemStack().getMaxDamage(): " + getAccessoryItemStack().getMaxDamage());
            Bids.LOG.debug("getAccessoryItemStack().getItemDamage(): " + getAccessoryItemStack().getItemDamage());

            getAccessoryItemStack().setItemDamage(getAccessoryItemStack().getItemDamage() + accessoryDamage);
            accessoryDamage = 0;

            Bids.LOG.debug("getAccessoryItemStack().getItemDamage() (after): " + getAccessoryItemStack().getItemDamage());

            if (storage[SLOT_ACCESSORY].getItemDamage() >= storage[SLOT_ACCESSORY].getMaxDamage()) {
                storage[SLOT_ACCESSORY] = null;
            }
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

            cookingEngine.onRecipeParametersChanged(true);

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
        if (hasLid() || hasTopLayerFluid()) {
            return itemStack;
        }

        if (hasFluid() && !hasTopLayerFluid()) {
            int drainedAmount = cookingEngine.handleFluidMixing(itemStack);
            if (drainedAmount > 0) {
                ItemStack drainingItemStack = CookingHelper.doDrainAmount(itemStack, drainedAmount);

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                clientNeedToUpdate = true;

                // Mixing liquids resets recipe parameters
                cookingEngine.onRecipeParametersChanged(true);

                return drainingItemStack;
            }
        }

        return itemStack;
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
                int amount = ((IFluidContainerItem) is.getItem()).fill(is, topFluidStack, true);
                if (amount > 0) {
                    removeLiquidAmount(amount);
                }

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

        cookingEngine.onRecipeParametersChanged(true);
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

            cookingEngine.onRecipeParametersChanged(true);
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

    public int getMaxCombinedCookingFluidVolume() {
        return 1000;
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

    @Override
    public FluidStack getFluidStack() {
        return getTopFluidStack();
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
        placement = CookingPotPlacement.GROUND;
        heatLevel = CookingHeatLevel.NONE;

        // Cancel recipe progress
        cookingEngine.cancelRecipeProgress();

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

        cookingEngine.onRecipeParametersChanged(false);
    }

    private void remixSeparatedFluids() {
        fluids[FLUID_PRIMARY] = fluids[FLUID_BEFORE_SEPARATION];
        fluids[FLUID_TOP_LAYER] = null;
        fluids[FLUID_BEFORE_SEPARATION] = null;
    }

    private void preventRemixingSeparatedLiquids() {
        fluids[FLUID_BEFORE_SEPARATION] = null;
    }

    @Override
    public void onItemChanged(ItemStack output) {
        if (output == null) {
            // When input item is consumed, apply any accumulated accessory damage
            applyAccessoryDamage();
        }

        storage[SLOT_INPUT] = output;

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();

        clientNeedToUpdate = true;
    }

    @Override
    public void onFluidChanged(FluidStack output, FluidStack secondaryOutput) {
        if (output != null && secondaryOutput != null) {
            // When two liquids are created, the secondary one goes to the top
            // Remember the input fluid to allow remixing
            fluids[FLUID_BEFORE_SEPARATION] = fluids[FLUID_PRIMARY];
            fluids[FLUID_PRIMARY] = output;
            fluids[FLUID_TOP_LAYER] = secondaryOutput;
        } else {
            fluids[FLUID_PRIMARY] = output;
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();

        clientNeedToUpdate = true;
    }

    private void handleInputItemTicking() {
        CookingFoodTickerResult result = cookingFoodTicker.tick(getInputItemStack(), getFluidStack(), heatLevel, hasSteamingMesh(), hasLid(), !hasTopLayerFluid());
        if (result.updated) {
            if (fluids[FLUID_PRIMARY].amount == 0) {
                fluids[FLUID_PRIMARY] = null;
            }

            // When steaming, the steaming mesh takes 1 damage
            if (hasSteamingMesh()) {
                accessoryDamage += 1;
                Bids.LOG.debug("accessoryDamage: " + accessoryDamage);
            }

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();

            clientNeedToUpdate = true;
        }
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
                sendUpdateMessage();

                clientNeedToUpdate = false;
            }

            if (heatCheckTimer.tick()) {
                CookingHeatLevel currentHeatLevel = placement.getPlacement().getHeatLevel(worldObj, xCoord, yCoord, zCoord);
                if (heatLevel != currentHeatLevel) {
                    heatLevel = currentHeatLevel;
                    onHeatLevelChanged();
                }
            }

            if (cookingEngine.update()) {
                // Sync data with the client
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
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
        if (placement != CookingPotPlacement.GROUND) {
            tag.setString("placement", placement.name());
        }

        if (heatLevel != CookingHeatLevel.NONE) {
            tag.setString("heatLevel", heatLevel.name());
        }

        if (hasAccessory() && accessoryDamage > 0) {
            tag.setInteger("accessoryDamage", accessoryDamage);
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

        cookingEngine.writeDataToNBT(tag);
    }

    public void readDataFromNBT(NBTTagCompound tag) {
        try {
            placement = CookingPotPlacement.valueOf(tag.getString("placement"));
        } catch (IllegalArgumentException e) {
            placement = CookingPotPlacement.GROUND;
        }
        cachedBounds = null;

        try {
            heatLevel = CookingHeatLevel.valueOf(tag.getString("heatLevel"));
        } catch (IllegalArgumentException e) {
            heatLevel = CookingHeatLevel.NONE;
        }

        accessoryDamage = tag.getInteger("accessoryDamage");

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

        cookingEngine.readDataFromNBT(tag);
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
    public void handleNetworkPacket(SimpleUpdatePacket packet) {
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        Bids.LOG.debug("Client updated at: [{},{},{}]", xCoord, yCoord, zCoord);
    }

    public void sendUpdateMessage() {
        Network.sendToTileEntity(new SimpleUpdatePacket(), this);
        Bids.LOG.debug("Sent update message");
    }

}
