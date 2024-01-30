package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.Items.ItemBlocks.ItemBarrels;
import com.dunk.tfc.Items.ItemBlocks.ItemLargeVessel;
import com.dunk.tfc.Items.Tools.ItemCustomBucketMilk;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsGui;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class BlockScrewPressBarrel extends BlockContainer {

    public BlockScrewPressBarrel() {
        super(Material.wood);

        setHardness(2f);
        setCreativeTab(BidsCreativeTabs.bidsBuildingBlocks);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityScrewPressBarrel) {
            TileEntityScrewPressBarrel teBarrel = (TileEntityScrewPressBarrel) te;
            if (teBarrel.getOutputFluidAmount() > 0 || teBarrel.getScrewPressDiscTileEntity() != null) {
                // Make it harder to break if it contains any liquid
                return super.getBlockHardness(world, x, y, z) * 5;
            }
        }

        return super.getBlockHardness(world, x, y, z);
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
    public int getRenderType() {
        return BidsBlocks.screwPressBarrelRenderId;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        blockIcon = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + "ScrewPressBarrelHoop");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        //When doing inventory item render, we increase the side by 10 so that we can draw the hoops instead of planks
        if (side >= 10)
        {
            side -= 10;
            if (side == 0 || side == 1)
                return TFC_Textures.invisibleTexture;
            else
                return blockIcon;
        }
        if (meta < 16)
        {
            return TFCBlocks.planks.getIcon(side, meta);
        }
        if(meta < 32)
        {
            return TFCBlocks.planks2.getIcon(side, meta - 16);
        }
        return TFCBlocks.planks3.getIcon(side, meta - 32);
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
        if (side == 0 || side == 1)
            return TFC_Textures.invisibleTexture;
        else
            return blockIcon;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntityScrewPressBarrel teBarrel = (TileEntityScrewPressBarrel) world.getTileEntity(x, y, z);
            ItemStack equippedItem = player.getCurrentEquippedItem();

            if (equippedItem != null) {
                if (FluidContainerRegistry.isEmptyContainer(equippedItem) || equippedItem.getItem() instanceof IFluidContainerItem) {
                    ItemStack tmp = equippedItem.copy();
                    tmp.stackSize = 1;
                    ItemStack is = teBarrel.removeLiquid(tmp);

                    // If we cannot remove the liquid from the barrel, open the interface.
                    if (ItemStack.areItemStacksEqual(equippedItem, is)) {
                        return false;
                    }

                    boolean finished = false;

                    while (!finished) {
                        tmp = is.copy();
                        is = teBarrel.removeLiquid(is);

                        finished = ItemStack.areItemStacksEqual(tmp, is);
                    }

                    if (is.getItem() == TFCItems.woodenBucketMilk) {
                        ItemCustomBucketMilk.createTag(is, 20f);
                    }

                    equippedItem.stackSize--;

                    if (equippedItem.stackSize == 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }

                    if (equippedItem.stackSize == 0 && (is.getMaxStackSize() == 1 || !player.inventory.hasItemStack(is))) { // put buckets in the slot you used them from.
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, is);
                    } else {
                        if (!player.inventory.addItemStackToInventory(is))
                            player.dropPlayerItemWithRandomChoice(is, false); // if the new item dosn't fit, drop it.
                    }

                    if (player.inventoryContainer != null) {
                        // for some reason the players inventory won't update without this.
                        player.inventoryContainer.detectAndSendChanges();
                    }

                    return true;
                } else if (equippedItem.getItem() instanceof ItemBarrels || equippedItem.getItem() instanceof ItemLargeVessel) {
                    ItemStack is = equippedItem.copy();
                    is.stackSize = 1;

                    if (!equippedItem.hasTagCompound()) {
                        if (teBarrel.getOutputFluid() != null) {
                            if (is.getItem() instanceof ItemBarrels) {
                                FluidStack fs = teBarrel.getOutputFluid();

                                NBTTagCompound nbt = new NBTTagCompound();
                                nbt.setTag("fluidNBT", fs.writeToNBT(new NBTTagCompound()));
                                nbt.setBoolean("Sealed", true);
                                is.setTagCompound(nbt);
                                equippedItem.stackSize--;
                                TFC_Core.giveItemToPlayer(is, player);

                                teBarrel.drainLiquid();
                            } else if (is.getItem() instanceof ItemLargeVessel) {
                                if (is.getItemDamage() == 0) {
                                    return false;
                                }

                                FluidStack fs = teBarrel.getOutputFluid();

                                NBTTagCompound nbt = new NBTTagCompound();
                                nbt.setTag("fluidNBT", fs.writeToNBT(new NBTTagCompound()));
                                nbt.setBoolean("Sealed", true);
                                is.setTagCompound(nbt);
                                equippedItem.stackSize--;
                                TFC_Core.giveItemToPlayer(is, player);

                                teBarrel.drainLiquid();
                            }

                            return true;
                        }
                    }
                }
            }

            if (teBarrel.getScrewPressDiscTileEntity() == null) {
                player.openGui(Bids.instance, BidsGui.screwPressBarrelGui, world, x, y, z);
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntityScrewPressBarrel teBarrel = (TileEntityScrewPressBarrel) world.getTileEntity(x, y, z);
        teBarrel.onBlockBroken();

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityScrewPressBarrel();
    }

}
