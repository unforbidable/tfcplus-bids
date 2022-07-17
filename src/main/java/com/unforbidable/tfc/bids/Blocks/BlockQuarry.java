package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class BlockQuarry extends BlockContainer {

    public BlockQuarry() {
        super(Material.wood);
        setHardness(5f);
    }

    @Override
    public int damageDropped(int meta) {
        return meta & 7;
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection d = ForgeDirection.getOrientation(meta & 7);

        float s2 = 1f / 16 * 1;
        float s1 = 1f - s2;

        float x1 = d == ForgeDirection.WEST ? s1 : 0;
        float y1 = d == ForgeDirection.DOWN ? s1 : 0;
        float z1 = d == ForgeDirection.NORTH ? s1 : 0;
        float x2 = d == ForgeDirection.EAST ? s2 : 1;
        float y2 = d == ForgeDirection.UP ? s2 : 1;
        float z2 = d == ForgeDirection.SOUTH ? s2 : 1;

        setBlockBounds(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.quarryRenderId;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityQuarry();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!world.isRemote) {
            TileEntityQuarry quarry = (TileEntityQuarry) world.getTileEntity(x, y, z);
            if (quarry != null) {
                quarry.dropAllWedges();
            }
        }
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack is) {
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        if (!world.isRemote) {
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null) {
                int[] equipIDs = OreDictionary.getOreIDs(heldItem);
                boolean isHammer = false;
                for (int id : equipIDs) {
                    String name = OreDictionary.getOreName(id);
                    if (name.startsWith("itemHammer")) {
                        isHammer = true;
                        break;
                    }
                }

                if (isHammer) {
                    if (QuarryHelper.isQuarryReadyAt(world, x, y, z)) {
                        Bids.LOG.info("Quarry completed");
                        dropQuarriedBlock(world, x, y, z);
                    }
                }
            }
        }

        super.onBlockHarvested(world, x, y, z, meta, player);
    }

    protected void dropQuarriedBlock(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityQuarry) {
            TileEntityQuarry quarry = (TileEntityQuarry) te;
            ForgeDirection d = quarry.getQuarryOrientation();
            int x2 = x - d.offsetX;
            int y2 = y - d.offsetY;
            int z2 = z - d.offsetZ;
            Block block = world.getBlock(x2, y2, z2);
            int meta = world.getBlockMetadata(x2, y2, z2);
            IQuarriable quarriable = QuarryRegistry.getBlockQuarriable(block);
            if (quarriable != null) {
                world.setBlockToAir(x2, y2, z2);
                // Drop the original quarried block for now
                Block droppedBlock = quarriable.getQuarriedBlock();
                int droppedMeta = quarriable.getQuarriedBlockMetadata(meta);
                ItemStack is = new ItemStack(Item.getItemFromBlock(droppedBlock), 1, droppedMeta);
                EntityItem entityItem = new EntityItem(world, x2, y2 + 1, z2, is);
                world.spawnEntityInWorld(entityItem);
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection d = ForgeDirection.getOrientation(meta & 7);
        ForgeDirection o = d.getOpposite();
        Block block = world.getBlock(x + o.offsetX, y + o.offsetY, z + o.offsetZ);
        return QuarryRegistry.isBlockQuarriable(block);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        super.onNeighborBlockChange(world, x, y, z, b);

        if (!canBlockStay(world, x, y, z))
            world.setBlockToAir(x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
            float hitY, float hitZ) {
        if (world.isRemote) {
            world.markBlockForUpdate(x, y, z);
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

}
