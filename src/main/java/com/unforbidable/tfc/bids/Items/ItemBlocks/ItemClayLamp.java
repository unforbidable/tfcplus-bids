package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayLamp;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.List;

public class ItemClayLamp extends ItemBlock implements ISize, IFluidContainerItem {

    public ItemClayLamp(Block material) {
        super(material);

        setMaxStackSize(1);
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
    public int getItemStackLimit(ItemStack is)
    {
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

        if (hasFuel(is)) {
            FluidStack fuelStack = FluidStack.loadFluidStackFromNBT(is.getTagCompound());
            list.add(StatCollector.translateToLocal("gui.Fuel") + ": " + fuelStack.amount + "/" + TileEntityClayLamp.FUEL_MAX_VOLUME);
        }
    }

    protected boolean hasFuel(ItemStack is)
    {
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
}
