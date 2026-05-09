package com.unforbidable.tfc.bids.core.keybinding;

import com.unforbidable.tfc.bids.core.Initializable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingInit extends Initializable {

    @Override
    public void postInitClientOnly(FMLPostInitializationEvent event) {
        KeyBinding[] newKeyBindings = KeyBindings.all;
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        KeyBinding[] allKeys = new KeyBinding[settings.keyBindings.length + newKeyBindings.length];
        System.arraycopy(settings.keyBindings, 0, allKeys, 0, settings.keyBindings.length);
        System.arraycopy(newKeyBindings, 0, allKeys, settings.keyBindings.length, newKeyBindings.length);
        settings.keyBindings = allKeys;
        settings.loadOptions();

        FMLCommonHandler.instance().bus().register(new KeyBindingHandler());
    }

}
