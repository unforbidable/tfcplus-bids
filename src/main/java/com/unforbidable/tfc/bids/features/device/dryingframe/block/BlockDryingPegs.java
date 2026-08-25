package com.unforbidable.tfc.bids.features.device.dryingframe.block;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Textures;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.device.dryingframe.main.DryingPegsHelper;
import com.unforbidable.tfc.bids.features.device.dryingframe.tileentity.TileEntityDryingPegs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDryingPegs extends BlockContainer {

    public BlockDryingPegs() {
        super(Material.cloth);

        setHardness(2f);
        setBlockBounds(0f, 0.3f, 0f, 1f, 0.34f, 1f);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFC_Textures.invisibleTexture;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return TFC_Textures.invisibleTexture;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        if (entityplayer.getHeldItem() == null) {
            if (!world.isRemote && entityplayer.isSneaking()) {
                TileEntityDryingPegs tileEntityDryingPegs = (TileEntityDryingPegs) world.getTileEntity(x, y, z);
                tileEntityDryingPegs.retrieveItem(entityplayer);

                world.setBlock(x, y, z, Blocks.air, 0, 2);
            }
        }

        return true;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        TileEntityDryingPegs te = (TileEntityDryingPegs)world.getTileEntity(x, y, z);
        te.onDryingPegsBroken();
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
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!canBlockStay(world, x, y, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        for (int i = 2; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            if (!DryingPegsHelper.isBlockDryingPegAnchor(world, x + dir.offsetX, y, z + dir.offsetZ)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityDryingPegs();
    }

}
