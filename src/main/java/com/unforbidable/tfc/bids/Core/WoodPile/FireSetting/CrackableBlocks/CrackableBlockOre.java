package com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.CrackableBlocks;

import com.dunk.tfc.TileEntities.TEOre;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.CrackableBlock;
import com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.StoneCracker;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CrackableBlockOre extends CrackableBlock {

    public CrackableBlockOre(Block source, Block target) {
        super(source, target);
    }

    @Override
    public void crackBlock(World world, int x, int y, int z) {
        TileEntity originalTe = world.getTileEntity(x, y, z);
        if (originalTe instanceof TEOre) {
            TEOre originalOre = (TEOre) originalTe;

            int meta = world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, target, meta, 2);

            TileEntity crackedTe = world.getTileEntity(x, y, z);
            if (crackedTe instanceof TEOre) {
                TEOre crackedOre = (TEOre) crackedTe;
                crackedOre.baseBlockID = originalOre.baseBlockID;
                crackedOre.baseBlockMeta = originalOre.baseBlockMeta;
                crackedOre.extraData = originalOre.extraData;

                world.markBlockForUpdate(x, y, z);
            } else {
                Bids.LOG.warn("Ore block not cracked! Expected TEOre at target block: " + world.getBlock(x, y, z));
            }
        } else {
            Bids.LOG.warn("Ore block not cracked! Expected TEOre at source block: " + world.getBlock(x, y, z));
        }
    }

    @Override
    public boolean isCrackable(World world, int x, int y, int z) {
        // First make sure the ore block itself is crackable
        if (!super.isCrackable(world, x, y, z)) {
            return false;
        }

        // And also the base block has to be crackable
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEOre) {
            TEOre ore = (TEOre) te;
            return StoneCracker.canBlockCrack(Block.getBlockById(ore.baseBlockID), ore.baseBlockMeta);
        }

        return false;
    }

    @Override
    public boolean isCrackable(Block block, int metadata) {
        // Coal ore blocks cannot crack
        // This is mainly for progression reasons
        // so that coal cannot be acquired before metal tools,
        // and also realistically coal would likely just burn instead
        if (block == TFCBlocks.ore && (metadata == 14 || metadata == 15)) {
            return false;
        }

        // Same with jet
        if (block == TFCBlocks.ore2 && metadata == 8) {
            return false;
        }

        return super.isCrackable(block, metadata);
    }

    @Override
    public float getHeatResistance(World world, int x, int y, int z) {
        // Get heat resistance of the base block
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEOre) {
            TEOre ore = (TEOre) te;
            return StoneCracker.getBlockHeatResistance(Block.getBlockById(ore.baseBlockID), ore.baseBlockMeta);
        }

        return super.getHeatResistance(world, x, y, z);
    }

}
