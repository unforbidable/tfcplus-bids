package com.unforbidable.tfc.bids.features.device.woodpile.container;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.unforbidable.tfc.bids.features.device.woodpile.container.slot.SlotWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerWoodpile extends ContainerTFC {

    final TileEntityWoodpile woodpileTileEntity;
    final World world;

    public ContainerWoodpile(InventoryPlayer inventory, TileEntityWoodpile te, World world, int x, int y, int z) {
        this.world = world;
        woodpileTileEntity = te;
        buildLayout();
        PlayerInventory.buildInventoryLayout(this, inventory, 8, 90, false, true);

        if (!world.isRemote) {
            woodpileTileEntity.openInventory();
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
                addSlotToContainer(new SlotWoodpile(woodpileTileEntity, i++,
                        slotOffsetX + ix * slotStrideX, slotOffsetY + iy * slotStrideY));
            }
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!world.isRemote) {
            woodpileTileEntity.closeInventory();
        }
    }

    @Override
    public ItemStack transferStackInSlotTFC(EntityPlayer entityplayer, int slotNum) {
        ItemStack origStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNum);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            origStack = slotStack.copy();

            int invSize = TileEntityWoodpile.MAX_STORAGE;

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
