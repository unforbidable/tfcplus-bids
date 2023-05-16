package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Terrain.BlockCollapsible;
import com.dunk.tfc.Blocks.Terrain.BlockGravel;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityAquifer;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockAquifer extends BlockContainer {

    private final int textureOffset;
    private final IIcon[] icons = new IIcon[Global.STONE_ALL.length];

    Block gravelBlock;

    public BlockAquifer(int textureOffset) {
        super(Material.ground);

        this.textureOffset = textureOffset;

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setTickRandomly(true);
    }

    public BlockAquifer setGravelBlock(Block gravelBlock) {
        this.gravelBlock = gravelBlock;

        return this;
    }

    public Block getGravelBlock() {
        return gravelBlock;
    }

    public int getTextureOffset() {
        return textureOffset;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        int count = Math.min(16, Global.STONE_ALL.length - textureOffset);
        for (int i = 0; i < count; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int damageDropped(int dmg) {
        return dmg;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(new BlockGravel(textureOffset), 1, damageDropped(metadata)));
        return ret;
    }

    @Override
    public IIcon getIcon(IBlockAccess bAccess, int x, int y, int z, int side) {
        int meta = bAccess.getBlockMetadata(x, y, z);
        if(meta >= icons.length) {
            return icons[icons.length - 1];
        }
        return icons[meta];
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if(meta >= icons.length) {
            return icons[icons.length - 1];
        }
        return icons[meta];
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        int count = Math.min(16, Global.STONE_ALL.length - textureOffset);
        for (int i = 0; i < count; i++) {
            icons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "soil/Aquifer " + Global.STONE_ALL[i + textureOffset]);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (ensureBlockCanStay(world, x, y, z)) {
            if (!world.isRemote) {
                TileEntityAquifer te = (TileEntityAquifer) world.getTileEntity(x, y, z);
                te.checkBeingExposed();
            }
        }
    }

    @Override
    public int tickRate(World world) {
        return 3;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        if (ensureBlockCanStay(world, x, y, z)) {
            if (!world.isRemote) {
                TileEntityAquifer te = (TileEntityAquifer) world.getTileEntity(x, y, z);
                te.checkBeingExposed();
            }
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntityAquifer te = (TileEntityAquifer) world.getTileEntity(x, y, z);
        te.onBlockDestroyed();
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (ensureBlockCanStay(world, x, y, z)) {
            if (!world.isRemote && random.nextInt(16) == 0) {
                TileEntityAquifer te = (TileEntityAquifer) world.getTileEntity(x, y, z);
                te.checkBeingExposed();
            }
        }
    }

    private boolean ensureBlockCanStay(World world, int x, int y, int z) {
        if (!checkBlockCanStay(world, x, y, z)) {
            int metadata = world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, getGravelBlock(), metadata, 3);
            return false;
        }

        return true;
    }

    private boolean checkBlockCanStay(World world, int x, int y, int z) {
        if (!world.isRemote && world.doChunksNearChunkExist(x, y, z, 1)) {
            boolean canFallOneBelow = BlockCollapsible.canFallBelow(world, x, y - 1, z);
            if (canFallOneBelow) {
                //Bids.LOG.info("Should fall down");
                return false;
            }

            byte count = 0;
            for (ForgeDirection d : new ForgeDirection[] { ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST }) {
                if (world.isAirBlock(x + d.offsetX, y, z + d.offsetZ) ||
                    TFC_Core.isFreshWater(world.getBlock(x + d.offsetX, y, z + d.offsetZ))) {
                    count++;

                    if (count > 2) {
                        //Bids.LOG.info("Should fall to a side");
                        return false;
                    }
                }
            }
        }

        //Bids.LOG.info("Can stay");

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityAquifer();
    }

}
