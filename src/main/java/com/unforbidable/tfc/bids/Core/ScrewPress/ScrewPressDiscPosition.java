package com.unforbidable.tfc.bids.Core.ScrewPress;

public class ScrewPressDiscPosition {

    private static final float POSITION_INACTIVE = -1f;

    private static final float NUT_OFF = 2.05f;
    private static final float NUT_TOP = 1.85f;
    private static final float NUT_BOTTOM = 0.05f;

    private static final float PIVOT_BOLT_HEIGHT = 0.8f;
    private static final float PIVOT_TO_SCREW_DISTANCE = 4f;
    private static final float PIVOT_TO_HOLDING_BOLT_DISTANCE = 2f;
    private static final float PIVOT_TO_DISC_DISTANCE = 1f;
    private static final float NUT_SPACING = 1f / 32 * 4;
    private static final float HOLDING_BOLT_SPACING = 1f / 32 * 6;
    private static final float DISC_SPACING = 1f / 32 * 2;

    public static final ScrewPressDiscPosition INACTIVE = new ScrewPressDiscPosition(POSITION_INACTIVE);

    private final float position;

    public ScrewPressDiscPosition(float position) {
        this.position = position;
    }

    public float getPosition() {
        return position;
    }

    public boolean isActive() {
        return position != POSITION_INACTIVE;
    }

    public float getLeverAngle() {
        float dy = PIVOT_BOLT_HEIGHT - getNutOffset() + NUT_SPACING;
        return (float) Math.atan(dy / PIVOT_TO_SCREW_DISTANCE);
    }

    public float getNutOffset() {
        if (isActive()) {
            return (NUT_TOP - NUT_BOTTOM) * position + NUT_BOTTOM;
        } else {
            return NUT_OFF;
        }
    }

    public float getDiscOffset() {
        float a = getLeverAngle();
        return (float) -(Math.tan(a) * PIVOT_TO_DISC_DISTANCE) + PIVOT_BOLT_HEIGHT - DISC_SPACING;
    }

    public float getHoldingBoltOffset() {
        float a = getLeverAngle();
        return (float) -(Math.tan(a) * PIVOT_TO_HOLDING_BOLT_DISTANCE) + PIVOT_BOLT_HEIGHT - HOLDING_BOLT_SPACING;
    }

    public static float getPivotBoltHeight() {
        return PIVOT_BOLT_HEIGHT;
    }

}
