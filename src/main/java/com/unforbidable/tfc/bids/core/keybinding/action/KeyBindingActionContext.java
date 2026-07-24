package com.unforbidable.tfc.bids.core.keybinding.action;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class KeyBindingActionContext {

    public final KeyBinding keyBinding;
    public final EntityPlayer player;

    public KeyBindingActionContext(KeyBinding keyBinding, EntityPlayer player) {
        this.keyBinding = keyBinding;
        this.player = player;
    }

}
