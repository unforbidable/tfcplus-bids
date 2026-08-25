package com.unforbidable.tfc.bids.features.material.skin;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingFrameRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingRecipe;
import com.unforbidable.tfc.bids.api.meta.MoreHideMeta;
import com.unforbidable.tfc.bids.api.names.GuiNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.compat.tfc.meta.PowderMeta;
import com.unforbidable.tfc.bids.compat.tfc.meta.RepairPatchMeta;
import com.unforbidable.tfc.bids.compat.tfc.nbt.FoodTag;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.soaking.SoakingRegistry;
import com.unforbidable.tfc.bids.features.device.dryingframe.DryingFrameRegistry;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import com.unforbidable.tfc.bids.features.device.processingsurface.ProcessingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.material.skin.container.ContainerSpecialCraftingSkin;
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinCuttingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinMergingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinSaltingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.eventhandler.SkinDecorativeSurfaceEventHandler;
import com.unforbidable.tfc.bids.features.material.skin.eventhandler.SkinDryingHandler;
import com.unforbidable.tfc.bids.features.material.skin.eventhandler.SkinLivingDropsEventHandler;
import com.unforbidable.tfc.bids.features.material.skin.eventhandler.SkinProcessingHandler;
import com.unforbidable.tfc.bids.features.material.skin.eventhandler.SkinSoakingHandler;
import com.unforbidable.tfc.bids.features.material.skin.gui.GuiKnappingSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemDehairedSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFinishedSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFreshSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import com.unforbidable.tfc.bids.features.material.skin.main.scheme.SkinIndex;
import com.unforbidable.tfc.bids.features.material.skin.main.scheme.SkinScheme;
import com.unforbidable.tfc.bids.features.material.skin.render.SkinItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.features.material.skin.crafting.action.CutSkin.cutSkin;

/**
 * Fresh skins decay rapidly until scraped.
 */
