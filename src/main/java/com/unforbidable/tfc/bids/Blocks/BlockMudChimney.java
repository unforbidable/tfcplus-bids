package com.unforbidable.tfc.bids.Blocks;

import java.util.List;
import java.util.Random;

import com.dunk.tfc.Reference;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChimney;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMudChimney extends BlockContainer {

    final int stoneOffset;
    final IIcon[] icons;

    Block sourceDirt;

    public BlockMudChimney(int stoneOffset) {
        super(Material.clay);
        this.stoneOffset = stoneOffset;
        int count = (stoneOffset == 0 ? 16 : Global.STONE_ALL.length - 16);
        icons = new IIcon[count];
        setHardness(12f);
        setCreativeTab(BidsCreativeTabs.BidsDefault);
    }

    public int getStoneOffset() {
        return stoneOffset;
    }

    public BlockMudChimney setDirt(Block dirt) {
        this.sourceDirt = dirt;
        return this;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        if (side == ForgeDirection.UP || side == ForgeDirection.DOWN) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return TFCBlocks.chimneyRenderId;
    }

    @Override
    public TileEntity createNewTileEntity(World arg0, int arg1) {
        return new TileEntityChimney();
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote && TFC_Core.isExposedToRain(world, x, y, z)) {
            /*
             * world.setBlock(x, y, z, sourceDirt, world.getBlockMetadata(x, y, z), 2);
             * world.scheduleBlockUpdate(x, y, z, sourceDirt, 5);
             * world.scheduleBlockUpdate(x-1, y, z, sourceDirt, 20);
             * world.scheduleBlockUpdate(x+1, y, z, sourceDirt, 20);
             * world.scheduleBlockUpdate(x, y, z-1, sourceDirt, 20);
             * world.scheduleBlockUpdate(x, y, z+1, sourceDirt, 20);
             * world.scheduleBlockUpdate(x, y-1, z, sourceDirt, 20);
             */
        } else if (!world.isRemote) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        if (!(y == 255 && j == 1) && !(y == 0 && j == -1) && (i * k == 0) && !(j != 0 && i != k)) {
                            Block b = world.getBlock(x + i, y + j, z + k);
                            if (TFC_Core.isWater(b)) {
                                world.setBlock(x, y, z, sourceDirt, world.getBlockMetadata(x, y, z), 2);
                                world.scheduleBlockUpdate(x, y, z, sourceDirt, 5);
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        int count;
        if (stoneOffset == 0)
            count = 16;
        else
            count = Global.STONE_ALL.length - 16;

        for (int i = 0; i < count; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    public int damageDropped(int dmg) {
        return dmg;
    }

    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune) {
        return Item.getItemFromBlock(sourceDirt);
    }

    @Override
    public IIcon getIcon(IBlockAccess bAccess, int x, int y, int z, int side) {
        int meta = bAccess.getBlockMetadata(x, y, z);
        if (meta >= icons.length)
            return icons[icons.length - 1];
        return icons[meta];
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta >= icons.length)
            return icons[icons.length - 1];
        return icons[meta];
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        int count = (stoneOffset == 0 ? 16 : Global.STONE_ALL.length - 16);
        for (int i = 0; i < count; i++)
            icons[i] = registerer
                    .registerIcon(Reference.MOD_ID + ":" + "soil/Mud Brick " + Global.STONE_ALL[i + stoneOffset]);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }

}
