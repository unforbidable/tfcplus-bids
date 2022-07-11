package com.unforbidable.tfc.bids.Core.Drinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.dunk.tfc.Items.ItemDrink;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class DrinkOverlayHandler {

    final ItemDrink item;
    final boolean isPottery;
    final int[] parts;
    final Map<Integer, IIcon> icons;

    public DrinkOverlayHandler(ItemDrink item, boolean isPottery, int[] parts) {
        super();
        this.item = item;
        this.isPottery = isPottery;

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
            // By default have overlay showing 100% fulness
            this.parts = new int[] { 100 };
        }

        icons = new HashMap<Integer, IIcon>(parts.length);
    }

    public void registerIcons(IIconRegister registerer) {
        String folder = isPottery ? "pottery" : "glassware";
        for (int i : parts) {
            String suffix = i == 100 ? "" : "." + i;
            IIcon icon = registerer.registerIcon(Tags.MOD_ID + ":" + folder + "/"
                    + ((Item) item).getContainerItem().getUnlocalizedName().replace("item.", "")
                    + ".Overlay" + suffix);
            icons.put(i, icon);
        }
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return isPottery
                    ? item.getContainerItem().getIcon(item.getContainerItem(stack), pass)
                    : item.getContainerItem().getIcon(stack, pass);
        } else if (pass == 1) {
            // Damage 0 means 100% full, the most common case
            // skip the calculation and lookup for preformance
            int dmg = stack.getItemDamage();
            if (dmg > 0) {
                int volume = (int) item.getVolume();
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

        return null;
    }

}
