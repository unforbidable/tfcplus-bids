package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Terrain.BlockCollapsible;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockRoughStone extends Block {

    protected IIcon[] topIcons;
    protected IIcon[] icons;
    protected String[] names;
    protected List<Integer> metaWhitelist = null;
    protected boolean allHaveTopTexture = false;
    protected List<Integer> metaHavingTopTexture = null;

    public BlockRoughStone() {
        super(Material.rock);
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setHardness(10f);
    }

    public BlockRoughStone setMetaOnly(Integer ...metaOnly) {
        this.metaWhitelist = Arrays.asList(metaOnly);
        return this;
    }

    public BlockRoughStone setAllHaveTopTexture(boolean allHaveTopTexture) {
        this.allHaveTopTexture = allHaveTopTexture;
        return this;
    }

    public BlockRoughStone setMetaHavingTopTexture(Integer ...metaHavingTopTexture) {
        this.metaHavingTopTexture = Arrays.asList(metaHavingTopTexture);
        return this;
    }

    public BlockRoughStone setNames(String[] names) {
        this.names = names;
        return this;
    }

    public String[] getNames() {
        return names;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        icons = new IIcon[names.length];
        topIcons = new IIcon[names.length];
        for (int i = 0; i < names.length; i++) {
            if (metaWhitelist != null && !metaWhitelist.contains(i)) {
                continue;
            }

            icons[i] = iconRegisterer.registerIcon(Tags.MOD_ID + ":rocks/"
                    + names[i] + " " + getTextureName());

            if (allHaveTopTexture || metaHavingTopTexture != null && metaHavingTopTexture.contains(i)) {
                topIcons[i] = iconRegisterer.registerIcon(Tags.MOD_ID + ":rocks/"
                    + names[i] + " " + getTextureName() + " Top");
            }
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side < 2) {
            if (allHaveTopTexture || metaHavingTopTexture != null && metaHavingTopTexture.contains(meta)) {
                return topIcons[meta];
            }
        }
        return icons[meta];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < names.length; i++) {
            if (metaWhitelist != null && !metaWhitelist.contains(i)) {
                continue;
            }

            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public int tickRate(World world) {
        return 3;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        tryToFall(world, x, y, z);
        super.updateTick(world, x, y, z, rand);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ, int metadata) {
        // Try to fall delayed
        // because metadata is not in the world data yet
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        return super.onBlockPlaced(world, x, y, z, side, hitX, hitY,
                hitZ, metadata);
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return true;
    }

    private void tryToFall(World world, int x, int y, int z) {
        if (!world.isRemote && canBlockFall(world, x, y, z)) {
            BlockCollapsible.tryToFall(world, x, y, z, this);
        }
    }

    protected boolean canBlockFall(World world, int x, int y, int z) {
        // This is to prevent blocks from falling and breaking a carving
        // because that would turn out to be rather annoying
        // However TFC blocks falling on a carving will still break it
        if (world.getTileEntity(x, y - 1, z) instanceof TileEntityCarving) {
            return false;
        }

        // Self-supporting arches and similar can override this
        // and add more exceptions
        return true;
    }

}
