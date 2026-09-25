package com.unforbidable.tfc.bids.features.device.cookingpot.tileentity;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.cooking.CookingAccessory;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHost;
import com.unforbidable.tfc.bids.api.features.cooking.CookingLidUsage;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipeInputTemplate;
import com.unforbidable.tfc.bids.common.network.SimpleUpdatePacket;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingEngine;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTicker;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingFoodTickerResult;
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
import net.minecraftforge.fluids.Fluid;
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

    public boolean interactWithItemStack(ItemStack equippedItem, EntityPlayer player) {
        // Lid can be placed when there is no lid already
        if (isValidLidItemStack(equippedItem) && !hasLid()) {
            storage[SLOT_LID] = equippedItem.copy();
            storage[SLOT_LID].stackSize = 1;
            equippedItem.stackSize--;

            Bids.LOG.debug("Placed lid: " + storage[SLOT_LID].getDisplayName());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            cookingEngine.onRecipeParametersChanged(false);

            return true;
        }

        // No interaction when lid is on
        if (hasLid()) {
            return false;
        }

        return cookingEngine.handleItemInteraction(equippedItem, player);
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
    public boolean isValidFluid(Fluid fluid) {
        return true;
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
