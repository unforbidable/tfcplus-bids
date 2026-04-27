package com.unforbidable.tfc.bids.Core.Drying;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemClothing;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Drying.Environment.ItemEnvironment;
import com.unforbidable.tfc.bids.Core.Drying.Environment.StaticEnvironment;
import com.unforbidable.tfc.bids.Core.Drying.Environment.DynamicEnvironment;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import com.unforbidable.tfc.bids.api.BidsOptions;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Interfaces.IDryingFoodRecipe;
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
            if (item != null) {
                updateForItem(ticks, item, dynamicEnvironment);
            }
        }
    }

    private void updateForItem(long ticks, DryingItem dryingItem, DynamicEnvironment dynamicEnvironment) {
        long ticksElapsed = ticks - dryingItem.lastProgressUpdatedTicks;

        dryingItem.lastProgressUpdatedTicks = ticks;

        // Update item wetness
        updateItemWetness(dryingItem, dynamicEnvironment, ticksElapsed);

        // Clothes do not get processed
        if (!(dryingItem.inputItem.getItem() instanceof ItemClothing)) {
            DryingRecipe recipe = host.getDryingRecipe(dryingItem);

            if (recipe != null) {
                ItemEnvironment itemEnvironment = dynamicEnvironment.ofItem(dryingItem);
                updateItemProgress(dryingItem, recipe, itemEnvironment, ticksElapsed);
            }
        }
    }

    private void updateItemProgress(DryingItem dryingItem, DryingRecipe recipe, ItemEnvironment env, long ticksElapsed) {
        boolean isComplete = dryingItem.progress == 1 || dryingItem.failure == 1;

        // check for failure
        // unless complete
        float failure = isComplete ? 0 : env.getRecipeFailure(recipe);
        if (dryingItem.failure != failure) {
            dryingItem.failure = failure;
            dryingItem.finishedTicks = 0;
            dryingItem.smokedTicks = 0;

            if (dryingItem.failure == 1) {
                dryingItem.resultItem = DryingHelper.getDestroyedResultItem(dryingItem, recipe);
                dryingItem.progress = 0;
                dryingItem.smoke = 0;

                itemChanged = true;
            }

            dataChanged = true;
        } else {
            // check for match, even partial
            // unless complete
            float match = isComplete ? 0 : env.getRecipeMatch(recipe);
            if (match > 0) {
                // reset any failure
                if (dryingItem.failure > 0) {
                    dryingItem.failure = 0;

                    dataChanged = true;
                }

                // progress gain depends on how close the environment match is
                long ticksRequiredTotal = (long) (recipe.getDuration() * TFC_Time.HOUR_LENGTH * BidsOptions.Crafting.dryingDurationMultiplier);
                float progressForIdealMatch = ticksElapsed / (float) ticksRequiredTotal;
                float progressToAdd = progressForIdealMatch * match;

                float prevProgress = dryingItem.progress;
                dryingItem.progress = Math.min(dryingItem.progress + progressToAdd, 1);

                long prevFinishedTicks = dryingItem.finishedTicks;
                long ticksRemaining = (long) ((1 - dryingItem.progress) * ticksRequiredTotal / match);
                dryingItem.finishedTicks = dryingItem.lastProgressUpdatedTicks + ticksRemaining;

                DryingHelper.applyInputItemProgress(dryingItem, recipe);

                if (dryingItem.progress == 1) {
                    dryingItem.finishedTicks = 0;

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
                    dataChanged = true;
                } else {
                    dataChanged = prevFinishedTicks == 0 ||
                        prevFinishedTicks / 1000 != dryingItem.finishedTicks / 1000 ||
                        Math.floor(dryingItem.progress * 100) != Math.floor(prevProgress * 100);
                }
            } else {
                if (dryingItem.finishedTicks != 0) {
                    dryingItem.finishedTicks = 0;

                    dataChanged = true;
                }
            }

            // check if smoking is possible and not complete
            if (recipe instanceof IDryingFoodRecipe && ((IDryingFoodRecipe) recipe).isAllowSmoke() && dryingItem.smoke < 1) {
                if (env.isSmoked()) {
                    // smoke gain is constant and does not depend on recipe match
                    long ticksRequiredTotal = (long) (((IDryingFoodRecipe) recipe).getSmokeDuration() * TFC_Time.HOUR_LENGTH * BidsOptions.Crafting.smokingDurationMultiplier);
                    float smokeToAdd = ticksElapsed / (float) ticksRequiredTotal;

                    float prevSmoke = dryingItem.smoke;
                    dryingItem.smoke = Math.min(dryingItem.smoke + smokeToAdd, 1);

                    long prevSmokedTicks = dryingItem.smokedTicks;
                    long ticksRemaining = (long) ((1 - dryingItem.smoke) * ticksRequiredTotal);
                    dryingItem.smokedTicks = dryingItem.lastProgressUpdatedTicks + ticksRemaining;

                    DryingHelper.applyInputItemSmoke(dryingItem, recipe, env.getFuelTasteProfile());

                    if (dryingItem.smoke == 1) {
                        dryingItem.smokedTicks = 0;

                        dryingItem.resultItem = DryingHelper.getResultItem(dryingItem, recipe);

                        itemChanged = true;
                        dataChanged = true;
                    } else {
                        dataChanged = prevSmokedTicks == 0 ||
                            prevSmokedTicks / 1000 != dryingItem.smokedTicks / 1000 ||
                            Math.floor(dryingItem.smoke * 100) != Math.floor(prevSmoke * 100);
                    }
                } else {
                    if (dryingItem.smokedTicks != 0) {
                        dryingItem.smokedTicks = 0;

                        dataChanged = true;
                    }
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
