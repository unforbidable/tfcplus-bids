package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.CookingPotBounds;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.EnumCookingPotPlacement;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.Core.Network.Messages.TileEntityUpdateMessage;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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

public class TileEntityCookingPot extends TileEntity implements IMessageHanldingTileEntity<TileEntityUpdateMessage> {

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

    private final ItemStack[] storage = new ItemStack[MAX_STORAGE];
    private final FluidStack[] fluids = new FluidStack[MAX_FLUIDS];

    private EnumCookingPotPlacement placement = EnumCookingPotPlacement.GROUND;
    private CookingPotBounds cachedBounds = null;
    private EnumCookingHeatLevel heatLevel = EnumCookingHeatLevel.NONE;

    boolean clientNeedToUpdate = false;
    boolean clientDataLoaded = false;

    private final Timer heatCheckTimer = new Timer(HEAT_CHECK_INTERVAL);

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

    public EnumCookingHeatLevel getHeatLevel() {
        return heatLevel;
    }

    public boolean placeItemStack(ItemStack itemStack) {
        // Lid can be placed when there is no lid already
        if (isValidLidItemStack(itemStack) && !hasLid()) {
            storage[SLOT_LID] = itemStack.copy();
            storage[SLOT_LID].stackSize = 1;
            itemStack.stackSize--;

            Bids.LOG.info("Placed lid: " + storage[SLOT_LID].getDisplayName());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        }

        // Input item can be placed when there is no lid or another input item already
        if (isValidInputItemStack(itemStack) && !hasLid() && !hasInputItem()) {
            storage[SLOT_INPUT] = itemStack.copy();
            storage[SLOT_INPUT].stackSize = 1;
            itemStack.stackSize--;

            Bids.LOG.info("Placed input item: " + storage[SLOT_INPUT].getDisplayName());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        }

        // Accessory can be placed when there is no lid, input item or another accessory already
        if (isValidAccessoryItemStack(itemStack) && !hasLid() && !hasInputItem() && !hasAccessory()) {
            storage[SLOT_ACCESSORY] = itemStack.copy();
            storage[SLOT_ACCESSORY].stackSize = 1;
            itemStack.stackSize--;

            Bids.LOG.info("Placed accessory: " + storage[SLOT_ACCESSORY].getDisplayName());

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        }

        return false;
    }

    public boolean retrieveItemStack(EntityPlayer player) {
        if (hasLid()) {
            Bids.LOG.info("Retrieving lid: " + storage[SLOT_LID].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_LID]);
            storage[SLOT_LID] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        } else if (hasInputItem()) {
            Bids.LOG.info("Retrieving input item: " + storage[SLOT_INPUT].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_INPUT]);
            storage[SLOT_INPUT] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        } else if (hasAccessory()) {
            Bids.LOG.info("Retrieving accessory: " + storage[SLOT_ACCESSORY].getDisplayName());
            giveItemStackToPlayer(player, storage[SLOT_ACCESSORY]);
            storage[SLOT_ACCESSORY] = null;

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            clientNeedToUpdate = true;

            return true;
        } else {
            return false;
        }
    }

    private boolean addLiquid(FluidStack inFS) {
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
            FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(is);
            if (addLiquid(fs)) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return FluidContainerRegistry.drainFluidContainer(is);
            }
        } else if (is.getItem() instanceof IFluidContainerItem) {
            FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
            if (addLiquid(isfs)) {
                ((IFluidContainerItem) is.getItem()).drain(is, is.getMaxDamage(), true);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }

        return is;
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

    protected boolean isValidInputItemStack(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemFoodTFC;
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

        if (fluids[FLUID_BEFORE_SEPARATION] != null) {
            // When block is broken - that is picked up -
            // any separated liquid is mixed together again
            remixSeparatedFluids();
        }
    }

    private void onHeatLevelChanged() {
        Bids.LOG.info("Heat level changed: " + heatLevel);

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        clientNeedToUpdate = true;
    }

    private void remixSeparatedFluids() {
        fluids[FLUID_PRIMARY] = fluids[FLUID_BEFORE_SEPARATION];
        fluids[FLUID_TOP_LAYER] = null;
        fluids[FLUID_BEFORE_SEPARATION] = null;
    }

    private void preventRemixingSeparatedLiquids() {
        fluids[FLUID_BEFORE_SEPARATION] = null;
    }

    public boolean isClientDataLoaded() {
        return clientDataLoaded;
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

        {
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

        {
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
    }

    @Override
    public void onTileEntityMessage(TileEntityUpdateMessage message) {
        worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
        Bids.LOG.info("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
            + message.getZCoord());
    }

    public static void sendUpdateMessage(World world, int x, int y, int z) {
        NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new TileEntityUpdateMessage(x, y, z, 0), tp);
        Bids.LOG.info("Sent update message");
    }

}
