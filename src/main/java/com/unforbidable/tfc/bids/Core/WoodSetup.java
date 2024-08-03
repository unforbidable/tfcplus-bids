package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Wood.WoodBuilder;

import static com.unforbidable.tfc.bids.Core.Wood.WoodScheme.DEFAULT;

public class WoodSetup {

    public static void preInit() {
        registerWood();
    }

    private static void registerWood() {
        Bids.LOG.info("Registering wood");

        DEFAULT.registerWood(new WoodBuilder(0, "OAK")
            .setFuelMaterial(EnumFuelMaterial.OAK)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(1, "ASPEN")
            .setFuelMaterial(EnumFuelMaterial.ASPEN)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(2, "BIRCH")
            .setFuelMaterial(EnumFuelMaterial.BIRCH)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(3, "CHESTNUT")
            .setFuelMaterial(EnumFuelMaterial.CHESTNUT)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(4, "DOUGLAS_FIR")
            .setFuelMaterial(EnumFuelMaterial.DOUGLASFIR)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setResinous()
            .build());

        DEFAULT.registerWood(new WoodBuilder(5, "HICKORY")
            .setFuelMaterial(EnumFuelMaterial.HICKORY)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(6, "MAPLE")
            .setFuelMaterial(EnumFuelMaterial.MAPLE)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(7, "ASH")
            .setFuelMaterial(EnumFuelMaterial.ASH)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(8, "PINE")
            .setFuelMaterial(EnumFuelMaterial.PINE)
            .setHasBark()
            .setHasLargeLogs()
            .setResinous()
            .build());

        DEFAULT.registerWood(new WoodBuilder(9, "SEQUOIA")
            .setFuelMaterial(EnumFuelMaterial.REDWOOD)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setResinous()
            .build());

        DEFAULT.registerWood(new WoodBuilder(10, "SPRUCE")
            .setFuelMaterial(EnumFuelMaterial.SPRUCE)
            .setHasBark()
            .setHasLargeLogs()
            .setResinous()
            .build());

        DEFAULT.registerWood(new WoodBuilder(11, "SYCAMORE")
            .setFuelMaterial(EnumFuelMaterial.SYCAMORE)
            .setHasBark()
            .setHasLargeLogs()
            .build());

        DEFAULT.registerWood(new WoodBuilder(12, "WHITE_CEDAR")
            .setFuelMaterial(EnumFuelMaterial.WHITECEDAR)
            .setHasBarkFibers()
            .setHasLargeLogs()
            .setResinous()
            .build());

        DEFAULT.registerWood(new WoodBuilder(13, "WHITE_ELM")
            .setFuelMaterial(EnumFuelMaterial.WHITEELM)
            .setHasBarkFibers()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(14, "WILLOW")
            .setFuelMaterial(EnumFuelMaterial.WILLOW)
            .setHasBarkFibers()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(15, "KAPOK")
            .setFuelMaterial(EnumFuelMaterial.KAPOK)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(16, "ACACIA")
            .setFuelMaterial(EnumFuelMaterial.ACACIA)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(17, "PALM")
            .setFuelMaterial(EnumFuelMaterial.PALM)
            .setInflammable()
            .build());

        DEFAULT.registerWood(new WoodBuilder(18, "EBONY")
            .setFuelMaterial(EnumFuelMaterial.EBONY)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(19, "FEVER")
            .setFuelMaterial(EnumFuelMaterial.FEVER)
            .setHasBarkTannin()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(20, "BAOBAB")
            .setFuelMaterial(EnumFuelMaterial.BAOBAB)
            .setHasBark()
            .setHasLargeLogs()
            .build());

        DEFAULT.registerWood(new WoodBuilder(21, "LIMBA")
            .setFuelMaterial(EnumFuelMaterial.LIMBA)
            .setHasBarkTannin()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(22, "MAHOGANY")
            .setFuelMaterial(EnumFuelMaterial.MAHOGANY)
            .setHasBarkTannin()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(23, "TEAK")
            .setFuelMaterial(EnumFuelMaterial.TEAK)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(24, "BAMBOO")
            .setFuelMaterial(EnumFuelMaterial.BAMBOO)
            .build());

        DEFAULT.registerWood(new WoodBuilder(25, "GINKGO")
            .setFuelMaterial(EnumFuelMaterial.GINGKO)
            .setHasBark()
            .setHasLargeLogs()
            .build());

        DEFAULT.registerWood(new WoodBuilder(26, "FRUIT_WOOD")
            .setFuelMaterial(EnumFuelMaterial.FRUITWOOD)
            .setHasBark()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(27, "MANGROVE")
            .setFuelMaterial(EnumFuelMaterial.ASPEN)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(28, "GHAF")
            .setFuelMaterial(EnumFuelMaterial.ASPEN)
            .setHasBark()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(29, "MAHAOE")
            .setFuelMaterial(EnumFuelMaterial.ASPEN)
            .setHasBark()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(30, "LAUREL")
            .setFuelMaterial(EnumFuelMaterial.ASPEN)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

        DEFAULT.registerWood(new WoodBuilder(31, "JOSHUA")
            .setFuelMaterial(EnumFuelMaterial.ASPEN)
            .setInflammable()
            .build());

        DEFAULT.registerWood(new WoodBuilder(32, "YEW")
            .setFuelMaterial(EnumFuelMaterial.ASPEN)
            .setHasBark()
            .setHasLargeLogs()
            .setHardwood()
            .build());

    }

}
