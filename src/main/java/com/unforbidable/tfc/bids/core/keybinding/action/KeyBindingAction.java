package com.unforbidable.tfc.bids.core.keybinding.action;

import java.util.function.Consumer;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingAction {

    public final KeyBinding keyBinding;
    public final Consumer<KeyBindingActionContext> action;

    public KeyBindingAction(KeyBinding keyBinding, Consumer<KeyBindingActionContext> action) {
        this.keyBinding = keyBinding;
        this.action = action;
    }

}
