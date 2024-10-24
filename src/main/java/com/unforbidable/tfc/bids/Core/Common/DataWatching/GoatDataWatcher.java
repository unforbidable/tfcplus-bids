package com.unforbidable.tfc.bids.Core.Common.DataWatching;

import net.minecraft.entity.Entity;

public class GoatDataWatcher extends EntityDataWatcher {

    private static final int DATA_WATCHER_ID = 25;

    private static final int FLAG_CAN_MILK = 0x1;

    public GoatDataWatcher(Entity entity) {
        super(entity, DATA_WATCHER_ID);
    }

    public void setCanMilk(boolean value) {
        setFlag(FLAG_CAN_MILK, value);
    }

    public boolean getCanMilk() {
        return hasFlag(FLAG_CAN_MILK);
    }

}
