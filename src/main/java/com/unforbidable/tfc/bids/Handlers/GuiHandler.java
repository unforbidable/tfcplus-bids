package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.unforbidable.tfc.bids.Containers.*;
import com.unforbidable.tfc.bids.TileEntities.*;
import com.unforbidable.tfc.bids.api.BidsGui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int i, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getServerGuiElement(int i, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        switch (i) {
            case BidsGui.clayCrucibleGui:
                return new ContainerClayCrucible(player.inventory, (TileEntityClayCrucible) te, world, x, y, z);

            case BidsGui.fireClayCrucibleGui:
                return new ContainerFireClayCrucible(player.inventory, (TileEntityFireClayCrucible) te, world, x, y, z);

            case BidsGui.glassKnappingGui:
                return new ContainerSpecialCraftingGlass(player.inventory, world, x, y, z);

            case BidsGui.barkFibreKnappingGui:
                return new ContainerSpecialCraftingBarkFibre(player.inventory, world, x, y, z);

            case BidsGui.woodPileGui:
                return new ContainerWoodPile(player.inventory, (TileEntityWoodPile) te, world, x, y, z);

            case BidsGui.newFirepitGui:
                return new ContainerNewFirepit(player.inventory, (TileEntityNewFirepit) te, world, x, y, z);

            case BidsGui.cookingPrepGui:
                return new ContainerCookingPrep(player.inventory, (TileEntityCookingPrep) te, world, x, y, z);

            case BidsGui.screwPressBarrelGui:
                return new ContainerScrewPress(player.inventory, (TileEntityScrewPressBarrel) te, world, x, y, z);

            case BidsGui.strawNestGui:
                return new ContainerStrawNest(player.inventory, (TileEntityStrawNest) te, world, x, y, z);

            case BidsGui.doughKnappingGui:
                PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(player);
                return new ContainerSpecialCraftingDough(player.inventory, pi.specialCraftingType, world, x, y, z);
        }
        return null;
    }

}
