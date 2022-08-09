package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.Tags;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockTiedStickBundle extends Block {

    private IIcon iconEnd;
    private IIcon iconSide;

    public BlockTiedStickBundle() {
        super(Material.wood);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        iconEnd = iconRegisterer.registerIcon(Tags.MOD_ID + ":Tied Stick Bundle End");
        iconSide = iconRegisterer.registerIcon(Tags.MOD_ID + ":Tied Stick Bundle Side");
    }

    @Override
    public IIcon getIcon(int side, int damage) {
        if (side > 1) {
            return iconSide;
        }

        return iconEnd;
    }

}
