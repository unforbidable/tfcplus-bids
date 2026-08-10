package com.unforbidable.tfc.bids.features.utility.handaxe;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.google.common.collect.Sets;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.stone.EnumStoneItemType;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneIndex;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneScheme;
import com.unforbidable.tfc.bids.features.utility.handaxe.item.ItemHandAxe;
import net.minecraft.item.Item;

@FeatureName("handAxe")
public class HandAxe extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.HAND_AXE_IG_IN, () -> new ItemHandAxe(TFCItems.igInToolMaterial));
        init.item(ItemNames.HAND_AXE_SED, () -> new ItemHandAxe(TFCItems.sedToolMaterial));
        init.item(ItemNames.HAND_AXE_IG_EX, () -> new ItemHandAxe(TFCItems.igExToolMaterial));
        init.item(ItemNames.HAND_AXE_MM, () -> new ItemHandAxe(TFCItems.mMToolMaterial));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        ItemHandAxe.effectiveAgainstBlocks.addAll(Sets.newHashSet(
            TFCBlocks.dirt, TFCBlocks.dirt2,
            TFCBlocks.grass, TFCBlocks.grass2,
            TFCBlocks.dryGrass, TFCBlocks.dryGrass2
        ));

        final Item[] handAxes = new Item[]{BidsItems.sedHandAxe, BidsItems.mMHandAxe, BidsItems.igInHandAxe, BidsItems.igExHandAxe};

        setup.ores("itemHandAxe")
            .add(handAxes);

        // Registering "itemKnifeStone" allow straw harvestable with a hand axe
        // but no usable in recipes where "itemKnife" is used
        setup.ores("itemKnifeStone")
            .add(handAxes);

        // Registering "itemAxeStone" allow bushes (and trees) harvestable with a hand axe
        // but no usable in recipes where "itemAxe" is used
        setup.ores("itemAxeStone")
            .add(handAxes);

        // Use for scrapping with speed penalty
        setup.ores("itemScrapingTool")
            .add(handAxes);
        setup.ores("itemPrimitiveTool")
            .add(handAxes);

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            setup.registry(TfcRegistry.Knapping.recipes)
                .add(KnappingRecipe.add(stone.items.getItem(EnumStoneItemType.HAND_AXE),
                    "  #  ", " ### ", " ### ", "#####", " ### ",
                    '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK)));
        }
    }

}
