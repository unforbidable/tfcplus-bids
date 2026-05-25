package com.unforbidable.tfc.bids.features.device.woodpile;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedOre;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedOre2;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedOre3;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneIgEx;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneIgIn;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneMM;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockCrackedStoneSed;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockLight;
import com.unforbidable.tfc.bids.features.device.woodpile.block.BlockWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.block.blockitem.ItemCrackedStone;
import com.unforbidable.tfc.bids.features.device.woodpile.container.ContainerWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.eventhandler.FireSettingHandler;
import com.unforbidable.tfc.bids.features.device.woodpile.eventhandler.KilnWoodDryingHandler;
import com.unforbidable.tfc.bids.features.device.woodpile.eventhandler.WoodpilePlacementHandler;
import com.unforbidable.tfc.bids.features.device.woodpile.gui.GuiWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.main.firesetting.crackable.CrackableBlockOre;
import com.unforbidable.tfc.bids.features.device.woodpile.main.firesetting.crackable.CrackableBlockStone;
import com.unforbidable.tfc.bids.features.device.woodpile.main.network.WoodpilePacket;
import com.unforbidable.tfc.bids.features.device.woodpile.main.renderable.RenderableLogsTFC;
import com.unforbidable.tfc.bids.features.device.woodpile.main.renderable.RenderableThickLogsTFC;
import com.unforbidable.tfc.bids.features.device.woodpile.nei.SeasoningNeiHandler;
import com.unforbidable.tfc.bids.features.device.woodpile.render.RenderCrackedOre;
import com.unforbidable.tfc.bids.features.device.woodpile.render.RenderCrackedStone;
import com.unforbidable.tfc.bids.features.device.woodpile.render.RenderWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import com.unforbidable.tfc.bids.features.device.woodpile.waila.CrackedOreWailaProvider;
import com.unforbidable.tfc.bids.features.device.woodpile.waila.WoodpileWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_ORE;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_ORE_1B;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_ORE_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_ORE_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_STONE_IG_EX;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_STONE_IG_IN;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_STONE_MM;
import static com.unforbidable.tfc.bids.api.names.BlockNames.CRACKED_STONE_SED;
import static com.unforbidable.tfc.bids.api.names.BlockNames.LIGHT;
import static com.unforbidable.tfc.bids.api.names.BlockNames.WOODPILE;

@FeatureName("woodpile")
public class Woodpile extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(WoodpileConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(WOODPILE, BlockWoodpile::new)
            .apply(b -> b.setBlockTextureName("Wood Pile"));

        init.block(CRACKED_STONE_SED, BlockCrackedStoneSed::new, ItemCrackedStone.class)
            .apply(b -> b.setBlockName("CrackedSedRock"));
        init.block(CRACKED_STONE_MM, BlockCrackedStoneMM::new, ItemCrackedStone.class)
            .apply(b -> b.setBlockName("CrackedMMRock"));
        init.block(CRACKED_STONE_IG_IN, BlockCrackedStoneIgIn::new, ItemCrackedStone.class)
            .apply(b -> b.setBlockName("CrackedIgInRock"));
        init.block(CRACKED_STONE_IG_EX, BlockCrackedStoneIgEx::new, ItemCrackedStone.class)
            .apply(b -> b.setBlockName("CrackedIgExRock"));

        init.block(CRACKED_ORE, BlockCrackedOre::new)
            .apply(b -> b.setBlockName("Ore"));
        init.block(CRACKED_ORE_1B, BlockCrackedOre::new)
            .apply(b -> b.setBlockName("Ore"))
            .apply(b -> b.setDamageOffset(16));
        init.block(CRACKED_ORE_2, BlockCrackedOre2::new)
            .apply(b -> b.setBlockName("Ore"));
        init.block(CRACKED_ORE_3, BlockCrackedOre3::new)
            .apply(b -> b.setBlockName("Ore"));

        init.block(LIGHT, BlockLight::new);

        init.tileEntity(TileEntityWoodpile.class, "BidsWoodPile");

        init.gui(WOODPILE, ContainerWoodpile::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderWoodpile())
            .block(BlockWoodpile.class);

        client.render(new RenderCrackedStone())
            .block(BlockCrackedStoneSed.class)
            .block(BlockCrackedStoneMM.class)
            .block(BlockCrackedStoneIgEx.class)
            .block(BlockCrackedStoneIgIn.class);

        client.render(new RenderCrackedOre())
            .block(BlockCrackedOre.class)
            .block(BlockCrackedOre2.class)
            .block(BlockCrackedOre3.class);

        client.gui(WOODPILE, GuiWoodpile::new);

        client.waila()
            .data(new WoodpileWailaProvider(), TileEntityWoodpile.class)
            .data(new CrackedOreWailaProvider(), BlockCrackedOre.class, BlockCrackedOre2.class, BlockCrackedOre3.class);

        client.nei()
            .handler(new SeasoningNeiHandler())
            .hide(BidsBlocks.woodPile)
            .hide(BidsBlocks.light);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // TODO check split log rendering when peeled logs / log walls are added
        setup.registry(WoodpileRegistry.renderable)
            .add(TFCItems.logs, new RenderableLogsTFC())
            .add(TFCItems.thickLogs, new RenderableThickLogsTFC());

        setup.registry(WoodpileRegistry.crackable)
            .add(new CrackableBlockStone(TFCBlocks.stoneSed, BidsBlocks.crackedStoneSed, 1f))
            .add(new CrackableBlockStone(TFCBlocks.stoneMM, BidsBlocks.crackedStoneMM, 1.2f))
            .add(new CrackableBlockStone(TFCBlocks.stoneIgIn, BidsBlocks.crackedStoneIgIn, 1.5f))
            .add(new CrackableBlockStone(TFCBlocks.stoneIgEx, BidsBlocks.crackedStoneIgEx, 2f))
            .add(new CrackableBlockOre(TFCBlocks.ore, BidsBlocks.crackedOre))
            .add(new CrackableBlockOre(TFCBlocks.ore1b, BidsBlocks.crackedOre1b))
            .add(new CrackableBlockOre(TFCBlocks.ore2, BidsBlocks.crackedOre2))
            .add(new CrackableBlockOre(TFCBlocks.ore3, BidsBlocks.crackedOre3));

        setup.event()
            .handler(new WoodpilePlacementHandler())
            .handler(new KilnWoodDryingHandler())
            .handler(new FireSettingHandler());

        setup.network()
            .register(WoodpilePacket.class);
    }

}
