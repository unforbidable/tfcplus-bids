package com.unforbidable.tfc.bids.features.utility.drill;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonPotteryMold;
import com.unforbidable.tfc.bids.common.item.ItemCommonToolHead;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.names.AnvilRules;
import com.unforbidable.tfc.bids.compat.tfc.names.Metals;
import com.unforbidable.tfc.bids.compat.tfc.names.Skills;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilPlan;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KilnRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.PartialMold;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.stone.EnumStoneItemType;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneIndex;
import com.unforbidable.tfc.bids.core.schemes.stone.StoneScheme;
import com.unforbidable.tfc.bids.features.utility.drill.item.ItemDrill;
import net.minecraft.item.ItemStack;

import static com.dunk.tfc.Core.Recipes.getStackNoTemp;

/**
 * <li>drill - tool from stone and metals up to wrought iron</li>
 */
@FeatureName("drill")
public class Drill extends Feature {

    private static final String DRILL_PLAN = "drill";

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.DRILL_HEAD_STONE_SED, () -> new ItemCommonToolHead(TFCItems.sedToolMaterial));
        init.item(ItemNames.DRILL_HEAD_STONE_MM, () -> new ItemCommonToolHead(TFCItems.mMToolMaterial));
        init.item(ItemNames.DRILL_HEAD_STONE_IG_EX, () -> new ItemCommonToolHead(TFCItems.igExToolMaterial));
        init.item(ItemNames.DRILL_HEAD_STONE_IG_IN, () -> new ItemCommonToolHead(TFCItems.igInToolMaterial));
        init.item(ItemNames.DRILL_HEAD_COPPER, () -> new ItemCommonToolHead(TFCItems.copperToolMaterial));
        init.item(ItemNames.DRILL_HEAD_BRONZE, () -> new ItemCommonToolHead(TFCItems.bronzeToolMaterial));
        init.item(ItemNames.DRILL_HEAD_BISMUTH_BRONZE, () -> new ItemCommonToolHead(TFCItems.bismuthBronzeToolMaterial));
        init.item(ItemNames.DRILL_HEAD_BLACK_BRONZE, () -> new ItemCommonToolHead(TFCItems.blackBronzeToolMaterial));
        init.item(ItemNames.DRILL_HEAD_WROUGHT_IRON, () -> new ItemCommonToolHead(TFCItems.ironToolMaterial));

        init.item(ItemNames.DRILL_STONE_SED, () -> new ItemDrill(TFCItems.sedToolMaterial));
        init.item(ItemNames.DRILL_STONE_MM, () -> new ItemDrill(TFCItems.mMToolMaterial));
        init.item(ItemNames.DRILL_STONE_IG_EX, () -> new ItemDrill(TFCItems.igExToolMaterial));
        init.item(ItemNames.DRILL_STONE_IG_IN, () -> new ItemDrill(TFCItems.igInToolMaterial));
        init.item(ItemNames.DRILL_COPPER, () -> new ItemDrill(TFCItems.copperToolMaterial));
        init.item(ItemNames.DRILL_BRONZE, () -> new ItemDrill(TFCItems.bronzeToolMaterial));
        init.item(ItemNames.DRILL_BISMUTH_BRONZE, () -> new ItemDrill(TFCItems.bismuthBronzeToolMaterial));
        init.item(ItemNames.DRILL_BLACK_BRONZE, () -> new ItemDrill(TFCItems.blackBronzeToolMaterial));
        init.item(ItemNames.DRILL_WROUGHT_IRON, () -> new ItemDrill(TFCItems.ironToolMaterial));

        init.item(ItemNames.DRILL_MOLD, ItemCommonPotteryMold::new)
            .mold(4, Metals.COPPER, Metals.BRONZE, Metals.BISMUTHBRONZE, Metals.BLACKBRONZE)
            .meta("Clay", "Ceramic", "Copper", "Bronze", "Bismuth Bronze", "Black Bronze");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemDrillHead")
            .add(BidsItems.sedStoneDrillHead, BidsItems.mMStoneDrillHead, BidsItems.igInStoneDrillHead, BidsItems.igExStoneDrillHead)
            .add(BidsItems.copperDrillHead)
            .add(BidsItems.bronzeDrillHead)
            .add(BidsItems.bismuthBronzeDrillHead)
            .add(BidsItems.blackBronzeDrillHead)
            .add(BidsItems.wroughtIronDrillHead);

        setup.ores("itemDrill")
            .add(BidsItems.sedStoneDrill, BidsItems.mMStoneDrill, BidsItems.igInStoneDrill, BidsItems.igExStoneDrill)
            .add(BidsItems.copperDrill)
            .add(BidsItems.bronzeDrill, BidsItems.bismuthBronzeDrill, BidsItems.blackBronzeDrill)
            .add(BidsItems.wroughtIronDrill);

        setup.ores("itemDrillStone")
            .add(BidsItems.sedStoneDrill, BidsItems.mMStoneDrill, BidsItems.igInStoneDrill, BidsItems.igExStoneDrill);

        setup.ores("itemDrillMetal")
            .add(BidsItems.copperDrill)
            .add(BidsItems.bronzeDrill, BidsItems.bismuthBronzeDrill, BidsItems.blackBronzeDrill)
            .add(BidsItems.wroughtIronDrill);

        setup.recipes().addShapeless(new ItemStack(BidsItems.copperDrillHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 2)));
        setup.recipes().addShapeless(new ItemStack(BidsItems.bronzeDrillHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 3)));
        setup.recipes().addShapeless(new ItemStack(BidsItems.bismuthBronzeDrillHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 4)));
        setup.recipes().addShapeless(new ItemStack(BidsItems.blackBronzeDrillHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldDrill, 1, 5)));

        setup.recipes().addShapeless(new ItemStack(BidsItems.igInStoneDrill),
            BidsItems.igInStoneDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.sedStoneDrill),
            BidsItems.sedStoneDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.igExStoneDrill),
            BidsItems.igExStoneDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.mMStoneDrill),
            BidsItems.mMStoneDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.copperDrill),
            BidsItems.copperDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.bronzeDrill),
            BidsItems.bronzeDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.bismuthBronzeDrill),
            BidsItems.bismuthBronzeDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.blackBronzeDrill),
            BidsItems.blackBronzeDrillHead, "stickWood", TFCItems.bow);
        setup.recipes().addShapeless(new ItemStack(BidsItems.wroughtIronDrill),
            BidsItems.wroughtIronDrillHead, "stickWood", TFCItems.bow);

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            setup.registry(TfcRegistry.Knapping.recipes)
                .add(KnappingRecipe.add(stone.items.getItem(EnumStoneItemType.DRILL_HEAD),
                    "     ", " ### ", "#####", " ### ", "  #  ",
                    '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK)));
        }

        setup.registry(TfcRegistry.Knapping.recipes)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.clayMoldDrill),
                "  #  ", "  #  ", "  #  ", " ### ", "  #  ",
                '#', new ItemStack(TFCItems.flatClay, 1, 1)));

        setup.registry(TfcRegistry.Kiln.recipes)
            .add(KilnRecipe.add(new ItemStack(BidsItems.clayMoldDrill), 0,
                new ItemStack(BidsItems.clayMoldDrill, 1, 1)));

        setup.registry(TfcRegistry.Metal.molds)
            .add(PartialMold.add(Metals.COPPER, BidsItems.clayMoldDrill, 2, BidsItems.clayMoldDrill, 4, 2))
            .add(PartialMold.add(Metals.BRONZE, BidsItems.clayMoldDrill, 3, BidsItems.clayMoldDrill, 4, 2))
            .add(PartialMold.add(Metals.BISMUTHBRONZE, BidsItems.clayMoldDrill, 4, BidsItems.clayMoldDrill, 4, 2))
            .add(PartialMold.add(Metals.BLACKBRONZE, BidsItems.clayMoldDrill, 5, BidsItems.clayMoldDrill, 4, 2));

        setup.registry(TfcRegistry.Anvil.plans)
            .add(AnvilPlan.add(DRILL_PLAN, AnvilRules.HITLAST, AnvilRules.PUNCHNOTLAST, AnvilRules.DRAWNOTLAST));

        setup.registry(TfcRegistry.Anvil.recipes)
            .add(AnvilRecipe.add(new ItemStack(TFCItems.copperIngot), null, DRILL_PLAN, 1,
                new ItemStack(BidsItems.copperDrillHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.bronzeIngot), null, DRILL_PLAN, 2,
                new ItemStack(BidsItems.bronzeDrillHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.bismuthBronzeIngot), null, DRILL_PLAN, 2,
                new ItemStack(BidsItems.bismuthBronzeDrillHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.blackBronzeIngot), null, DRILL_PLAN, 2,
                new ItemStack(BidsItems.blackBronzeDrillHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.wroughtIronIngot), null, DRILL_PLAN, 3,
                new ItemStack(BidsItems.wroughtIronDrillHead), Skills.TOOLSMITH));
    }

}
