package com.unforbidable.tfc.bids.features.device.cookingpit.block;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemBlocks.ItemBarrels;
import com.dunk.tfc.Items.ItemBlocks.ItemLargeVessel;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.features.cooking.CookingMixtureFluid;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityCookingPit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public abstract class BlockCookingPit extends BlockContainer {

    protected IIcon sideIcon;
    protected IIcon topIcon;

    public BlockCookingPit() {
        super(Material.ground);

        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setHardness(0.25f);

        setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        MovingObjectPosition mop = CookingPitHelper.onCookingPitCollisionRayTrace(world, x, y, z, startVec, endVec);
        if (mop != null) {
            return mop;
        }

        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

    @SuppressWarnings({"unchecked"})
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registerer) {
        sideIcon = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Side");
        topIcon = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Top");
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
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        if (!canBlockStay(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return CookingPitHelper.isBlockValidPitSide(world, x, y - 1, z, ForgeDirection.DOWN) &&
            CookingPitHelper.isBlockValidPitSide(world, x + 1, y, z, ForgeDirection.EAST) &&
            CookingPitHelper.isBlockValidPitSide(world, x - 1, y, z, ForgeDirection.WEST) &&
            CookingPitHelper.isBlockValidPitSide(world, x, y, z + 1, ForgeDirection.SOUTH) &&
            CookingPitHelper.isBlockValidPitSide(world, x, y, z - 1, ForgeDirection.NORTH);
    }

    @Override
    protected void dropBlockAsItem(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack) {
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityCookingPit) {
            TileEntityCookingPit cookingPit = (TileEntityCookingPit) te;
            cookingPit.onBreakBlock();
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }


    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntityCookingPit te = (TileEntityCookingPit) world.getTileEntity(x, y, z);
        ItemStack equippedItem = player.getCurrentEquippedItem();
        boolean rimHit = (hitX < 8 / 32f || hitX > 24 / 32f) && (hitZ < 8 / 32f || hitZ > 24 / 32f);

        if (!world.isRemote) {
            if (equippedItem == null) {
                if (player.isSneaking()) {
                    Bids.LOG.debug("Retrieving items from cooking pot");
                    if (te.hasInputItem() && !rimHit) {
                        te.retrieveItemStack(player);
                    } else {
                        te.retrieveStones(player);
                    }
                }
            } else {
                // First try placing the item
                // then all the container mechanics
                if (te.placeItemStack(equippedItem, player)) {
                    return true;
                } else if ((FluidContainerRegistry.isFilledContainer(equippedItem)
                    || equippedItem.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem) equippedItem.getItem()).getFluid(equippedItem) != null)) {
                    ItemStack tmp = equippedItem.copy();
                    tmp.stackSize = 1;

                    ItemStack is = te.addLiquid(tmp);
                    if (ItemStack.areItemStacksEqual(equippedItem, is)) {
                        return true;
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

                    if (player.inventoryContainer != null) {
                        // for some reason the players inventory won't update without this.
                        player.inventoryContainer.detectAndSendChanges();
                    }

                    return true;
                } else if (FluidContainerRegistry.isEmptyContainer(equippedItem) || equippedItem.getItem() instanceof IFluidContainerItem) {
                    ItemStack tmp = equippedItem.copy();
                    tmp.stackSize = 1;
                    ItemStack is = te.removeLiquid(tmp);

                    // If we cannot remove the liquid from the barrel, open the interface.
                    if (ItemStack.areItemStacksEqual(equippedItem, is)) {
                        return true;
                    }

                    boolean finished = false;

                    while (!finished) {
                        tmp = is.copy();
                        is = te.removeLiquid(is);

                        finished = ItemStack.areItemStacksEqual(tmp, is);
                    }

                    BidsEventFactory.onFillContainer(player, equippedItem, is);

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
                    Bids.LOG.debug("Cooking pot activated with barrel/large vessel: " + equippedItem.getDisplayName());

                    ItemStack is = equippedItem.copy();
                    is.stackSize = 1;

                    if (equippedItem.hasTagCompound()) {
                        if (equippedItem.getTagCompound().hasKey("fluidNBT") && !equippedItem.getTagCompound().hasKey("Items") &&
                            te.getTotalLiquidVolume() < te.getMaxFluidVolume()) {
                            FluidStack fs = FluidStack.loadFluidStackFromNBT(equippedItem.getTagCompound().getCompoundTag("fluidNBT"));
                            if (te.addLiquid(fs)) {
                                if (fs.amount == 0) {
                                    equippedItem.getTagCompound().removeTag("Sealed");
                                    equippedItem.getTagCompound().removeTag("fluidNBT");
                                    if (equippedItem.getTagCompound().hasNoTags())
                                        equippedItem.setTagCompound(null);
                                } else {
                                    fs.writeToNBT(equippedItem.getTagCompound().getCompoundTag("fluidNBT"));
                                }

                                return true;
                            }
                        }
                    } else {
                        if (te.getFluidStack() != null && !(te.getFluidStack().getFluid() instanceof CookingMixtureFluid)) {
                            if (is.getItem() instanceof ItemBarrels) {
                                FluidStack fs = te.getFluidStack();

                                NBTTagCompound nbt = new NBTTagCompound();
                                nbt.setTag("fluidNBT", fs.writeToNBT(new NBTTagCompound()));
                                nbt.setBoolean("Sealed", true);
                                is.setTagCompound(nbt);
                                equippedItem.stackSize--;
                                TFC_Core.giveItemToPlayer(is, player);

                                te.drainLiquid();
                            } else if (is.getItem() instanceof ItemLargeVessel) {
                                if (is.getItemDamage() == 0) {
                                    return false;
                                }

                                FluidStack fs = te.getFluidStack().copy();

                                NBTTagCompound nbt = new NBTTagCompound();
                                nbt.setTag("fluidNBT", fs.writeToNBT(new NBTTagCompound()));
                                nbt.setBoolean("Sealed", true);
                                is.setTagCompound(nbt);
                                equippedItem.stackSize--;
                                TFC_Core.giveItemToPlayer(is, player);

                                te.drainLiquid();
                            }

                            return true;
                        }
                    }
                }
            }
        }

        return true;
    }

}
