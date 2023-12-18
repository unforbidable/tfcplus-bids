package com.unforbidable.tfc.bids.Blocks;

import java.util.List;
import java.util.Random;

import com.dunk.tfc.Reference;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChimney;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
        super(Material.ground);
        this.stoneOffset = stoneOffset;
        int count = (stoneOffset == 0 ? 16 : Global.STONE_ALL.length - 16);
        icons = new IIcon[count];
        setHardness(12f);
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
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
        if (!world.isRemote) {
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

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityChimney) {
            if (((TileEntityChimney) world.getTileEntity(x, y, z)).getChimneySmoke() > 0
                    && world.isAirBlock(x, y + 1, z)) {
                double centerX = x + 0.5F;
                double centerY = y + 2F;
                double centerZ = z + 0.5F;
                // double d3 = 0.2199999988079071D;
                // double d4 = 0.27000001072883606D;
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 2,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 2,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.15D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 1,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 1,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.15D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 2,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 2,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.15D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 1,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 1,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.15D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 2,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 2,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.15D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 1,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + ((rand.nextDouble() - 0.5) * 0.5), centerY - 1,
                        centerZ + ((rand.nextDouble() - 0.5) * 0.5), 0.0D, 0.15D, 0.0D);
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
