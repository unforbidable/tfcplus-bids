package com.unforbidable.tfc.bids.core.features.setup.help;

import java.util.List;
import net.minecraft.item.ItemStack;

public class HelpGroup {

    public final ItemStack itemStack;
    public final List<String> hints;

    public HelpGroup(ItemStack itemStack, List<String> hints) {
        this.itemStack = itemStack;
        this.hints = hints;
    }

}
