package com.unforbidable.tfc.bids.features.device.kiln;

import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;

public class KilnConfig {

    public static boolean enableTunnelKiln = true;
    public static boolean enableSquareKiln = true;
    public static boolean enableBeehiveKiln = true;
    public static boolean enableClimbingKiln = true;
    public static int maxTunnelKilnHeight = 2;
    public static int maxSquareKilnHeight = 2;
    public static int maxClimbingKilnHeight = 3;

    public static void load(FeatureConfig config) {

        enableBeehiveKiln = config.getBoolean(
            "enableBeehiveKiln",
            enableBeehiveKiln,
            "Set this to true if you want to add Beehive kiln as an available kiln structure");
        enableTunnelKiln = config.getBoolean(
            "enableTunnelKiln",
            enableTunnelKiln,
            "Set this to true if you want to add Tunnel kiln as an available kiln structure");
        enableSquareKiln = config.getBoolean(
            "enableSquareKiln",
            enableSquareKiln,
            "Set this to true if you want to add Square kiln as an available kiln structure");
        enableClimbingKiln = config.getBoolean(
            "enableClimbingKiln",
            enableClimbingKiln,
            "Set this to true if you want to add Climbing kiln as an available kiln structure");
        maxTunnelKilnHeight = config.getInt(
            "maxTunnelKilnHeight",
            maxTunnelKilnHeight, 1, 2,
            "Maximum allowed height of the Tunnel kiln chamber; setting this to 2 allows the Tunnel kiln to be walked in");
        maxSquareKilnHeight = config.getInt(
            "maxSquareKilnHeight",
            maxSquareKilnHeight, 1, 2,
            "Maximum allowed height of the Square kiln chamber; setting this to 2 allows the Square kiln to be walked in");
        maxClimbingKilnHeight = config.getInt(
            "maxClimbingKilnHeight",
            maxClimbingKilnHeight, 1, 3,
            "Maximum allowed height of the Climbing kiln chamber; one unit of height corresponds to one section adding 6 more pottery slots on top of the initial 6");
    }

}
