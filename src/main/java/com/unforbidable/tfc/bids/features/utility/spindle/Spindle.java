package com.unforbidable.tfc.bids.features.utility.spindle;

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
import com.unforbidable.tfc.bids.features.utility.spindle.item.ItemSpindle;
import com.unforbidable.tfc.bids.features.utility.spindle.item.ItemWhorl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@FeatureName("spindle")
public class Spindle extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.WHORL, ItemWhorl::new)
            .meta("Stone");
        init.item(ItemNames.SPINDLE, () -> new ItemSpindle(TFCItems.woodToolMaterial));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new HandworkToolItemRenderer())
            .item(BidsItems.spindle);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemWhorl")
            .add(BidsItems.whorl);

        setup.ores("itemSpindle")
            .add(BidsItems.spindle);

        setup.recipes().addShapeless(new ItemStack(BidsItems.whorl),
            "itemRock", "itemDrillHead");

        setup.recipes().addShapeless(new ItemStack(BidsItems.spindle),
            "itemWhorl", "stickWood");
    }

}
