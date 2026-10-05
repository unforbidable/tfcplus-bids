package com.unforbidable.tfc.bids.features.utility.bow;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingRecipe;
import com.unforbidable.tfc.bids.api.meta.UnfinishedCompositeBowMeta;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonMisc;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.cooking.CookingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

@FeatureName("bow")
public class Bow extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.UNFINISHED_COMPOSITE_BOW, ItemCommonMisc::new)
            .meta("Extended", "Glued", "Tied");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // unstrung bow + 2x horn strip -> unfinished composite bow (horn tips)
        setup.recipes()
            .addShaped(new ItemStack(BidsItems.unfinishedCompositeBow, 1, UnfinishedCompositeBowMeta.EXTENDED), "UH", "H ",
                'H', new ItemStack(TFCItems.hornStrips), 'U', new ItemStack(TFCItems.unstrungBow));

        // unfinished composite bow (hide glue) -> unfinished composite bow (sinew)
        setup.recipes()
            .addShaped(new ItemStack(BidsItems.unfinishedCompositeBow, 1, UnfinishedCompositeBowMeta.TIED), "US", "S ",
                'S', new ItemStack(TFCItems.sinew), 'U', new ItemStack(BidsItems.unfinishedCompositeBow, 1, UnfinishedCompositeBowMeta.GLUED));

        // unfinished composite bow (horn tips) + hide glue -> unfinished composite bow (hide glue)
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(BidsItems.unfinishedCompositeBow, 1, UnfinishedCompositeBowMeta.EXTENDED))
                .consumes(new FluidStack(BidsFluids.hideGlue, 500))
                .produces(new ItemStack(BidsItems.unfinishedCompositeBow, 1, UnfinishedCompositeBowMeta.GLUED))
                .inTime(200)
                .build());

        // unfinished composite bow (sinew) + pitch glue -> unstrung composite bow
        setup.registry(CookingRegistry.recipes)
            .add(CookingRecipe.builder()
                .consumes(new ItemStack(BidsItems.unfinishedCompositeBow, 1, UnfinishedCompositeBowMeta.TIED))
                .consumes(new FluidStack(BidsFluids.pitchGlue, 500))
                .produces(new ItemStack(TFCItems.unstrungCompositeBow))
                .inTime(200)
                .build());
    }

}
