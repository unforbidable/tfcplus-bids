package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.Tools.ItemCustomBucketMilk;
import com.dunk.tfc.TileEntities.TEFirepit;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.EnumCookingPotPlacement;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPot;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

public class BlockCookingPot extends BlockContainer {

    public static final ForgeDirection[] FORGE_DIRECTIONS_WENS = {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH};

    IIcon[] topIcons = new IIcon[2];
    IIcon[] sideIcons = new IIcon[2];

    public BlockCookingPot() {
        super(Material.rock);

        setCreativeTab(BidsCreativeTabs.bidsDefault);
        setHardness(0.25f);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        TileEntityCookingPot tileEntityCookingPot = (TileEntityCookingPot) world.getTileEntity(x, y, z);
        AxisAlignedBB bounds = tileEntityCookingPot.getCachedBounds().getEntireBounds();
        setBlockBounds((float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        return CookingHelper.onCookingPotCollisionRayTrace(world, x, y, z, startVec, endVec);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(this, 1, 0));
        par3List.add(new ItemStack(this, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registerer) {
        sideIcons[0] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Side");
        sideIcons[1] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Side");
        topIcons[0] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Clay.Top");
        topIcons[1] = registerer.registerIcon(Tags.MOD_ID + ":" + getTextureName() + ".Ceramic.Top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 0 || side == 1) {
            return topIcons[meta];
        } else {
            return sideIcons[meta];
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
        return BidsBlocks.cookingPotRenderId;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is) {
        super.onBlockPlacedBy(world, x, y, z, player, is);

        if (world.getTileEntity(x, y, z) instanceof TileEntityCookingPot) {
            TileEntityCookingPot te = (TileEntityCookingPot) world.getTileEntity(x, y, z);

            // Load NBT data from the itemstack
            if (is.hasTagCompound()) {
                NBTTagCompound tag = is.getTagCompound();
                te.readDataFromNBT(tag);
            }

            // Override the placement
            te.setPlacement(EnumCookingPotPlacement.GROUND);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        if (!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            TFC_Core.setBlockToAirWithDrops(world, x, y, z);
        }
    }

    @Override
    protected void dropBlockAsItem(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack) {
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityCookingPot) {
            TileEntityCookingPot cookingPot = (TileEntityCookingPot) te;
            cookingPot.onBreakBlock();

            int dmg = CookingHelper.getCookingPotInventoryRenderMetadata(cookingPot, 1);

            // Drop as item with content preserved
            ItemStack is = new ItemStack(Item.getItemFromBlock(block), 1, dmg);

            if (cookingPot.hasFluid() || cookingPot.hasInputItem() || cookingPot.hasLid() || cookingPot.hasAccessory()) {
                NBTTagCompound nbt = new NBTTagCompound();
                cookingPot.writeDataToNBT(nbt);
                is.setTagCompound(nbt);
            }

            EntityItem crucibleEntityItem = new EntityItem(world, x, y, z, is);
            world.spawnEntityInWorld(crucibleEntityItem);
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityCookingPot();
    }

    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntityCookingPot te = (TileEntityCookingPot) world.getTileEntity(x, y, z);
        ItemStack equippedItem = player.getCurrentEquippedItem();
        boolean cookingPotHit = hitY > 4 / 32f;

        if (!world.isRemote) {
            if (cookingPotHit) {
                if (equippedItem == null) {
                    if (player.isSneaking()) {
                        Bids.LOG.info("Retrieving items from cooking pot");
                        te.retrieveItemStack(player);
                    }
                } else if ((FluidContainerRegistry.isFilledContainer(equippedItem)
                    || equippedItem.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem) equippedItem.getItem()).getFluid(equippedItem) != null)) {
                    ItemStack tmp = equippedItem.copy();
                    tmp.stackSize = 1;

                    ItemStack is = te.addLiquid(tmp);
                    if (ItemStack.areItemStacksEqual(equippedItem, is)) {
                        return false;
                    }

                    boolean finished = false;
                    while (!finished) {
                        tmp = is.copy();
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
                        return false;
                    }

                    boolean finished = false;

                    while (!finished) {
                        tmp = is.copy();
                        is = te.removeLiquid(is);

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
                } else {
                    Bids.LOG.info("Placing item on cooking pot: " + equippedItem.getDisplayName());
                    te.placeItemStack(equippedItem);
                }
            } else {
                if (equippedItem == null) {
                    ForgeDirection directionFirepit = getFirepitDirection(world, x, y, z);
                    if (directionFirepit != null) {
                        Bids.LOG.info("Firepit found: " + directionFirepit);
                        if (te.getPlacement() == EnumCookingPotPlacement.GROUND) {
                            Bids.LOG.info("Moving cooking pot to firepit: " + directionFirepit);
                            te.setPlacement(EnumCookingPotPlacement.getFirepitEdgePlacementForDirection(directionFirepit));
                        } else {
                            Bids.LOG.info("Moving cooking pot off the firepit");
                            te.setPlacement(EnumCookingPotPlacement.GROUND);
                        }
                    }
                }
            }
        }

        return true;
    }

    private ForgeDirection getFirepitDirection(World world, int x, int y, int z) {
        for (ForgeDirection d : FORGE_DIRECTIONS_WENS) {
            TileEntity te = world.getTileEntity(x + d.offsetX, y, z + d.offsetZ);
            if (te instanceof TEFirepit) {
                return d;
            }
        }

        return null;
    }

}
