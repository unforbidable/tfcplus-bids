package com.unforbidable.tfc.bids.features.device.cookingpit.block;

import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityClayCookingPit;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockClayCookingPit extends BlockCookingPit {

    protected IIcon constructingSideIcon;
    protected IIcon constructingTopIcon;

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0) {
            // Dried clay lined cooking pit is harder to break
            return 1f;
        }

        return super.getBlockHardness(world, x, y, z);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        super.registerBlockIcons(registerer);

        constructingSideIcon = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Constructing.Side");
        constructingTopIcon = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Constructing.Top");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta > 0) {
            if (side == 0 || side == 1) {
                return constructingTopIcon;
            } else {
                return constructingSideIcon;
            }
        } else {
            return super.getIcon(side, meta);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityClayCookingPit();
    }

}
