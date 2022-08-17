package com.unforbidable.tfc.bids.Blocks;

import java.util.Random;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsGui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWoodPile extends BlockContainer {

    IIcon icon;

    public BlockWoodPile() {
        super(Material.wood);

        setHardness(10f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityWoodPile();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        icon = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName());
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityWoodPile) {
            return ((TileEntityWoodPile) te).isFull();
        }

        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
            float par8, float par9) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityWoodPile) {
            TileEntityWoodPile woodPile = (TileEntityWoodPile) world.getTileEntity(x, y, z);
            if (handleInteraction(world, x, y, z, player, woodPile)) {
                return true;
            }

            if (!world.isRemote) {
                player.openGui(Bids.instance, BidsGui.woodPileGui, world, x, y, z);
            }

            return true;
        }

        return false;
    }

    protected boolean handleInteraction(World world, int x, int y, int z, EntityPlayer player,
            TileEntityWoodPile woodPile) {
        ItemStack heldItemStack = player.getCurrentEquippedItem();
        if (heldItemStack != null
                && WoodPileHelper.insertIntoWoodPileAt(heldItemStack, player, world, x, y, z)) {
            return true;
        }

        if (WoodPileHelper.retrieveSelectedItemFromWoodPileAt(player, world, x, y, z)) {
            return true;
        }

        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.woodPileRenderId;
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityWoodPile te = (TileEntityWoodPile) world.getTileEntity(x, y, z);
        te.onWoodPileBroken();

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = WoodPileHelper.onWoodPileCollisionRayTrace(world, x, y, z, startVec, endVec);
        if (mop != null) {
            return mop;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

}
