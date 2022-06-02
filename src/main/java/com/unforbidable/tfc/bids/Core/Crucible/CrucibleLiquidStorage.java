package com.unforbidable.tfc.bids.Core.Crucible;

import java.util.ArrayList;
import java.util.List;

import com.dunk.tfc.api.Metal;
import com.unforbidable.tfc.bids.Bids;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CrucibleLiquidStorage {

    int volume = 0;
    float heatCapacity = 0f;
    List<CrucibleLiquidItem> items = new ArrayList<CrucibleLiquidItem>();
    Metal output;

    boolean dirty = true;
    boolean dirtyOutput = true;

    public void readFromNBT(NBTTagCompound tag) {
        items.clear();

        NBTTagList liquidTags = tag.getTagList("LiquidStorage", 10);
        for (int i = 0; i < liquidTags.tagCount(); i++) {
            NBTTagCompound itemTag = liquidTags.getCompoundTagAt(i);
            int id = itemTag.getInteger("ID");
            float volume = itemTag.getFloat("Volume");
            addLiquid(CrucibleHelper.getMetalFromItem(Item.getItemById(id)), volume);
        }

        dirty = true;
    }

    public void writeToNBT(NBTTagCompound tag) {
        NBTTagList liquidTags = new NBTTagList();
        for (int i = 0; i < getItemCount(); i++) {
            CrucibleLiquidItem liquidItem = getItem(i);
            NBTTagCompound itemTag = new NBTTagCompound();
            itemTag.setInteger("ID", Item.getIdFromItem(liquidItem.getMetal().ingot));
            itemTag.setFloat("Volume", liquidItem.getVolume());
            liquidTags.appendTag(itemTag);
        }
        tag.setTag("LiquidStorage", liquidTags);
    }

    private String formatFloat(float value) {
        String formatted = String.format("%.1f", value);
        if (formatted.charAt(formatted.length() - 1) == '0') {
            formatted = formatted.substring(0, formatted.length() - 2);
        }
        return formatted;
    }

    public List<String> getInfo(String componentPrefix) {
        List<String> list = new ArrayList<String>();
        if (items.size() > 0) {
            list.add(getVolume() + " " + getOutputMetal().name);
            if (items.size() > 1) {
                for (CrucibleLiquidItem it : items) {
                    float volumePercent = it.getVolume() / (float) getVolume() * 100;
                    list.add(componentPrefix + formatFloat(it.getVolume()) + " " + it.getMetal().name
                            + " (" + formatFloat(volumePercent) + "%)");
                }
            }
        }
        return list;
    }

    public int getItemCount() {
        return items.size();
    }

    public int getVolume() {
        if (dirty)
            update();

        return volume;
    }

    public void clear() {
        items.clear();
        dirty = true;
    }

    public float getHeatCapacity() {
        if (dirty)
            update();

        return heatCapacity;
    }

    public Metal getOutputMetal() {
        if (dirty)
            update();
        return output;
    }

    public CrucibleLiquidItem getItem(int index) {
        return items.get(index);
    }

    public void addLiquid(Metal metal, float volume) {
        boolean added = false;
        for (CrucibleLiquidItem it : items) {
            if (it.getMetal() == metal) {
                it.setVolume(it.getVolume() + volume);
                added = true;
                break;
            }
        }

        if (!added) {
            items.add(new CrucibleLiquidItem(metal, volume));
        }

        dirtyOutput = true;
        dirty = true;
    }

    public List<CrucibleLiquidItem> removeLiquid(int volumeToRemove) {
        List<CrucibleLiquidItem> removedItems = new ArrayList<CrucibleLiquidItem>(items.size());

        if (items.size() > 0) {
            int origTotalVolume = getVolume();
            float newTotalVolume = 0;
            for (CrucibleLiquidItem it : items) {
                float volumeToRemoveFromItem = ((float) volumeToRemove) / ((float) (origTotalVolume)) * it.getVolume();
                float origItemVolume = it.getVolume();
                float newItemVolume = origItemVolume - volumeToRemoveFromItem;
                removedItems.add(new CrucibleLiquidItem(it.getMetal(), volumeToRemoveFromItem));
                it.setVolume(newItemVolume);
                newTotalVolume += newItemVolume;
            }

            if (newTotalVolume < 0.5f) {
                if (newTotalVolume > 0) {
                    // It is not unlikely there might be a very tiny amount of volume left due to
                    // substracting repeating decimals such as 1/3 which should be discarded
                    Bids.LOG.debug("Discarded " + newTotalVolume + " of liquid volume");
                }
                dirtyOutput = true;
                items.clear();
            }

            // When removing liquid, we always remove appropriate ratio of each metal
            // hence we don't need to update the output, unless we empty it completely
            dirty = true;
        }

        return removedItems;
    }

    public boolean isAllLiquid(float temp) {
        for (CrucibleLiquidItem it : items) {
            if (!CrucibleHelper.isMeltedAtTemp(it.getMetal(), temp)) {
                return false;
            }
        }
        return true;
    }

    private void update() {
        // Using float to sum item volumes
        // even though item volume can be fractions of units
        // the total volume should add up to integer
        float volumeFloat = 0f;
        heatCapacity = 0f;
        for (CrucibleLiquidItem it : items) {
            volumeFloat += it.getVolume();
            heatCapacity += CrucibleHelper.getHeatCapacity(new ItemStack(it.getMetal().ingot)) * it.getVolume();
        }
        volume = Math.round(volumeFloat);
        if (volume != volumeFloat) {
            Bids.LOG.debug("Liquid volume " + volumeFloat + " has been rounded to " + volume);
        }

        if (dirtyOutput) {
            output = CrucibleHelper.getMetalFromLiquid(items);
            dirtyOutput = false;
        }

        dirty = false;
    }

    public CrucibleLiquidStorage copy() {
        CrucibleLiquidStorage copy = new CrucibleLiquidStorage();
        for (CrucibleLiquidItem it : items) {
            copy.addLiquid(it.getMetal(), it.getVolume());
        }
        return copy;
    }

}
