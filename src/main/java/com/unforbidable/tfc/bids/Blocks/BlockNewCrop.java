package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.BlockCrop;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemFertilizer;
import com.dunk.tfc.TileEntities.TEFarmland;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropIndex;
import com.unforbidable.tfc.bids.Core.Crops.CropAccess;
import com.unforbidable.tfc.bids.Core.Crops.CropHelper;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropManager;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewCrop;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNewCrop extends BlockCrop {

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityNewCrop();
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        for (BidsCropIndex crop : BidsCropManager.getCrops()) {
            crop.cropRenderer.registerBlockIcons(register, crop.numGrowthStages);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int meta) {
        CropAccess cropAccess = new CropAccess(access, x, y, z);
        BidsCropIndex crop = cropAccess.getIndex();
        return crop.cropRenderer.getBlockIcon(cropAccess.getGrowthStage(), cropAccess.hasFarmland());
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        Block below = world.getBlock(x, y - 1, z);
        return CropHelper.isFarmland(below) || TFC_Core.isSoil(below);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack heldStack = entityplayer.getHeldItem();
            if (CropHelper.isFarmland(world.getBlock(x, y - 1, z))
                && heldStack != null && heldStack.getItem() instanceof ItemFertilizer) {
                TEFarmland tef = (TEFarmland) world.getTileEntity(x, y - 1, z);
                if (tef.nutrients[3] != TEFarmland.getSoilMax()) {
                    tef.fertilize(heldStack, false);

                    world.markBlockForUpdate(x, y - 1, z);
                }

                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, entityplayer, side, hitX, hitY, hitZ);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.newCropsRenderId;
    }

}
