package com.unforbidable.tfc.bids.Core.Common.Metadata;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DecorativeSurfaceMetadata {

    protected final int metadata;

    public static DecorativeSurfaceMetadata at(IBlockAccess world, int x, int y, int z) {
        return new DecorativeSurfaceMetadata(world.getBlockMetadata(x, y, z));
    }

    public static DecorativeSurfaceMetadata of(TileEntity te) {
        return new DecorativeSurfaceMetadata(te.getBlockMetadata());
    }

    public static DecorativeSurfaceMetadata forVerticalFace(ForgeDirection dir) {
        return new DecorativeSurfaceMetadata(((dir.ordinal() - 2) & 0x03) | 0x08);
    }

    public static DecorativeSurfaceMetadata forHorizontalOrientation(int orientation) {
        return new DecorativeSurfaceMetadata(orientation & 0x03);
    }

    protected DecorativeSurfaceMetadata(int metadata) {
        this.metadata = metadata;
    }

    public int getMetadata() {
        return metadata;
    }

    public boolean isHorizontal() {
        return (metadata & 0x08) == 0;
    }

    public ForgeDirection getVerticalFace() {
        return ForgeDirection.getOrientation((metadata & 0x03) + 2);
    }

    public int getHorizontalOrientation() {
        return metadata & 0x03;
    }

}
