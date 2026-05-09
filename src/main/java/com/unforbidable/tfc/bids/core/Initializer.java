package com.unforbidable.tfc.bids.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

public class Initializer {

    private static final Initializable[] initializers = {
    };

    public static void preInit(FMLPreInitializationEvent event) {
        Arrays.stream(initializers).forEach(initializable -> initializable.preInit(event));
    }

    public static void preInitClientOnly(FMLPreInitializationEvent event) {
        Arrays.stream(initializers).forEach(initializable -> initializable.preInitClientOnly(event));
    }

    public static void init(FMLInitializationEvent event) {
        Arrays.stream(initializers).forEach(initializable -> initializable.init(event));
    }

    public static void initClientOnly(FMLInitializationEvent event) {
        Arrays.stream(initializers).forEach(initializable -> initializable.initClientOnly(event));
    }

    public static void postInit(FMLPostInitializationEvent event) {
        Arrays.stream(initializers).forEach(initializable -> initializable.postInit(event));
    }

    public static void postInitClientOnly(FMLPostInitializationEvent event) {
        Arrays.stream(initializers).forEach(initializable -> initializable.postInitClientOnly(event));
    }

}
