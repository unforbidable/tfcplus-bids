package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.CookingPotBounds;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class BlockCookingPotLid extends Block {

    IIcon[] topIcons = new IIcon[2];
    IIcon[] sideIcons = new IIcon[2];

    public BlockCookingPotLid() {
        super(Material.rock);

        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setHardness(0.25f);

        AxisAlignedBB bb = CookingPotBounds.LID_ONLY_ENTIRE_BOUNDS;
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, 0));
        par3List.add(new ItemStack(this, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registerer) {
        sideIcons[0] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Side");
        sideIcons[1] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Side");
        topIcons[0] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Top");
        topIcons[1] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) {
            return topIcons[meta];
        } else {
            return sideIcons[meta];
        }
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
        return BidsBlocks.cookingPotLidRenderId;
    }

    @Override
    public int damageDropped(int meta) {
        return super.damageDropped(meta);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            ItemStack is = new ItemStack(Item.getItemFromBlock(this), 1, world.getBlockMetadata(x, y, z));
            EntityItem entityItem = new EntityItem(world, x, y, z, is);
            world.spawnEntityInWorld(entityItem);

            world.setBlockToAir(x, y, z);

            return true;
        }

        return true;
    }

}
