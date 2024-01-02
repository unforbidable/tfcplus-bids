package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.TileEntities.TEAxleBearing;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAxleWallBearing extends TEAxleBearing {

    private int coverBlockId = 0;
    private int coverBlockMetadata = 0;

    public int getCoverBlockId() {
        return coverBlockId;
    }

    public int getCoverBlockMetadata() {
        return coverBlockMetadata;
    }

    public void setCover(Block block, int meta) {
        coverBlockId = Block.getIdFromBlock(block);
        coverBlockMetadata = meta;
    }

    public void clearCover() {
        coverBlockId = 0;
        coverBlockMetadata = 0;
    }

    public boolean hasCover() {
        return coverBlockId != 0;
    }

    @Override
    public void createInitNBT(NBTTagCompound nbt) {
        super.createInitNBT(nbt);

        nbt.setShort("coverId", (short) coverBlockId);
        nbt.setByte("coverMeta", (byte) coverBlockMetadata);
    }

    @Override
    public void handleInitPacket(NBTTagCompound nbt) {
        super.handleInitPacket(nbt);

        coverBlockId = nbt.getShort("coverId");
        coverBlockMetadata = nbt.getByte("coverMeta");
    }

}
