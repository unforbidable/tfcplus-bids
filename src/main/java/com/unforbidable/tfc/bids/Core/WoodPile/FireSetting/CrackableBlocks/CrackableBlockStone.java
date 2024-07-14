package com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.CrackableBlocks;

import com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.CrackableBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class CrackableBlockStone extends CrackableBlock {

    protected final float resistance;

    public CrackableBlockStone(Block source, Block target, float resistance) {
        super(source, target);

        this.resistance = resistance;
    }

    @Override
    public void crackBlock(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, target, meta, 2);
    }

    @Override
    public float getHeatResistance(World world, int x, int y, int z) {
        return resistance;
    }

    @Override
    public float getHeatResistance(Block block, int metadata) {
        return resistance;
    }

}
