package com.unforbidable.tfc.bids.compat.tfc.registry.values;

import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRaw;
import com.dunk.tfc.api.HeatRegistry;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class HeatValueStage extends RegistryStage<HeatValue> {

    public static final HeatValueStage instance = new HeatValueStage();

    @Override
    public void add(HeatValue value) {
        Bids.LOG.info("Register TFC heat index for {}", value.input);

        try {
            HeatRaw raw = new HeatRaw(value.specificHeat, value.meltTemp);
            HeatIndex heatIndex = new HeatIndex(value.input, raw, value.output)
                .setKeepNBT(value.keepNbt);
            HeatRegistry.getInstance().addIndex(heatIndex);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to register TFC heat index for {}: {}", value.input, ex.getMessage(), ex);
        }
    }

}
