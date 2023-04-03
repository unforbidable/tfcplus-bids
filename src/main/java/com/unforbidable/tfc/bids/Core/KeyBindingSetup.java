package com.unforbidable.tfc.bids.Core;

import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyBindingSetup {

    public static KeyBinding KEY_BINDING_TOOL_MODE = new KeyBinding("key.BidsToolMode", Keyboard.KEY_N, Tags.MOD_NAME);

    public static void postInit() {
        KeyBinding[] newKeyBindings = new KeyBinding[] { KEY_BINDING_TOOL_MODE };

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        KeyBinding[] allKeys = new KeyBinding[settings.keyBindings.length + newKeyBindings.length];
        System.arraycopy(settings.keyBindings, 0, allKeys, 0, settings.keyBindings.length);
        System.arraycopy(newKeyBindings, 0, allKeys, settings.keyBindings.length, newKeyBindings.length);
        settings.keyBindings = allKeys;
        settings.loadOptions();
    }
}
