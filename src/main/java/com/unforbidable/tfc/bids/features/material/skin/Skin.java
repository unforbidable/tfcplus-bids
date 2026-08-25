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
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinShearingRecipe;
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
import com.unforbidable.tfc.bids.features.material.skin.render.SkinItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.features.material.skin.crafting.action.CutSkin.cutSkin;
import static com.unforbidable.tfc.bids.features.material.skin.crafting.action.ShearSkin.shearSkin;

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
            .handler(new SkinDryingHandler());

        setup.recipes()
            .add(new SkinCuttingRecipe(SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_PRESERVED),
                "itemKnife", TFCItems.furScrap, null, new ItemStack(TFCItems.repairPatch, 1, RepairPatchMeta.FUR)))
            .action(cutSkin())
            .action(damageTool("itemKnife"));

        setup.recipes()
            .add(new SkinCuttingRecipe(SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_PRESERVED),
                "itemKnife", TFCItems.wolfFurScrap, null, new ItemStack(TFCItems.repairPatch, 1, RepairPatchMeta.WOLF_FUR)))
            .action(cutSkin())
            .action(damageTool("itemKnife"));

        setup.recipes()
            .add(new SkinCuttingRecipe(SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_PRESERVED),
                "itemKnife", TFCItems.bearFurScrap, null, new ItemStack(TFCItems.repairPatch, 1, RepairPatchMeta.BEAR_FUR)))
            .action(cutSkin())
            .action(damageTool("itemKnife"));

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
            .add(new SkinShearingRecipe(SkinHelper.createStack(BidsItems.sheepSkin, SkinTagAccess.STAGE_PRESERVED),
                "itemKnife", SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_PRESERVED, tag -> tag.setAnimal("sheepTFC.sheared"))))
            .action(shearSkin(new ItemStack(TFCItems.wool)))
            .action(damageTool("itemKnife"));

        setup.recipes()
            .add(new SkinMergingRecipe(SkinHelper.createStack(BidsItems.leather),
                null, SkinHelper.SKIN_MAX_WEIGHT));

        setup.recipes()
            .add(new SkinMergingRecipe(SkinHelper.createStack(BidsItems.rawhide),
                null, SkinHelper.WEIGHT_MEDIUM - 0.01f));

        setup.recipes()
            .add(new SkinMergingRecipe(SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_PRESERVED),
                null, SkinHelper.WEIGHT_MEDIUM - 0.01f));

        setup.recipes()
            .add(new SkinMergingRecipe(SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_PRESERVED),
                null, SkinHelper.WEIGHT_MEDIUM - 0.01f));

        setup.recipes()
            .add(new SkinMergingRecipe(SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_PRESERVED),
                null, SkinHelper.WEIGHT_MEDIUM - 0.01f));

        Item[] skins = {BidsItems.genericSkin, BidsItems.genericFur, BidsItems.sheepSkin, BidsItems.wolfFur, BidsItems.bearFur};

        // Salting
        for (Item skin : skins) {
            setup.recipes()
                .add(new SkinSaltingRecipe(SkinHelper.createStack(skin),
                    SkinHelper.createStack(skin, SkinTag::setSalted), new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));

            setup.recipes()
                .add(new SkinSaltingRecipe(SkinHelper.createStack(skin, SkinTagAccess.STAGE_FLESHED),
                    SkinHelper.createStack(skin, SkinTagAccess.STAGE_FLESHED, SkinTag::setSalted), new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));

            setup.recipes()
                .add(new SkinSaltingRecipe(SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN),
                    SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN, SkinTag::setSalted), new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));

            // Washing salt off of clean skin, so it can be prepared for dehairing
            setup.registry(SoakingRegistry.recipes)
                .add(new SoakingRecipe(SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN, FoodTag::setSalted),
                    SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 200)));
        }

        setup.recipes()
            .add(new SkinSaltingRecipe(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, SkinTag::setSalted),
                new ItemStack(TFCItems.powder, 1, PowderMeta.SALT)));

        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, FoodTag::setSalted),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                new FluidStack(TFCFluids.FRESHWATER, 200)));

        // Fresh -> Fleshed
        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericFur),
                SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericSkin),
                SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.sheepSkin),
                SkinHelper.createStack(BidsItems.sheepSkin, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.wolfFur),
                SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.bearFur),
                SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f));

        // Prepared -> Dehaired
        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                "itemScrapingTool", "blockScrapingSurface", 2f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.sheepSkin, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal("sheepTFC")),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal("wolfTFC")),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new ProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal("bearTFC")),
                "itemScrapingTool", "blockScrapingSurface", 6f));

        // Fleshed -> Clean
        for (Item skin : skins) {
            setup.registry(SoakingRegistry.recipes)
                .add(new SoakingRecipe(SkinHelper.createStack(skin, SkinTagAccess.STAGE_FLESHED),
                    SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 1000), 4000))
                // Salted fleshed skin can also be rinsed
                .add(new SoakingRecipe(SkinHelper.createStack(skin, SkinTagAccess.STAGE_FLESHED, SkinTag::setSalted),
                    SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 1000), 4000));
        }

        // Clean -> Prepared
        Fluid[] fluidsToLime = {TFCFluids.LIMEWATER, BidsFluids.weakWoodAshLye};
        for (Fluid fluid : fluidsToLime) {
            setup.registry(SoakingRegistry.recipes)
                .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_CLEAN),
                    SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_PREPARED, tag -> tag.setFluid(fluid.getName())),
                    new FluidStack(fluid, 400), 8000))
                .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_CLEAN),
                    SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_PREPARED, tag -> tag.setFluid(fluid.getName())),
                    new FluidStack(fluid, 200), 4000))
                .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.sheepSkin, SkinTagAccess.STAGE_CLEAN),
                    SkinHelper.createStack(BidsItems.sheepSkin, SkinTagAccess.STAGE_PREPARED, tag -> tag.setFluid(fluid.getName())),
                    new FluidStack(fluid, 400), 8000))
                .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_CLEAN),
                    SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_PREPARED, tag -> tag.setFluid(fluid.getName())),
                    new FluidStack(fluid, 400), 8000))
                .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_CLEAN),
                    SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_PREPARED, tag -> tag.setFluid(fluid.getName())),
                    new FluidStack(fluid, 600), 12000));
        }

        // Dehaired -> Tanned
        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_TANNED),
                new FluidStack(TFCFluids.TANNIN, 1000), 8000));

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

        // Preserved -> Clean
        for (Item skin : skins) {
            setup.registry(SoakingRegistry.recipes)
                .add(new SoakingRecipe(SkinHelper.createStack(skin, SkinTagAccess.STAGE_PRESERVED),
                    SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN),
                    new FluidStack(TFCFluids.FRESHWATER, 1000), 8000));
        }

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

        // Clean -> Preserved
        for (Item skin : skins) {
            setup.registry(DryingRackRegistry.recipes)
                .add((DryingRackRecipe) DryingRackRecipe.builder()
                    .consumes(SkinHelper.createStack(skin, SkinTagAccess.STAGE_CLEAN))
                    .produces(SkinHelper.createStack(skin, SkinTagAccess.STAGE_PRESERVED))
                    .dry()
                    .smoke()
                    .hours(12)
                    .build());
        }

        // Dehaired Skin -> Rawhide0
        setup.registry(DryingFrameRegistry.recipes)
            .add((DryingFrameRecipe) DryingFrameRecipe.builder()
                .consumesTyingEquipment()
                .consumes(SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED))
                .produces(SkinHelper.createStack(BidsItems.rawhide))
                .dry()
                .cover()
                .hours(12)
                .build());

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
    }

}
