package com.unforbidable.tfc.bids.Core.Cooking.CookingPot.Placements;

import com.dunk.tfc.TileEntities.TEFirepit;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.CookingPotPlacement;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FirepitEdge extends CookingPotPlacement {

    private final ForgeDirection direction;

    public FirepitEdge(ForgeDirection direction) {
        this.direction = direction;
    }

    @Override
    public EnumCookingHeatLevel getHeatLevel(World world, int xCoord, int yCoord, int zCoord) {
        TileEntity te = world.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        if (te instanceof TEFirepit) {
            TEFirepit teFirepit = (TEFirepit) te;
            if (teFirepit.fireTemp > 500) {
                // Any logs except Baobab, Bamboo and Palm
                return EnumCookingHeatLevel.MEDIUM;
            } else if (teFirepit.fireTemp > 200) {
                // Any fuel including sticks
                return EnumCookingHeatLevel.LOW;
            }
        }

        // No lit firepit
        return EnumCookingHeatLevel.NONE;
    }

    @Override
    public Vec3 getOffset() {
        return Vec3.createVectorHelper(0.28 * direction.offsetX, direction.offsetY, 0.28 * direction.offsetZ);
    }

    @Override
    public ForgeDirection getDirection() {
        return direction;
    }

}
