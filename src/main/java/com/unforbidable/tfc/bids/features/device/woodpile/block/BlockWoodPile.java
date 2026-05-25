package com.unforbidable.tfc.bids.features.device.woodpile.block;

import com.dunk.tfc.api.Interfaces.IHeatSource;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.device.woodpile.main.WoodpileHelper;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import com.unforbidable.tfc.bids.util.GuiUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
public class BlockWoodpile extends BlockContainer implements IHeatSource {

    IIcon icon;

    public BlockWoodpile() {
        super(Material.wood);

        setHardness(10f);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityWoodpile();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        icon = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + getTextureName());
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
            float par8, float par9) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityWoodpile) {
            TileEntityWoodpile woodpile = (TileEntityWoodpile) world.getTileEntity(x, y, z);
            if (handleInteraction(world, x, y, z, player, woodpile)) {
                return true;
            }

            if (!world.isRemote) {
                GuiUtil.openGui(BlockNames.WOODPILE, player, woodpile);
            }

            return true;
        }

        return false;
    }

    protected boolean handleInteraction(World world, int x, int y, int z, EntityPlayer player,
            TileEntityWoodpile woodpile) {
        ItemStack heldItemStack = player.getCurrentEquippedItem();
        if (heldItemStack != null
                && WoodpileHelper.insertIntoWoodpileAt(heldItemStack, player, world, x, y, z)) {
            return true;
        }

        if (WoodpileHelper.retrieveSelectedItemFromWoodpileAt(player, world, x, y, z)) {
            return true;
        }

        return false;
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityWoodpile te = (TileEntityWoodpile) world.getTileEntity(x, y, z);
        te.onWoodpileBroken();

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = WoodpileHelper.onWoodpileCollisionRayTrace(world, x, y, z, startVec, endVec);
        if (mop != null) {
            return mop;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityWoodpile) {
            TileEntityWoodpile woodpile = (TileEntityWoodpile) world.getTileEntity(x, y, z);
            woodpile.tryToCatchFire();
            woodpile.tryToSpreadFire();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityWoodpile) {
            TileEntityWoodpile woodpile = (TileEntityWoodpile) te;
            if (woodpile.isOnFire()) {
                double centerX = x + 0.5F;
                double centerY = y + 2F;
                double centerZ = z + 0.5F;
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.15D, 0.0D);
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY - 1, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.1D, 0.0D);
                world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY - 1, centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.15D, 0.0D);
            }
        }
    }

    @Override
    public float getHeatSourceRadius() {
        return 7;
    }

    @Override
    public Class getTileEntityType() {
        return TileEntityWoodpile.class;
    }

}
