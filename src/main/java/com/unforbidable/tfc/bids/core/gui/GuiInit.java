package com.unforbidable.tfc.bids.core.gui;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.Initializable;
import com.unforbidable.tfc.bids.core.gui.client.ClientGuiHandler;
import com.unforbidable.tfc.bids.core.gui.provider.TileEntityGuiFunction;
import com.unforbidable.tfc.bids.features.device.crucible.gui.GuiClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityClayCrucible;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class GuiInit extends Initializable {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new GuiHandler());

        TileEntityGuiFunction<InventoryPlayer, TileEntityClayCrucible, World, Integer, Integer, Integer, GuiScreen> fn;
        fn = GuiClayCrucible::new;
    }

    @Override
    public void preInitClientOnly(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new ClientGuiHandler());
    }

}
