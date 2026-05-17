package com.unforbidable.tfc.bids.features.building.palisade.block;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.common.block.BlockCommonFence;
import com.unforbidable.tfc.bids.util.wood.WoodHelper;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockPalisade extends BlockCommonFence {

    private final int offset;

    public BlockPalisade(Block block, int offset) {
        super(block, 0);

        this.metaNames = WoodHelper.getWoodOffsetNames(offset);
        this.offset = offset;

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setHardness(4f);
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < metaNames.length; i++) {
            WoodIndex wood = WoodScheme.DEFAULT.findWood(offset + i);
            if (wood.blocks.hasPalisade()) {
                list.add(wood.blocks.getPalisade());
            }
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (offset == 0) {
            return BidsBlocks.logWallVert.getIcon(side, meta);
        } else if (offset == 16) {
            return BidsBlocks.logWallVert2.getIcon(side, meta);
        } else {
            return BidsBlocks.logWallVert3.getIcon(side, meta);
        }
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.palisadeRenderId;
    }

    @Override
    public int damageDropped(int damage) {
        return damage;
    }

}
