package com.unforbidable.tfc.bids.Blocks;

import java.util.List;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.api.Enums.EnumLogWallType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockLogWall extends Block {

    IIcon[][] icons = new IIcon[3][];

    final String[] names;
    final int offset;
    final EnumLogWallType type;

    public BlockLogWall(EnumLogWallType type, int offset) {
        super(Material.wood);
        this.offset = offset;
        this.type = type;
        names = WoodHelper.getWoodOffsetNames(offset);
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
        setHardness(10);
        setHarvestLevel("axe", 0);
    }

    public int getOffset() {
        return offset;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        for (int j = 0; j < 3; j++) {
            icons[j] = new IIcon[names.length];
            for (int i = 0; i < names.length; i++) {
                if (WoodHelper.canBuildLogWall(i + offset)) {
                    icons[j][i] = iconRegisterer.registerIcon(Tags.MOD_ID + ":wood/"
                            + names[i] + " Log Wall " + getTextureSuffix(j));
                }
            }
        }
    }

    private String getTextureSuffix(int halfSide) {
        switch (type) {
            case EAST:
                switch (halfSide) {
                    case 0:
                        return "Side";
                    case 1:
                        return "Side";
                    case 2:
                        return "End";
                }

            case NORTH:
                switch (halfSide) {
                    case 0:
                        return "Top";
                    case 1:
                        return "End Shifted";
                    case 2:
                        return "Side Shifted";
                }

            case CORNER:
                switch (halfSide) {
                    case 0:
                        return "Top";
                    case 1:
                        return "End Shifted";
                    case 2:
                        return "End";
                }

            case EAST_ALT:
                switch (halfSide) {
                    case 0:
                        return "Top Turned";
                    case 1:
                        return "Side Shifted";
                    case 2:
                        return "End Shifted";
                }

            case NORTH_ALT:
                switch (halfSide) {
                    case 0:
                        return "Side Turned";
                    case 1:
                        return "End";
                    case 2:
                        return "Side";
                }

            case CORNER_ALT:
                switch (halfSide) {
                    case 0:
                        return "Side";
                    case 1:
                        return "Side";
                    case 2:
                        return "Side Shifted";
                }

            default:
                return "Unknown";
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (WoodHelper.canBuildLogWall(meta + offset)) {
            return icons[side / 2][meta];
        }

        return blockIcon;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        if (WoodHelper.getDefaultLogWallType() == type) {
            for (int i = 0; i < names.length; i++) {
                if (WoodHelper.canBuildLogWall(i + offset))
                    par3List.add(new ItemStack(par1, 1, i));
            }
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack is) {

        Block block = WoodHelper.getDefaultLogWallBlock(offset);
        is = new ItemStack(block, 1, is.getItemDamage());

        super.dropBlockAsItem(world, x, y, z, is);
    }

}
