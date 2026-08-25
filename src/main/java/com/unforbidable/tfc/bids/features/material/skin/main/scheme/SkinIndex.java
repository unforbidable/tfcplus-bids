package com.unforbidable.tfc.bids.features.material.skin.main.scheme;

import net.minecraft.item.Item;

public class SkinIndex {

    public final Item item;
    public final String name;
    public final SkinFurSpec fur;
    public final SkinWoolSpec wool;

    public SkinIndex(Item item, String name, SkinFurSpec fur, SkinWoolSpec wool) {
        this.item = item;
        this.name = name;
        this.fur = fur;
        this.wool = wool;
    }

    public static SkinIndexBuilder builder(Item item) {
        return builder(item, null);
    }

    public static SkinIndexBuilder builder(Item item, String name) {
        return new SkinIndexBuilder(item, name);
    }

    public int getLimingFluidAmount() {
        if (fur != null) {
            return (int) (100 * fur.dehairingEffort);
        } else {
            return 200;
        }
    }

    public int getLimingTicks() {
        if (fur != null) {
            return (int) (2000 * fur.dehairingEffort);
        } else {
            return 4000;
        }
    }

    public float getDehairingEffort() {
        if (fur != null) {
            return fur.dehairingEffort;
        } else {
            return 2f;
        }
    }

}
