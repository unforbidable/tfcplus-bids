package com.unforbidable.tfc.bids.features.device.crucible.block;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityFireClayCrucible;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockFireClayCrucible extends BlockCrucible {

    IIcon[] icons = new IIcon[2];

    public BlockFireClayCrucible() {
        super(Material.rock);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setBlockBounds(0.0625f, 0.25f, 0.0625f, 0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityFireClayCrucible();
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        icons[0] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Top");
        icons[1] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Side");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        int i = side < 2 ? 0 : 1;
        return icons[i];
    }

}
