package com.unforbidable.tfc.bids.compat.tfc.registry.values;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryActor;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryAddingActor;
import net.minecraft.item.ItemStack;

public class HeatValue {

    public final ItemStack input;
    public final float specificHeat;
    public final float meltTemp;
    public final ItemStack output;
    public final boolean keepNbt;

    public HeatValue(ItemStack input, float specificHeat, float meltTemp, ItemStack output, boolean keepNbt) {
        this.input = input;
        this.specificHeat = specificHeat;
        this.meltTemp = meltTemp;
        this.output = output;
        this.keepNbt = keepNbt;
    }

    public static RegistryActor<HeatValue> add(ItemStack input, float specificHeat, float meltTemp, ItemStack output) {
        return add(input, specificHeat, meltTemp, output, false);
    }

    public static RegistryActor<HeatValue> add(ItemStack input, float specificHeat, float meltTemp, ItemStack output, boolean keepNbt) {
        return new RegistryAddingActor<>(HeatValueStage.instance, new HeatValue(input, specificHeat, meltTemp, output, keepNbt));
    }

}
