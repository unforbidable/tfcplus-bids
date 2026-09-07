package com.unforbidable.tfc.bids.features.material.heatingstone;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.meta.HeatingStoneMeta;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.dryingsurface.DryingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.material.heatingstone.eventhandler.HeatingStoneEventHandler;
import com.unforbidable.tfc.bids.features.material.heatingstone.item.ItemHeatingStone;
import net.minecraft.item.ItemStack;

@FeatureName("heatingStone")
public class HeatingStone extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.HEATING_STONE, ItemHeatingStone::new)
            .meta("Fresh", "Cured", "Hot");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new HeatingStoneEventHandler());

        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.FRESH))
                .produces(new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.CURED))
                .dry()
                .hours(20)
                .build());

        setup.registry(TfcRegistry.Heat.values)
            .add(HeatValue.add(new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.CURED), 0.5f, 480,
                new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.HOT)))
            .add(HeatValue.add(new ItemStack(BidsItems.heatingStone, 1, HeatingStoneMeta.HOT), 1f, 1480, null));
    }

}
