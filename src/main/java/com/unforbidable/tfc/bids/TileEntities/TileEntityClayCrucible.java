package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.api.BidsGui;

import net.minecraft.item.ItemStack;

public class TileEntityClayCrucible extends TileEntityCrucible {

    @Override
    public int getGui() {
        return BidsGui.clayCrucibleGui;
    }

    @Override
    public int getInputSlotCount() {
        return 4;
    }

    @Override
    public String getInventoryName() {
        return "Clay Crucible";
    }

    @Override
    public int getMaxVolume() {
        return 1500;
    }

    @Override
    public int getMaxTemp() {
        return 1100;
    }

    @Override
    public boolean hasLiquidInputSlot() {
        return true;
    }

    @Override
    public boolean isItemStackValidLiquidInput(ItemStack is) {
        return true;
    }

    @Override
    public boolean isItemStackValidInput(ItemStack is) {
        return super.isItemStackValidInput(is)
                && is.getItem() instanceof ISize
                && ((ISize) is.getItem()).getSize(is).stackSize >= EnumSize.SMALL.stackSize;
    }

}
