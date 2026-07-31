package com.unforbidable.tfc.bids.util;

import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityScanner {

    protected final World world;
    protected final int x;
    protected final int y;
    protected final int z;
    private List<EntityItem> entityItems;

    public EntityScanner(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public List<EntityItem> getEntities() {
        if (entityItems == null) {
            entityItems = getEntityItemNearBy();
        }

        return entityItems;
    }

    @SuppressWarnings("unchecked")
    private List<EntityItem> getEntityItemNearBy() {
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x, y, z,
            x + 1, y + 1.2, z + 1);
        return (List<EntityItem>) world.getEntitiesWithinAABB(EntityItem.class, bounds);
    }

}
