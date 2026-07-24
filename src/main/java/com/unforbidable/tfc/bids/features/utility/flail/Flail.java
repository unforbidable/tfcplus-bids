package com.unforbidable.tfc.bids.features.utility.flail;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.utility.flail.item.ItemFlail;
import com.unforbidable.tfc.bids.features.utility.flail.render.FlailItemRenderer;
import net.minecraft.item.ItemStack;

@FeatureName("flail")
public class Flail extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.WOODEN_FLAIL, () -> new ItemFlail(TFCItems.woodToolMaterial, TFCItems.woodToolMaterial.getDamageVsEntity()))
            .apply(i -> i.setCrushDamageShape("3X5_MACE")
                .setAttackSpeed(19));
    }

    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new FlailItemRenderer())
            .item(BidsItems.woodenFlail);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemThreshingTool")
            .add(BidsItems.woodenFlail);

        setup.recipes()
            .addShaped(new ItemStack(BidsItems.woodenFlail), "BS", "P ",
                'S', "stickWood", 'P', TFCItems.pole, 'B', "materialBindingStrong");
    }

}
