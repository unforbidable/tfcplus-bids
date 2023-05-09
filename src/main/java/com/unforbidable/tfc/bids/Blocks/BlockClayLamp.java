package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.BlockCandle;
import com.dunk.tfc.Blocks.BlockCandleOff;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Common.Bounds.ClayLampBounds;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayLamp;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.Random;

public class BlockClayLamp extends BlockContainer {

    IIcon topIcon;
    IIcon sideIcon;

    public BlockClayLamp() {
        super(Material.circuits);

        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setTickRandomly(true);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int orientation = world.getBlockMetadata(x, y, z) & 3;
        AxisAlignedBB bounds = ClayLampBounds.getBoundsForOrientation(orientation).getEntireBounds();
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta > 7) {
            return 10;
        }

        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registerer) {
        sideIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "Ceramic Lamp.Side");
        topIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "Ceramic Lamp.Top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) {
            return topIcon;
        } else {
            return sideIcon;
        }
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return BidsBlocks.clayLampRenderId;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return canSupportTorch(world, x, y - 1, z);
    }

    private boolean canSupportTorch(World world, int x, int y, int z)
    {
        if (World.doesBlockHaveSolidTopSurface(world, x, y, z)) {
            return true;
        } else {
            Block block = world.getBlock(x, y, z);
            return block.canPlaceTorchOnTop(world, x, y, z);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        super.updateTick(world, x, y, z, rand);

        if (world.getTileEntity(x, y, z) instanceof TileEntityClayLamp) {
            TileEntityClayLamp te = (TileEntityClayLamp) world.getTileEntity(x, y, z);
            if (te.isOnFire()) {
                te.updateFuelAmount(true);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is)
    {
        if (world.getTileEntity(x, y, z) instanceof TileEntityClayLamp) {
            final int orientation = (int) Math.floor(player.rotationYaw * 4F / 360F + 0.5D) & 3;
            world.setBlockMetadataWithNotify(x, y, z, orientation, 0x2);

            TileEntityClayLamp te = (TileEntityClayLamp) world.getTileEntity(x, y, z);
            te.onPlacedFromItemStack(is, orientation);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b)
    {
        if(!World.doesBlockHaveSolidTopSurface(world, x, y-1, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta)
    {
        if (!world.isRemote)
        {
            TileEntityClayLamp te = (TileEntityClayLamp)world.getTileEntity(x, y, z);
            if (te != null)
            {
                if (te.isOnFire()) {
                    te.updateFuelAmount(false);
                }

                if(te.hasFuel())
                {
                    ItemStack is = new ItemStack(this, 1, 0);
                    NBTTagCompound nbt = te.getFuel().writeToNBT(new NBTTagCompound());
                    is.setTagCompound(nbt);
                    EntityItem ei = new EntityItem(world,x,y,z,is);
                    world.spawnEntityInWorld(ei);
                }
                else
                {
                    ItemStack is = new ItemStack(this, 1, 0);
                    EntityItem ei = new EntityItem(world,x,y,z,is);
                    world.spawnEntityInWorld(ei);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta > 7) {
            int orientation = meta & 3;
            Vec3 flamePos = ClayLampBounds.getBoundsForOrientation(orientation).getFlamePos();

            double flameX = x + flamePos.xCoord;
            double flameY = y + flamePos.yCoord + 1 / 32f;
            double flameZ = z + flamePos.zCoord;

            world.spawnParticle("smoke", flameX, flameY, flameZ, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", flameX, flameY, flameZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote) {
            TileEntityClayLamp te = (TileEntityClayLamp) world.getTileEntity(x, y, z);
            if (te != null) {
                ItemStack equippedItem = player.getCurrentEquippedItem();
                if (equippedItem != null) {
                    if ((FluidContainerRegistry.isFilledContainer(equippedItem)
                        || equippedItem.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem) equippedItem.getItem()).getFluid(equippedItem) != null))
                    {
                        ItemStack tmp = equippedItem.copy();
                        tmp.stackSize = 1;

                        ItemStack is = te.addLiquid(tmp);
                        if (ItemStack.areItemStacksEqual(equippedItem, is)) {
                            return false;
                        }

                        boolean finished = false;
                        while (!finished) {
                            tmp =  is.copy();
                            is = te.addLiquid(is);

                            finished = ItemStack.areItemStacksEqual(tmp, is);
                        }

                        equippedItem.stackSize--;

                        if (equippedItem.stackSize == 0) {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                        }

                        if (equippedItem.stackSize == 0 && (is.getMaxStackSize() == 1 || !player.inventory.hasItemStack(is))) {
                            // put empty container in the slot you used them from.
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, is);
                        } else {
                            if (!player.inventory.addItemStackToInventory(is)) {
                                // if the new item doesn't fit, drop it.
                                player.dropPlayerItemWithRandomChoice(is, false);
                            }
                        }

                        if (player.inventoryContainer != null)  {
                            // for some reason the players inventory won't update without this.
                            player.inventoryContainer.detectAndSendChanges();
                        }

                        return true;
                    } else if (!te.isOnFire()) {
                        if (equippedItem.getItem() == Item.getItemFromBlock(TFCBlocks.torch) ||
                            Block.getBlockFromItem(equippedItem.getItem()) instanceof BlockCandle &&
                                !(Block.getBlockFromItem(player.inventory.getCurrentItem().getItem()) instanceof BlockCandleOff)) {
                            if (!TFC_Core.isExposedToRain(world, x, y, z)) {
                                te.setOnFire(true);
                            }
                            return true;
                        }
                    } else if (te.isOnFire()) {
                        if (equippedItem.getItem() == Item.getItemFromBlock(TFCBlocks.torchOff)) {
                            player.inventory.consumeInventoryItem(equippedItem.getItem());
                            TFC_Core.giveItemToPlayer(new ItemStack(TFCBlocks.torch), player);
                        } else if (Block.getBlockFromItem(equippedItem.getItem()) instanceof BlockCandleOff) {
                            player.inventory.consumeInventoryItem(equippedItem.getItem());
                            TFC_Core.giveItemToPlayer(new ItemStack(((BlockCandle) Block.getBlockFromItem(equippedItem.getItem())).getAlternate()), player);
                        }
                    }
                } else {
                    if (te.isOnFire()) {
                        te.setOnFire(false);
                        return true;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityClayLamp();
    }

}
