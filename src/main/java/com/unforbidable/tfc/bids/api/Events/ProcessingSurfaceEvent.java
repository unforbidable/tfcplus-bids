package com.unforbidable.tfc.bids.api.Events;

import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class ProcessingSurfaceEvent extends Event {

    public final TileEntityProcessingSurface tileEntity;
    public final ItemStack input;
    public final ItemStack result;
    public final ItemStack tool;
    public final EntityPlayer player;
    public final float effort;

    public ProcessingSurfaceEvent(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float effort) {
        this.tileEntity = tileEntity;
        this.input = input;
        this.result = result;
        this.tool = tool;
        this.player = player;
        this.effort = effort;
    }

    public static class ToolEfficiencyCheck extends ProcessingSurfaceEvent {

        public final float originalEfficiency;
        public float newEfficiency;

        public ToolEfficiencyCheck(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float effort, float originalEfficiency) {
            super(tileEntity, input, result, tool, player, effort);

            this.originalEfficiency = originalEfficiency;
            newEfficiency = originalEfficiency;
        }
    }

    public static class Progress extends ProcessingSurfaceEvent {

        public final float progress;

        public Progress(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float effort, float progress) {
            super(tileEntity, input, result, tool, player, effort);

            this.progress = progress;
        }
    }

}
