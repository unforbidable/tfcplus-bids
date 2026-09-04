package com.unforbidable.tfc.bids.features.utility.shovel.item;

import com.unforbidable.tfc.bids.common.item.ItemCommonShovel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import java.util.HashSet;
import java.util.Set;

public class ItemPrimitiveShovel extends ItemCommonShovel {

    public static final Set<Block> effectiveAgainstBlocks = new HashSet<>();

    public ItemPrimitiveShovel(ToolMaterial material) {
        super(material);
    }

    public float func_150893_a(ItemStack is, Block block) {
        return effectiveAgainstBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
    }

}
