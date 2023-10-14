package com.unforbidable.tfc.bids.NEI;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class HandlerCatalystInfo {

    private final Block block;
    private final int blockMetadata;
    private final Item item;
    private final int itemDamage;

    public HandlerCatalystInfo(Block block, int metadata) {
        this.block = block;
        this.blockMetadata = metadata;
        this.item = null;
        this.itemDamage = 0;
    }

    public HandlerCatalystInfo(Item item, int damage) {
        this.block = null;
        this.blockMetadata = 0;
        this.item = item;
        this.itemDamage = damage;
    }

    public String getUniqueBlockOrItemId() {
        if (block != null) {
            return GameRegistry.findUniqueIdentifierFor(block) + ":" + blockMetadata;
        } else {
            return GameRegistry.findUniqueIdentifierFor(item) + ":" + itemDamage;
        }
    }

}
