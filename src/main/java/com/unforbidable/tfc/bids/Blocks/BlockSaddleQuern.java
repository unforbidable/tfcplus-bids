package com.unforbidable.tfc.bids.Blocks;

import java.util.List;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySaddleQuern;
import com.unforbidable.tfc.bids.TileEntities.TileEntitySaddleQuern.Selection;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockSaddleQuern extends BlockContainer {

    final BlockRoughStone materialBlock;

    public BlockSaddleQuern(BlockRoughStone materialBlock) {
        super(materialBlock.getMaterial());

        this.materialBlock = materialBlock;

        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setHardness(10f);
    }

    public Block getMaterialBlock() {
        return materialBlock;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z,
            EntityLivingBase player, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, player, itemStack);

        final int orientation = (int) Math.floor(player.rotationYaw * 4F / 360F + 0.5D) & 3;

        TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
        saddleQuern.setOrientation(orientation);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
            EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
        ItemStack heldItemStack = player.getCurrentEquippedItem();
        if (!world.isRemote) {
            if (heldItemStack == null) {
                if (player.isSneaking() && side == 1) {
                    boolean isItemHit = getIsItemHit(saddleQuern, side, hitX, hitY, hitZ);
                    if (isItemHit) {
                        saddleQuern.retrieveInputStack(player);
                    } else {
                        saddleQuern.retrieveWorkStone(player);
                    }
                } else {
                    saddleQuern.operate();
                }
            } else {
                if (saddleQuern.isValidWorkStone(heldItemStack)) {
                    saddleQuern.setWorkStone(heldItemStack);
                } else if (saddleQuern.isValidInput(heldItemStack)) {
                    saddleQuern.setInputStack(heldItemStack);
                }
            }
        }

        return true;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = super.collisionRayTrace(world, x, y, z, startVec, endVec);

        if (mop != null) {
            TileEntitySaddleQuern saddleQuern = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
            if (mop.sideHit == 1) {
                boolean isItemHit = getIsItemHit(saddleQuern, mop.sideHit,
                        (float) mop.hitVec.xCoord - x, (float) mop.hitVec.yCoord - y, (float) mop.hitVec.zCoord - z);
                if (isItemHit) {
                    saddleQuern.setSelection(Selection.INPUT_STACK);
                } else {
                    saddleQuern.setSelection(Selection.WORK_STONE);
                }
            } else {
                saddleQuern.setSelection(Selection.NONE);
            }
        }

        return mop;
    }

    private boolean getIsItemHit(TileEntitySaddleQuern saddleQuern, int side, float hitX, float hitY, float hitZ) {
        switch (saddleQuern.getOrientation()) {
            case 0:
                return hitZ > 0.66;

            case 1:
                return hitX < 0.33;

            case 2:
                return hitZ < 0.33;

            case 3:
                return hitX > 0.66;
        }

        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < materialBlock.getNames().length; i++) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        // When the draining stone has no work stone
        // reduce hardness to allow breaking by hand
        TileEntitySaddleQuern te = (TileEntitySaddleQuern) world.getTileEntity(x, y, z);
        if (te != null && !te.hasWorkStone()) {
            return 1f;
        }

        return super.getBlockHardness(world, x, y, z);
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
        return BidsBlocks.saddleQuernRenderId;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntitySaddleQuern();
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return materialBlock.getIcon(side, metadata);
    }

}
