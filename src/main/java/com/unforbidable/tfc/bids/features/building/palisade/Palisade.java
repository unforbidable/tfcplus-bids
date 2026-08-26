package com.unforbidable.tfc.bids.features.building.palisade;

import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureLoadAfter;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.building.palisade.block.BlockPalisade;
import com.unforbidable.tfc.bids.features.building.palisade.block.blockitem.ItemPalisade;
import com.unforbidable.tfc.bids.features.building.palisade.block.blockitem.ItemPalisade16;
import com.unforbidable.tfc.bids.features.building.palisade.block.blockitem.ItemPalisade32;
import com.unforbidable.tfc.bids.features.building.palisade.render.RenderPalisade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("palisade")
@FeatureLoadAfter("logWall")
public class Palisade extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.PALISADE, () -> new BlockPalisade(lookup.block(BlockNames.LOG_WALL_VERT), 0), ItemPalisade.class)
            .harvest("axe", 0)
            .fireInfo(5, 5);
        init.block(BlockNames.PALISADE_2, () -> new BlockPalisade(lookup.block(BlockNames.LOG_WALL_VERT_2), 16), ItemPalisade16.class)
            .harvest("axe", 0)
            .fireInfo(5, 5);
        init.block(BlockNames.PALISADE_3, () -> new BlockPalisade(lookup.block(BlockNames.LOG_WALL_VERT_3), 32), ItemPalisade32.class)
            .harvest("axe", 0)
            .fireInfo(5, 5);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderPalisade())
            .block(BlockPalisade.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.blocks.hasPalisade()) {
                setup.recipes().addShaped(wood.blocks.getPalisade(2),
                        "A1", " 1", '1', wood.getOreWithSuffix("logWood"), 'A', "itemAxe")
                    .action(damageTool("itemAxe"));
            }
        }
    }

}
