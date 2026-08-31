package com.unforbidable.tfc.bids.features.utility.ropemaker;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.handwork.render.HandworkToolItemRenderer;
import com.unforbidable.tfc.bids.features.utility.ropemaker.item.ItemPrimitiveRopeMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("ropeMaker")
public class RopeMaker extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.PRIMITIVE_ROPE_MAKER, () -> new ItemPrimitiveRopeMaker(TFCItems.woodToolMaterial));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new HandworkToolItemRenderer())
            .item(BidsItems.primitiveRopeMaker);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShapeless(new ItemStack(BidsItems.primitiveRopeMaker),
                "stickWood", "stickWood", "materialBindingStrong", "itemKnife")
            .action(damageTool("itemKnife"));
        setup.recipes().addShapeless(new ItemStack(BidsItems.primitiveRopeMaker),
                "stickWood", "stickWood", "materialBindingStrong", "itemHandAxe")
            .action(damageTool("itemHandAxe"));
    }

}
