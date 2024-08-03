package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodIndex;
import com.unforbidable.tfc.bids.Core.Wood.WoodScheme;
import com.unforbidable.tfc.bids.Tags;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockStackedFirewood extends Block {

    IIcon[] iconsTop;
    IIcon[] iconsSide;

    final String[] names;
    final int offset;

    public BlockStackedFirewood(int offset) {
        super(Material.wood);
        this.offset = offset;
        names = WoodHelper.getWoodOffsetNames(offset);
    }

    public int getOffset() {
        return offset;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        iconsTop = new IIcon[names.length];
        iconsSide = new IIcon[names.length];

        for (int i = 0; i < names.length; i++) {
            WoodIndex wood = WoodScheme.DEFAULT.findWood(i + offset);
            if (wood.blocks.hasStackedFirewood()) {
                iconsTop[i] = iconRegisterer.registerIcon(Tags.MOD_ID + ":wood/"
                        + names[i] + " Firewood Top");
                iconsSide[i] = iconRegisterer.registerIcon(Tags.MOD_ID + ":wood/"
                        + names[i] + " Firewood Side");
            }
        }
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        WoodIndex wood = WoodScheme.DEFAULT.findWood(meta + offset);
        if (wood.blocks.hasStackedFirewood()) {
            if (side < 2) {
                return iconsTop[meta];
            } else {
                return iconsSide[meta];
            }
        }

        return blockIcon;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < names.length; i++) {
            WoodIndex wood = WoodScheme.DEFAULT.findWood(i + offset);
            if (wood.blocks.hasStackedFirewood()) {
                par3List.add(wood.blocks.getStackedFirewood());
            }
        }
    }

}
