package com.unforbidable.tfc.bids.Core.Kilns.SquareKiln;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Kilns.KilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class SquareKilnChamber extends KilnChamber<SquareKilnValidator> {

    public SquareKilnChamber(IKilnHeatSource heatSource) {
        super(heatSource);
    }

    @Override
    protected SquareKilnValidator createValidator(World world, int x, int y, int z) {
        return new SquareKilnValidator(world, x, y, z);
    }

    @Override
    public String getName() {
        return "SQUARE_KILN";
    }

    @Override
    public BlockCoord getChimneyLocation() {
        ForgeDirection chamberDir = getValidator().getParams().direction;
        SquareKilnChimneyRotation chimneyRotation = getValidator().getParams().chimneyRotation;
        ForgeDirection chimneyDir = chamberDir.getRotation(chimneyRotation.getAxis());

        int x = heatSource.getTileX() + chamberDir.offsetX * 2 + chimneyDir.offsetX;
        int y = heatSource.getTileY() + 1 + getValidator().getParams().height;
        int z = heatSource.getTileZ() + chamberDir.offsetZ * 2 + chimneyDir.offsetZ;
        return new BlockCoord(x, y, z);
    }

    @Override
    public List<BlockCoord> getPotteryLocations() {
        ForgeDirection chamberDir = getValidator().getParams().direction;
        SquareKilnChimneyRotation chimneyRotation = getValidator().getParams().chimneyRotation;
        ForgeDirection chimneyDir = chamberDir.getRotation(chimneyRotation.getAxis());

        int entryX = chamberDir.offsetX;
        int entryZ = chamberDir.offsetZ;
        int chimneyX = chamberDir.offsetX * 2 + chimneyDir.offsetX;
        int chimneyZ =  chamberDir.offsetZ * 2 + chimneyDir.offsetZ;
        int minX = Math.min(entryX, chimneyX);
        int minZ = Math.min(entryZ, chimneyZ);

        List<BlockCoord> list = new ArrayList<BlockCoord>();

        for (int i = minX; i < minX + 2; i++) {
            for (int j = minZ; j < minZ + 2; j++) {
                int x = heatSource.getTileX() + i;
                int y = heatSource.getTileY() + 1;
                int z = heatSource.getTileZ() + j;
                list.add(new BlockCoord(x, y, z));
            }
        }

        return list;
    }

}
