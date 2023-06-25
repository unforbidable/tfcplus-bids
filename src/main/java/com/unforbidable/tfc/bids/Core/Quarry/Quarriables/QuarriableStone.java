package com.unforbidable.tfc.bids.Core.Quarry.Quarriables;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class QuarriableStone implements IQuarriable {

    final Block rawBlock;
    final Block quarriedBlock;
    final int drillDamage;
    final float drillDurationMultiplier;

    public QuarriableStone(Block rawBlock, Block quarriedBlock,
            int drillDamage, float drillDurationMultiplier) {
        super();
        this.rawBlock = rawBlock;
        this.quarriedBlock = quarriedBlock;
        this.drillDamage = drillDamage;
        this.drillDurationMultiplier = drillDurationMultiplier;
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
    public int getQuarriedBlockMetadata(Block block, int metadata) {
        return metadata;
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
    public boolean canQuarryBlock(Block block, int metadata) {
        if (block == TFCBlocks.stoneSed)
            return true;
        else
            return false;
    }

    @Override
    public boolean isSufficientEquipmentTier(Block block, int metadata, int equipmentTier) {
        if (block == TFCBlocks.stoneSed)
            return true;
        else
            return equipmentTier > 0;
    }

    @Override
    public boolean isQuarryReady(World world, int x, int y, int z) {
        ForgeDirection[] sides = new ForgeDirection[] { ForgeDirection.UP, ForgeDirection.NORTH, ForgeDirection.WEST };
        for (ForgeDirection d : sides) {
            // There must be a ready quarry on each of the 3 sides or their opposites
            if (!checkSideIsReady(world, x, y, z, d) && !checkSideIsReady(world, x, y, z, d.getOpposite()))
                return false;
        }
        return true;
    }

    @Override
    public boolean blockRequiresWedgesToDetach(Block block, int metadata) {
        return TFC_Core.isRawStone(block) || TFC_Core.isOreStone(block);
    }

    @Override
    public int getDrillDamage(Block block, int metadata) {
        return drillDamage;
    }

    @Override
    public float getDrillDurationMultiplier(Block block, int metadata) {
        return drillDurationMultiplier;
    }

    private boolean checkSideIsReady(World world, int x, int y, int z, ForgeDirection d) {
        int x2 = x + d.offsetX;
        int y2 = y + d.offsetY;
        int z2 = z + d.offsetZ;
        int meta = world.getBlockMetadata(x2, y2, z2);
        TileEntity te = world.getTileEntity(x2, y2, z2);

        // We expect a properly facing quarry that is ready
        if (te != null && te instanceof TileEntityQuarry) {
            TileEntityQuarry quarry = (TileEntityQuarry) te;
            return quarry.isQuarryReady() && quarry.getQuarryOrientation(meta) == d;
        }

        // Or none of the edges to require wedges
        // which means no quarry is needed here
        boolean found = false;
        for (ForgeDirection edge : ForgeDirection.VALID_DIRECTIONS) {
            // Skip d and opposite
            if (d != edge && d.getOpposite() != edge) {
                Block block = world.getBlock(x + edge.offsetX, y + edge.offsetY, z + edge.offsetZ);
                int metadata = world.getBlockMetadata(x + edge.offsetX, y + edge.offsetY, z + edge.offsetZ);
                if (blockRequiresWedgesToDetach(block, metadata)) {
                    found = true;
                    break;
                }
            }
        }

        return !found;
    }

}
