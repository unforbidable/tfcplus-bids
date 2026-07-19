package com.unforbidable.tfc.bids.features.device.cookingpot.main.placements;

import com.dunk.tfc.TileEntities.TEFirepit;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.features.device.cookingpot.main.CookingPotPlacementSpec;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class FirepitEdge extends CookingPotPlacementSpec {

    private final ForgeDirection direction;

    public FirepitEdge(ForgeDirection direction) {
        this.direction = direction;
    }

    @Override
    public CookingHeatLevel getHeatLevel(World world, int xCoord, int yCoord, int zCoord) {
        TileEntity te = world.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        if (te instanceof TEFirepit) {
            TEFirepit teFirepit = (TEFirepit) te;
            if (teFirepit.fireTemp > 500) {
                // Any logs except Baobab, Bamboo and Palm
                return CookingHeatLevel.MEDIUM;
            } else if (teFirepit.fireTemp > 200) {
                // Any fuel including sticks
                return CookingHeatLevel.LOW;
            }
        }

        // No lit firepit
        return CookingHeatLevel.NONE;
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
