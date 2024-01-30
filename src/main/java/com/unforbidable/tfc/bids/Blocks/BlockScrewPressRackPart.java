package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

public abstract class BlockScrewPressRackPart extends Block {

    private static final AxisAlignedBB[] DEFAULT_INDIVIDUAL_BOUNDS = new AxisAlignedBB[0];
    private static final AxisAlignedBB[] DEFAULT_INVENTORY_BOUNDS = new AxisAlignedBB[0];

    public BlockScrewPressRackPart() {
        super(Material.wood);

        setHardness(2f);
    }

    public AxisAlignedBB[] getIndividualBoundsForOrientation(int orientation) {
        return DEFAULT_INDIVIDUAL_BOUNDS;
    }

    public AxisAlignedBB[] getInventoryBlockBounds() {
        return DEFAULT_INVENTORY_BOUNDS;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.screwPressRackRenderId;
    }

    @Override
    public IIcon getIcon(int i, int j) {
        return TFCBlocks.woodSupportH.getIcon(0, 0);
    }

}
