package com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers;

import com.dunk.tfc.TileEntities.TEPottery;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PotteryFireStartingHandler extends FireStartingHandler {

    public PotteryFireStartingHandler(int priority) {
        super(priority);
    }

    @Override
    public boolean start(EntityPlayer player, World world, int x, int y, int z, int side) {
        return lightPottery(player, world, x, y, z, side, false);
    }

    @Override
    public boolean ignite(EntityPlayer player, World world, int x, int y, int z, int side) {
        return lightPottery(player, world, x, y, z, side, true);
    }

    private boolean lightPottery(EntityPlayer player, World world, int x, int y, int z, int side, boolean ignite) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEPottery) {
            TEPottery tePottery = (TEPottery) te;
            if (!tePottery.isLit() && tePottery.wood == 8) {
                if (ignite) {
                    tePottery.startPitFire();
                }

                return true;
            }
        }

        return false;
    }

}
