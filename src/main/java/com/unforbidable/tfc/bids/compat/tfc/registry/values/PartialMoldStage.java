package com.unforbidable.tfc.bids.compat.tfc.registry.values;

import com.dunk.tfc.Core.Metal.MetalRegistry;
import com.dunk.tfc.api.Metal;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class PartialMoldStage extends RegistryStage<PartialMold> {

    public static final PartialMoldStage instance = new PartialMoldStage();

    @Override
    public void add(PartialMold value) {
        Bids.LOG.info("Register TFC partial mold {} for metal {}", value.mold, value.metal);

        try {
            Metal metal = MetalRegistry.instance.getMetalFromString(value.metal);
            metal.addValidPartialMold(value.mold, value.baseValue, value.result, value.counter, value.damageOffset);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to register TFC partial mold {} for metal {}: {}", value.mold, value.metal, ex.getMessage(), ex);
        }
    }

}
