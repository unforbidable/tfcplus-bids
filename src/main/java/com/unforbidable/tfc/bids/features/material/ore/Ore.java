package com.unforbidable.tfc.bids.features.material.ore;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.material.ore.item.ItemOreBit;
import com.unforbidable.tfc.bids.util.metal.MetalHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

/**
 * <li><b>ore bit</b> - ore broken into more granular form yielding 5 unit per item, easier to smelt than larger chunks</li>
 */
@FeatureName("ore")
public class Ore extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.ORE_BIT, ItemOreBit::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemHammerIronBits")
            .add(new ItemStack(TFCItems.steelHammer, 1, OreDictionary.WILDCARD_VALUE))
            .add(new ItemStack(TFCItems.blackSteelHammer, 1, OreDictionary.WILDCARD_VALUE))
            .add(new ItemStack(TFCItems.redSteelHammer, 1, OreDictionary.WILDCARD_VALUE))
            .add(new ItemStack(TFCItems.blueSteelHammer, 1, OreDictionary.WILDCARD_VALUE));

        // TODO create metal scheme instead
        for (int i = 0; i < Global.ORE_METAL.length; i++) {
            // Skip coal and lignite
            if (i != 14 && i != 15) {
                ItemStack small = new ItemStack(TFCItems.smallOreChunk, 1, i);
                ItemStack poor = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade2Offset + i);
                ItemStack normal = new ItemStack(TFCItems.oreChunk, 1, i);
                ItemStack rich = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade1Offset + i);

                if (MetalHelper.isOreIron(small)) {
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 2, i),
                            small, "itemHammerIronBits")
                        .action(damageTool("itemHammerIronBits", 10));
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 3, i),
                            poor, "itemHammerIronBits")
                        .action(damageTool("itemHammerIronBits", 20));
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 5, i),
                            normal, "itemHammerIronBits")
                        .action(damageTool("itemHammerIronBits", 30));
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 7, i),
                            rich, "itemHammerIronBits")
                        .action(damageTool("itemHammerIronBits", 40));
                } else {
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 2, i),
                            small, "itemHammer")
                        .action(damageTool("itemHammer", 1));
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 3, i),
                            poor, "itemHammer")
                        .action(damageTool("itemHammer", 2));
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 5, i),
                            normal, "itemHammer")
                        .action(damageTool("itemHammer", 3));
                    setup.recipes().addShapeless(new ItemStack(BidsItems.oreBit, 7, i),
                            rich, "itemHammer")
                        .action(damageTool("itemHammer", 4));
                }

                setup.registry(TfcRegistry.Values.heat)
                    .add(HeatValue.clone(new ItemStack(TFCItems.smallOreChunk, 1, i))
                        .as(new ItemStack(BidsItems.oreBit, 1, i)));
            }
        }
    }

}
