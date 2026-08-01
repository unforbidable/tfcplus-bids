package com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers;

import com.dunk.tfc.TileEntities.TEBlastFurnace;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlastFurnaceFireStartingHandler extends FireStartingHandler {

    public BlastFurnaceFireStartingHandler(int priority) {
        super(priority);
    }

    @Override
    public boolean ignite(EntityPlayer player, World world, int x, int y, int z, int side) {
        return lightBlastFurnace(player, world, x, y, z, side, true);
    }

    private boolean lightBlastFurnace(EntityPlayer player, World world, int x, int y, int z, int side, boolean ignite) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEBlastFurnace) {
            TEBlastFurnace teBlastFurnace = (TEBlastFurnace) te;
            if (teBlastFurnace.isValid && canLightBlastFurnace(teBlastFurnace)) {
                if (ignite) {
                    return teBlastFurnace.canLight();
                }

                return true;
            }
        }

        return false;
    }

    private boolean canLightBlastFurnace(TEBlastFurnace teBlastFurnace) {
        // This code is adapted from TEBlastFurnace::canLight
        // which contrary to its name lights the bloomer on a successful check
        return teBlastFurnace.charcoalCount >= teBlastFurnace.oreCount &&
            teBlastFurnace.charcoalCount >= 4 &&
            teBlastFurnace.fireTemp == 0.0F;
    }

}
