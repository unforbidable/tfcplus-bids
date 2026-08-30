package com.unforbidable.tfc.bids.api.features.woodworking;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class WoodworkingToolEvent extends Event {

    public final EntityPlayer player;
    public final ItemStack tool;
    public final WoodworkingAction action;

    public WoodworkingToolEvent(EntityPlayer player, ItemStack tool, WoodworkingAction action) {
        this.player = player;
        this.tool = tool;
        this.action = action;
    }

    public static class Damage extends WoodworkingToolEvent {

        public final float initialDamage;
        public float newDamage;

        public Damage(EntityPlayer player, ItemStack tool, WoodworkingAction action, float initialDamage) {
            super(player, tool, action);
            this.initialDamage = initialDamage;
        }

    }

}
