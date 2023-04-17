package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.SaddleQuern.StonePressHelper;
import com.unforbidable.tfc.bids.Core.SaddleQuern.WeightBounds;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStonePressWeight;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class BlockStonePressWeight extends BlockContainer {

    public final BlockRoughStone materialBlock;

    public BlockStonePressWeight(BlockRoughStone materialBlock) {
        super(materialBlock.getMaterial());

        this.materialBlock = materialBlock;

        setHardness(10);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
        AxisAlignedBB bounds = WeightBounds.getTotal();
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        boolean lifted = StonePressHelper.isWeightLiftedAt(world, x, y, z);
        AxisAlignedBB bounds = WeightBounds.fromLifted(lifted).getWeight();
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ,
            (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        List<ItemStack> materialBlockSubBlocks = new ArrayList<ItemStack>();
        materialBlock.getSubBlocks(item, tabs, materialBlockSubBlocks);

        for (ItemStack is : materialBlockSubBlocks) {
            list.add(new ItemStack(this, 1, is.getItemDamage()));
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return materialBlock.getIcon(side, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
            float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityStonePressWeight weightTileEntity = (TileEntityStonePressWeight) world.getTileEntity(x, y, z);
            ItemStack heldItemStack = player.getCurrentEquippedItem();
            if (heldItemStack != null && weightTileEntity.isValidRopeItem(heldItemStack)
                && weightTileEntity.setRopeItem(heldItemStack)) {
                return true;
            } else if (heldItemStack == null
                && weightTileEntity.retrieveRope(player)) {
                return true;
            }
        }

        return true;
    }

    @Override
    public int damageDropped(int damage) {
        return damage;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityStonePressWeight te = (TileEntityStonePressWeight) world.getTileEntity(x, y, z);
        if (te != null) {
            te.onBlockBroken();
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.stonePressWeightRenderId;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityStonePressWeight();
    }

}
