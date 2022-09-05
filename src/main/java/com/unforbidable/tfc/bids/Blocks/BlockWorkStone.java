package com.unforbidable.tfc.bids.Blocks;

import java.util.List;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWorkStone extends Block {

    final BlockRoughStone materialBlock;

    public BlockWorkStone(BlockRoughStone materialBlock) {
        super(materialBlock.getMaterial());

        this.materialBlock = materialBlock;

        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setHardness(10f);
    }

    public Block getMaterialBlock() {
        return materialBlock;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < materialBlock.getNames().length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return materialBlock.getIcon(side, metadata);
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
        return BidsBlocks.workStoneRenderId;
    }

}
