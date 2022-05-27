package com.unforbidable.tfc.bids.Containers;

import com.unforbidable.tfc.bids.TFC.ContainerTFC;
import com.unforbidable.tfc.bids.TFC.PlayerInventory;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ContainerCrucible extends ContainerTFC {

    protected final TileEntityCrucible crucibleTileEntity;

    public ContainerCrucible(InventoryPlayer inventory, TileEntityCrucible te, World world, int x, int y, int z) {
        crucibleTileEntity = te;
        buildLayout();
        PlayerInventory.buildInventoryLayout(this, inventory, 8, 90, false, true);
    }

    protected abstract void buildLayout();

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlotTFC(EntityPlayer player, int slotNum) {
        ItemStack origStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            origStack = slotStack.copy();

            // From crucible to inventory
            if (slotNum < crucibleTileEntity.getInputSlotCount() + 2) {
                if (!this.mergeItemStack(slotStack, crucibleTileEntity.getInputSlotCount() + 2, inventorySlots.size(),
                        true))
                    return null;
            }
            // Ceramic molds into output slot
            else if (crucibleTileEntity.isItemStackValidLiquidOutput(slotStack)) {
                if (!this.mergeItemStack(slotStack, crucibleTileEntity.getInputSlotCount() + 1,
                        crucibleTileEntity.getInputSlotCount() + 2, true))
                    return null;
            }
            // To input slots
            else if (crucibleTileEntity.isItemStackValidInput(slotStack)) {
                if (!this.mergeItemStack(slotStack, 0, crucibleTileEntity.getInputSlotCount(), true))
                    return null;
            }

            if (slotStack.stackSize <= 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (slotStack.stackSize == origStack.stackSize)
                return null;

            slot.onPickupFromSlot(player, slotStack);
        }

        return origStack;
    }

}
