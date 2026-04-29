package com.unforbidable.tfc.bids.Core.Crafting;

import java.util.function.Consumer;

public interface ICraftingAction {

    Consumer<CraftingContext> action();

}
