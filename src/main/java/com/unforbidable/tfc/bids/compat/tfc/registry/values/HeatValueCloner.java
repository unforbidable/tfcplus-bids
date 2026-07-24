package com.unforbidable.tfc.bids.compat.tfc.registry.values;

import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;
import com.unforbidable.tfc.bids.compat.tfc.registry.actors.RegistryCloningActor;
import java.util.function.Predicate;
import net.minecraft.item.ItemStack;

public class HeatValueCloner extends RegistryCloningActor<HeatValue> {

    public HeatValueCloner(RegistryStage<HeatValue> stage, Predicate<HeatValue> predicate) {
        super(stage, predicate);
    }

    public HeatValueCloner as(ItemStack input) {
        as(v -> new HeatValue(input, v.specificHeat, v.meltTemp, v.output, v.keepNbt));

        return this;
    }

    public HeatValueCloner as(ItemStack input, ItemStack output) {
        as(v -> new HeatValue(input, v.specificHeat, v.meltTemp, output, v.keepNbt));

        return this;
    }

    public HeatValueCloner as(ItemStack input, int outputCount) {
        as(v -> new HeatValue(input, v.specificHeat, v.meltTemp, new ItemStack(v.output.getItem(), outputCount, v.output.getItemDamage()), v.keepNbt));

        return this;
    }

}
