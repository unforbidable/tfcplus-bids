package com.unforbidable.tfc.bids.core;

import com.unforbidable.tfc.bids.core.config.ConfigInit;
import com.unforbidable.tfc.bids.core.crafting.CraftingInit;
import com.unforbidable.tfc.bids.core.drink.DrinkInit;
import com.unforbidable.tfc.bids.core.gui.GuiInit;
import com.unforbidable.tfc.bids.core.keybinding.KeyBindingInit;
import com.unforbidable.tfc.bids.compat.tfc.TfcInit;
import com.unforbidable.tfc.bids.compat.waila.WailaInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

public class Initializer {

    private static final Initializable[] initializers = {
        // Core initializers are listed here
        new ConfigInit(),
        new GuiInit(),
        new KeyBindingInit(),
        new CraftingInit(),
        new DrinkInit(),
        // These must run after FeatureInit
        // this is where TFC, WAILA and NEI stuff get actually registered
        new TfcInit(),
        new WailaInit(),
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
