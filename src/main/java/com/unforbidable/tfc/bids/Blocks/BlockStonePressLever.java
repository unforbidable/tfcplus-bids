package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.Core.SaddleQuern.LeverBounds;
import com.unforbidable.tfc.bids.Core.SaddleQuern.StonePressHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStonePressLever;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Events.SaddleQuernPlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockStonePressLever extends BlockContainer {

    public BlockStonePressLever() {
        super(Material.wood);

        setHardness(2);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        AxisAlignedBB bounds = LeverBounds.fromOrientation(world.getBlockMetadata(x, y, z)).getLog();
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ,
            (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
            float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityStonePressLever leverTileEntity = (TileEntityStonePressLever) world.getTileEntity(x, y, z);
            ItemStack heldItemStack = player.getCurrentEquippedItem();
            if (heldItemStack != null && leverTileEntity.isValidRopeItem(heldItemStack)) {
                if (leverTileEntity.setRopeItem(heldItemStack)) {
                    SaddleQuernPlayerEvent event = new SaddleQuernPlayerEvent(player, null, SaddleQuernPlayerEvent.Action.ATTACH_WEIGHT_STONE);
                    MinecraftForge.EVENT_BUS.post(event);

                    return true;
                }
            } else if (heldItemStack == null) {
                if (leverTileEntity.retrieveRope(player)) {
                    SaddleQuernPlayerEvent event = new SaddleQuernPlayerEvent(player, null, SaddleQuernPlayerEvent.Action.DETACH_WEIGHT_STONE);
                    MinecraftForge.EVENT_BUS.post(event);

                    return true;
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityStonePressLever te = (TileEntityStonePressLever) world.getTileEntity(x, y, z);
        if (te != null) {
            te.onBlockBroken();
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        super.onNeighborBlockChange(world, x, y, z, b);

        if (!canBlockStay(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        TileEntityStonePressLever te = (TileEntityStonePressLever) world.getTileEntity(x, y, z);
        ForgeDirection d = te.getSaddleQuernOutputForgeDirection();

        if (te.getLeverPart() == TileEntityStonePressLever.PART_BASE) {
            // Saddle quern below, attached to lever front and anchor block back
            return world.getBlock(x, y - 1, z) == BidsBlocks.saddleQuernBaseSed
                && world.getBlock(x + d.offsetX, y, z + d.offsetZ) == BidsBlocks.stonePressLever
                && StonePressHelper.isValidAnchorBlockAt(world, x - d.offsetX, y, z - d.offsetZ);
        }

        if (te.getLeverPart() == TileEntityStonePressLever.PART_FREE) {
            // Just attached to lever front and back
            return world.getBlock(x + d.offsetX, y, z + d.offsetZ) == BidsBlocks.stonePressLever
                && world.getBlock(x - d.offsetX, y, z - d.offsetZ) == BidsBlocks.stonePressLever;
        }

        if (te.getLeverPart() == TileEntityStonePressLever.PART_WEIGHT) {
            // Just attached to lever at the back
            return world.getBlock(x - d.offsetX, y, z - d.offsetZ) == BidsBlocks.stonePressLever;
        }

        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.stonePressLeverRenderId;
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess bAccess, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityStonePressLever();
    }

}
