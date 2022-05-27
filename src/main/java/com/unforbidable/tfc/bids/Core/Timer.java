package com.unforbidable.tfc.bids.Core;

public class Timer {

    int ticksToGo;
    final int defaultInterval;

    public Timer(int defaultInterval) {
        this.defaultInterval = ticksToGo = defaultInterval;
    }

    public boolean tick() {
        if (--ticksToGo <= 0) {
            ticksToGo = defaultInterval;
            return true;
        } else {
            return false;
        }
    }

    public void delay(int delayByTicks) {
        ticksToGo = ticksToGo % defaultInterval + delayByTicks;
    }

}
