package com.unforbidable.tfc.bids.features.building.firebrick;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.building.firebrick.block.BlockFirebrickChimney;
import com.unforbidable.tfc.bids.features.building.firebrick.block.blockitem.ItemFireBrickChimney;
import com.unforbidable.tfc.bids.features.building.firebrick.tileentity.TileEntityFireBrickChimney;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.FIREBRICK_CHIMNEY;

/**
 * <li>fire brick chimney</li> - advanced chimney for advanced kiln
 */
@FeatureName("firebrick")
public class Firebrick extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(FIREBRICK_CHIMNEY, BlockFirebrickChimney::new, ItemFireBrickChimney.class);

        init.tileEntity(TileEntityFireBrickChimney.class, "BidsFireBrickChimney");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShaped(new ItemStack(BidsBlocks.fireBrickChimney, 2),
            "P P", "X X", "P P", 'P', new ItemStack(TFCItems.fireBrick, 1, 1),
            'X', new ItemStack(TFCItems.mortar, 1));
    }

}
