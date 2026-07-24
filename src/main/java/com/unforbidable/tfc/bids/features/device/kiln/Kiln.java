package com.unforbidable.tfc.bids.features.device.kiln;

import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.kiln.eventhandler.KilnPotteryFiringHandler;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.beehive.BeehiveKilnChamber;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.climbing.ClimbingKilnChamber;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.square.SquareKilnChamber;
import com.unforbidable.tfc.bids.features.device.kiln.main.kilns.tunnel.TunnelKilnChamber;

@FeatureName("kiln")
public class Kiln extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(KilnConfig::load);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new KilnPotteryFiringHandler());

        if (KilnConfig.enableTunnelKiln) {
            setup.registry(KilnRegistry.chambers)
                .add(TunnelKilnChamber.class);
        }

        if (KilnConfig.enableSquareKiln) {
            setup.registry(KilnRegistry.chambers)
                .add(SquareKilnChamber.class);
        }

        if (KilnConfig.enableBeehiveKiln) {
            setup.registry(KilnRegistry.chambers)
                .add(BeehiveKilnChamber.class);
        }

        if (KilnConfig.enableClimbingKiln) {
            setup.registry(KilnRegistry.chambers)
                .add(ClimbingKilnChamber.class);
        }
    }

}
