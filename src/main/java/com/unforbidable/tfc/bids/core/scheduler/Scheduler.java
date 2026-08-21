package com.unforbidable.tfc.bids.core.scheduler;

import com.unforbidable.tfc.bids.core.scheduler.task.EmptyContext;
import com.unforbidable.tfc.bids.core.scheduler.task.PlayerContext;
import com.unforbidable.tfc.bids.core.scheduler.task.ScheduledTask;

public class Scheduler {

    private static final EmptyContext emptyContext = new EmptyContext();

    private static final SchedulerQueue<EmptyContext> server = new SchedulerQueue<>();
    private static final SchedulerQueue<EmptyContext> client = new SchedulerQueue<>();
    private static final SchedulerQueue<PlayerContext> player = new SchedulerQueue<>();

    public static void server(Runnable runnable, int ticks) {
        server.schedule(new ScheduledTask<>(ticks, ctx -> runnable.run()));
    }

    public static void server(Runnable runnable) {
        server.schedule(new ScheduledTask<>(0, ctx -> runnable.run()));
    }

    static void serverTick() {
        server.tick(emptyContext);
    }

}
