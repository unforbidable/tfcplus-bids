package com.unforbidable.tfc.bids.features.material.skin;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.processing.ProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.processing.SkinProcessingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.meta.MoreHideMeta;
import com.unforbidable.tfc.bids.api.names.GuiNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.api.util.nbt.SkinTagAccess;
import com.unforbidable.tfc.bids.compat.tfc.meta.RepairPatchMeta;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.processingsurface.ProcessingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.material.skin.container.ContainerSpecialCraftingSkin;
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinCuttingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinMergingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinShearingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.eventhandler.SkinLivingDropsEventHandler;
import com.unforbidable.tfc.bids.features.material.skin.gui.GuiKnappingSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemDehairedSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFinishedSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFreshSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.render.SkinItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

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
            .handler(new SkinLivingDropsEventHandler());

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
                "itemKnife", SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_PRESERVED, tag -> tag.setAnimal("sheepTFC"))))
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

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericFur),
                SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericSkin),
                SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.sheepSkin),
                SkinHelper.createStack(BidsItems.sheepSkin, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.wolfFur),
                SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.bearFur),
                SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_FLESHED),
                "itemScrapingTool", "blockScrapingSurface", 4f));

        setup.registry(ProcessingSurfaceRegistry.recipes)
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericFur, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                "itemScrapingTool", "blockScrapingSurface", 6f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.genericSkin, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED),
                "itemScrapingTool", "blockScrapingSurface", 2f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.sheepSkin, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal("sheepTFC")),
                "itemScrapingTool", "blockScrapingSurface", 8f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.wolfFur, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal("wolfTFC")),
                "itemScrapingTool", "blockScrapingSurface", 8f))
            .add(new SkinProcessingSurfaceRecipe(SkinHelper.createStack(BidsItems.bearFur, SkinTagAccess.STAGE_PREPARED),
                SkinHelper.createStack(BidsItems.dehairedSkin, SkinTagAccess.STAGE_DEHAIRED, tag -> tag.setAnimal("bearTFC")),
                "itemScrapingTool", "blockScrapingSurface", 10f));
    }

}
