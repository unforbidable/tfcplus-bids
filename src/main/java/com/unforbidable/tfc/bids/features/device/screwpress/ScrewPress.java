package com.unforbidable.tfc.bids.features.device.screwpress;

import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.pressing.PressingRegistry;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressLever;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressLeverTop;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackBottom;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackBridge;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackMiddle;
import com.unforbidable.tfc.bids.features.device.screwpress.block.BlockScrewPressRackTop;
import com.unforbidable.tfc.bids.features.device.screwpress.block.blockitem.ItemScrewPress;
import com.unforbidable.tfc.bids.features.device.screwpress.container.ContainerScrewPress;
import com.unforbidable.tfc.bids.features.device.screwpress.gui.GuiScrewPress;
import com.unforbidable.tfc.bids.features.device.screwpress.main.ScrewPressHelper;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressLever;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderScrewPressRack;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderTileScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.render.RenderTileScrewPressLever;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressBarrel;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressDisc;
import com.unforbidable.tfc.bids.features.device.screwpress.tileentity.TileEntityScrewPressLever;
import com.unforbidable.tfc.bids.features.device.screwpress.waila.ScrewPressBarrelWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("screwPress")
public class ScrewPress extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(ScrewPressConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.SCREW_PRESS_RACK_BOTTOM, BlockScrewPressRackBottom::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(BlockNames.SCREW_PRESS_RACK_MIDDLE, BlockScrewPressRackMiddle::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(BlockNames.SCREW_PRESS_RACK_TOP, BlockScrewPressRackTop::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(BlockNames.SCREW_PRESS_RACK_BRIDGE, BlockScrewPressRackBridge::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(BlockNames.SCREW_PRESS_BARREL, BlockScrewPressBarrel::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(BlockNames.SCREW_PRESS_DISC, BlockScrewPressDisc::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(BlockNames.SCREW_PRESS_LEVER, BlockScrewPressLever::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(BlockNames.SCREW_PRESS_LEVER_TOP, BlockScrewPressLeverTop::new, ItemScrewPress.class)
            .fireInfo(5, 5);

        init.tileEntity(TileEntityScrewPressBarrel.class, "BidsScrewPressBarrel");
        init.tileEntity(TileEntityScrewPressDisc.class, "BidsScrewPressDisc");
        init.tileEntity(TileEntityScrewPressLever.class, "BidsScrewPressLever");

        init.gui(BlockNames.SCREW_PRESS_BARREL, ContainerScrewPress::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderScrewPressRack())
            .block(BlockScrewPressRackBottom.class)
            .block(BlockScrewPressRackMiddle.class)
            .block(BlockScrewPressRackTop.class)
            .block(BlockScrewPressRackBridge.class);

        client.render(new RenderScrewPressBarrel())
            .block(BlockScrewPressBarrel.class);

        client.render(new RenderScrewPressDisc())
            .block(BlockScrewPressDisc.class);

        client.render(new RenderScrewPressLever())
            .block(BlockScrewPressLever.class)
            .block(BlockScrewPressLeverTop.class);

        client.render(new RenderTileScrewPressDisc())
            .tileEntity(TileEntityScrewPressDisc.class);

        client.render(new RenderTileScrewPressLever())
            .tileEntity(TileEntityScrewPressLever.class);

        client.gui(BlockNames.SCREW_PRESS_BARREL, GuiScrewPress::new);

        client.waila()
            .data(new ScrewPressBarrelWailaProvider(), TileEntityScrewPressBarrel.class);

        client.nei()
            .hide(BidsBlocks.screwPressRackMiddle)
            .hide(BidsBlocks.screwPressRackTop)
            .hide(BidsBlocks.screwPressLeverTop);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShaped(new ItemStack(BidsBlocks.screwPressRackBottom),
            "SLS", "S S", "SLS", 'L', "woodLumber", 'S', "supportWood");
        setup.recipes().addShaped(new ItemStack(BidsBlocks.screwPressRackBridge),
            "SSS", "L L", "SSS", 'L', "woodLumber", 'S', "supportWood");
        setup.recipes().addShaped(new ItemStack(BidsBlocks.screwPressBarrel),
            "LTL", "LLL", "LPL", 'T', "plateToolMetal", 'L', "woodLumber", 'P', "plankWood");
        setup.recipes().addShaped(new ItemStack(BidsBlocks.screwPressDisc),
            "L L", "LPL", "   ", 'L', "woodLumber", 'P', "plankWood");
        setup.recipes().addShaped(new ItemStack(BidsBlocks.screwPressLever),
                "LTL", " L ", " L ", 'T', "itemSaw", 'L', "logWoodPeeledSeasoned")
            .action(damageTool("itemSaw"));

        setup.registry(ScrewPressRegistry.recipes)
            .adapt(PressingRegistry.recipes, ScrewPressHelper::adaptPressingRecipe);
    }

}
