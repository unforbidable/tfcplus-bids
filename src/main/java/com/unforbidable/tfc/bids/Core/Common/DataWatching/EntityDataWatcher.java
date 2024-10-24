package com.unforbidable.tfc.bids.Core.Common.DataWatching;

import net.minecraft.entity.Entity;

public abstract class EntityDataWatcher {

    private final Entity entity;
    private final int id;

    protected EntityDataWatcher(Entity entity, int id) {
        this.entity = entity;
        this.id = id;
    }

    public void init() {
        entity.getDataWatcher().addObject(id, 0);
    }

    protected void set(int value) {
        entity.getDataWatcher().updateObject(id, value);
    }

    protected int get() {
       return entity.getDataWatcher().getWatchableObjectInt(id);
    }

    protected void setFlag(int flag, boolean value) {
        int original = get();
        if (value) {
            set(original | flag);
        } else {
            set(original & ~flag);
        }
    }

    protected boolean hasFlag(int flag) {
        return (get() & flag) != 0;
    }

}