@FeatureName("skin")
public class Skin extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(SkinConfig::load, "butchery");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.GENERIC_SKIN, ItemFreshSkin::new);
        init.item(ItemNames.GENERIC_FUR, ItemFreshSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatFur));
        init.item(ItemNames.WOLF_FUR, ItemFreshSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatWolfFur));
        init.item(ItemNames.BEAR_FUR, ItemFreshSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatBearFur));
        init.item(ItemNames.SHEEP_SKIN, ItemFreshSkin::new);
        init.item(ItemNames.DEHAIRED_SKIN, ItemDehairedSkin::new);
        init.item(ItemNames.RAWHIDE, ItemFinishedSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatHide));
        init.item(ItemNames.LEATHER, ItemFinishedSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatLeather));

        init.gui(GuiNames.SKIN, ContainerSpecialCraftingSkin::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new SkinItemRenderer())
            .item(BidsItems.genericSkin)
            .item(BidsItems.genericFur)
            .item(BidsItems.wolfFur)
            .item(BidsItems.bearFur)
            .item(BidsItems.sheepSkin)
            .item(BidsItems.dehairedSkin)
            .item(BidsItems.rawhide)
            .item(BidsItems.leather);

        client.gui(GuiNames.SKIN, GuiKnappingSkin::new);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new SkinLivingDropsEventHandler())
            .handler(new SkinProcessingHandler())
            .handler(new SkinSoakingHandler())
            .handler(new SkinDryingHandler())
            .handler(new SkinDecorativeSurfaceEventHandler());

        // Use scheme to set up recipes for all sorts of fresh hides
        SkinScheme.setup();

        for (SkinIndex skin : SkinScheme.skins) {
            if (skin.fur != null) {
                // Cutting preserved furs into fixed size TFC furs
                setup.recipes()
                    .add(new SkinCuttingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_PRESERVED),
                        "itemKnife",
                        skin.fur.largeOutput, skin.fur.mediumOutput, skin.fur.smallOutput, skin.fur.verySmallOutput, skin.fur.tinyOutput))
                    .action(cutSkin())
                    .action(damageTool("itemKnife"));

                // Merging preserved furs
                setup.recipes()
                    .add(new SkinMergingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_PRESERVED),
                        null, SkinHelper.WEIGHT_MEDIUM - 0.01f));
            }

            if (skin.wool != null) {
                // Sheering
                setup.registry(ProcessingSurfaceRegistry.recipes)
                    .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN),
                        SkinHelper.createStack(skin.wool.outputItem, SkinTagAccess.STAGE_CLEAN, tag -> tag.setAnimal(skin.wool.outputName)),
                        "itemScrapingTool", "blockScrapingSurface", 0.5f))
                    .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_PRESERVED),
                        SkinHelper.createStack(skin.wool.outputItem, SkinTagAccess.STAGE_PRESERVED, tag -> tag.setAnimal(skin.wool.outputName)),
                        "itemScrapingTool", "blockScrapingSurface", 0.5f));
            }

            // Salting
            setup.recipes()
                .add(new SkinSaltingRecipe(SkinHelper.createStack(skin.item),
                    SkinHelper.createStack(skin.item, SkinTag::setSalted), new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));
            setup.recipes()
                .add(new SkinSaltingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_FLESHED),
                    SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_FLESHED, SkinTag::setSalted), new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));
            setup.recipes()
                .add(new SkinSaltingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN),
                    SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN, SkinTag::setSalted), new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));

            // Washing salt off of clean skin, so it can be prepared for dehairing
            setup.registry(SoakingRegistry.recipes)
                .add(new SoakingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN, FoodTag::setSalted),
                    SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 200)));

            // Fresh -> Fleshed
            setup.registry(ProcessingSurfaceRegistry.recipes)
                .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(skin.item),
                    SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_FLESHED),
                    "itemScrapingTool", "blockScrapingSurface", 4f));

            // Fleshed -> Clean
            setup.registry(SoakingRegistry.recipes)
                .add(new SoakingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_FLESHED),
                    SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 1000), 4000))
                // Salted fleshed skin can also be rinsed
                .add(new SoakingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_FLESHED, SkinTag::setSalted),
                    SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 1000), 4000));

            // Clean -> Preserved
            setup.registry(DryingRackRegistry.recipes)
                .add((DryingRackRecipe) DryingRackRecipe.builder()
                    .consumes(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN))
                    .produces(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_PRESERVED))
                    .dry()
                    .smoke()
                    .hours(12)
                    .build());

            // Preserved -> Clean
            setup.registry(SoakingRegistry.recipes)
                .add(new SoakingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_PRESERVED),
                    SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 1000), 8000));

            // Clean -> Prepared
            Fluid[] fluidsToLime = {TFCFluids.LIMEWATER, BidsFluids.weakWoodAshLye};
            for (Fluid fluid : fluidsToLime) {
                setup.registry(SoakingRegistry.recipes)
                    .add(new SoakingRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_CLEAN),
                        SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_PREPARED, tag -> tag.setFluid(fluid.getName())),
                        new FluidStack(fluid, skin.getLimingFluidAmount()), skin.getLimingTicks()));
            }

            // Prepared -> Dehaired
            setup.registry(ProcessingSurfaceRegistry.recipes)
                .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(skin.item, SkinTagAccess.STAGE_PREPARED),
                    SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal(skin.name)),
                    "itemScrapingTool", "blockScrapingSurface", skin.getDehairingEffort()));
        }

        // Salting dehaired
        setup.recipes()
            .add(new SkinSaltingRecipe(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, SkinTag::setSalted),
                new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));

        // Washing salt off of dehaired
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, FoodTag::setSalted),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                new FluidStack(TFCFluids.FRESHWATER, 200)));

        // Dehaired Skin -> Rawhide
        setup.registry(DryingFrameRegistry.recipes)
            .add((DryingFrameRecipe) DryingFrameRecipe.builder()
                .consumesTyingEquipment()
                .consumes(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED))
                .produces(SkinHelper.createStack(BidsItems.rawhide))
                .dry()
                .cover()
                .hours(12)
                .build());

        // Dehaired -> Tanned
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_TANNED),
                new FluidStack(TFCFluids.TANNIN, 200), 8000));

        // Tanned Skin -> Dried Skin
        setup.registry(DryingFrameRegistry.recipes)
            .add((DryingFrameRecipe) DryingFrameRecipe.builder()
                .consumesTyingEquipment()
                .consumes(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_TANNED))
                .produces(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DRIED))
                .dry()
                .cover()
                .hours(8)
                .build());

        // Dried Skin -> Worked Skin
        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DRIED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_WORKED),
                "itemLeatherSmoothingTool", "blockScrapingSurface", 0.5f));

        // Worked Skin -> Leather
        setup.registry(DryingFrameRegistry.recipes)
            .add((DryingFrameRecipe) DryingFrameRecipe.builder()
                .consumesTyingEquipment()
                .consumes(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_WORKED))
                .produces(SkinHelper.createStack(BidsItems.leather))
                .dry()
                .cover()
                .hours(8)
                .build());

        setup.recipes()
            .add(new SkinCuttingRecipe(SkinHelper.createStack(BidsItems.rawhide),
                "itemKnife", TFCItems.hide, new ItemStack(BidsItems.moreHide, 1, MoreHideMeta.VERY_SMALL_HIDE), null))
            .action(cutSkin())
            .action(damageTool("itemKnife"));

        setup.recipes()
            .add(new SkinCuttingRecipe(SkinHelper.createStack(BidsItems.leather),
                "itemKnife", null, null, new ItemStack(TFCItems.leather), null, new ItemStack(TFCItems.repairPatch, 1, RepairPatchMeta.LEATHER)))
            .action(cutSkin())
            .action(damageTool("itemKnife"));

        setup.recipes()
            .add(new SkinMergingRecipe(SkinHelper.createStack(BidsItems.leather),
                null, SkinHelper.SKIN_MAX_WEIGHT));

        setup.recipes()
            .add(new SkinMergingRecipe(SkinHelper.createStack(BidsItems.rawhide),
                null, SkinHelper.WEIGHT_MEDIUM - 0.01f));

        // Rawhide -> Dehaired
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.rawhide),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                new FluidStack(TFCFluids.FRESHWATER, 1000), 8000));

        // Very Small Raw Hide -> Dehaired
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(BidsItems.moreHide, 1, MoreHideMeta.VERY_SMALL_HIDE),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinHelper.WEIGHT_VERY_SMALL, SkinTagAccess.STAGE_DEHAIRED),
                new FluidStack(TFCFluids.FRESHWATER, 100), 1000));

        // TFC Raw Hide -> Dehaired
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(TFCItems.hide, 1, 0),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinHelper.WEIGHT_SMALL, SkinTagAccess.STAGE_DEHAIRED),
                new FluidStack(TFCFluids.FRESHWATER, 200), 2000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.hide, 1, 1),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_DEHAIRED),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.hide, 1, 2),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_DEHAIRED),
                new FluidStack(TFCFluids.FRESHWATER, 800), 8000));

        // TFC Fur -> Clean (Fur)
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(TFCItems.fur, 1, 0),
                SkinHelper.createStack(BidsItems.genericFur, SkinHelper.WEIGHT_SMALL, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 200), 2000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.fur, 1, 1),
                SkinHelper.createStack(BidsItems.genericFur, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.fur, 1, 2),
                SkinHelper.createStack(BidsItems.genericFur, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 800), 8000));

        // TFC Fur Scrap -> Clean (Fur)
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(TFCItems.furScrap, 1, 0),
                SkinHelper.createStack(BidsItems.genericFur, SkinHelper.WEIGHT_SMALL, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 200), 2000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.furScrap, 1, 1),
                SkinHelper.createStack(BidsItems.genericFur, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.furScrap, 1, 2),
                SkinHelper.createStack(BidsItems.genericFur, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 800), 8000));

        // TFC Wolf Fur -> Clean (Wolf Fur)
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(TFCItems.wolfFur, 1, 0),
                SkinHelper.createStack(BidsItems.wolfFur, SkinHelper.WEIGHT_SMALL, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 200), 2000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.wolfFur, 1, 1),
                SkinHelper.createStack(BidsItems.wolfFur, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.wolfFur, 1, 2),
                SkinHelper.createStack(BidsItems.wolfFur, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 800), 8000));

        // TFC Wolf Fur Scrap -> Clean (Wolf Fur)
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(TFCItems.wolfFurScrap, 1, 0),
                SkinHelper.createStack(BidsItems.wolfFur, SkinHelper.WEIGHT_SMALL, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 200), 2000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.wolfFurScrap, 1, 1),
                SkinHelper.createStack(BidsItems.wolfFur, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.wolfFurScrap, 1, 2),
                SkinHelper.createStack(BidsItems.wolfFur, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 800), 8000));

        // TFC Bear Fur -> Clean (Bear Fur)
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(TFCItems.bearFur, 1, 0),
                SkinHelper.createStack(BidsItems.bearFur, SkinHelper.WEIGHT_SMALL, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 200), 2000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.bearFur, 1, 1),
                SkinHelper.createStack(BidsItems.bearFur, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.bearFur, 1, 2),
                SkinHelper.createStack(BidsItems.bearFur, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 800), 8000));

        // TFC Bear Fur Scrap -> Clean (Bear Fur)
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(TFCItems.bearFurScrap, 1, 0),
                SkinHelper.createStack(BidsItems.bearFur, SkinHelper.WEIGHT_SMALL, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 200), 2000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.bearFurScrap, 1, 1),
                SkinHelper.createStack(BidsItems.bearFur, SkinHelper.WEIGHT_MEDIUM, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 400), 4000))
            .add(new SoakingRecipe(new ItemStack(TFCItems.bearFurScrap, 1, 2),
                SkinHelper.createStack(BidsItems.bearFur, SkinHelper.WEIGHT_LARGE, SkinTagAccess.STAGE_CLEAN),
                new FluidStack(TFCFluids.FRESHWATER, 800), 8000));
    }

}
