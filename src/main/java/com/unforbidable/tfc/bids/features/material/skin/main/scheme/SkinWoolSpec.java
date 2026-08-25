package com.unforbidable.tfc.bids.features.material.skin.main.scheme;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SkinWoolSpec {

    public static final SkinWoolSpec sheep = new SkinWoolSpec(0.5f,
        BidsItems.genericSkin, "sheepTFC.sheared", new ItemStack(TFCItems.wool));

    public final float shearingEffort;
    public final Item outputItem;
    public final String outputName;
    public final ItemStack extraDrop;

    public SkinWoolSpec(float shearingEffort, Item outputItem, String outputName, ItemStack extraDrop) {
        this.shearingEffort = shearingEffort;
        this.outputItem = outputItem;
        this.outputName = outputName;
        this.extraDrop = extraDrop;
    }

}
