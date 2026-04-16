package com.unforbidable.tfc.bids.Core.Drying;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemClothing;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drying.Environment.ItemEnvironment;
import com.unforbidable.tfc.bids.Core.Drying.Environment.StaticEnvironment;
import com.unforbidable.tfc.bids.Core.Drying.Environment.DynamicEnvironment;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Registry.WetnessInfo;
import net.minecraft.tileentity.TileEntity;

public class DryingEngine {

    private final IDryingHost host;

    public boolean dataChanged;
    public boolean itemChanged;

    public DryingEngine(IDryingHost host) {
        this.host = host;
    }

    public void update() {
        dataChanged = false;
        itemChanged = false;

        TileEntity te = (TileEntity) host;
        StaticEnvironment staticEnvironment = new StaticEnvironment(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);

        long totalTicks = TFC_Time.getTotalTicks();

        long lastDryingTicks = getLastDryingTickFromIncompleteItems();
        if (lastDryingTicks != -1) {
            if (totalTicks - lastDryingTicks > TFC_Time.HOUR_LENGTH + 250) {
                while (lastDryingTicks != -1 && totalTicks > lastDryingTicks) {
                    long ticksRemaining = totalTicks - lastDryingTicks;
                    long partialTicks = getPartialTickChunkSize(ticksRemaining);
                    Bids.LOG.debug("ticks to catch up: " + ticksRemaining + " + " + partialTicks);

                    updateForTicks(lastDryingTicks + partialTicks, staticEnvironment);

                    // This also makes sure there are items that still need catching up
                    lastDryingTicks = getLastDryingTickFromIncompleteItems();
                }

                if (lastDryingTicks != -1) {
                    updateForTicks(totalTicks, staticEnvironment);
                }
            } else {
                updateForTicks(totalTicks, staticEnvironment);
            }
        }

        if (dataChanged) {
            te.getWorldObj().markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
        }

        if (itemChanged) {
            host.notifyClientChanges();
        }
    }

    private long getPartialTickChunkSize(long remaining) {
        if (remaining > TFC_Time.DAY_LENGTH * 2) {
            return TFC_Time.DAY_LENGTH;
        } else if (remaining > TFC_Time.DAY_LENGTH) {
            return TFC_Time.HOUR_LENGTH * 12;
        } else if (remaining > TFC_Time.HOUR_LENGTH * 8) {
            return TFC_Time.HOUR_LENGTH * 4;
        } else {
            return TFC_Time.HOUR_LENGTH;
        }
    }

    private long getLastDryingTickFromIncompleteItems() {
        for (DryingItem item : host.getDryingStorage()) {
            if (item != null && item.progress < 1 && item.failure < 1) {
                return item.lastProgressUpdatedTicks;
            }
        }

        return -1;
    }

    private void updateForTicks(long ticks, StaticEnvironment staticEnvironment) {
        DynamicEnvironment dynamicEnvironment = staticEnvironment.ofTicks(ticks);

        for (DryingItem item : host.getDryingStorage()) {
            if (item != null && item.progress < 1 && item.failure < 1) {
                updateForItem(ticks, item, dynamicEnvironment);
            }
        }
    }

    private void updateForItem(long ticks, DryingItem dryingItem, DynamicEnvironment dynamicEnvironment) {
        long ticksElapsed = ticks - dryingItem.lastProgressUpdatedTicks;

        // Update item wetness
        updateItemWetness(dryingItem, dynamicEnvironment, ticksElapsed);

        // Clothes do not get processed
        if (!(dryingItem.inputItem.getItem() instanceof ItemClothing)) {
            DryingRecipe recipe = host.getDryingRecipe(dryingItem);

            if (recipe != null) {
                ItemEnvironment itemEnvironment = dynamicEnvironment.ofItem(dryingItem);

                // Update item progress
                updateItemProgress(dryingItem, recipe, itemEnvironment, ticksElapsed);
            }
        }

        dryingItem.lastProgressUpdatedTicks = ticks;
    }

