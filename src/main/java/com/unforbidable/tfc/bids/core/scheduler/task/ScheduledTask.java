package com.unforbidable.tfc.bids.core.scheduler.task;

import com.unforbidable.tfc.bids.util.Timer;
import java.util.function.Consumer;

public class ScheduledTask<T> {

    public final Timer timer;
    public final Consumer<T> consumer;

    public ScheduledTask(int ticks, Consumer<T> consumer) {
        this.timer = ticks > 0 ? new Timer(ticks) : null;
        this.consumer = consumer;
    }

}
