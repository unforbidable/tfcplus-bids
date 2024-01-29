package com.unforbidable.tfc.bids.Handlers.Client;

import com.unforbidable.tfc.bids.Gui.*;
import com.unforbidable.tfc.bids.Handlers.GuiHandler;
import com.unforbidable.tfc.bids.TileEntities.*;
import com.unforbidable.tfc.bids.api.BidsGui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientGuiHandler extends GuiHandler {

    @Override
    public Object getClientGuiElement(int i, EntityPlayer player, World world, int x, int y, int z) {

        TileEntity te = world.getTileEntity(x, y, z);
        switch (i) {
            case BidsGui.clayCrucibleGui:
                return new GuiClayCrucible(player.inventory, (TileEntityClayCrucible) te, world, x, y, z);

            case BidsGui.fireClayCrucibleGui:
                return new GuiFireClayCrucible(player.inventory, (TileEntityFireClayCrucible) te, world, x, y, z);

            case BidsGui.glassKnappingGui:
                return new GuiKnappingGlass(player.inventory, world, x, y, z);

            case BidsGui.barkFibreKnappingGui:
                return new GuiKnappingBarkFibre(player.inventory, world, x, y, z);

            case BidsGui.woodPileGui:
                return new GuiWoodPile(player.inventory, (TileEntityWoodPile) te, world, x, y, z);

            case BidsGui.newFirepitGui:
                return new GuiNewFirepit(player.inventory, (TileEntityNewFirepit) te, world, x, y, z);

            case BidsGui.cookingPrepGui:
                return new GuiCookingPrep(player.inventory, (TileEntityCookingPrep) te, world, x, y, z);

            case BidsGui.screwPressBarrelGui:
                return new GuiScrewPress(player.inventory, (TileEntityScrewPressBarrel) te, world, x, y, z);
        }

        return null;
    }

}
