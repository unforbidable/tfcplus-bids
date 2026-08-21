package com.unforbidable.tfc.bids.core.features.init.entity;


import net.minecraft.entity.Entity;

public class EntitySpec {

    public final String name;
    public final Class<? extends Entity> type;

    public EntitySpec(String name, Class<? extends Entity> type) {
        this.name = name;
        this.type = type;
    }

}
