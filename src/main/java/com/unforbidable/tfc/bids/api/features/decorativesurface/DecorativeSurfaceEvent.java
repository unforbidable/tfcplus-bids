package com.unforbidable.tfc.bids.api.features.decorativesurface;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class DecorativeSurfaceEvent extends Event {

    public final ItemStack item;

    protected DecorativeSurfaceEvent(ItemStack item) {
        this.item = item;
    }

    @Cancelable
    public static final class Place extends DecorativeSurfaceEvent {

        public final EntityPlayer player;
        public final World world;
        public final int x;
        public final int y;
        public final int z;
        public final int face;
        public boolean canPlace;

        public Place(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int face) {
            super(item);
            this.player = player;
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.face = face;
        }

    }

}
