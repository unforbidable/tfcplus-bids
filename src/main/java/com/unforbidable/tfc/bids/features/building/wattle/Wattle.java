package com.unforbidable.tfc.bids.features.building.wattle;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.building.wattle.block.BlockWattleGate;
import com.unforbidable.tfc.bids.features.building.wattle.block.BlockWattleTrapDoor;
import com.unforbidable.tfc.bids.features.building.wattle.block.BlockWattleTrapDoorCover;
import com.unforbidable.tfc.bids.features.building.wattle.block.blockitem.ItemWattleGate;
import com.unforbidable.tfc.bids.features.building.wattle.block.blockitem.ItemWattleTrapDoor;
import com.unforbidable.tfc.bids.features.building.wattle.render.RenderWattleGate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.WATTLE_GATE;
import static com.unforbidable.tfc.bids.api.names.BlockNames.WATTLE_TRAPDOOR;
import static com.unforbidable.tfc.bids.api.names.BlockNames.WATTLE_TRAPDOOR_COVER;

@FeatureName("wattle")
public class Wattle extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(WATTLE_TRAPDOOR, BlockWattleTrapDoor::new, ItemWattleTrapDoor.class)
            .harvest("axe", 0)
            .fireInfo(10, 30)
            .apply(i -> i.setBlockTextureName("Wattle Trap Door"));

        init.block(WATTLE_TRAPDOOR_COVER, BlockWattleTrapDoorCover::new, ItemWattleGate.class)
            .fireInfo(60, 20);

        init.block(WATTLE_GATE, BlockWattleGate::new)
            .harvest("axe", 0)
            .fireInfo(5, 5)
            .apply(i -> i.setBlockTextureName("Wattle Gate"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderWattleGate())
            .block(BlockWattleGate.class);

        client.nei()
            .hide(BidsBlocks.wattleTrapdoorCover);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShaped(new ItemStack(BidsBlocks.wattleGate),
            "PW", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);
        setup.recipes().addShaped(new ItemStack(BidsBlocks.wattleGate),
            "WP", "  ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);

        setup.recipes().addShaped(new ItemStack(BidsBlocks.wattleTrapdoor),
            "P ", "W ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);
        setup.recipes().addShaped(new ItemStack(BidsBlocks.wattleTrapdoor),
            "W ", "P ", 'P', TFCItems.pole, 'W', TFCBlocks.wattle);
    }

}
