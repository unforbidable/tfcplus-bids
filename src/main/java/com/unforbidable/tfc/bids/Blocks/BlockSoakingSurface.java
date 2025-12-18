package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Textures;
import com.unforbidable.tfc.bids.Core.SoakingSurface.SoakingSurfaceHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySoakingSurface;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockSoakingSurface extends BlockContainer {

    public BlockSoakingSurface() {
        super(Material.ground);

        setHardness(1f);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
                                    float hitY, float hitZ) {
        if (!world.isRemote) {
            if (player.isSneaking() && player.getHeldItem() == null) {
                return SoakingSurfaceHelper.retrieveItemFromSoakingSurface(world, x, y, z, player, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block par5) {
        if (!canBlockStay(world, x, y ,z)) {
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
        return te.hasValidFluidBlock();
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
        TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
        if (te.getOriginalBlockId() != 0) {
            Block block = BlockScrew.getBlockById(te.getOriginalBlockId());
            return block.getIcon(side, te.getOriginalBlockId());
        } else {
            return TFC_Textures.invisibleTexture;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntitySoakingSurface te = (TileEntitySoakingSurface) world.getTileEntity(x, y, z);
        te.onSoakingSurfaceBroken();

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = SoakingSurfaceHelper.onSoakingSurfaceCollisionRayTrace(world, x, y, z, startVec, endVec);
        if (mop != null) {
            return mop;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.soakingSurfaceRenderId;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySoakingSurface();
    }

}
