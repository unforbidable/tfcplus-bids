package com.unforbidable.tfc.bids.features.device.cookingpot.main;

import com.unforbidable.tfc.bids.features.device.cookingpot.main.placements.FirepitEdge;
import com.unforbidable.tfc.bids.features.device.cookingpot.main.placements.Ground;
import net.minecraftforge.common.util.ForgeDirection;

public enum CookingPotPlacement {

    GROUND(new Ground()),
    FIREPIT_EDGE_WEST(new FirepitEdge(ForgeDirection.WEST)),
    FIREPIT_EDGE_EAST(new FirepitEdge(ForgeDirection.EAST)),
    FIREPIT_EDGE_NORTH(new FirepitEdge(ForgeDirection.NORTH)),
    FIREPIT_EDGE_SOUTH(new FirepitEdge(ForgeDirection.SOUTH));

    private final CookingPotPlacementSpec placement;

    CookingPotPlacement(CookingPotPlacementSpec placement) {
        this.placement = placement;
    }

    public static CookingPotPlacement getFirepitEdgePlacementForDirection(ForgeDirection direction) {
        switch (direction) {
            case WEST:
                return FIREPIT_EDGE_WEST;
            case EAST:
                return FIREPIT_EDGE_EAST;
            case NORTH:
                return FIREPIT_EDGE_NORTH;
            case SOUTH:
                return FIREPIT_EDGE_SOUTH;
        }

        return GROUND;
    }

    public CookingPotPlacementSpec getPlacement() {
        return placement;
    }

    public boolean isFirepitEdgePlacement() {
        return this == CookingPotPlacement.FIREPIT_EDGE_EAST ||
            this == CookingPotPlacement.FIREPIT_EDGE_WEST ||
            this == CookingPotPlacement.FIREPIT_EDGE_NORTH ||
            this == CookingPotPlacement.FIREPIT_EDGE_SOUTH;
    }

}
