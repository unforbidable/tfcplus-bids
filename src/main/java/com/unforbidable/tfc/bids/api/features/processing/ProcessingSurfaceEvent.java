package com.unforbidable.tfc.bids.api.features.processing;

import com.unforbidable.tfc.bids.features.device.processingsurface.tileentity.TileEntityProcessingSurface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class ProcessingSurfaceEvent extends ProcessingEvent {

    public final TileEntityProcessingSurface tileEntity;
    public final ItemStack tool;

    public ProcessingSurfaceEvent(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player) {
        super(input, result, player);
        this.tileEntity = tileEntity;
        this.tool = tool;
    }

    public static class ToolEfficiencyCheck extends ProcessingSurfaceEvent {

        public final float originalEfficiency;
        public float newEfficiency;

        public ToolEfficiencyCheck(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float originalEfficiency) {
            super(tileEntity, input, result, tool, player);

            this.originalEfficiency = originalEfficiency;
            newEfficiency = originalEfficiency;
        }
    }

    public static class EffortCheck extends ProcessingSurfaceEvent {

        public final float originalEffort;
        public float newEffort;

        public EffortCheck(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float originalEffort) {
            super(tileEntity, input, result, tool, player);

            this.originalEffort = originalEffort;
            newEffort = originalEffort;
        }

    }

    public static class Progress extends ProcessingSurfaceEvent {

        public final float progress;
        public final float effort;

        public Progress(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float progress, float effort) {
            super(tileEntity, input, result, tool, player);

            this.progress = progress;
            this.effort = effort;
        }
    }

}
