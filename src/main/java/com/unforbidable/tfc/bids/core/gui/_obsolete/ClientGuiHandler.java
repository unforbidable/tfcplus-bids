package com.unforbidable.tfc.bids.core.gui._obsolete;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.unforbidable.tfc.bids.api._obsolete.BidsGui;
import com.unforbidable.tfc.bids.features.crafting.woodworking.gui.GuiWoodworking;
import com.unforbidable.tfc.bids.features.device.cookingprep.gui.GuiCookingPrep;
import com.unforbidable.tfc.bids.features.device.cookingprep.tileentity.TileEntityCookingPrep;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.gui.GuiKnappingGlass;
import com.unforbidable.tfc.bids.features.device.crucible.gui.GuiClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.gui.GuiFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.firepit.gui.GuiNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.tileentity.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.features.device.screwpress.gui.GuiScrewPress;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.strawnest.gui.GuiStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.tileentity.TileEntityStrawNest;
import com.unforbidable.tfc.bids.features.device.woodpile.gui.GuiWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import com.unforbidable.tfc.bids.features.food.coarseflour.gui.GuiKnappingDough;
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

            case BidsGui.woodPileGui:
                return new GuiWoodpile(player.inventory, (TileEntityWoodpile) te, world, x, y, z);

            case BidsGui.newFirepitGui:
                return new GuiNewFirepit(player.inventory, (TileEntityNewFirepit) te, world, x, y, z);

            case BidsGui.cookingPrepGui:
                return new GuiCookingPrep(player.inventory, (TileEntityCookingPrep) te, world, x, y, z);

            case BidsGui.screwPressBarrelGui:
                return new GuiScrewPress(player.inventory, (TileEntityScrewPressBarrel) te, world, x, y, z);

            case BidsGui.strawNestGui:
                return new GuiStrawNest(player.inventory, (TileEntityStrawNest) te, world, x, y, z);

            case BidsGui.doughKnappingGui:
                PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(player);
                return new GuiKnappingDough(player.inventory, pi.specialCraftingType, world, x, y, z);

            case BidsGui.woodworkingGui:
                return new GuiWoodworking(player.inventory, world, x, y, z);
        }

        return null;
    }

}
