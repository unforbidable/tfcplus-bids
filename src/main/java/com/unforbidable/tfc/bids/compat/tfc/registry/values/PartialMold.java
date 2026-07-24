package com.unforbidable.tfc.bids.compat.tfc.registry.values;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import net.minecraft.item.Item;

public class PartialMold {

    public final String metal;
    public final Item mold;
    public final int baseValue;
    public final Item result;
    public final int counter;
    public final int damageOffset;

    public PartialMold(String metal, Item mold, int baseValue, Item result, int counter, int damageOffset) {
        this.metal = metal;
        this.mold = mold;
        this.baseValue = baseValue;
        this.result = result;
        this.counter = counter;
        this.damageOffset = damageOffset;
    }

    public static RegistryActor<PartialMold> add(String metal, Item mold, int baseValue, Item result, int counter, int damageOffset) {
        return new RegistryAddingActor<>(PartialMoldStage.instance, new PartialMold(metal, mold, baseValue, result, counter, damageOffset));
    }

}
