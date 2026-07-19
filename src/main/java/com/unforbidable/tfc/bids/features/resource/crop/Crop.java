package com.unforbidable.tfc.bids.features.resource.crop;

import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Enums.EnumRegion;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.common.render.FoodItemRenderer;
import com.unforbidable.tfc.bids.core.crop.BidsCropIndex;
import com.unforbidable.tfc.bids.core.crop.CropCoastAffinity;
import com.unforbidable.tfc.bids.core.crop.CropIds;
import com.unforbidable.tfc.bids.core.crop.CropRegistry;
import com.unforbidable.tfc.bids.core.crop.cultivation.CropCultivation;
import com.unforbidable.tfc.bids.core.crop.cultivation.CropCultivationTemp;
import com.unforbidable.tfc.bids.core.crop.render.CropRenderType;
import com.unforbidable.tfc.bids.core.crop.render.CropRenderer;
import com.unforbidable.tfc.bids.core.crop.render.CropRendererTFC;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.resource.crop.block.BlockNewCrop;
import com.unforbidable.tfc.bids.features.resource.crop.block.BlockNewFarmland;
import com.unforbidable.tfc.bids.features.resource.crop.eventhandler.CropChunkEventHandler;
import com.unforbidable.tfc.bids.features.resource.crop.eventhandler.CropPlayerInteractHandler;
import com.unforbidable.tfc.bids.features.resource.crop.eventhandler.FarmlandHighlightHandler;
import com.unforbidable.tfc.bids.features.resource.crop.item.ItemNewCustomSeeds;
import com.unforbidable.tfc.bids.features.resource.crop.render.RenderNewCrop;
import com.unforbidable.tfc.bids.features.resource.crop.tileentity.TileEntityNewCrop;
import com.unforbidable.tfc.bids.features.resource.crop.tileentity.TileEntityNewFarmland;
import com.unforbidable.tfc.bids.features.resource.crop.worldgen.CropWorldGen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.CROP;
import static com.unforbidable.tfc.bids.api.names.BlockNames.TILLED_SOIL;
import static com.unforbidable.tfc.bids.api.names.BlockNames.TILLED_SOIL2;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BEETROOT;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BROAD_BEANS;
import static com.unforbidable.tfc.bids.api.names.ItemNames.SEA_BEET;
import static com.unforbidable.tfc.bids.api.names.ItemNames.SUGAR_BEET;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WILD_BEANS;

@FeatureName("crop")
public class Crop extends Feature {


