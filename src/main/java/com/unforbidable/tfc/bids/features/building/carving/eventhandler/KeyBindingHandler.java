package com.unforbidable.tfc.bids.features.building.carving.eventhandler;

import com.unforbidable.tfc.bids.features.building.carving.main.CarvingHelper;
import com.unforbidable.tfc.bids.features.utility.adze.item.ItemAdze;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.entity.EntityClientPlayerMP;

import static com.unforbidable.tfc.bids.core.keybinding.KeyBindings.toolMode;

public class KeyBindingHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;

        if (FMLClientHandler.instance().getClient().inGameHasFocus &&
            FMLClientHandler.instance().getClient().thePlayer.getCurrentEquippedItem() != null &&
            FMLClientHandler.instance().getClient().currentScreen == null) {
            if (toolMode.isPressed()) {
                if (player.getCurrentEquippedItem().getItem() instanceof ItemAdze) {
                    CarvingHelper.setPlayerCarvingMode(player);

                    //Let's send the actual EnumAdzeMode so the server/client does not
                    //come out of sync.
                }

            }
        }
    }
}
