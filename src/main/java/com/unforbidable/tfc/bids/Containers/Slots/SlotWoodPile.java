package com.unforbidable.tfc.bids.Containers.Slots;

import com.unforbidable.tfc.bids.Core.WoodPile.EnumSlotGroup;
import com.unforbidable.tfc.bids.api.WoodPileRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWoodPile extends Slot {

    final EnumSlotGroup slotGroup;

    public SlotWoodPile(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);

        slotGroup = EnumSlotGroup.fromSlot(i);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        final IWoodPileRenderProvider render = WoodPileRegistry.findItem(itemStack.getItem());
        if (render == null) {
            return false;
        }

        if (render.isWoodPileLargeItem(itemStack)) {
            // This slot must be a hybrid slot
            // and crresponding shared slots need to be free
            if (!EnumSlotGroup.isHybridSlot(slotNumber)) {
                // System.out.println("Cannot insert large item into a shared slot " +
                // slotNumber);
                return false;
            }

            for (int sharedSlot : slotGroup.getSharedSlots()) {
                if (inventory.getStackInSlot(sharedSlot) != null) {
                    // System.out.println("Cannot insert large item into a hybrid slot " +
                    // slotNumber
                    // + " because shared slot " + sharedSlot + " is occupied");
                    return false;
                }
            }

            // System.out.println("Can insert large item into a hybrid slot " + slotNumber
            // + " because none of its shared slots are occupied");
            return true;
        } else {
            // Corresponding hybrid slot must be free
            // or occupied with a normal size item
            if (EnumSlotGroup.isHybridSlot(slotNumber)) {
                // System.out.println("Can insert normal item into a hybrid slot " +
                // slotNumber);
                return true;
            }

            if (inventory.getStackInSlot(slotGroup.getHybridSlot()) == null) {
                // System.out.println("Can insert normal item into slot " + slotNumber
                // + " because hybrid slot " + slotGroup.getHybridSlot() + " is not occupied");
                return true;
            }

            final ItemStack itemStackInHybridSlot = inventory.getStackInSlot(slotGroup.getHybridSlot());
            IWoodPileRenderProvider renderInHybridSlot = WoodPileRegistry.findItem(itemStackInHybridSlot.getItem());
            if (!renderInHybridSlot.isWoodPileLargeItem(itemStackInHybridSlot)) {
                // System.out.println("Can insert normal item into slot " + slotNumber
                // + " because hybrid slot " + slotGroup.getHybridSlot() + " is occupied with a
                // normal item");
                return true;
            }

            // System.out.println("Cannot insert normal item into slot " + slotNumber
            // + " because hybrid slot " + slotGroup.getHybridSlot() + " is occupied with a
            // large item");
            return false;
        }
    }

    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return isWoodPileSlotEnabled(slotNumber, inventory);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_111238_b() {
        return isWoodPileSlotEnabled(slotNumber, inventory);
    }

    public static boolean isWoodPileSlotEnabled(int slot, IInventory inventory) {
        if (EnumSlotGroup.isSharedSlot(slot)) {
            EnumSlotGroup slotGroup = EnumSlotGroup.fromSlot(slot);
            final ItemStack itemStackInHybridSlot = inventory
                    .getStackInSlot(slotGroup.getHybridSlot());
            if (itemStackInHybridSlot != null) {
                IWoodPileRenderProvider renderInHybridSlot = WoodPileRegistry
                        .findItem(itemStackInHybridSlot.getItem());
                if (renderInHybridSlot.isWoodPileLargeItem(itemStackInHybridSlot)) {
                    return false;
                }
            }
        }

        return true;
    }

}
