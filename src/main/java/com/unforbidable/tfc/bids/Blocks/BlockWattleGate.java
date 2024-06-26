package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Vanilla.BlockCustomWall;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.IConnectableFenceGate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockWattleGate extends BlockCustomWall implements IConnectableFenceGate {

    public BlockWattleGate() {
        super(BidsBlocks.tiedStickBundle, 0);

        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setHardness(2f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        list.add((new ItemStack(this, 1, 0)));
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFCBlocks.wattle.getIcon(side, meta);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.wattleGateRenderId;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is) {
        int dir = (MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5) & 3) % 4;
        world.setBlockMetadataWithNotify(x, y, z, dir, 2);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int size, float hitX, float hitY, float hitZ) {
        int meta = world.getBlockMetadata(x, y, z);
        if (isFenceGateOpen(meta)) {
            world.setBlockMetadataWithNotify(x, y, z, meta & -5, 2);
        } else {
            world.setBlockMetadataWithNotify(x, y, z, meta | 4, 2);
        }

        world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
        return true;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = getDirection(world.getBlockMetadata(x, y, z));
        if (meta != 2 && meta != 0) {
            this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        } else {
            this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
        }
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity) {
        int meta = world.getBlockMetadata(x, y, z);
        if (!isFenceGateOpen(meta)) {
            AxisAlignedBB bounds;

            if (entity instanceof EntityPlayer) {
                if (meta != 2 && meta != 0) {
                    bounds = AxisAlignedBB.getBoundingBox(x + 0.375F, y, z, x + 0.625F, y + 1.5F, z + 1);
                } else {
                    bounds = AxisAlignedBB.getBoundingBox(x, y, z + 0.375F, x + 1, y + 1.5F, z + 0.625F);
                }
            } else {
                bounds = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1.5f, z + 1);
            }

            if (aabb.intersectsWith(bounds)) {
                list.add(bounds);
            }
        }
    }

    public static boolean isFenceGateOpen(int meta) {
        return (meta & 4) != 0;
    }

    public static int getDirection(int meta) {
        return meta & 3;
    }

}
