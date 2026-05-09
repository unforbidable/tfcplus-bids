package com.unforbidable.tfc.bids.core.crafting;

import java.util.function.Consumer;

public interface CraftingAction {

    Consumer<CraftingContext> action();

}
