package com.unforbidable.tfc.bids.NEI;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class HandlerInfo {

    private final Block block;
    private final int blockMetadata;
    private final Item item;
    private final int itemDamage;

    private final List<HandlerCatalystInfo> catalysts = new ArrayList<HandlerCatalystInfo>();

    public HandlerInfo(Block block) {
        this(block, 0);
    }

    public HandlerInfo(Block block, int blockMetadata) {
        this.block = block;
        this.blockMetadata = blockMetadata;
        this.item = null;
        this.itemDamage = 0;
    }

    public HandlerInfo(Item item) {
        this(item, 0);
    }

    public HandlerInfo(Item item, int itemDamage) {
        this.block = null;
        this.blockMetadata = 0;
        this.item = item;
        this.itemDamage = itemDamage;
    }

    public List<HandlerCatalystInfo> getCatalysts() {
        return catalysts;
    }

    public void addCatalyst(Block block, int meta) {
        this.catalysts.add(new HandlerCatalystInfo(block, meta));
    }

    public void addCatalyst(Block block) {
        this.catalysts.add(new HandlerCatalystInfo(block, 0));
    }

    public void addCatalyst(Item item, int meta) {
        this.catalysts.add(new HandlerCatalystInfo(item, meta));
    }

    public void addCatalyst(Item item) {
        this.catalysts.add(new HandlerCatalystInfo(item, 0));
    }

    public String getUniqueBlockOrItemId() {
        if (block != null) {
            return GameRegistry.findUniqueIdentifierFor(block) + ":" + blockMetadata;
        } else {
            return GameRegistry.findUniqueIdentifierFor(item) + ":" + itemDamage;
        }
    }

}
