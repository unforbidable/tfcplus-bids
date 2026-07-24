package com.unforbidable.tfc.bids.core.features.client.keybinding;

import com.unforbidable.tfc.bids.core.keybinding.KeyBindingRegistry;
import com.unforbidable.tfc.bids.core.keybinding.action.KeyBindingAction;
import com.unforbidable.tfc.bids.core.keybinding.action.KeyBindingActionContext;
import java.util.function.Consumer;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingClientHelper {

    public KeyBindingClientHelper action(KeyBinding toolMode, Consumer<KeyBindingActionContext> action) {
        KeyBindingRegistry.actions.add(new KeyBindingAction(toolMode, action));

        return this;
    }

}
