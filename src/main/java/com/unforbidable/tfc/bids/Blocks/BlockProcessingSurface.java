package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Textures;
import com.unforbidable.tfc.bids.Core.ProcessingSurface.ProcessingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceManager;
import com.unforbidable.tfc.bids.api.Crafting.ProcessingSurfaceRecipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockProcessingSurface extends BlockContainer {

    public BlockProcessingSurface() {
        super(Material.wood);

        this.setBlockBounds(0, 0, 0, 1, 0.001f, 1);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = entityplayer.getHeldItem();
        if (heldItem == null) {
            if (!world.isRemote && entityplayer.isSneaking()) {
                world.setBlock(x, y, z, Blocks.air, 0, 2);
            }
        } else {
            TileEntityProcessingSurface te = (TileEntityProcessingSurface) world.getTileEntity(x, y, z);
            te.workItem(entityplayer);
        }

        return true;
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        TileEntityProcessingSurface te = (TileEntityProcessingSurface)world.getTileEntity(x, y, z);
        ProcessingSurfaceRecipe recipe = ProcessingSurfaceManager.findMatchingRecipe(te.getInputItem(), world, x, y - 1, z);
        return recipe != null;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!canBlockStay(world, x, y ,z)) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntityProcessingSurface te = (TileEntityProcessingSurface)world.getTileEntity(x, y, z);
        te.onProcessingSurfaceBroken();
    }

    @Override
    public Item getItemDropped(int meta, Random rnd, int fortune) {
        return null;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess w, int x, int y, int z) {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        ProcessingSurfaceHelper.registerProcessingSurfaceRecipeIcons(registerer);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFC_Textures.invisibleTexture;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntityProcessingSurface te = (TileEntityProcessingSurface) world.getTileEntity(x, y, z);
        if (te.getCurrentItem() != null) {
            return ProcessingSurfaceHelper.getIconForItem(te.getCurrentItem());
        } else {
            return super.getIcon(world, x, y, z, side);
        }
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
        return BidsBlocks.processingSurfaceRenderId;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityProcessingSurface();
    }

}
