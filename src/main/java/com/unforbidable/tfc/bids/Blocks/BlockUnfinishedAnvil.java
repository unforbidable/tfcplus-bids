package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockUnfinishedAnvil extends Block {

    private final int stage;

    public static final int NUM_STAGES = 6;
    public static final String[] META_NAMES = new String[] { "Stone", "Copper", "Bronze", "Wrought Iron", "Steel", "Black Steel", "Blue Steel", "Red Steel",
        "Rose Gold", "Bismuth Bronze", "Black Bronze" };

    public BlockUnfinishedAnvil(int stage) {
        super(Material.iron);

        this.stage = stage;

        this.setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        // Show only stage 1 unfinished anvils in Creative
        if (stage == 0) {
            for(int i = 1; i < META_NAMES.length; i++) {
                par3List.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public IIcon getIcon(int i, int j) {
        if (j < 8) {
            return TFCBlocks.anvil.getIcon(i, j);
        } else {
            return TFCBlocks.anvil2.getIcon(i, j - 8);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
        return true;
    }

    @Override
    public int getRenderType() {
        switch (stage) {
            case 0:
                return BidsBlocks.unfinishedAnvilStage1RenderId;
            case 1:
                return BidsBlocks.unfinishedAnvilStage2RenderId;
            case 2:
                return BidsBlocks.unfinishedAnvilStage3RenderId;
            case 3:
                return BidsBlocks.unfinishedAnvilStage4RenderId;
            case 4:
                return BidsBlocks.unfinishedAnvilStage5RenderId;
            case 5:
            default:
                return BidsBlocks.unfinishedAnvilStage6RenderId;
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
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return false;
    }

    public int getStage() {
        return stage;
    }

    public static ItemStack getFinishedAnvil(int mat) {
        if (mat < 8) {
            return new ItemStack(TFCBlocks.anvil, 1, mat);
        } else {
            return new ItemStack(TFCBlocks.anvil2, 1, mat - 8);
        }
    }

    public static ItemStack getUnfinishedAnvil(int mat, int stage) {
        switch (stage) {
            case 0:
                return new ItemStack(BidsBlocks.unfinishedAnvilStage1, 1, mat);
            case 1:
                return new ItemStack(BidsBlocks.unfinishedAnvilStage2, 1, mat);
            case 2:
                return new ItemStack(BidsBlocks.unfinishedAnvilStage3, 1, mat);
            case 3:
                return new ItemStack(BidsBlocks.unfinishedAnvilStage4, 1, mat);
            case 4:
                return new ItemStack(BidsBlocks.unfinishedAnvilStage5, 1, mat);
            case 5:
            default:
                return new ItemStack(BidsBlocks.unfinishedAnvilStage6, 1, mat);
        }
    }

}
