package com.unforbidable.tfc.bids.compat.tfc;

import com.dunk.tfc.Core.Metal.MetalRegistry;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Metal;
import com.unforbidable.tfc.bids.Bids;
import net.minecraft.item.ItemStack;

public class TfcUtil {

    public static Metal[] getMetalsFromNames(String[] names) {
        try {
            Metal[] metals = new Metal[names.length];

            int j = 0;
            for (String name : names) {
                Metal m = MetalRegistry.instance.getMetalFromString(name);
                if (m != null) {
                    metals[j++] = m;
                } else {
                    Bids.LOG.error("Unable to find TFC metal with name '{}'", name);
                }
            }

            if (j == names.length) {
                return metals;
            } else {
                // Not all metals were converted
                Metal[] less = new Metal[j];
                System.arraycopy(metals, 0, less, 0, j);
                return less;
            }
        } catch (Exception ex) {
            Bids.LOG.error("Failed to get TFC metals for given names: {}", ex.getMessage(), ex);

            return new Metal[0];
        }
    }


    public static float getItemStackDurabilityBuff(ItemStack stack) {
        return AnvilManager.getDurabilityBuff(stack);
    }

}
