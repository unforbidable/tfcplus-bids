package com.unforbidable.tfc.bids.core.keybinding;

import com.unforbidable.tfc.bids.core.keybinding.action.KeyBindingAction;
import com.unforbidable.tfc.bids.core.keybinding.action.KeyBindingActionContext;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;

import java.util.Arrays;

public class KeyBindingHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (FMLClientHandler.instance().getClient().inGameHasFocus &&
            FMLClientHandler.instance().getClient().currentScreen == null) {

            Arrays.stream(KeyBindings.all)
                .filter(KeyBinding::isPressed)
                .forEach(this::handleKeyBindingPressed);
        }
    }

    private void handleKeyBindingPressed(KeyBinding keyBinding) {
        KeyBindingRegistry.actions.stream()
            .filter(a -> a.keyBinding == keyBinding)
            .forEach(this::triggerKeyBindingAction);
    }

    private void triggerKeyBindingAction(KeyBindingAction action) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
        KeyBindingActionContext context = new KeyBindingActionContext(action.keyBinding, player);

        action.action.accept(context);
    }

}
