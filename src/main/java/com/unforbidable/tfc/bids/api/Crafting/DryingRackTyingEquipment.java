package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class DryingRackTyingEquipment {

    public final Item item;
    public final boolean isReusable;
    public final Block renderBlock;
    public final int renderBlockMetadata;

    public DryingRackTyingEquipment(Item item, boolean isReusable, Block renderBlock, int renderBlockMetadata) {
        this.item = item;
        this.isReusable = isReusable;
        this.renderBlock = renderBlock;
        this.renderBlockMetadata = renderBlockMetadata;
    }

}
