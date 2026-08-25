package com.unforbidable.tfc.bids.features.material.skin.main.scheme;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.compat.tfc.meta.RepairPatchMeta;
import net.minecraft.item.ItemStack;

public class SkinFurSpec {

    public static final SkinFurSpec generic = new SkinFurSpec(4f,
        new ItemStack(TFCItems.fur, 1, 2), new ItemStack(TFCItems.fur, 1 ,1), new ItemStack(TFCItems.fur, 1, 0),
        null, new ItemStack(TFCItems.repairPatch, RepairPatchMeta.FUR));
    public static final SkinFurSpec wolf = new SkinFurSpec(4f,
        new ItemStack(TFCItems.wolfFurScrap, 1, 2), new ItemStack(TFCItems.wolfFurScrap, 1 ,1), new ItemStack(TFCItems.wolfFurScrap, 1, 0),
        null, new ItemStack(TFCItems.repairPatch, RepairPatchMeta.WOLF_FUR));
    public static final SkinFurSpec bear = new SkinFurSpec(6f,
        new ItemStack(TFCItems.bearFurScrap, 1, 2), new ItemStack(TFCItems.bearFurScrap, 1 ,1), new ItemStack(TFCItems.bearFurScrap, 1, 0),
        null, new ItemStack(TFCItems.repairPatch, RepairPatchMeta.BEAR_FUR));

    public final float dehairingEffort;
    public final ItemStack largeOutput;
    public final ItemStack mediumOutput;
    public final ItemStack smallOutput;
    public final ItemStack verySmallOutput;
    public final ItemStack tinyOutput;

    public SkinFurSpec(float dehairingEffort, ItemStack largeOutput, ItemStack mediumOutput, ItemStack smallOutput, ItemStack verySmallOutput, ItemStack tinyOutput) {
        this.dehairingEffort = dehairingEffort;
        this.largeOutput = largeOutput;
        this.mediumOutput = mediumOutput;
        this.smallOutput = smallOutput;
        this.verySmallOutput = verySmallOutput;
        this.tinyOutput = tinyOutput;
    }

}
