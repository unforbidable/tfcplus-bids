package com.unforbidable.tfc.bids.Core.WoodPile;

public enum EnumSlotGroup {
    TOPLEFT(3, 0, 1, 2),
    TOPRIGHT(4, 5, 6, 7),
    BOTTOMLEFT(11, 8, 9, 10),
    BOTTOMRIGHT(12, 13, 14, 15);

    final int hybridSlot;
    final int[] sharedSlots;

    public static final EnumSlotGroup[] ALL_VALUES = new EnumSlotGroup[] { TOPLEFT, TOPRIGHT,
            BOTTOMLEFT, BOTTOMRIGHT };

    EnumSlotGroup(int hybridSlot, int... sharedSlots) {
        this.hybridSlot = hybridSlot;
        this.sharedSlots = sharedSlots;
    }

    public int getHybridSlot() {
        return hybridSlot;
    }

    public int[] getSharedSlots() {
        return sharedSlots;
    }

    public boolean hasSlot(int slot) {
        return slot / 4 == ordinal();
    }

    public static EnumSlotGroup fromSlot(int slot) {
        return ALL_VALUES[slot / 4];
    }

    public static boolean isHybridSlot(int slot) {
        return fromSlot(slot).hybridSlot == slot;
    }

    public static boolean isSharedSlot(int slot) {
        return fromSlot(slot).hybridSlot != slot;
    }

}
