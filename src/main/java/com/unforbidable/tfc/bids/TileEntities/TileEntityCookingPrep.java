package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemMeal;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Containers.Slots.ISlotTracker;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPrep.PrepVirtualCuttingRecipe;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.BidsFood;
import com.unforbidable.tfc.bids.api.Crafting.PrepManager;
import com.unforbidable.tfc.bids.api.Crafting.PrepRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IMoreSandwich;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;

public class TileEntityCookingPrep extends TileEntity implements IInventory, ISlotTracker {

    private final static int MAX_STORAGE = 10;
    private static final int SLOT_OUTPUT = 5;

    private static final int ITEM_TICK_INTERVAL = 50;
    private static final int UPDATE_WEIGHTS_INTERVAL = 1;

    public ItemStack[] storage = new ItemStack[MAX_STORAGE];
    public int[] dayStored = new int[MAX_STORAGE];

    private final Timer itemTickTimer = new Timer(ITEM_TICK_INTERVAL);
    private final Timer updateWeightsTimer = new Timer(UPDATE_WEIGHTS_INTERVAL);

    private float[] recipeIngredientWeights = null;
    private boolean needToUpdateWeights = false;

    public float[] getRecipeIngredientWeights() {
        return recipeIngredientWeights;
    }

    public void onBlockBroken() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null && i != SLOT_OUTPUT) {
                final EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, storage[i]);
                worldObj.spawnEntityInWorld(ei);

                storage[i] = null;
            }
        }
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (itemTickTimer.tick()) {
                handleItemTicking();
            }
        } else {
            if (updateWeightsTimer.tick()) {
                if (needToUpdateWeights) {
                    updateRecipeWeights();
                    needToUpdateWeights = false;
                }
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

    public void readDataFromNBT(NBTTagCompound tag) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            storage[i] = null;
        }

        NBTTagList itemTagList = tag.getTagList("storage", 10);
        for (int i = 0; i < itemTagList.tagCount(); i++) {
            NBTTagCompound itemTag = itemTagList.getCompoundTagAt(i);
            final int slot = itemTag.getInteger("slot");
            storage[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }

        // Update weights shown in the GUI for the current recipe
        if (worldObj != null && worldObj.isRemote) {
            needToUpdateWeights = true;
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (storage[i] != null) {
            if (storage[i].stackSize <= j) {
                ItemStack itemstack = storage[i];
                storage[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = storage[i].splitStack(j);
            if (storage[i].stackSize == 0) {
                storage[i] = null;
            }
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public int getSizeInventory() {
        return MAX_STORAGE;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return storage[i];
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if (!TFC_Core.areItemsEqual(storage[i], itemstack)) {
            if (dayStored[i] != -1 && itemstack == null) {
                dayStored[i] = -1;
            } else if (dayStored[i] <= TFC_Time.getTotalDays() && itemstack != null) {
                dayStored[i] = TFC_Time.getTotalDays();
            }
        }
        storage[i] = itemstack;
    }

    @Override
    public String getInventoryName() {
        return "CookingPrep";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return false;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
        if (worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

    @Override
    public void onSlotChanged(Slot slot) {
        if (slot.slotNumber != SLOT_OUTPUT) {
            onIngredientsChanged();
        }
    }

    @Override
    public void onPickupFromSlot(Slot slot, EntityPlayer player, ItemStack itemStack) {
        if (slot.slotNumber == SLOT_OUTPUT) {
            onOutputPickedFromSlot(itemStack, player);
        }
    }

    private void handleItemTicking() {
        ItemStack[] prev = new ItemStack[MAX_STORAGE];
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                prev[i] = storage[i].copy();
            }
        }

        TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, false);

        if (detectStorageChanges(prev)) {
            if (!worldObj.isRemote) {
                onIngredientsChanged();
            }
        }
    }

    private boolean detectStorageChanges(ItemStack[] prev) {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (prev[i] != null) {
                if (storage[i] == null) {
                    return true;
                }

                if (prev[i].stackSize != storage[i].stackSize) {
                    return true;
                }

                if (prev[i].getItem() instanceof IFood) {
                    if (Food.getDecay(prev[i]) != Food.getDecay(storage[i])) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void onIngredientsChanged() {
        if (!worldObj.isRemote) {
            updateRecipeResultPreview();
        } else {
            needToUpdateWeights = true;
        }
    }

    private void onOutputPickedFromSlot(ItemStack itemStack, EntityPlayer player) {
        Bids.LOG.debug("Picked output " + itemStack.getDisplayName());

        if (!worldObj.isRemote) {
            ItemStack result = getMatchingRecipeResult(true);
            if (result != null) {
                removeConsumedIngredients();

                if (!recipeResultsAreEqual(result, itemStack)) {
                    Bids.LOG.warn("Item picked is different from the item crafted while consuming ingredients");
                }

                Bids.LOG.debug("Consumed ingredients");
            } else {
                Bids.LOG.warn("Could not consume ingredients because recipe was not found.");
            }

            // Set decay when the output is picked as if it was just created
            Food.setDecayTimer(itemStack, (int)TFC_Time.getTotalHours() + 1);

            // Default decay rate for meals is 2x
            Food.setDecayRate(itemStack, 2);

            if (player != null && itemStack.getItem() instanceof ItemMeal) {
                int skillIncrease = getCookingSkillIncrease(itemStack);

                // Advance player's skill
                TFC_Core.getSkillStats(player).increaseSkill(Global.SKILL_COOKING, skillIncrease);

                // Save player's skill to the crafted item
                Food.setMealSkill(itemStack, TFC_Core.getSkillStats(player).getSkillRank(Global.SKILL_COOKING).ordinal());
            }

            if (itemStack.getItem() instanceof IMoreSandwich) {
                ((IMoreSandwich)itemStack.getItem()).onCrafted(itemStack, player);
            }

            updateRecipeResultPreview();

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } else {
            needToUpdateWeights = true;
        }
    }

    private int getCookingSkillIncrease(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IMoreSandwich) {
            return ((IMoreSandwich) itemStack.getItem()).getCookingSkillIncrease(itemStack);
        }

        return 1;
    }

    private void removeConsumedIngredients() {
        for (int i = 0; i < MAX_STORAGE; i++) {
            if (storage[i] != null) {
                if (storage[i].stackSize == 0) {
                    storage[i] = null;
                }
            }
        }
    }

    private void updateRecipeResultPreview() {
        ItemStack result = getMatchingRecipeResult(false);
        if (result != null) {
            Bids.LOG.debug("Result: " + result.getDisplayName() + "[" + result.stackSize + "]");

            // Preview will never decay
            Food.setDecayTimer(result, Integer.MAX_VALUE);

            if (!isSameRecipeResult(result)) {
                Bids.LOG.debug("Result changed");

                setInventorySlotContents(SLOT_OUTPUT, result);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        } else {
            if (storage[SLOT_OUTPUT] != null) {
                setInventorySlotContents(SLOT_OUTPUT, null);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
    }

    private ItemStack getMatchingRecipeResult(boolean consumeIngredients) {
        ItemStack[] ingredients = getIngredientsForRecipe(false);
        PrepRecipe recipe = PrepManager.getMatchingRecipe(ingredients);
        if (recipe != null) {
            return recipe.getResult(ingredients, consumeIngredients);
        }

        PrepRecipe virtualCuttingRecipe = getMatchingVirtualCuttingRecipe(ingredients);
        if (virtualCuttingRecipe != null) {
            return virtualCuttingRecipe.getResult(ingredients, consumeIngredients);
        }

        ItemStack[] ingredientsWithStoredVessel = getIngredientsForRecipe(true);
        PrepRecipe recipeWithStoredVessel = PrepManager.getMatchingRecipe(ingredientsWithStoredVessel);
        if (recipeWithStoredVessel != null) {
            return recipeWithStoredVessel.getResult(ingredientsWithStoredVessel, consumeIngredients);
        }

        return null;
    }

    private PrepRecipe getMatchingVirtualCuttingRecipe(ItemStack[] ingredients) {
        // If there is only one item placed in any slot from 1 to 4
        // create virtual cutting recipe
        int slot = 0;
        for (int i = 1; i <= 4; i++) {
            if (storage[i] != null) {
                if (slot == 0) {
                    slot = i;
                } else {
                    slot = -1;
                }
            }
        }

        // Exactly one item was found in slot
        if (slot > 0) {
            PrepVirtualCuttingRecipe virtualCuttingRecipe = PrepVirtualCuttingRecipe.forIngredientInSlot(storage[slot], slot);
            if (virtualCuttingRecipe.matches(ingredients)) {
                return virtualCuttingRecipe;
            }
        }

        return null;
    }

    private boolean isSameRecipeResult(ItemStack result) {
        return storage[SLOT_OUTPUT] != null && recipeResultsAreEqual(result, storage[SLOT_OUTPUT]);
    }

    private boolean recipeResultsAreEqual(ItemStack result, ItemStack prev) {
        if (prev.getItem() != result.getItem() ||
            prev.getItemDamage() != result.getItemDamage() ||
            !BidsFood.areEqual(prev, result) ||
            Food.getWeight(prev) != Food.getWeight(result) ||
            Food.getFoodGroups(prev).length != Food.getFoodGroups(prev).length) {
            return false;
        }

        int[] prevFoodGroups = Food.getFoodGroups(prev);
        int[] resultFoodGroups = Food.getFoodGroups(result);
        for (int i = 0; i < resultFoodGroups.length; i++) {
            if (prevFoodGroups[i] != resultFoodGroups[i]) {
                return false;
            }
        }

        return true;
    }

    private ItemStack[] getIngredientsForRecipe(boolean checkStorageForVessel) {
        ItemStack[] ingredients = Arrays.copyOfRange(storage, 0, 5);

        if (ingredients[0] == null && checkStorageForVessel) {
            // When the vessel slot is empty, look for bowls in the storage slots
            for (int i = 6; i <= 9; i++) {
                if (storage[i] != null) {
                    for (ItemStack is : OreDictionary.getOres("itemCookingPrepVessel", false)) {
                        if (OreDictionary.itemMatches(is, storage[i], false)) {
                            ingredients[0] = storage[i];
                            break;
                        }
                    }
                }

                if (ingredients[0] != null) {
                    break;
                }
            }
        }

        return ingredients;
    }

    private void updateRecipeWeights() {
        int count = 0;
        for (int i = 1; i <= 4; i++) {
            if (storage[i] != null) {
                count++;
            }
        }

        if (count == 1 && storage[0] == null) {
            // Exactly one ingredient and also empty vessel slot
            ItemStack[] ingredients = getIngredientsForRecipe(false);
            PrepRecipe recipe = getMatchingVirtualCuttingRecipe(ingredients);
            if (recipe != null) {
                recipeIngredientWeights = recipe.getIngredientWeights();
                Bids.LOG.debug("Updated weights for cutting: " + Arrays.toString(recipeIngredientWeights));
                return;
            }
        } else if (count > 1 || storage[0] != null) {
            ItemStack[] ingredientsWithStoredVessel = getIngredientsForRecipe(true);
            if (ingredientsWithStoredVessel[0] != null) {
                List<PrepRecipe> recipes = PrepManager.getRecipesUsingVessel(ingredientsWithStoredVessel[0]);
                if (recipes.size() > 0) {
                    recipeIngredientWeights = recipes.get(0).getIngredientWeights();
                    Bids.LOG.debug("Updated weights for recipe: " + Arrays.toString(recipeIngredientWeights));
                    return;
                }
            }
        }

        recipeIngredientWeights = null;
    }

}
