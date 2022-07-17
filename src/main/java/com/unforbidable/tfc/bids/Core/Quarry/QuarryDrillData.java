package com.unforbidable.tfc.bids.Core.Quarry;

public class QuarryDrillData {

    final int duration;
    final QuarryDrillTarget target;

    public QuarryDrillData(int duration, QuarryDrillTarget target) {
        super();
        this.duration = duration;
        this.target = target;
    }

    public int getDuration() {
        return duration;
    }

    public QuarryDrillTarget getTarget() {
        return target;
    }

}
