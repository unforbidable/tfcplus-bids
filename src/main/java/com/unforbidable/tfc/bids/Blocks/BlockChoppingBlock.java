package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ChoppingBlock.ChoppingBlockHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChoppingBlock;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
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

import java.util.List;

public class BlockChoppingBlock extends BlockContainer {

    public final Block materialBlock;

    public BlockChoppingBlock(Block materialBlock) {
        super(materialBlock.getMaterial());

        this.materialBlock = materialBlock;

        setHardness(10);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setBlockBounds(0, 0, 0, 1, ChoppingBlockHelper.getChoppingBlockBoundsHeight(), 1);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        int offset = materialBlock.damageDropped(0);
        int size = Math.max(0, Math.min(16, Global.WOOD_ALL.length - offset));
        for (int i = 0; i < size; i++) {
            WoodIndex wood = WoodScheme.DEFAULT.findWood(offset + i);
            if (wood.blocks.hasChoppingBlock()) {
                list.add(wood.blocks.getChoppingBlock());
            }
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return materialBlock.getIcon(side, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
            float hitY, float hitZ) {
        ItemStack heldItemStack = player.getCurrentEquippedItem();
        if (heldItemStack != null) {
            if (ChoppingBlockHelper.placeItemOnChoppingBlockAt(heldItemStack, player, world, x, y, z, hitX, hitY,
                    hitZ)) {
                return true;
            }
        } else {
            if (ChoppingBlockHelper.retrieveItemFromChoppingBlockAt(player, world, x, y, z, hitX, hitY, hitZ)) {
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
        TileEntityChoppingBlock te = (TileEntityChoppingBlock) world.getTileEntity(x, y, z);
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
        return BidsBlocks.choppingBlockRenderId;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityChoppingBlock();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = ChoppingBlockHelper.onChoppingBlockCollisionRayTrace(world, x, y, z, startVec,
                endVec);
        if (mop != null) {
            return mop;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

}
