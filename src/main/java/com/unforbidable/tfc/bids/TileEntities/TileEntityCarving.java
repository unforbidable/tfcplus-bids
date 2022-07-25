package com.unforbidable.tfc.bids.TileEntities;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBit;
import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;
import com.unforbidable.tfc.bids.Core.Carving.CarvingMessage;
import com.unforbidable.tfc.bids.Core.Network.IMessageHanldingTileEntity;
import com.unforbidable.tfc.bids.api.CarvingRegistry;
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
import net.minecraftforge.common.util.ForgeDirection;

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
    boolean clientInitialized = false;
    int carvedBitCount = 0;

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

    public boolean carveSelectedBit() {
        if (!selectedBit.isEmpty()) {
            return carveBit(selectedBit);
        } else {
            Bids.LOG.warn("Cannot carve selected bit when none is selected");
            return false;
        }
    }

    public boolean carveBit(CarvingBit bit) {
        if (canCarveBit(bit)) {
            Bids.LOG.debug("Carved bit " + bit.bitX + ", " + bit.bitY + ", " + bit.bitZ);
            carvedBits.setBit(bit);

            dropHarvestAtRatioCarved(getCarvedBitCount() / (float) getTotalBitCount());
            dropExtraHarvest();

            if (getCarvedBitCount() == getTotalBitCount()) {
                Bids.LOG.debug("The entire carving was removed");
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }

            return true;
        }

        return false;
    }

    public boolean canCarveBit(CarvingBit bit) {
        // Can be carved if bit is at the edge
        final int maxBit = CARVING_DIMENSION - 1;
        if (bit.bitX == 0 || bit.bitX == maxBit ||
                bit.bitY == 0 || bit.bitY == maxBit ||
                bit.bitZ == 0 || bit.bitZ == maxBit) {
            return true;
        }

        // Either one of each of the three opposing sides need to be exposed
        if ((carvedBits.testBit(bit.getBitToDirection(ForgeDirection.WEST))
                || carvedBits.testBit(bit.getBitToDirection(ForgeDirection.EAST)))
                && (carvedBits.testBit(bit.getBitToDirection(ForgeDirection.SOUTH))
                        || carvedBits.testBit(bit.getBitToDirection(ForgeDirection.NORTH)))
                && (carvedBits.testBit(bit.getBitToDirection(ForgeDirection.UP))
                        || carvedBits.testBit(bit.getBitToDirection(ForgeDirection.DOWN)))) {
            return true;
        }

        // Surrounding bits need to be exposed
        // to any one side the carved bit is exposed
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            CarvingBit sideBit = bit.getBitToDirection(d);
            if (carvedBits.testBit(sideBit)) {
                // Each of the four bits around the side bit that had been carved
                // need to have been carved out too
                int sideBitNeighborCarvedCount = 0;
                for (ForgeDirection ds : ForgeDirection.VALID_DIRECTIONS) {
                    // Skip back and front relative to the bit being carved out
                    if (ds != d && ds != d.getOpposite()) {
                        CarvingBit sideBitNeighbor = sideBit.getBitToDirection(ds);
                        if (carvedBits.testBit(sideBitNeighbor)) {
                            sideBitNeighborCarvedCount++;
                        }
                    }
                }

                if (sideBitNeighborCarvedCount == 4) {
                    return true;
                } else {
                    Bids.LOG.debug("Not enough exposed bits around to " + d + ": " + sideBitNeighborCarvedCount);
                }
            }
        }

        Bids.LOG.debug("Cannot carve bit that is too deep");

        return false;
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
        dropHarvestAtRatioCarved(1);
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
                EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
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
            EntityItem ei = new EntityItem(worldObj, xCoord, yCoord, zCoord, is);
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
    }

    @Override
    public void onTileEntityMessage(CarvingMessage message) {
        if (worldObj.isRemote) {
            switch (message.getAction()) {
                case ACTION_UPDATE:
                    carvedBits.setBytes(message.getCarveData());
                    worldObj.markBlockForUpdate(message.getXCoord(), message.getYCoord(), message.getZCoord());
                    Bids.LOG.debug("Client updated at: " + message.getXCoord() + ", " + message.getYCoord() + ", "
                            + message.getZCoord());
                    break;
            }
        } else {
            switch (message.getAction()) {
                case ACTION_SELECT_BIT:
                    selectedBit = message.getBit();
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

    public static void sendSelectBitMessage(World world, int x, int y, int z, CarvingBit bit) {
        Bids.network.sendToServer(new CarvingMessage(x, y, z, TileEntityCarving.ACTION_SELECT_BIT).setBit(bit));
        Bids.LOG.debug("Send select bit message " + bit.bitX + ", " + bit.bitY +
                ", " + bit.bitZ);
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
