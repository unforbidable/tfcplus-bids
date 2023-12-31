package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import com.unforbidable.tfc.bids.Core.Carving.CarvingMessage;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.api.CarvingRegistry;
import com.unforbidable.tfc.bids.api.Crafting.CarvingManager;
import com.unforbidable.tfc.bids.api.Crafting.CarvingRecipe;
import com.unforbidable.tfc.bids.api.Enums.EnumAdzeMode;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityCarving extends TileEntity implements IMessageHanldingTileEntity<CarvingMessage> {

    public final static int CARVING_DIMENSION = 4;

    public final static int ACTION_SELECT_BIT = 1;
    public final static int ACTION_UPDATE = 2;

    int carvedBlockId;
    int carvedBlockMetadata;
    CarvingBitMap carvedBits = new CarvingBitMap(CARVING_DIMENSION);
    int harvestDroppedCount = 0;
    boolean isCarvingLocked = false;

    CarvingBit selectedBit = CarvingBit.Empty;
    int selectedSide = 0;
    boolean clientInitialized = false;
    int carvedBitCount = 0;
    EnumAdzeMode carvingMode = EnumAdzeMode.DEFAULT_MODE;

    ItemStack cachedCraftingResult = null;
    boolean cachedCraftingResultIsValid = false;

    public TileEntityCarving() {
        super();
    }

    public void setCarvedBlockId(int carvedBlockId) {
        this.carvedBlockId = carvedBlockId;
    }

    public int getCarvedBlockId() {
        return carvedBlockId;
    }

    public void setCarvedBlockMetadata(int carvedBlockMetadata) {
        this.carvedBlockMetadata = carvedBlockMetadata;
    }

    public int getCarvedBlockMetadata() {
        return carvedBlockMetadata;
    }

    public void setCarvingLocked(boolean isCarvingLocked) {
        this.isCarvingLocked = isCarvingLocked;
    }

    public boolean isCarvingLocked() {
        return isCarvingLocked;
    }

    public ItemStack getCraftingResult() {
        if (!cachedCraftingResultIsValid) {
            final CarvingRecipe recipe = CarvingManager.findMatchingRecipe(carvedBlockId, carvedBlockMetadata,
                    carvedBits);
            cachedCraftingResult = recipe != null ? recipe.getCraftingResult() : null;
            cachedCraftingResultIsValid = true;
        }

        return cachedCraftingResult;
    }

    public boolean setSelectedBit(CarvingBit bit) {
        if (!bit.equals(selectedBit)) {
            if (!bit.isEmpty()) {
                Bids.LOG.debug("Selected bit " + bit.bitX + ", " + bit.bitY + ", " + bit.bitZ);
            } else {
                Bids.LOG.debug("Selected empty bit");
            }

            selectedBit = bit;
            return true;
        }

        return false;
    }

    public CarvingBit getSelectedBit() {
        return selectedBit;
    }

    public void setCarvingMode(EnumAdzeMode mode) {
        carvingMode = mode;
    }

    public EnumAdzeMode getCarvingMode() {
        return carvingMode;
    }

    public int getSelectedSide() {
        return selectedSide;
    }

    public void setSelectedSide(int selectedSide) {
        this.selectedSide = selectedSide;
    }

    public boolean carveSelectedBit() {
        if (!selectedBit.isEmpty()) {
            return carveBit(selectedBit, selectedSide);
        } else {
            Bids.LOG.warn("Cannot carve selected bit when none is selected");
            return false;
        }
    }

    private boolean carveBit(CarvingBit bit, int side) {
        if (canCarveBit(bit, side)) {
            Bids.LOG.debug("Can carve bit " + bit.bitX + ", " + bit.bitY + ", " + bit.bitZ + " side " + side);

            List<CarvingBit> bitsToCarve = carvingMode.getCarvingMode().getBitsToCarve(bit, side, carvedBits);

            if (!bitsToCarve.isEmpty()) {
                for (CarvingBit b : bitsToCarve) {
                    Bids.LOG.debug("Carved bit " + b.bitX + ", " + b.bitY + ", " + b.bitZ + " mode " + carvingMode);
                    carvedBits.setBit(b);
                }

                dropHarvestAtRatioCarved(getCarvedBitCount() / (float) getTotalBitCount());
                dropExtraHarvest();

                if (getCarvedBitCount() == getTotalBitCount()) {
                    Bids.LOG.debug("The entire carving was removed");
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                }

                cachedCraftingResultIsValid = false;

                return true;
            }
        }

        return false;
    }

    public boolean canCarveBit(CarvingBit bit, int side) {
        return carvingMode.getCarvingMode().canCarveBit(bit, side, carvedBits);
    }

    public boolean isBitCarved(int bitX, int bitY, int bitZ) {
        return carvedBits.testBit(bitX, bitY, bitZ);
    }

    public boolean isQuadUncarved(int quadX, int quadY, int quadZ) {
        int half = CARVING_DIMENSION / 2;
        for (int bitX = quadX * half; bitX < quadX * half + half; bitX++) {
            for (int bitY = quadY * half; bitY < quadY * half + half; bitY++) {
                for (int bitZ = quadZ * half; bitZ < quadZ * half + half; bitZ++) {
                    if (carvedBits.testBit(bitX, bitY, bitZ))
                        return false;
                }
            }
        }

        return true;
    }

    public int getTotalBitCount() {
        final int total = CARVING_DIMENSION * CARVING_DIMENSION * CARVING_DIMENSION;
        return total;
    }

    public int getCarvedBitCount() {
        return carvedBits.count();
    }

    public void onCarvingBroken() {
        if (getCraftingResult() != null) {
            dropCraftingResult();
        } else {
            dropHarvestAtRatioCarved(1);
        }
    }

    private void dropCraftingResult() {
        ItemStack is = getCraftingResult();
        EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.25, zCoord + 0.5, is);
        worldObj.spawnEntityInWorld(ei);
        Bids.LOG.debug("Crafting result harvested: " + is.getDisplayName());
    }

    private void dropHarvestAtRatioCarved(float ratio) {
        ICarving carving = CarvingRegistry.getBlockCarving(this);
        ItemStack[] rewardStack = carving.getCarvingHarvest(Block.getBlockById(getCarvedBlockId()),
                getCarvedBlockMetadata(), worldObj.rand);
        if (rewardStack != null) {
            int maxCount = rewardStack.length;
            int earnedCount = Math.min((int) Math.floor(maxCount * ratio), maxCount);
            while (earnedCount > harvestDroppedCount) {
                ItemStack is = rewardStack[harvestDroppedCount++];
                EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.25, zCoord + 0.5, is);
                worldObj.spawnEntityInWorld(ei);
                Bids.LOG.debug("Harvested: " + is.getDisplayName() + "[" + is.stackSize + "]");
            }
        }
    }

    private void dropExtraHarvest() {
        ICarving carving = CarvingRegistry.getBlockCarving(this);
        ItemStack is = carving.getCarvingExtraHarvest(Block.getBlockById(getCarvedBlockId()),
                getCarvedBlockMetadata(), worldObj.rand, 1f / getTotalBitCount());
        if (is != null) {
            EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.25, zCoord + 0.5, is);
            worldObj.spawnEntityInWorld(ei);
            Bids.LOG.debug("Harvested (extra): " + is.getDisplayName() + "[" + is.stackSize + "]");
        }
    }

    @Override
    public S35PacketUpdateTileEntity getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeCarvingDataToNBT(tag);
        S35PacketUpdateTileEntity pack = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
        return pack;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeCarvingDataToNBT(tag);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        readCarvingDataFromNBT(tag);
    }

    public void writeCarvingDataToNBT(NBTTagCompound tag) {
        tag.setInteger("carvedBlockId", carvedBlockId);
        tag.setInteger("carvedBlockMetadata", carvedBlockMetadata);
        tag.setInteger("harvestDroppedCount", harvestDroppedCount);
        tag.setBoolean("isCarvingLocked", isCarvingLocked);

        carvedBits.writeToNBT(tag, "carvedBits");
    }

    public void readCarvingDataFromNBT(NBTTagCompound tag) {
        carvedBlockId = tag.getInteger("carvedBlockId");
        carvedBlockMetadata = tag.getInteger("carvedBlockMetadata");
        harvestDroppedCount = tag.getInteger("harvestDroppedCount");
        isCarvingLocked = tag.getBoolean("isCarvingLocked");

        carvedBits.readFromNBT(tag, "carvedBits");

        cachedCraftingResultIsValid = false;
    }

    @Override
    public void onTileEntityMessage(CarvingMessage message) {
        if (worldObj.isRemote) {
            switch (message.getAction()) {
                case ACTION_UPDATE:
                    carvedBits.setBytes(message.getCarveData());
                    cachedCraftingResultIsValid = false;
                    worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
                    Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
                            + message.getZCoord());
                    break;
            }
        } else {
            switch (message.getAction()) {
                case ACTION_SELECT_BIT:
                    selectedBit = message.getBit();
                    selectedSide = message.getSide();
                    carvingMode = message.getCarvingMode();
                    Bids.LOG.debug("Selected bit " + (selectedBit.isEmpty() ? "None"
                            : (selectedBit.bitX + ", " + selectedBit.bitY + ", " + selectedBit.bitZ)));

                    if (!clientInitialized) {
                        // First time a carving is selected
                        // refresh the client render
                        sendUpdateMessage(worldObj, xCoord, yCoord, zCoord, 0);
                        clientInitialized = true;
                    }
                    break;
            }
        }

    }

    public static void sendSelectBitMessage(World world, int x, int y, int z, CarvingBit bit, int side, EnumAdzeMode mode) {
        Bids.network.sendToServer(new CarvingMessage(x, y, z, TileEntityCarving.ACTION_SELECT_BIT)
                .setBit(bit)
                .setSide(side)
                .setCarvingMode(mode));
        Bids.LOG.debug("Send select bit message " + bit.bitX + ", " + bit.bitY +
                ", " + bit.bitZ + " side " + side + " mode " + mode);
    }

    public static void sendUpdateMessage(World world, int x, int y, int z, int flags) {
        TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);
        TargetPoint tp = new TargetPoint(world.provider.dimensionId, x, y, z, 255);
        Bids.network.sendToAllAround(new CarvingMessage(x, y, z, TileEntityCarving.ACTION_UPDATE)
                .setFlag(flags)
                .setCarvedData(te.carvedBits.getBytes()), tp);
        Bids.LOG.debug("Sent update message: " + flags);
    }

}
