package com.unforbidable.tfc.bids.Core.Quarry;

import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class QuarriableStone implements IQuarriable {

    final Block rawBlock;
    final Block quarriedBlock;

    public QuarriableStone(Block rawBlock, Block quarriedBlock) {
        super();
        this.rawBlock = rawBlock;
        this.quarriedBlock = quarriedBlock;
    }

    @Override
    public Block getRawBlock() {
        return rawBlock;
    }

    @Override
    public Block getQuarriedBlock() {
        return quarriedBlock;
    }

    @Override
    public boolean canQuarryBlockAt(World world, int x, int y, int z, int side) {
        ForgeDirection d = ForgeDirection.getOrientation(side);
        x += d.offsetX;
        y += d.offsetY;
        z += d.offsetZ;
        // The quarried side of the block must be air
        return world.isAirBlock(x, y, z);
    }

    @Override
    public boolean isQuarryReady(World world, int x, int y, int z) {
        ForgeDirection[] sides = new ForgeDirection[] { ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.WEST };
        for (ForgeDirection d : sides) {
            // There must be a ready quarry on each of the 3 sides or their opposites
            if (!checkSide(world, x, y, z, d) && !checkSide(world, x, y, z, d.getOpposite()))
                return false;

        }
        return true;
    }

    private boolean checkSide(World world, int x, int y, int z, ForgeDirection d) {
        x += d.offsetX;
        y += d.offsetY;
        z += d.offsetZ;
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);

        // We expect a properly facing quarry that is ready
        if (meta == d.ordinal() && te != null && te instanceof TileEntityQuarry) {
            return ((TileEntityQuarry) te).isQuarryReady();
        }

        return false;
    }

}
