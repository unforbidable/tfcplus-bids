package com.unforbidable.tfc.bids.core.features.setup.help;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;

public class HelpGroupBuilder {

    private final ItemStack itemStack;
    private final List<String> hints = new ArrayList<>();

    public HelpGroupBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public HelpGroupBuilder hints(String ...hints) {
        this.hints.addAll(Arrays.asList(hints));

        return this;
    }

    public HelpGroup build() {
        return new HelpGroup(itemStack, hints);
    }

}
