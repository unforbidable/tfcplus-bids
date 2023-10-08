package com.unforbidable.tfc.bids.Core.Cooking;

import com.dunk.tfc.Core.TFC_Time;
import net.minecraft.nbt.NBTTagCompound;

public class CookingRecipeProgress {

    private String outputDisplayText;
    private final String outputHashString;
    private float progress = 0f;
    private boolean progressPaused = false;
    private long lastUpdateTicks;
    private final int totalRuns;

    public CookingRecipeProgress(String outputDisplayText, String outputHashString, int totalRuns) {
        this.outputDisplayText = outputDisplayText;
        this.outputHashString = outputHashString;
        this.lastUpdateTicks = TFC_Time.getTotalTicks();
        this.totalRuns = totalRuns;
    }

    private CookingRecipeProgress(String outputDisplayText, String outputHashString, float progress, boolean progressPaused, long lastUpdateTicks, int totalRuns) {
        this.outputDisplayText = outputDisplayText;
        this.outputHashString = outputHashString;
        this.progress = progress;
        this.progressPaused = progressPaused;
        this.lastUpdateTicks = lastUpdateTicks;
        this.totalRuns = totalRuns;
    }

    public void addProgress(float progress) {
        lastUpdateTicks = TFC_Time.getTotalTicks();

        this.progress += progress;

        if (this.progress > 1) {
            this.progress = 1;
        }
    }

    public void setProgressPaused(boolean progressPaused) {
        this.progressPaused = progressPaused;
    }

    public String getOutputDisplayText() {
        return outputDisplayText;
    }

    public void setOutputDisplayText(String outputDisplayText) {
        this.outputDisplayText = outputDisplayText;
    }

    public String getOutputHashString() {
        return outputHashString;
    }

    public float getProgress() {
        return progress;
    }

    public int getProgressRounded() {
        return (int)Math.floor(this.progress * 10) * 10;
    }

    public boolean isProgressPaused() {
        return progressPaused;
    }

    public long getLastUpdateTicks() {
        return lastUpdateTicks;
    }

    public int getTotalRuns() {
        return totalRuns;
    }

    public void writeToNBT(NBTTagCompound tag) {
        tag.setString("outputShort", outputDisplayText);
        tag.setString("outputFull", outputHashString);
        tag.setFloat("progress", progress);
        tag.setBoolean("progressPaused", progressPaused);
        tag.setLong("lastUpdateTicks", lastUpdateTicks);
        tag.setInteger("totalRuns", totalRuns);
    }

    public static CookingRecipeProgress readFromNBT(NBTTagCompound tag) {
        String outputShort = tag.getString("outputShort");
        String outputFull = tag.getString("outputFull");
        float progress = tag.getFloat("progress");
        boolean progressPaused = tag.getBoolean("progressPaused");
        long lastUpdateTicks = tag.getLong("lastUpdateTicks");
        int totalRuns = tag.getInteger("totalRuns");

        return new CookingRecipeProgress(outputShort, outputFull, progress, progressPaused, lastUpdateTicks, totalRuns);
    }

}
