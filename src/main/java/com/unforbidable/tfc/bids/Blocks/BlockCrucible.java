package com.unforbidable.tfc.bids.Blocks;

import java.util.Random;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.ILiquidMetalContainer;

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
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockCrucible extends BlockContainer {

    IIcon[] icons = new IIcon[2];
    String textureName = "";

    protected BlockCrucible(Material material) {
        super(material);
    }

    protected void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7,
            float par8, float par9) {
        if (!world.isRemote && world.getTileEntity(i, j, k) instanceof TileEntityCrucible) {
            TileEntityCrucible crucible = (TileEntityCrucible) world.getTileEntity(i, j, k);
            if (!handleInteraction(entityplayer, crucible))
                entityplayer.openGui(Bids.instance, crucible.getGui(), world, i, j, k);
        }
        return true;
    }

    protected boolean handleInteraction(EntityPlayer player, TileEntityCrucible crucible) {
        ItemStack itemstack = player.inventory.getCurrentItem();
        if (itemstack != null
                && isValidBlowpipe(itemstack)
                && crucible instanceof ILiquidMetalContainer) {
            ILiquidMetalContainer container = (ILiquidMetalContainer) crucible;

            // See if we can quickly load or unload molten glass
            // from or into a compatible tile entity
            int blowpipeGlassAmount = itemstack.getItemDamage() > 1
                    ? itemstack.getMaxDamage() - itemstack.getItemDamage() + 1
                    : 0;
            int itemDamage = itemstack.getItemDamage();
            // Assume cooler than molten but still workable temp
            float moltenGlassTemp = Global.GLASS.getMeltingPoint() * 0.80f;
            if (blowpipeGlassAmount < 100 && container.canEjectLiquidMetal(Global.GLASS, 1)) {
                int amountNeeded = 100 - blowpipeGlassAmount;
                int actuallyEjectedAmount = container.ejectLiquidMetal(Global.GLASS, amountNeeded);
                if (amountNeeded == actuallyEjectedAmount) {
                    itemDamage = 2;
                } else {
                    itemDamage = itemstack.getMaxDamage() - blowpipeGlassAmount - actuallyEjectedAmount + 1;
                }
            } else if (blowpipeGlassAmount > 0 && container.canAcceptLiquidMetal(Global.GLASS, 1, moltenGlassTemp)) {
                int actuallyAcceptedAmount = container.acceptLiquidMetal(Global.GLASS, blowpipeGlassAmount,
                        moltenGlassTemp);
                if (blowpipeGlassAmount == actuallyAcceptedAmount) {
                    itemDamage = 1;
                } else {
                    itemDamage = itemstack.getMaxDamage() - blowpipeGlassAmount + actuallyAcceptedAmount + 1;
                }
            }

            if (itemDamage != itemstack.getItemDamage()) {
                itemstack.setItemDamage(itemDamage);
                return true;
            }
        }

        return false;
    }

    protected boolean isValidBlowpipe(ItemStack itemstack) {
        return itemstack.getItemDamage() > 0
                && (itemstack.getItem() == BidsItems.metalBlowpipe
                        || itemstack.getItem() == TFCItems.clayBlowpipe);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        icons[0] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + textureName + " Top");
        icons[1] = iconRegisterer.registerIcon(Tags.MOD_ID + ":" + textureName + " Side");
    }

    @Override
    public IIcon getIcon(int i, int j) {
        if (i < 2) {
            return icons[0];
        }

        return icons[1];
    }

    @Override
    public abstract int getRenderType();

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5) {
        return true;
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
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCrucible) {
            TileEntityCrucible crucibleTileEntity = (TileEntityCrucible) te;

            // Drop all inventory
            for (int i = 0; i < crucibleTileEntity.getSizeInventory(); i++) {
                if (crucibleTileEntity.getStackInSlot(i) != null) {
                    ItemStack isItemStack = crucibleTileEntity.getStackInSlot(i);
                    EntityItem slotEntityItem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5,
                            isItemStack);
                    slotEntityItem.motionX = 0;
                    slotEntityItem.motionY = 0;
                    slotEntityItem.motionZ = 0;
                    world.spawnEntityInWorld(slotEntityItem);

                    // Also empty the slot so that it isn't saved
                    crucibleTileEntity.decrStackSize(i, isItemStack.stackSize);
                }
            }

            // Drop as item with liquid & liquid temp preserved
            ItemStack is = new ItemStack(Item.getItemFromBlock(block), 1);
            NBTTagCompound nbt = new NBTTagCompound();
            crucibleTileEntity.writeCrucibleDataToNBT(nbt);
            is.setTagCompound(nbt);
            EntityItem crucibleEntityItem = new EntityItem(world, x, y, z, is);
            world.spawnEntityInWorld(crucibleEntityItem);
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase player, ItemStack is) {
        super.onBlockPlacedBy(world, i, j, k, player, is);

        TileEntity te = world.getTileEntity(i, j, k);
        if (te != null && is.hasTagCompound() && te instanceof TileEntityCrucible) {
            TileEntityCrucible tileEntityCrucible = (TileEntityCrucible) te;
            tileEntityCrucible.readCrucibleDataFromNBT(is.getTagCompound());
        }
    }

    @Override
    protected void dropBlockAsItem(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityCrucible) {
            TileEntityCrucible tileEntityCrucible = (TileEntityCrucible) te;
            if (tileEntityCrucible.isGlassMakingActive()) {
                TileEntity teChimeny = tileEntityCrucible.getGlassMakingChimney();
                if (teChimeny != null) {
                    double centerX = teChimeny.xCoord + 0.5F;
                    double centerY = teChimeny.yCoord + 2F;
                    double centerZ = teChimeny.zCoord + 0.5F;
                    world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY,
                            centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.1D, 0.0D);
                    world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY,
                            centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.15D, 0.0D);
                    world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY - 1,
                            centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.1D, 0.0D);
                    world.spawnParticle("smoke", centerX + (rand.nextDouble() - 0.5), centerY - 1,
                            centerZ + (rand.nextDouble() - 0.5), 0.0D, 0.15D, 0.0D);
                }
            }
        }
    }

}
