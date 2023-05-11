package com.unforbidable.tfc.bids.Core.Common.Bounds;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class WallHookBounds {

    AxisAlignedBB entire;

    AxisAlignedBB[] hooks = new AxisAlignedBB[2];
    Vec3 item;

    public static WallHookBounds getBoundsForOrientation(int orientation) {
        return new WallHookBounds(orientation, 1f);
    }

    public static WallHookBounds getBoundsScaledAndCentered(float scale) {
        return new WallHookBounds(-1, scale);
    }

    WallHookBounds(int orientation, float scale) {
        WallHookDimensions dims = new WallHookDimensions(scale);

        float hookStartXZ = dims.getHookPosXZ() - dims.getHookWidth() / 2;
        float hookStartY = dims.getHookPosY() - dims.getHookWidth() / 2;

        if (orientation == 1) {
            hooks[0] = AxisAlignedBB.getBoundingBox(0, hookStartY, hookStartXZ, dims.getHookLength(), hookStartY + dims.getHookWidth(), hookStartXZ + dims.getHookWidth());
            hooks[1] = AxisAlignedBB.getBoundingBox(dims.getHookLength(), hookStartY, hookStartXZ, dims.getHookLength() + dims.getHookWidth(), hookStartY + dims.getHookWidth() + dims.getHookHeight(), hookStartXZ + dims.getHookWidth());

            entire = AxisAlignedBB.getBoundingBox(0, 0, 0, dims.getHookLength() + dims.getHookWidth(), 1, 1);

            item = Vec3.createVectorHelper(0 + dims.getItemOffsetX(), hookStartY + dims.getItemOffsetY(), hookStartXZ + dims.getItemOffsetZ());
        } else if (orientation == 3) {
            hooks[0] = AxisAlignedBB.getBoundingBox(1 - dims.getHookLength(), hookStartY, hookStartXZ, 1, hookStartY + dims.getHookWidth(), hookStartXZ + dims.getHookWidth());
            hooks[1] = AxisAlignedBB.getBoundingBox(1 - dims.getHookLength() - dims.getHookWidth(), hookStartY, hookStartXZ, 1 - dims.getHookLength(), hookStartY + dims.getHookWidth() + dims.getHookHeight(), hookStartXZ + dims.getHookWidth());

            entire = AxisAlignedBB.getBoundingBox(1 - dims.getHookLength() - dims.getHookWidth(), 0, 0, 1, 1, 1);

            item = Vec3.createVectorHelper(1 - dims.getItemOffsetX(), hookStartY + dims.getItemOffsetY(), hookStartXZ + dims.getItemOffsetZ());
        } else if (orientation == 2) {
            hooks[0] = AxisAlignedBB.getBoundingBox(hookStartXZ, hookStartY, 0, hookStartXZ + dims.getHookWidth(), hookStartY + dims.getHookWidth(), dims.getHookLength());
            hooks[1] = AxisAlignedBB.getBoundingBox(hookStartXZ, hookStartY, dims.getHookLength(), hookStartXZ + dims.getHookWidth(), hookStartY + dims.getHookWidth() + dims.getHookHeight(), dims.getHookLength() + dims.getHookWidth());

            entire = AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, dims.getHookLength() + dims.getHookWidth());

            item = Vec3.createVectorHelper(hookStartXZ + dims.getItemOffsetZ(), hookStartY + dims.getItemOffsetY(), 0 + dims.getItemOffsetX());
        } else if (orientation == 0) {
            hooks[0] = AxisAlignedBB.getBoundingBox(hookStartXZ, hookStartY, 1 - dims.getHookLength(), hookStartXZ + dims.getHookWidth(), hookStartY + dims.getHookWidth(), 1);
            hooks[1] = AxisAlignedBB.getBoundingBox(hookStartXZ, hookStartY, 1 - dims.getHookLength() - dims.getHookWidth(), hookStartXZ + dims.getHookWidth(), hookStartY + dims.getHookWidth() + dims.getHookHeight(), 1 - dims.getHookLength());

            entire = AxisAlignedBB.getBoundingBox(0, 0, 1 - dims.getHookLength() - dims.getHookWidth(), 1, 1, 1);

            item = Vec3.createVectorHelper(hookStartXZ + dims.getItemOffsetZ(), hookStartY + dims.getItemOffsetY(), 1 - dims.getItemOffsetX());
        } else if (orientation == -1) {
            float centerOffsetZ = 0.5f - dims.getHookLength();
            float centerOffsetY = -0.25f;

            hooks[0] = AxisAlignedBB.getBoundingBox(hookStartXZ, hookStartY + centerOffsetY, 0 + centerOffsetZ, hookStartXZ + dims.getHookWidth(), hookStartY + dims.getHookWidth() + centerOffsetY, dims.getHookLength() + centerOffsetZ);
            hooks[1] = AxisAlignedBB.getBoundingBox(hookStartXZ, hookStartY + centerOffsetY, dims.getHookLength() + centerOffsetZ, hookStartXZ + dims.getHookWidth(), hookStartY + dims.getHookWidth() + dims.getHookHeight() + centerOffsetY, dims.getHookLength() + dims.getHookWidth() + centerOffsetZ);

            entire = AxisAlignedBB.getBoundingBox(hookStartXZ, hookStartY + centerOffsetY, 0, hookStartXZ + dims.getHookWidth(), hookStartY + dims.getHookWidth() + dims.getHookHeight() + centerOffsetY, 1);

            item = Vec3.createVectorHelper(hookStartXZ + dims.getItemOffsetZ(), hookStartY + dims.getItemOffsetY() + centerOffsetY, dims.getItemOffsetX() + centerOffsetZ);
        }
    }

    public AxisAlignedBB getEntireBounds() {
        return entire;
    }

    public AxisAlignedBB[] getHooksBounds() {
        return hooks;
    }

    public Vec3 getItemPos() {
        return item;
    }

    static class WallHookDimensions {
        private static final float hookPosXZ = 8 / 16f;
        private static final float hookPosY = 10 / 16f;
        private static final float hookWidth = 1 / 16f;
        private static final float hookLength = 1 / 32f;
        private static final float hookHeight = 2 / 16f;
        private static final float itemOffsetX = 2 / 128f;
        private static final float itemOffsetZ = 4 / 128f;
        private static final float itemOffsetY = -12 / 128f;

        private final float scale;

        public WallHookDimensions(float scale) {
            this.scale = scale;
        }

        public float getHookPosXZ() {
            return hookPosXZ;
        }

        public float getHookPosY() {
            return hookPosY;
        }

        public float getHookWidth() {
            return hookWidth * scale;
        }

        public float getHookLength() {
            return hookLength * scale;
        }

        public float getHookHeight() {
            return hookHeight * scale;
        }

        public float getItemOffsetX() {
            return itemOffsetX;
        }

        public float getItemOffsetZ() {
            return itemOffsetZ;
        }

        public float getItemOffsetY() {
            return itemOffsetY;
        }

    }

}
