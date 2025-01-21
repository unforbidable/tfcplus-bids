package com.unforbidable.tfc.bids.api.Events;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class AnimalMilkEvent extends Event {

    public final EntityPlayer player;
    public final Entity entity;

    public AnimalMilkEvent(EntityPlayer player, Entity entity) {
        this.player = player;
        this.entity = entity;
    }

    public static class Check extends AnimalMilkEvent {

        public Fluid fluid;

        public Check(EntityPlayer player, Entity entity) {
            super(player, entity);
        }

    }

    @Cancelable
    public static class Milking extends AnimalMilkEvent {

        public final int amount;

        public Milking(EntityPlayer player, Entity entity, int amount) {
            super(player, entity);
            this.amount = amount;
        }
    }

    public static class Milked extends AnimalMilkEvent {

        public final FluidStack result;

        public Milked(EntityPlayer player, Entity entity, FluidStack result) {
            super(player, entity);
            this.result = result;
        }
    }

}
