package com.unforbidable.tfc.bids.features.building.roughstone.block;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.common.block.BlockCommonFence;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockRoughStoneFence extends BlockCommonFence {

    public final Block materialBlock;

    protected Block materialBlockTopBottom;

    public BlockRoughStoneFence(Block materialBlock) {
        super(materialBlock, 0);

        this.materialBlock = materialBlock;

        metaNames = null;

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setHardness(10f);
    }

    public BlockRoughStoneFence setMaterialBlockTopBottom(Block materialBlockTopBottom) {
        this.materialBlockTopBottom = materialBlockTopBottom;

        return this;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        List<ItemStack> materialBlockSubBlocks = new ArrayList<ItemStack>();
        materialBlock.getSubBlocks(item, tabs, materialBlockSubBlocks);

        for (ItemStack is : materialBlockSubBlocks) {
            list.add(new ItemStack(this, 1, is.getItemDamage()));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if ((side == 0 || side == 1) && materialBlockTopBottom != null) {
            return materialBlockTopBottom.getIcon(side, meta);
        }

        return materialBlock.getIcon(side, meta);
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @Override
    public int damageDropped(int damage) {
        return damage;
    }

}
