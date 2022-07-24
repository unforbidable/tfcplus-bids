package com.unforbidable.tfc.bids.Blocks;

import java.util.List;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockClayCrucible extends BlockCrucible {

    IIcon[] icons = new IIcon[6];

    public BlockClayCrucible() {
        super(Material.rock);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
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
        par3List.add(new ItemStack(this, 1, 2));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        icons[0] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Top");
        icons[1] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Side");
        icons[2] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Top");
        icons[3] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Side");
        icons[4] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ruined.Top");
        icons[5] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ruined.Side");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        int i = side < 2 ? 0 : 1;
        // meta 0 is cermamic, meta 1 is clay (unfired)
        if (meta == 0)
            i += 2;
        else if (meta == 2)
            i += 4;
        return icons[i];
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        int meta = access.getBlockMetadata(x, y, z);
        int i = side < 2 ? 0 : 1;
        // meta 0 is cermamic, meta 1 is clay (unfired)
        if (meta == 0)
            i += 2;
        else if (meta == 2)
            i += 4;
        return icons[i];
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int par6, float par7,
            float par8, float par9) {
        TileEntity te = world.getTileEntity(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (!world.isRemote && meta == 2 && te instanceof TileEntityCrucible) {
            return true;
        } else {
            return super.onBlockActivated(world, x, y, z, entityplayer, par6, par7, par8, par9);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && metadata == 2 && te instanceof TileEntityCrucible) {
            TileEntityCrucible crucible = (TileEntityCrucible) te;

            // Drop all inventory
            dropContents(crucible);

            dropLiquidContentAsLump(crucible);

            world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "dig.glass",
                    0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());

            return;
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    private boolean dropLiquidContentAsLump(TileEntityCrucible crucible) {
        // Only glass
        if (crucible.isLiquidMetal(Global.GLASS)) {
            Bids.LOG.info("Glass retrieved from crucible: " + crucible.getLiquidMetalVolume());
            ItemStack lump = new ItemStack(BidsItems.glassLump);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("volume", crucible.getLiquidMetalVolume());
            lump.setTagCompound(nbt);
            EntityItem lumpEntityItem = new EntityItem(crucible.getWorldObj(),
                    crucible.xCoord, crucible.yCoord, crucible.zCoord, lump);
            crucible.getWorldObj().spawnEntityInWorld(lumpEntityItem);
            return true;
        }

        return false;
    }

}
