package com.unforbidable.tfc.bids.Containers;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.unforbidable.tfc.bids.Containers.Slots.SlotWoodPile;
import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerWoodPile extends ContainerTFC {

    final TileEntityWoodPile woodPileTileEntity;
    final World world;

    public ContainerWoodPile(InventoryPlayer inventory, TileEntityWoodPile te, World world, int x, int y, int z) {
        this.world = world;
        woodPileTileEntity = te;
        buildLayout();
        PlayerInventory.buildInventoryLayout(this, inventory, 8, 90, false, true);

        if (!world.isRemote) {
            woodPileTileEntity.openInventory();
        }
    }

    protected void buildLayout() {
        int slotOffsetX = 17;
        int slotOffsetY = 26;
        int slotStrideX = 18;
        int slotStrideY = 18;
        int slotRows = 2;
        int slotColumns = 8;

        int i = 0;
        for (int iy = 0; iy < slotRows; iy++) {
            for (int ix = 0; ix < slotColumns; ix++) {
                addSlotToContainer(new SlotWoodPile(woodPileTileEntity, i++,
                        slotOffsetX + ix * slotStrideX, slotOffsetY + iy * slotStrideY));
            }
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!world.isRemote) {
            woodPileTileEntity.closeInventory();
        }
    }

    @Override
    public ItemStack transferStackInSlotTFC(EntityPlayer entityplayer, int slotNum) {
        ItemStack origStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            origStack = slotStack.copy();

            int invSize = TileEntityWoodPile.MAX_STORAGE;

            // From pile to inventory
            if (slotNum < invSize) {
                if (!this.mergeItemStack(slotStack, invSize, inventorySlots.size(), true))
                    return null;
            } else {
                if (!this.mergeItemStack(slotStack, 0, invSize, false))
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
