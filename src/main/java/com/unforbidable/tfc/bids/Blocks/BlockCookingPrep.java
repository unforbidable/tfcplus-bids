package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Textures;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPrep;
import com.unforbidable.tfc.bids.api.BidsGui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class BlockCookingPrep extends BlockContainer {

    public BlockCookingPrep() {
        super(Material.rock);

        setHardness(1);
        this.setBlockBounds(0, 0, 0, 1, 0.15f, 1);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
                                    float par8, float par9) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityCookingPrep) {
            TileEntityCookingPrep tileEntityCookingPrep = (TileEntityCookingPrep) world.getTileEntity(x, y, z);
            if (handleInteraction(world, x, y, z, player, tileEntityCookingPrep)) {
                return true;
            }

            if (!world.isRemote) {
                player.openGui(Bids.instance, BidsGui.cookingPrepGui, world, x, y, z);
            }

            return true;
        }

        return false;
    }

    private boolean handleInteraction(World world, int x, int y, int z, EntityPlayer player, TileEntityCookingPrep te) {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            if (!world.isSideSolid(x, y - 1, z, ForgeDirection.UP)) {
                TFC_Core.setBlockToAirWithDrops(world, x, y, z);
            }
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityCookingPrep te = (TileEntityCookingPrep) world.getTileEntity(x, y, z);
        te.onBlockBroken();

        super.breakBlock(world, x, y, z, block, meta);
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
    public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int i, int j, int k) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return null;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int i, int j, int k) {
        return AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 0.15, k + 1);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityCookingPrep();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess access, int i, int j, int k, int meta) {
        return TFC_Textures.invisibleTexture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getItemIconName() {
        return Tags.MOD_ID + ":" + "devices/cookingprep";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        return world.getBlock(x, y, z) == this;
    }

}
