package com.unforbidable.tfc.bids.features.crafting.firestarting.main;

import com.unforbidable.tfc.bids.util.EntityScanner;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

public class FireStartingEntityScanner extends EntityScanner {

    private boolean tinderChecked;
    private EntityItem tinder;

    public FireStartingEntityScanner(World world, int x, int y, int z) {
        super(world, x, y, z);
    }

    public EntityItem getTinder() {
        if (!tinderChecked) {
            tinder = getEntities().stream()
                .filter(e -> FireStartingHelper.isTinder(e.getEntityItem()))
                .findFirst().orElse(null);
            tinderChecked = true;
        }

        return tinder;
    }

    public boolean hasTinder() {
        return getTinder() != null;
    }

    public TinderQuality getTinderQuality() {
        return hasTinder() ? FireStartingHelper.getTinderQuality(getTinder().getEntityItem()) : TinderQuality.NONE;
    }

}
