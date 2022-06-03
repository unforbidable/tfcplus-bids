package com.unforbidable.tfc.bids.Core;

public class Timer {

    int ticksToGo;
    final int defaultInterval;

    public Timer(int defaultInterval) {
        this.defaultInterval = ticksToGo = defaultInterval;
    }

    public int getTicksToGo() {
        return ticksToGo;
    }

    public boolean tick() {
        if (ticksToGo > 0 && --ticksToGo == 0) {
            ticksToGo = defaultInterval;
            return true;
        } else {
            return false;
        }
    }

    public void delay(int delayByTicks) {
        if (defaultInterval == 0) {
            ticksToGo = delayByTicks;
        } else {
            ticksToGo = ticksToGo % defaultInterval + delayByTicks;
        }
    }

}
