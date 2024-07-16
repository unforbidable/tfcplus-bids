package com.unforbidable.tfc.bids.Core.WoodPile.FireSetting;

import com.unforbidable.tfc.bids.api.Interfaces.ICrackableBlock;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class CrackableBlock implements ICrackableBlock {

    protected final Block source;
    protected final Block target;

    public CrackableBlock(Block source, Block target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public Block getSource() {
        return source;
    }

    @Override
    public Block getTarget() {
        return target;
    }

    @Override
    public boolean isCrackable(World world, int x, int y, int z) {
        return isCrackable(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    @Override
    public boolean isCrackable(Block block, int metadata) {
        return true;
    }

    @Override
    public float getHeatResistance(World world, int x, int y, int z) {
        return getHeatResistance(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    @Override
    public float getHeatResistance(Block block, int metadata) {
        return 1f;
    }

}
