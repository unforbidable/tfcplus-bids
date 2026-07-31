package com.unforbidable.tfc.bids.features.device.firepit.main;

import com.unforbidable.tfc.bids.util.EntityScanner;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

public class FirepitFuelEntityScanner extends EntityScanner {

    protected List<EntityItem> fuel;

    public FirepitFuelEntityScanner(World world, int x, int y, int z) {
        super(world, x, y, z);
    }

    public List<EntityItem> getFuelEntities() {
        if (fuel == null) {
            // Kindling first
            fuel = getEntities().stream()
                .sorted(Comparator.comparing(e -> !FirepitHelper.isKindling(e.getEntityItem())))
                .collect(Collectors.toList());
        }

        return fuel;
    }

}
