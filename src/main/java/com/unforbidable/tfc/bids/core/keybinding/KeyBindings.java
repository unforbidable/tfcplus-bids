package com.unforbidable.tfc.bids.core.keybinding;

import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyBindings {

    public static final KeyBinding toolMode = new KeyBinding("key.BidsToolMode", Keyboard.KEY_N, Tags.MOD_NAME);

    public static KeyBinding[] all = new KeyBinding[] {
        KeyBindings.toolMode
    };

}
