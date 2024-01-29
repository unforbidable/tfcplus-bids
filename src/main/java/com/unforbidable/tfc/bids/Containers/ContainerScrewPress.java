package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.unforbidable.tfc.bids.Containers.Slots.*;
import com.unforbidable.tfc.bids.TileEntities.TileEntityScrewPressBarrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerScrewPress extends ContainerTFC {

    private final TileEntityScrewPressBarrel tileEntityScrewPressBarrel;

    public ContainerScrewPress(InventoryPlayer inventoryplayer, TileEntityScrewPressBarrel te, World world, int x, int y, int z) {
        this.tileEntityScrewPressBarrel = te;

        for (int i = 0; i < 5; ++i) {
            this.addSlotToContainer(new SlotScrewPressInput(te, i, 44 + i * 18, 17));
        }
        for (int i = 5; i < 10; ++i) {
            this.addSlotToContainer(new SlotScrewPressOutput(te, i, 44 + i * 18, 45));
        }

        PlayerInventory.buildInventoryLayout(this, inventoryplayer, 8, 90, false, true);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlotTFC(EntityPlayer player, int slotNum) {
        ItemStack origStack = null;
        Slot slot = (Slot) inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            origStack = slotStack.copy();

            // From container to inventory
            if (slotNum <= 9) {
                if (!this.mergeItemStack(slotStack, 9, inventorySlots.size(), true))
                    return null;
            }
            // Slot exception lists already handle what should go where
            else
            {
                if (!this.mergeItemStack(slotStack, 0, 9, false))
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

    @SuppressWarnings({ "rawtype", "unchecked" })
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int var1 = 0; var1 < this.inventorySlots.size(); ++var1) {
            ItemStack var2 = ((Slot) this.inventorySlots.get(var1)).getStack();
            ItemStack var3 = (ItemStack) this.inventoryItemStacks.get(var1);

            if (!ItemStack.areItemStacksEqual(var3, var2)) {
                var3 = var2 == null ? null : var2.copy();
                this.inventoryItemStacks.set(var1, var3);

                for (int var4 = 0; var4 < this.crafters.size(); ++var4)
                    ((ICrafting) this.crafters.get(var4)).sendSlotContents(this, var1, var3);
            }
        }
    }

    @Override
    public void updateProgressBar(int par1, int par2) {
    }

}