    @Override
    public void config(FeatureConfig config) {
        config.using(CropConfig::load, "crops");
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(CROP, BlockNewCrop::new)
            .fireInfo(5, 5);
        init.block(TILLED_SOIL, () -> new BlockNewFarmland(TFCBlocks.dirt, 0));
        init.block(TILLED_SOIL2, () -> new BlockNewFarmland(TFCBlocks.dirt2, 16));

        init.tileEntity(TileEntityNewCrop.class, "BidsNewCrop");
        init.tileEntity(TileEntityNewFarmland.class, "BidsNewFarmland");

        init.item(SEA_BEET, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 40, 10, 0))
            .food(1.4f, 0.2f);
        init.item(BEETROOT, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 10, 0, 0, 10, 30))
            .food(0.8f, 0.8f);
        init.item(SUGAR_BEET, () -> new ItemExtraFood(EnumFoodGroup.Vegetable, 60, 0, 0, 0, 0))
            .food(0.8f, 0.8f);
        init.item(WILD_BEANS, () -> new ItemExtraFood(EnumFoodGroup.Protein, 10, 0, 0, 10, 20))
            .food(0.5f, 0.2f)
            .apply(i -> i.setCookTempIndex(1));
        init.item(BROAD_BEANS, () -> new ItemExtraFood(EnumFoodGroup.Protein, 10, 0, 0, 10, 40))
            .food(0.25f, 0.2f)
            .apply(i -> i.setCookTempIndex(1));

        init.item(ItemNames.SEEDS_SEA_BEET, () -> new ItemNewCustomSeeds(CropIds.SEABEET));
        init.item(ItemNames.SEEDS_BEETROOT, () -> new ItemNewCustomSeeds(CropIds.BEETROOT));
        init.item(ItemNames.SEEDS_SUGAR_BEET, () -> new ItemNewCustomSeeds(CropIds.SUGARBEET));
        init.item(ItemNames.SEEDS_WILD_BEAN, () -> new ItemNewCustomSeeds(CropIds.WILDBEANS));
        init.item(ItemNames.SEEDS_BROAD_BEAN, () -> new ItemNewCustomSeeds(CropIds.BROADBEANS));
        init.item(ItemNames.SEEDS_NEW_WHEAT, () -> new ItemNewCustomSeeds(CropIds.WHEAT));
        init.item(ItemNames.SEEDS_NEW_OAT, () -> new ItemNewCustomSeeds(CropIds.OAT));
        init.item(ItemNames.SEEDS_NEW_BARLEY, () -> new ItemNewCustomSeeds(CropIds.BARLEY));
        init.item(ItemNames.SEEDS_NEW_RYE, () -> new ItemNewCustomSeeds(CropIds.RYE));
        init.item(ItemNames.SEEDS_WINTER_WHEAT, () -> new ItemNewCustomSeeds(CropIds.WINTERWHEAT));
        init.item(ItemNames.SEEDS_WINTER_OAT, () -> new ItemNewCustomSeeds(CropIds.WINTEROAT));
        init.item(ItemNames.SEEDS_WINTER_BARLEY, () -> new ItemNewCustomSeeds(CropIds.WINTERBARLEY));
        init.item(ItemNames.SEEDS_WINTER_RYE, () -> new ItemNewCustomSeeds(CropIds.WINTERRYE));
        init.item(ItemNames.SEEDS_NEW_ONION, () -> new ItemNewCustomSeeds(CropIds.ONION));
        init.item(ItemNames.SEEDS_NEW_CABBAGE, () -> new ItemNewCustomSeeds(CropIds.CABBAGE));
        init.item(ItemNames.SEEDS_NEW_GARLIC, () -> new ItemNewCustomSeeds(CropIds.GARLIC));
        init.item(ItemNames.SEEDS_NEW_CARROT, () -> new ItemNewCustomSeeds(CropIds.CARROT));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderNewCrop())
            .block(BlockNewCrop.class);

        client.render(new FoodItemRenderer())
            .item(BidsItems.seaBeet)
            .item(BidsItems.beetroot)
            .item(BidsItems.sugarBeet)
            .item(BidsItems.wildBeans)
            .item(BidsItems.broadBeans);

        client.event()
            .handler(new FarmlandHighlightHandler());

        client.nei()
            .hide(BidsBlocks.newCrops)
            .hide(BidsBlocks.newTilledSoil)
            .hide(BidsBlocks.newTilledSoil2);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new CropPlayerInteractHandler())
            .handler(new CropChunkEventHandler());

        setup.world()
            .gen(new CropWorldGen(), 0);

        setup.ores("seedCultivated")
            .add(BidsItems.seedsBeetroot)
            .add(new ItemStack(BidsItems.seedsSugarBeet))
            .add(new ItemStack(BidsItems.seedsBroadBeans))
            .add(new ItemStack(BidsItems.seedsWinterBarley))
            .add(new ItemStack(BidsItems.seedsWinterOat))
            .add(new ItemStack(BidsItems.seedsWinterRye))
            .add(new ItemStack(BidsItems.seedsWinterWheat));

        setup.ores("seedWinterCereal")
            .add(new ItemStack(BidsItems.seedsWinterBarley))
            .add(new ItemStack(BidsItems.seedsWinterOat))
            .add(new ItemStack(BidsItems.seedsWinterRye))
            .add(new ItemStack(BidsItems.seedsWinterWheat));

        // Manual seed conversion
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsBarley), BidsItems.seedsNewBarley);
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsOat), BidsItems.seedsNewOat);
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsRye), BidsItems.seedsNewRye);
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsWheat), BidsItems.seedsNewWheat);
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsOnion), BidsItems.seedsNewOnion);
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsCabbage), BidsItems.seedsNewCabbage);
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsGarlic), BidsItems.seedsNewGarlic);
        setup.recipes().addShapeless(new ItemStack(TFCItems.seedsCarrot), BidsItems.seedsNewCarrot);

        // Reverse manual seed conversion
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewBarley), TFCItems.seedsBarley);
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewOat), TFCItems.seedsOat);
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewRye), TFCItems.seedsRye);
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewWheat), TFCItems.seedsWheat);
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewOnion), TFCItems.seedsOnion);
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewCabbage), TFCItems.seedsCabbage);
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewGarlic), TFCItems.seedsGarlic);
        setup.recipes().addShapeless(new ItemStack(BidsItems.seedsNewCarrot), TFCItems.seedsCarrot);

        // TODO allow wild beans growing in warmer areas (FEATURE)

        setup.registry(CropRegistry.crops)
            .add(BidsCropIndex.builder(CropIds.SEABEET, "seebeet")
                .grows(1, 16, 4, 4, 5, 0, 0.25f)
                .generates(new EnumRegion[]{EnumRegion.EUROPE, EnumRegion.AFRICA, EnumRegion.ASIA},
                    150, 3000, 1, 18, CropCoastAffinity.COAST_ONLY)
                .withCommonness(7)
                .giveSkillWildHarvestChance(25)
                .dropsSeed(BidsItems.seedsSeaBeet, 50)
                .dropsOutput(BidsItems.seaBeet, 8f)
                .canBeCultivated(new CropCultivation(BidsItems.seedsBeetroot, 1, 0.05f))
                .renders(new CropRenderer(CropRenderType.CROSSED_SQUARES, 0.40, 0.8, "Sea Beet"))
                .build())
            .add(BidsCropIndex.builder(CropIds.BEETROOT, "beetroot")
                .grows(1, 24, 4, 3, 5, 0, 0.9f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsBeetroot)
                .dropsOutput(BidsItems.beetroot, 20f)
                .canBeCultivated(new CropCultivation(BidsItems.seedsSugarBeet, 2, 0.1f))
                .renders(new CropRenderer(CropRenderType.CROSSED_SQUARES, 0.45, 1, "Beetroot"))
                .build())
            .add(BidsCropIndex.builder(CropIds.SUGARBEET, "sugarbeet")
                .grows(1, 28, 4, 3, 6, 0, 1.2f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsSugarBeet)
                .dropsOutput(BidsItems.sugarBeet, 24f)
                .renders(new CropRenderer(CropRenderType.CROSSED_SQUARES, 0.45, 1, "Sugar Beet"))
                .build())
            .add(BidsCropIndex.builder(CropIds.WILDBEANS, "wildbeans")
                .grows(1, 28, 7, 4, 5, 0, 0.4f)
                .restoresNutrients(5, 0, 5)
                .generates(new EnumRegion[]{EnumRegion.AFRICA, EnumRegion.ASIA},
                    120, 1200, 3, 18, CropCoastAffinity.INLAND_ONLY)
                .dropsSeed(BidsItems.seedsWildBeans)
                .dropsOutput(BidsItems.wildBeans, 10f)
                .canBeCultivated(new CropCultivation(BidsItems.seedsBroadBeans, 1, 0.1f))
                .renders(new CropRenderer(CropRenderType.CROSSED_SQUARES, 0.40, 0.8, "Wild Beans"))
                .build())
            .add(BidsCropIndex.builder(CropIds.BROADBEANS, "broadbeens")
                .grows(1, 24, 7, 4, 5, 0, 0.8f)
                .restoresNutrients(10, 0, 10)
                .requiresPole()
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsBroadBeans)
                .dropsOutput(BidsItems.broadBeans, 20f)
                .renders(new CropRenderer(CropRenderType.CROSSED_SQUARES, 0.45, 1, "Broad Beans"))
                .build())
            .add(BidsCropIndex.builder(CropIds.BARLEY, "barley")
                .grows(0, 33, 7, 4, 4, 0, 0.85f)
                .dropsSeed(BidsItems.seedsNewBarley)
                .dropsOutput(TFCItems.barleyWhole, 14.0f * 2)
                .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterBarley, 1, 0.1f, 10f))
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Barley"))
                .build())
            .add(BidsCropIndex.builder(CropIds.OAT, "oat")
                .grows(0, 32, 7, 4, 4, 0, 0.9f)
                .dropsSeed(BidsItems.seedsNewOat)
                .dropsOutput(TFCItems.oatWhole, 14.0f * 2)
                .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterOat, 1, 0.1f, 10f))
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Oat"))
                .build())
            .add(BidsCropIndex.builder(CropIds.RYE, "rye")
                .grows(0, 32, 7, 4, 4, 0, 0.9f)
                .dropsSeed(BidsItems.seedsNewRye)
                .dropsOutput(TFCItems.ryeWhole, 14.0f * 2)
                .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterRye, 1, 0.1f, 10f))
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Rye"))
                .build())
            .add(BidsCropIndex.builder(CropIds.WHEAT, "wheat")
                .grows(0, 32, 7, 4, 4, 0, 0.9f)
                .dropsSeed(BidsItems.seedsNewWheat)
                .dropsOutput(TFCItems.wheatWhole, 14.0f * 2)
                .canBeCultivated(new CropCultivationTemp(BidsItems.seedsWinterWheat, 1, 0.1f, 10f))
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Wheat"))
                .build())
            .add(BidsCropIndex.builder(CropIds.WINTERBARLEY, "winterbarley")
                .grows(0, 33, 7, 4, 4, 0, 0.85f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsWinterBarley)
                .dropsOutput(TFCItems.barleyWhole, 14.0f * 2)
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Barley"))
                .build())
            .add(BidsCropIndex.builder(CropIds.WINTEROAT, "winteroat")
                .grows(0, 32, 7, 4, 4, 0, 0.9f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsWinterOat)
                .dropsOutput(TFCItems.oatWhole, 14.0f * 2)
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Oat"))
                .build())
            .add(BidsCropIndex.builder(CropIds.WINTERRYE, "winterrye")
                .grows(0, 32, 7, 4, 4, 0, 0.9f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsWinterRye)
                .dropsOutput(TFCItems.ryeWhole, 14.0f * 2)
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Rye"))
                .build())
            .add(BidsCropIndex.builder(CropIds.WINTERWHEAT, "winterwheat")
                .grows(0, 32, 7, 4, 4, 0, 0.9f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsWinterWheat)
                .dropsOutput(TFCItems.wheatWhole, 14.0f * 2)
                .renders(new CropRendererTFC(CropRenderType.BLOCK, 0.5, 1, "Wheat"))
                .build())
            .add(BidsCropIndex.builder(CropIds.ONION, "onion")
                .grows(1, 16, 6, 3, 8, 0, 1.2f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsNewOnion)
                .dropsOutput(TFCItems.onion, 36f)
                .renders(new CropRendererTFC(CropRenderType.CROSSED_SQUARES, 0.45, 1, "Onion"))
                .build())
            .add(BidsCropIndex.builder(CropIds.CABBAGE, "cabbage")
                .grows(1, 29, 5, 3, 7, 0, 0.9f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsNewCabbage)
                .dropsOutput(TFCItems.cabbage, 32f)
                .renders(new CropRendererTFC(CropRenderType.CROSSED_SQUARES, 0.45, 1, "Cabbage"))
                .build())
            .add(BidsCropIndex.builder(CropIds.GARLIC, "garlic")
                .grows(2, 25, 4, 3, 8, 0, 0.5f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsNewGarlic)
                .dropsOutput(TFCItems.garlic, 20f)
                .renders(new CropRendererTFC(CropRenderType.CROSSED_SQUARES, 0.45, 1, "Garlic"))
                .build())
            .add(BidsCropIndex.builder(CropIds.CARROT, "carrot")
                .grows(2, 23, 4, 3, 8, 0, 0.75f)
                .goesDormantInFrost()
                .dropsSeed(BidsItems.seedsNewCarrot)
                .dropsOutput(TFCItems.carrot, 30f)
                .renders(new CropRendererTFC(CropRenderType.CROSSED_SQUARES, 0.45, 1, "Carrots"))
                .build());
    }

}
