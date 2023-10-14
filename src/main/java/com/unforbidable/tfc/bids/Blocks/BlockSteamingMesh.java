package com.unforbidable.tfc.bids.Blocks;

import com.unforbidable.tfc.bids.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockSteamingMesh extends Block {

    public BlockSteamingMesh() {
        super(Material.cloth);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        blockIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureName);
    }

}
