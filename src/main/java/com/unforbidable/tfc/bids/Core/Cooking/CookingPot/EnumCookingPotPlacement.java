package com.unforbidable.tfc.bids.Core.Cooking.CookingPot;

import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.Placements.Ground;
import com.unforbidable.tfc.bids.Core.Cooking.CookingPot.Placements.FirepitEdge;
import net.minecraftforge.common.util.ForgeDirection;

public enum EnumCookingPotPlacement {

    GROUND(new Ground()),
    FIREPIT_EDGE_WEST(new FirepitEdge(ForgeDirection.WEST)),
    FIREPIT_EDGE_EAST(new FirepitEdge(ForgeDirection.EAST)),
    FIREPIT_EDGE_NORTH(new FirepitEdge(ForgeDirection.NORTH)),
    FIREPIT_EDGE_SOUTH(new FirepitEdge(ForgeDirection.SOUTH));

    private final CookingPotPlacement placement;

    EnumCookingPotPlacement(CookingPotPlacement placement) {
        this.placement = placement;
    }

    public static EnumCookingPotPlacement getFirepitEdgePlacementForDirection(ForgeDirection direction) {
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

    public CookingPotPlacement getPlacement() {
        return placement;
    }

}
