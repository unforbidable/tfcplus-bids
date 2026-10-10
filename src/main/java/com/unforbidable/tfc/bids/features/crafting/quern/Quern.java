package com.unforbidable.tfc.bids.features.crafting.quern;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.quern.QuernRecipe;
import com.unforbidable.tfc.bids.compat.tfc.meta.DyeMeta;
import com.unforbidable.tfc.bids.compat.tfc.meta.LooseRockMeta;
import com.unforbidable.tfc.bids.compat.tfc.meta.OreMeta;
import com.unforbidable.tfc.bids.compat.tfc.meta.PowderMeta;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.quern.nei.SaddleQuernNeiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@FeatureName("quern")
public class Quern extends Feature {

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.nei()
            .handler(new SaddleQuernNeiHandler());
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // Salt
        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(new ItemStack(TFCItems.looseRock, 1, LooseRockMeta.ROCK_SALT),
                new ItemStack(TFCItems.powder, 2, PowderMeta.SALT), 2));

        // Dyes
        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(new ItemStack(TFCItems.bone),
                new ItemStack(TFCItems.dye, 1, DyeMeta.BONE), 2))
            .add(new QuernRecipe(new ItemStack(TFCItems.boneFragment),
                new ItemStack(TFCItems.dye, 1, DyeMeta.BONE), 2));

        setup.registry(QuernRegistry.recipes)
            .add(new QuernRecipe(new ItemStack(TFCItems.smallOreChunk, 1, OreMeta.HEMATITE),
                new ItemStack(TFCItems.powder, 1, PowderMeta.HEMATITE), 4))
            .add(new QuernRecipe(new ItemStack(TFCItems.smallOreChunk, 1, OreMeta.LIMONITE),
                new ItemStack(TFCItems.powder, 1, PowderMeta.LIMONITE), 4))
            .add(new QuernRecipe(new ItemStack(TFCItems.smallOreChunk, 1, OreMeta.MALACHITE),
                new ItemStack(TFCItems.powder, 1, PowderMeta.MALACHITE), 4))
            .add(new QuernRecipe(new ItemStack(TFCItems.oreChunk, 1, OreMeta.LAPIS_LAZULI),
                new ItemStack(TFCItems.powder, 2, PowderMeta.LAPIS_LAZULI), 4));
    }

}
