package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.api.Interfaces.IHeatSource;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileHelper;
import com.unforbidable.tfc.bids.Tags;
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

import java.util.Random;

public class BlockWoodPile extends BlockContainer implements IHeatSource {

    IIcon icon;

    public BlockWoodPile() {
        super(Material.wood);

        setHardness(10f);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityWoodPile) {
            return ((TileEntityWoodPile) te).isFull();
        }

        return false;
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

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityWoodPile) {
            TileEntityWoodPile woodPile = (TileEntityWoodPile) world.getTileEntity(x, y, z);
            return woodPile.isBurning() ? 15 : 0;
        }

        return super.getLightValue(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityWoodPile) {
            TileEntityWoodPile woodPile = (TileEntityWoodPile) world.getTileEntity(x, y, z);
            woodPile.tryToCatchFire();
            woodPile.tryToSpreadFire();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityWoodPile) {
            TileEntityWoodPile woodPile = (TileEntityWoodPile) te;
            if (woodPile.isOnFire()) {
                double centerX = x + 0.5F;
                double centerY = y + 2F;
                double centerZ = z + 0.5F;
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.15D, 0.0D);
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY - 1, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY - 1, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.15D, 0.0D);
            }
        }
    }

    @Override
    public float getHeatSourceRadius() {
        return 7;
    }

    @Override
    public Class getTileEntityType() {
        return TileEntityWoodPile.class;
    }

}
