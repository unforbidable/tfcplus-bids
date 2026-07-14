package com.unforbidable.tfc.bids.api.features.surfaceitem;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class SurfaceItemEvent extends Event {

    public final ItemStack itemStack;

    public SurfaceItemEvent(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static class Place extends SurfaceItemEvent {

        public final World world;
        public final int x;
        public final int y;
        public final int z;
        public final int face;
        public final float hitX;
        public final float hitY;
        public final float hitZ;
        public final EntityPlayer player;
        public boolean placed = false;

        public Place(ItemStack itemStack, World world, int x, int y, int z, int face, float hitX, float hitY, float hitZ, EntityPlayer player) {
            super(itemStack);
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.face = face;
            this.hitX = hitX;
            this.hitY = hitY;
            this.hitZ = hitZ;
            this.player = player;
        }
    }

    public static class Icon extends SurfaceItemEvent {

        public String iconName;

        public Icon(ItemStack itemStack, String iconName) {
            super(itemStack);
            this.iconName = iconName;
        }

    }

}
