package com.unforbidable.tfc.bids.features.device.screwpress;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.pressing.ScrewPressRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
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
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_BARREL;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_DISC;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_LEVER;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_LEVER_TOP;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_RACK_BOTTOM;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_RACK_BRIDGE;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_RACK_MIDDLE;
import static com.unforbidable.tfc.bids.api.names.BlockNames.SCREW_PRESS_RACK_TOP;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("screwPress")
public class ScrewPress extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(ScrewPressConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(SCREW_PRESS_RACK_BOTTOM, BlockScrewPressRackBottom::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(SCREW_PRESS_RACK_MIDDLE, BlockScrewPressRackMiddle::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(SCREW_PRESS_RACK_TOP, BlockScrewPressRackTop::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(SCREW_PRESS_RACK_BRIDGE, BlockScrewPressRackBridge::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(SCREW_PRESS_BARREL, BlockScrewPressBarrel::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(SCREW_PRESS_DISC, BlockScrewPressDisc::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(SCREW_PRESS_LEVER, BlockScrewPressLever::new, ItemScrewPress.class)
            .fireInfo(5, 5);
        init.block(SCREW_PRESS_LEVER_TOP, BlockScrewPressLeverTop::new, ItemScrewPress.class)
            .fireInfo(5, 5);

        init.tileEntity(TileEntityScrewPressBarrel.class, "BidsScrewPressBarrel");
        init.tileEntity(TileEntityScrewPressDisc.class, "BidsScrewPressDisc");
        init.tileEntity(TileEntityScrewPressLever.class, "BidsScrewPressLever");

        init.gui(SCREW_PRESS_BARREL, ContainerScrewPress::new);
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

        client.gui(SCREW_PRESS_BARREL, GuiScrewPress::new);

        client.waila()
            .data(new ScrewPressBarrelWailaProvider(), TileEntityScrewPressBarrel.class);
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

        // Screw press efficiency affects the recipe input or output volume
        float inputMult = 1 / ScrewPressConfig.efficiency; // input multiplier (for non-food input)
        float outputMult = ScrewPressConfig.efficiency; // output multiplier (for food input)

        setup.registry(ScrewPressRegistry.recipes)
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.OLIVEOIL, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.olive), 0.64f * inputMult), 1f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.redApple), 0.7f * inputMult), 1f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.APPLEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.greenApple), 0.7f * inputMult), 1f));

        setup.registry(ScrewPressRegistry.recipes)
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.GRAPEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.grapes), 0.5f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.CANEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.sugarcane), 0.8f * inputMult), 0.8f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.LEMONJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.lemon), 0.65f * inputMult), 0.65f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.ORANGEJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.orange), 0.5f * inputMult), 0.65f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.PEACHJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.peach), 0.55f * inputMult), 0.8f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.PLUMJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.plum), 0.65f * inputMult), 0.8f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.FIGJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.fig), 0.5f * inputMult), 0.8f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.CHERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.cherry), 0.7f * inputMult), 0.8f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.DATEJUICE, 6),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.date), 0.8f * inputMult), 0.8f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.PAPAYAJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.papaya), 0.6f * inputMult), 0.8f));

        setup.registry(ScrewPressRegistry.recipes)
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.strawberry), 0.65f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.blackberry), 0.61f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.blueberry), 0.6f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.bunchberry), 0.68f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.cranberry), 0.7f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.elderberry), 0.58f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.gooseberry), 0.6f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.raspberry), 0.6f * inputMult), 0.5f))
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.BERRYJUICE, 10),
                ItemFoodTFC.createTag(new ItemStack(TFCItems.snowberry), 0.66f * inputMult), 0.5f));

        setup.registry(ScrewPressRegistry.recipes)
            .add(new ScrewPressRecipe(new FluidStack(TFCFluids.AGAVEJUICE, Math.round(40 * outputMult)),
                new ItemStack(TFCItems.agave, 1), 0.8f));
    }

}
