package com.unforbidable.tfc.bids.compat.tfc.registry.values;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import net.minecraft.item.ItemStack;

public class HeatValue {

    public final ItemStack input;
    public final float specificHeat;
    public final float meltTemp;
    public final ItemStack output;

    public HeatValue(ItemStack input, float specificHeat, float meltTemp, ItemStack output) {
        this.input = input;
        this.specificHeat = specificHeat;
        this.meltTemp = meltTemp;
        this.output = output;
    }

    public static RegistryActor<HeatValue> add(ItemStack input, float specificHeat, float meltTemp, ItemStack output) {
        return new RegistryAddingActor<>(HeatValueStage.instance, new HeatValue(input, specificHeat, meltTemp, output));
    }

}
