package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Containers.Slots.SlotSize;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.dunk.tfc.api.Enums.EnumSize;
import com.unforbidable.tfc.bids.Containers.Slots.SlotPrepIngredient;
import com.unforbidable.tfc.bids.Containers.Slots.SlotPrepOutput;
import com.unforbidable.tfc.bids.Containers.Slots.SlotPrepStorage;
import com.unforbidable.tfc.bids.Containers.Slots.SlotPrepVessel;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPrep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerCookingPrep extends ContainerTFC {

    private final TileEntityCookingPrep tileEntityCookingPrep;


    public ContainerCookingPrep(InventoryPlayer inventoryplayer, TileEntityCookingPrep te, World world, int x, int y, int z) {
        this.tileEntityCookingPrep = te;

        addSlotToContainer(new SlotPrepVessel(te, 0, 40, 24));

        addSlotToContainer(new SlotPrepIngredient(te, 1, 62, 24));
        addSlotToContainer(new SlotPrepIngredient(te, 2, 80, 24));
        addSlotToContainer(new SlotPrepIngredient(te, 3, 98, 24));
        addSlotToContainer(new SlotPrepIngredient(te, 4, 116, 24));

        addSlotToContainer(new SlotPrepOutput(te, 5, 40, 46));

        addSlotToContainer(new SlotPrepStorage(te, 6, 145, 8));
        addSlotToContainer(new SlotPrepStorage(te, 7, 145, 26));
        addSlotToContainer(new SlotPrepStorage(te, 8, 145, 44));
        addSlotToContainer(new SlotPrepStorage(te, 9, 145, 62));

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

            // From output
            if (slotNum == 5) {
                return null;
            }
            // From food prep to inventory
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
