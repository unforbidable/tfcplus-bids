package com.unforbidable.tfc.bids.features.building.carving.tileentity;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.features.carving.AdzeMode;
import com.unforbidable.tfc.bids.api.features.carving.Carvable;
import com.unforbidable.tfc.bids.api.features.carving.CarvingRecipe;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.core.network.packet.PacketHandler;
import com.unforbidable.tfc.bids.features.building.carving.CarvingRegistry;
import com.unforbidable.tfc.bids.features.building.carving.main.CarvingBit;
import com.unforbidable.tfc.bids.features.building.carving.main.CarvingBitMap;
import com.unforbidable.tfc.bids.features.building.carving.main.CarvingHelper;
import com.unforbidable.tfc.bids.features.building.carving.network.CarvingPacket;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarving extends TileEntity implements PacketHandler<CarvingPacket> {

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
    AdzeMode carvingMode = AdzeMode.DEFAULT_MODE;

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
            final Block block = Block.getBlockById(carvedBlockId);
            final ItemStack itemStack = new ItemStack(block, 1, carvedBlockMetadata);
            final CarvingRecipe recipe = CarvingRegistry.recipes.findMatchingRecipe(r -> r.matches(itemStack, carvedBits));
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

    public void setCarvingMode(AdzeMode mode) {
        carvingMode = mode;
    }

    public AdzeMode getCarvingMode() {
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
        Carvable carvable = CarvingHelper.getCarvableBlock(this);
        ItemStack[] rewardStack = carvable.getCarvingHarvest(Block.getBlockById(getCarvedBlockId()),
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
        Carvable carvable = CarvingHelper.getCarvableBlock(this);
        ItemStack is = carvable.getCarvingExtraHarvest(Block.getBlockById(getCarvedBlockId()),
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
        readCarvingDataFromNBT(tag);
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
    public void handleNetworkPacket(CarvingPacket packet) {
        if (worldObj.isRemote) {
            if (packet.getAction() == ACTION_UPDATE) {
                carvedBits.setBytes(packet.getCarveData());
                cachedCraftingResultIsValid = false;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                Bids.LOG.debug("Client updated at [{},{},{}]", xCoord, yCoord, zCoord);
            }
        } else {
            if (packet.getAction() == ACTION_SELECT_BIT) {
                selectedBit = packet.getBit();
                selectedSide = packet.getSide();
                carvingMode = packet.getCarvingMode();
                Bids.LOG.debug("Selected bit " + (selectedBit.isEmpty() ? "None"
                    : (selectedBit.bitX + ", " + selectedBit.bitY + ", " + selectedBit.bitZ)));

                if (!clientInitialized) {
                    // First time a carving is selected
                    // refresh the client render
                    sendUpdateMessage(0);
                    clientInitialized = true;
                }
            }
        }

    }

    public void sendSelectBitMessage(CarvingBit bit, int side, AdzeMode mode) {
        CarvingPacket packet = new CarvingPacket(TileEntityCarving.ACTION_SELECT_BIT);
        packet.setBit(bit);
        packet.setSide(side);
        packet.setCarvingMode(mode);
        Network.sendToTileEntity(packet, this);

        Bids.LOG.debug("Send select bit packet " + bit.bitX + ", " + bit.bitY +
                ", " + bit.bitZ + " side " + side + " mode " + mode);
    }

    public void sendUpdateMessage(int flags) {
        CarvingPacket packet = new CarvingPacket(TileEntityCarving.ACTION_UPDATE);
        packet.setFlag(flags);
        packet.setCarvedData(carvedBits.getBytes());
        Network.sendToTileEntity(packet, this);

        Bids.LOG.debug("Sent update packet: " + flags);
    }

}
