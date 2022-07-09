package com.unforbidable.tfc.bids.Blocks;

import java.util.List;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockClayCrucible extends BlockCrucible {

    IIcon[] icons = new IIcon[4];

    public BlockClayCrucible() {
        super(Material.rock);
        setCreativeTab(BidsCreativeTabs.BidsDefault);
        setBlockBounds(0.125f, 0.25f, 0.125f, 0.875f, 0.625f, 0.875f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityClayCrucible();
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.clayCrucibleRenderId;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, 0));
        par3List.add(new ItemStack(this, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        icons[0] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Top");
        icons[1] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Side");
        icons[2] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Top");
        icons[3] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Side");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        int i = side < 2 ? 0 : 1;
        // meta 0 is cermamic, meta 1 is clay (unfired)
        if (meta == 0)
            i += 2;
        return icons[i];
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        int meta = access.getBlockMetadata(x, y, z);
        int i = side < 2 ? 0 : 1;
        // meta 0 is cermamic, meta 1 is clay (unfired)
        if (meta == 0)
            i += 2;
        return icons[i];
    }

}
