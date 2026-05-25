package com.unforbidable.tfc.bids.features.device.saddlequern.block;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.device.saddlequern.main.WorkStoneType;
import com.unforbidable.tfc.bids.util.accessor.BlockMetaNamesAccessor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWorkStone extends Block {

    final Block materialBlock;
    WorkStoneType workStoneType;

    public BlockWorkStone(Block materialBlock) {
        super(materialBlock.getMaterial());

        this.materialBlock = materialBlock;

        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setHardness(10f);
    }

    public Block getMaterialBlock() {
        return materialBlock;
    }

    public WorkStoneType getWorkStoneType() {
        return workStoneType;
    }

    public Block setWorkStoneType(WorkStoneType workStoneType) {
        this.workStoneType = workStoneType;

        return this;
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        if (materialBlock instanceof BlockMetaNamesAccessor) {
            for (int i = 0; i < ((BlockMetaNamesAccessor) materialBlock).getMetaNames().length; i++) {
                par3List.add(new ItemStack(par1, 1, i));
            }
        } else {
            par3List.add(new ItemStack(par1, 1, 0));
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
        return BlockRenderIdProvider.get(BlockNames.SADDLE_QUERN_HANDSTONE);
    }

}
