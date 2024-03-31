package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Containers.Slots.SlotOutputOnly;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.unforbidable.tfc.bids.TileEntities.TileEntityStrawNest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerStrawNest extends ContainerTFC {

    public ContainerStrawNest(InventoryPlayer inventoryplayer, TileEntityStrawNest te, World world, int x, int y, int z) {
        this.addSlotToContainer(new SlotOutputOnly(te, 0, 80, 34));

        PlayerInventory.buildInventoryLayout(this, inventoryplayer, 8, 90, false, true);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlotTFC(EntityPlayer player, int slotNum)
    {
        ItemStack origStack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            origStack = slotStack.copy();

            if (slotNum < 4)
            {
                if (!this.mergeItemStack(slotStack, 4, inventorySlots.size(), true))
                    return null;
            }
            // Slots are output only, so there's no need to do input logic.
			/*else
			{
				if (!this.mergeItemStack(slotStack, 0, 4, false))
					return null;
			}*/

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
