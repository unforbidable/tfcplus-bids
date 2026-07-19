package com.unforbidable.tfc.bids.compat.nei;

import codechicken.nei.api.API;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.compat.nei.registry.NeiHandlerEntry;
import com.unforbidable.tfc.bids.compat.nei.registry.NeiHiderEntry;
import com.unforbidable.tfc.bids.compat.nei.registry.NeiRegistry;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class NeiSetup {

    public static void registerHandlers() {
        Bids.LOG.info("Registering NEI handlers");

        NeiRegistry.handlers.stream()
            .forEach(NeiSetup::registerHandler);
    }

    private static void registerHandler(NeiHandlerEntry entry) {
        API.registerRecipeHandler(entry.handler);
        API.registerUsageHandler(entry.handler);

        if (entry.handler instanceof IHandlerInfoProvider) {
            registerHandlerHeaderInfo(entry.handler.getOverlayIdentifier(), ((IHandlerInfoProvider)entry.handler).getHandlerInfo());
        }
    }

    private static void registerHandlerHeaderInfo(String handlerId, HandlerInfo handlerInfo) {
        NBTTagCompound handlerMetadata = new NBTTagCompound();
        handlerMetadata.setString("handler", handlerId);
        handlerMetadata.setString("modName", Tags.MOD_NAME);
        handlerMetadata.setString("modId", Tags.MOD_ID);
        handlerMetadata.setBoolean("modRequired", true);
        handlerMetadata.setString("itemName", handlerInfo.getUniqueBlockOrItemId());
        handlerMetadata.setInteger("handlerHeight", handlerInfo.getHeight());
        handlerMetadata.setInteger("maxRecipesPerPage", handlerInfo.getRecipesPerPage());
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", handlerMetadata);

        Bids.LOG.info("Sent registerHandlerInfo message for: " + handlerId);

        for (HandlerCatalystInfo catalystInfo : handlerInfo.getCatalysts()) {
            NBTTagCompound catalystMetadata = new NBTTagCompound();
            catalystMetadata.setString("handlerID", handlerId);
            catalystMetadata.setString("itemName", catalystInfo.getUniqueBlockOrItemId());
            FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", catalystMetadata);

            Bids.LOG.debug("Sent registerCatalystInfo message for: " + handlerId + ", " + catalystInfo.getUniqueBlockOrItemId());
        }
    }

    public static void hideItemStacks() {
        Bids.LOG.info("Hide items and blocks from NEI");

        NeiRegistry.hiders.stream()
            .forEach(NeiSetup::hide);
    }

    private static void hide(NeiHiderEntry neiHiderEntry) {
        API.hideItem(neiHiderEntry.itemStack);
    }

}
