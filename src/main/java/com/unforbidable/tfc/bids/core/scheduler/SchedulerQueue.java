package com.unforbidable.tfc.bids.core.scheduler;

import com.unforbidable.tfc.bids.core.scheduler.task.ScheduledTask;
import java.util.ArrayDeque;
import java.util.Queue;

public class SchedulerQueue<T> {

    private final Queue<ScheduledTask<T>> queue = new ArrayDeque<>();

    public void schedule(ScheduledTask<T> task) {
        queue.add(task);
    }

    @SuppressWarnings("ConstantConditions")
    public void tick(T context) {
        int size = queue.size();
        for (int i = 0; i < size; i++) {
            ScheduledTask<T> task = queue.poll();
            if (task.timer == null || task.timer.tick()) {
                task.consumer.accept(context);
            } else {
                queue.add(task);
            }
        }
    }

}
