package com.unforbidable.tfc.bids.core.keybinding.action;

import net.minecraft.client.settings.KeyBinding;

import java.util.function.Consumer;

public class KeyBindingAction {

    public final KeyBinding keyBinding;
    public final Consumer<KeyBindingActionContext> action;

    public KeyBindingAction(KeyBinding keyBinding, Consumer<KeyBindingActionContext> action) {
        this.keyBinding = keyBinding;
        this.action = action;
    }

}
