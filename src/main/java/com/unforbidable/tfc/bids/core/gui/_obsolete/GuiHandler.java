package com.unforbidable.tfc.bids.core.gui._obsolete;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.unforbidable.tfc.bids.api._obsolete.BidsGui;
import com.unforbidable.tfc.bids.features.crafting.woodworking.container.ContainerWoodworking;
import com.unforbidable.tfc.bids.features.device.cookingprep.container.ContainerCookingPrep;
import com.unforbidable.tfc.bids.features.device.cookingprep.tileentity.TileEntityCookingPrep;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.container.ContainerSpecialCraftingGlass;
import com.unforbidable.tfc.bids.features.device.crucible.container.ContainerClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.container.ContainerFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.firepit.container.ContainerNewFirepit;
import com.unforbidable.tfc.bids.features.device.firepit.tileentity.TileEntityNewFirepit;
import com.unforbidable.tfc.bids.features.device.screwpress.container.ContainerScrewPress;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.strawnest.container.ContainerStrawNest;
import com.unforbidable.tfc.bids.features.device.strawnest.tileentity.TileEntityStrawNest;
import com.unforbidable.tfc.bids.features.device.woodpile.container.ContainerWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import com.unforbidable.tfc.bids.features.food.coarseflour.container.ContainerSpecialCraftingDough;
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

            case BidsGui.woodPileGui:
                return new ContainerWoodpile(player.inventory, (TileEntityWoodpile) te, world, x, y, z);

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

            case BidsGui.woodworkingGui:
                return new ContainerWoodworking(player.inventory, world, x, y, z);
        }
        return null;
    }

}
