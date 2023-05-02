package com.unforbidable.tfc.bids.Core.Drinks;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import java.util.*;

public class FluidOverlayMap {

    final int[] parts;
    Map<Integer, IIcon> icons = new HashMap<Integer, IIcon>();

    public FluidOverlayMap(int[] parts) {
        if (parts.length > 0) {
            // Check the range of values and uniqueness, then sort
            ArrayList<Integer> checkedAndSorted = new ArrayList<Integer>();
            for (int i = 0; i < parts.length; i++) {
                int value = Math.min(Math.max(parts[i], 0), 100);
                if (!checkedAndSorted.contains(value))
                    checkedAndSorted.add(value);
            }

            if (!checkedAndSorted.contains(100))
                checkedAndSorted.add(100);

            Collections.sort(checkedAndSorted, Collections.reverseOrder());

            this.parts = new int[checkedAndSorted.size()];
            int i = 0;
            for (Integer value : checkedAndSorted) {
                this.parts[i++] = value;
            }
        } else {
            // By default, have overlay showing 100% fullness
            this.parts = new int[] { 100 };
        }
    }

    public void registerOverlayIcons(IIconRegister registerer, String folder, String baseName) {
        for (int i : parts) {
            String suffix = i == 100 ? "" : "." + i;
            IIcon icon = registerer.registerIcon(Tags.MOD_ID + ":" + folder + "/"
                + baseName
                + ".Overlay" + suffix);
            icons.put(i, icon);
        }
    }

    public IIcon getOverlayIcon(int dmg, int volume) {
        // Damage 0 means 100% full, the most common case
        // skip the calculation and lookup for preformance
        if (dmg > 0) {
            int sipsTotal = volume / 50;
            int sips = sipsTotal - dmg;
            int sipsPercent = (int) Math.ceil(sips * 100 / (float) sipsTotal);

            // Look for the first part that is lower than
            // given perentage
            for (int i : parts) {
                if (i <= sipsPercent) {
                    return icons.get(i);
                }
            }
        }
        return icons.get(100);
    }

}
