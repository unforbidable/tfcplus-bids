package com.unforbidable.tfc.bids.features.device.woodpile.container.slot;

import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.features.device.woodpile.WoodpileRegistry;
import com.unforbidable.tfc.bids.features.device.woodpile.main.EnumSlotGroup;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotWoodpile extends Slot {

    final EnumSlotGroup slotGroup;

    public SlotWoodpile(IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);

        slotGroup = EnumSlotGroup.fromSlot(i);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        final WoodpileRenderable render = WoodpileRegistry.renderable.get(itemStack.getItem());
        if (render == null) {
            return false;
        }

        if (render.renderAsLargeWoodpileItem(itemStack)) {
            // This slot must be a hybrid slot
            // and corresponding shared slots need to be free
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
            WoodpileRenderable renderInHybridSlot = WoodpileRegistry.renderable.get(itemStackInHybridSlot.getItem());
            if (!renderInHybridSlot.renderAsLargeWoodpileItem(itemStackInHybridSlot)) {
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
        return isWoodpileSlotEnabled(slotNumber, inventory);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_111238_b() {
        return isWoodpileSlotEnabled(slotNumber, inventory);
    }

    public static boolean isWoodpileSlotEnabled(int slot, IInventory inventory) {
        if (EnumSlotGroup.isSharedSlot(slot)) {
            EnumSlotGroup slotGroup = EnumSlotGroup.fromSlot(slot);
            final ItemStack itemStackInHybridSlot = inventory
                    .getStackInSlot(slotGroup.getHybridSlot());
            if (itemStackInHybridSlot != null) {
                WoodpileRenderable renderInHybridSlot = WoodpileRegistry.renderable.get(itemStackInHybridSlot.getItem());
                if (renderInHybridSlot.renderAsLargeWoodpileItem(itemStackInHybridSlot)) {
                    return false;
                }
            }
        }

        return true;
    }

}
