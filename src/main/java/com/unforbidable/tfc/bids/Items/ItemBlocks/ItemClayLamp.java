package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Sounds;
import com.dunk.tfc.Handlers.Network.ItemPotterySmashPacket;
import com.dunk.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.Interfaces.ISmashable;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayLamp;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

public class ItemClayLamp extends ItemTerraBlock implements ISize, IFluidContainerItem, ISmashable {

    public ItemClayLamp(Block material) {
        super(material);

        setMaxStackSize(1);
    }

    @Override
    public boolean isDamaged(ItemStack is) {
        return is.hasTagCompound();
    }

    @Override
    public int getMaxDamage(ItemStack is) {
        return getCapacity(is);
    }

    @Override
    public int getDisplayDamage(ItemStack is) {
        FluidStack fuel = FluidStack.loadFluidStackFromNBT(is.getTagCompound());
        int amt = fuel != null ? fuel.amount : 0;
        return getMaxDamage(is) - amt;
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        if (hasFuel(is)) {
            FluidStack fuelStack = FluidStack.loadFluidStackFromNBT(is.getTagCompound());
            return super.getItemStackDisplayName(is) + " (" + fuelStack.getLocalizedName() + ")";
        } else {
            return super.getItemStackDisplayName(is);
        }
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return hasFuel(is) ? 1 : 4;
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.LIGHT;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

    protected boolean hasFuel(ItemStack is) {
        return is.hasTagCompound();
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return FluidStack.loadFluidStackFromNBT(container.getTagCompound());
    }

    @Override
    public int getCapacity(ItemStack container) {
        return TileEntityClayLamp.FUEL_MAX_VOLUME;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        FluidStack fs = getFluid(container);
        int inAmt = 0;
        if(fs != null) {
            int max = getCapacity(container) - fs.amount;
            if(max > 0 && fs.isFluidEqual(resource)) {
                inAmt = Math.min(max, resource.amount);
                if(doFill) {
                    fs.amount += inAmt;
                    if(container.getTagCompound() == null) {
                        container.setTagCompound(new NBTTagCompound());
                    }
                    fs.writeToNBT(container.getTagCompound());
                }
            }
        } else {
            inAmt = Math.min(getCapacity(container), resource.amount);
            if(doFill) {
                fs = resource.copy();
                fs.amount = inAmt;
                if(container.getTagCompound() == null) {
                    container.setTagCompound(new NBTTagCompound());
                }
                fs.writeToNBT(container.getTagCompound());
            }
        }
        return inAmt;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        FluidStack fs = getFluid(container);
        if (fs == null) {
            // If someone tries to drain an empty lamp
            // return null and hope they deal with it
            return null;
        }

        FluidStack fsOut = fs.copy();
        fsOut.amount = Math.min(maxDrain, fs.amount);

        if (doDrain) {
            if(fs.amount - fsOut.amount <= 0) {
                container.stackTagCompound = null;
            } else {
                fs.amount -= fsOut.amount;
                if(container.getTagCompound() == null) {
                    container.setTagCompound(new NBTTagCompound());
                }
                fs.writeToNBT(container.getTagCompound());
            }
        }

        return fsOut;
    }

    @Override
    public void onSmashed(ItemStack stack, World world, double x, double y, double z) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ISmashable)
        {
            world.playSoundEffect(x, y, z, TFC_Sounds.CERAMICBREAK, 1.0f, 0.8f + world.rand.nextFloat() * 0.4f);
            ItemPotterySmashPacket smashPkt = new ItemPotterySmashPacket(Item.getIdFromItem(stack.getItem()), x, y, z);
            NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32);
            TerraFirmaCraft.PACKET_PIPELINE.sendToAllAround(smashPkt, tp);

            if (stack.hasTagCompound()) {
                FluidStack fs = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
                if (fs != null)
                {
                    TFC_Core.smashFluidInWorld(world, x, y, z, fs);
                }
            }
        }
    }

    @Override
    public void smashAnimate(World world, double x, double y, double z) {
        for (double i = 0; i < Math.PI * 2; i += Math.PI / 4) {
            world.spawnParticle("blockdust_" + Block.getIdFromBlock(TFCBlocks.pottery) + "_15", x, y, z, Math.cos(i) * 0.2D, 0.15D + world.rand.nextDouble() * 0.1D,
                Math.sin(i) * 0.2D);
        }
    }

    @Override
    public boolean canSmash(ItemStack itemStack) {
        return true;
    }

}