    private void updateItemProgress(DryingItem dryingItem, DryingRecipe recipe, ItemEnvironment env, long ticksElapsed) {
        // check for failure
        float failure = env.getRecipeFailure(recipe);
        if (dryingItem.failure != failure) {
            dryingItem.failure = failure;

            if (dryingItem.failure == 1) {
                dryingItem.resultItem = DryingHelper.getDestroyedResultItem(dryingItem, recipe);
                dryingItem.paused = false;
                dryingItem.progress = 0;

                itemChanged = true;
            } else {
                dryingItem.paused = true;
            }

            dataChanged = true;
        } else {
            // check for match, even partial
            float match = env.getRecipeMatch(recipe);
            if (match > 0) {
                // progress gain depends on how close the environment match is
                long ticksRequiredTotal = recipe.getDuration() * TFC_Time.HOUR_LENGTH;
                float progressForIdealMatch = ticksElapsed / (float) ticksRequiredTotal;
                float progressToAdd = progressForIdealMatch * match;
                dryingItem.progress = Math.min(dryingItem.progress + progressToAdd, 1);
                dryingItem.paused = false;
                dryingItem.failure = 0;

                DryingHelper.applyInputItemProgress(dryingItem, recipe);

                if (dryingItem.progress == 1) {
                    dryingItem.resultItem = DryingHelper.getResultItem(dryingItem, recipe);

                    if (dryingItem.resultItem != null) {
                        DryingItem nextDryingItem = new DryingItem();
                        nextDryingItem.inputItem = dryingItem.resultItem;

                        DryingRecipe nextRecipe = host.getDryingRecipe(nextDryingItem);
                        if (nextRecipe != null) {
                            if (BidsEventFactory.onDryingItemNextRecipeSelected((TileEntity) host, dryingItem, recipe, nextRecipe)) {
                                dryingItem.inputItem = dryingItem.resultItem;
                                DryingHelper.initializeInputItem(dryingItem, nextRecipe);
                            }
                        }
                    }

                    itemChanged = true;
                }

                dataChanged = true;
            } else {
                // pause progress unless total failure
                if (!dryingItem.paused && dryingItem.failure != 1) {
                    dryingItem.paused = true;
                    dataChanged = true;
                }
            }
        }
    }

    private void updateItemWetness(DryingItem dryingItem, DynamicEnvironment env, long ticksElapsed) {
        WetnessInfo wetnessInfo = DryingHelper.getWetnessInfo(dryingItem.inputItem);

        if (wetnessInfo != null && wetnessInfo.capacity > 0) {
            // item is able to get wet in rain
            if (env.getPrecipitation() > 0 && env.getTemperature() > 0 && env.isExposed() && dryingItem.wetness < 1) {
                // when raining on block, item gets wet
                float wetness = wetnessInfo.capacity * dryingItem.wetness;
                float wetnessToAdd = env.getPrecipitation() * ticksElapsed * wetnessInfo.rate * host.getWetnessIncreaseRate();
                float wetnessAdded = Math.min(wetness + wetnessToAdd, wetnessInfo.capacity);
                dryingItem.wetness = wetnessAdded / wetnessInfo.capacity;

                DryingHelper.applyInputItemWetness(dryingItem);

                dataChanged = true;
            } else if (env.getHumidity() < 1 && env.getAirflow() > 0 && dryingItem.wetness > 0) {
                // when humidity and airflow allows, wet item can dry off
                float wetness = wetnessInfo.capacity * dryingItem.wetness;
                float wetnessToRemove = (1 - env.getHumidity()) * env.getAirflow() * ticksElapsed * host.getWetnessReductionRate();
                float wetnessRemoved = Math.max(wetness - wetnessToRemove, 0);
                dryingItem.wetness = wetnessRemoved / wetnessInfo.capacity;

                DryingHelper.applyInputItemWetness(dryingItem);

                dataChanged = true;
            }
        }
    }

}
