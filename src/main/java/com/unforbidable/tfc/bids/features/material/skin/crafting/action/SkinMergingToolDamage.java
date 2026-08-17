package com.unforbidable.tfc.bids.features.material.skin.crafting.action;

import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import net.minecraft.item.ItemStack;

public class SkinMergingToolDamage {

    public static Integer provide(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSkin) {
            float weight = SkinTag.of(itemStack).getWeight();
            return Math.max((int) (Math.round(Math.sqrt(weight) / 2f)), 1) * 4;
        } else {
            return 0;
        }
    }

}
