package com.unforbidable.tfc.bids.Core.Woodworking.Plans;

import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingPlan;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingShape;
import net.minecraft.item.ItemStack;

import java.awt.Polygon;
import java.awt.geom.Area;

public class PlanInstance {

    private final String name;
    private final IWoodworkingPlan plan;
    private final ItemStack result;

    private Area cutout;

    public PlanInstance(String name, IWoodworkingPlan plan, ItemStack result) {
        this.name = name;
        this.plan = plan;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public ItemStack getResult() {
        return result;
    }

    public Area getCutout() {
        if (cutout != null) {
            return cutout;
        }

        cutout = new Area();
        for (IWoodworkingShape shape : plan.getCutoutShapes()) {
            Polygon polygon = shape.getPolygon();
            cutout.add(new Area(polygon));
        }

        return cutout;
    }

}
