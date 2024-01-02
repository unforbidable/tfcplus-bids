package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.BlockPlanks;
import com.dunk.tfc.Blocks.Devices.BlockAxleBearing;
import com.dunk.tfc.Blocks.Terrain.BlockMMBrick;
import com.dunk.tfc.Blocks.Terrain.BlockSmooth;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.TEAxleBearing;
import com.dunk.tfc.TileEntities.TERotator;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Common.Bounds.AxleWallBearingBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityAxleWallBearing;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAxleWallBearing extends BlockAxleBearing {

    public BlockAxleWallBearing(Material m) {
        super(m);

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.axleWallBearingRenderId;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileEntityAxleWallBearing();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess bAccess, int x, int y, int z) {
        int meta = bAccess.getBlockMetadata(x, y, z);
        int dir = getDirectionFromMetadata(meta);
        AxisAlignedBB bounds = AxleWallBearingBounds.getBounds(dir).getTotal();

        TileEntityAxleWallBearing te = (TileEntityAxleWallBearing)bAccess.getTileEntity(x, y, z);
        if (te.hasAxle() || te.hasCover()) {
            setBlockBounds(0, 0, 0, 1, 1, 1);
        } else {
            setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        TileEntityAxleWallBearing te = (TileEntityAxleWallBearing) world.getTileEntity(x, y, z);

        ItemStack heldItem = entityplayer.getHeldItem();
        if (heldItem != null) {
            Block heldBlock = Block.getBlockFromItem(heldItem.getItem());
            int heldBlockMetadata = heldBlock.damageDropped(heldItem.getItemDamage());
            if (!te.hasCover() && isValidCoverBlock(heldBlock, heldBlockMetadata)) {
                if (!world.isRemote) {
                    if (!entityplayer.capabilities.isCreativeMode) {
                        entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem].stackSize--;
                    }

                    if (entityplayer.getHeldItem().stackSize == 0) {
                        entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
                    }

                    te.setCover(heldBlock, heldBlockMetadata);

                    world.markBlockForUpdate(x, y, z);
                }

                // Needs to return true for both server and client to prevent the block getting placed
                return true;
            }
        }

        // Call super to allow removal of a gear, before removing the cover
        if (super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9)) {
            return true;
        }

        if (!world.isRemote && te.hasCover() && entityplayer.isSneaking()) {
            Block coverBlock = Block.getBlockById(te.getCoverBlockId());
            int coverBlockMetadata = te.getCoverBlockMetadata();
            ItemStack is = new ItemStack(coverBlock, 1, coverBlockMetadata);
            TFC_Core.giveItemToPlayer(is, entityplayer);

            te.clearCover();

            world.markBlockForUpdate(x, y, z);

            return true;
        }

        return false;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int oldMeta) {
        super.onBlockPreDestroy(world, x, y, z, oldMeta);

        TileEntityAxleWallBearing te = (TileEntityAxleWallBearing)world.getTileEntity(x, y, z);
        if (te.hasCover()) {
            Block coverBlock = Block.getBlockById(te.getCoverBlockId());
            int coverBlockMetadata = te.getCoverBlockMetadata();
            ItemStack is = new ItemStack(coverBlock, 1, coverBlockMetadata);
            world.spawnEntityInWorld(new EntityItem(world, x, y, z, is));
        }
    }

    private boolean isValidCoverBlock(Block block, int metadata) {
        return block instanceof BlockRoughStoneBrick ||
            block instanceof BlockPlanks ||
            block instanceof BlockSmooth;
    }

}
