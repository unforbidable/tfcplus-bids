package com.unforbidable.tfc.bids.features.utility.adze;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.google.common.collect.Sets;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
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
import com.unforbidable.tfc.bids.features.utility.adze.item.ItemAdze;
import net.minecraft.item.ItemStack;

import static com.dunk.tfc.Core.Recipes.getStackNoTemp;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_BISMUTH_BRONZE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_BLACK_BRONZE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_BRONZE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_COPPER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_BISMUTH_BRONZE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_BLACK_BRONZE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_BRONZE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_COPPER;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_STONE_IG_EX;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_STONE_IG_IN;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_STONE_MM;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_STONE_SED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_HEAD_WROUGHT_IRON;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_MOLD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_STONE_IG_EX;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_STONE_IG_IN;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_STONE_MM;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_STONE_SED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.ADZE_WROUGHT_IRON;

/**
 * <li>adze - tool from stone and metals up to wrought iron</li>
 */
@FeatureName("adze")
public class Adze extends Feature {

    private static final String ADZE_PLAN = "adze";

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ADZE_HEAD_STONE_SED, () -> new ItemCommonToolHead(TFCItems.sedToolMaterial));
        init.item(ADZE_HEAD_STONE_MM, () -> new ItemCommonToolHead(TFCItems.mMToolMaterial));
        init.item(ADZE_HEAD_STONE_IG_EX, () -> new ItemCommonToolHead(TFCItems.igExToolMaterial));
        init.item(ADZE_HEAD_STONE_IG_IN, () -> new ItemCommonToolHead(TFCItems.igInToolMaterial));
        init.item(ADZE_HEAD_COPPER, () -> new ItemCommonToolHead(TFCItems.copperToolMaterial));
        init.item(ADZE_HEAD_BRONZE, () -> new ItemCommonToolHead(TFCItems.bronzeToolMaterial));
        init.item(ADZE_HEAD_BISMUTH_BRONZE, () -> new ItemCommonToolHead(TFCItems.bismuthBronzeToolMaterial));
        init.item(ADZE_HEAD_BLACK_BRONZE, () -> new ItemCommonToolHead(TFCItems.blackBronzeToolMaterial));
        init.item(ADZE_HEAD_WROUGHT_IRON, () -> new ItemCommonToolHead(TFCItems.ironToolMaterial));

        init.item(ADZE_STONE_SED, () -> new ItemAdze(TFCItems.sedToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_STONE_MM, () -> new ItemAdze(TFCItems.mMToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_STONE_IG_EX, () -> new ItemAdze(TFCItems.igExToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_STONE_IG_IN, () -> new ItemAdze(TFCItems.igInToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_COPPER, () -> new ItemAdze(TFCItems.copperToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_BRONZE, () -> new ItemAdze(TFCItems.bronzeToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_BISMUTH_BRONZE, () -> new ItemAdze(TFCItems.bismuthBronzeToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_BLACK_BRONZE, () -> new ItemAdze(TFCItems.blackBronzeToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);
        init.item(ADZE_WROUGHT_IRON, () -> new ItemAdze(TFCItems.ironToolMaterial))
            .harvest("shovel", 1)
            .harvest("axe", 1);

        init.item(ADZE_MOLD, ItemCommonPotteryMold::new)
            .mold(4, Metals.COPPER, Metals.BRONZE, Metals.BISMUTHBRONZE, Metals.BLACKBRONZE)
            .meta("Clay", "Ceramic", "Copper", "Bronze", "Bismuth Bronze", "Black Bronze");
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // TODO see if using new too class "adze" is possible
        ItemAdze.effectiveAgainstBlocks.addAll(Sets.newHashSet(BidsBlocks.carvingRock, BidsBlocks.carvingWood,
            BidsBlocks.roughStoneSed, BidsBlocks.roughStoneBrickSed, BidsBlocks.roughStoneTileSed,
            BidsBlocks.mudBrickChimney, BidsBlocks.mudBrickChimney2,
            TFCBlocks.mudBricks, TFCBlocks.mudBricks2));

        setup.ores("itemAdzeHead")
            .add(BidsItems.sedStoneAdzeHead, BidsItems.mMStoneAdzeHead, BidsItems.igInStoneAdzeHead, BidsItems.igExStoneAdzeHead)
            .add(BidsItems.copperAdzeHead)
            .add(BidsItems.bronzeAdzeHead)
            .add(BidsItems.bismuthBronzeAdzeHead)
            .add(BidsItems.blackBronzeAdzeHead)
            .add(BidsItems.wroughtIronAdzeHead);

        setup.ores("itemAdze")
            .add(BidsItems.sedStoneAdze, BidsItems.mMStoneAdze, BidsItems.igInStoneAdze, BidsItems.igExStoneAdze)
            .add(BidsItems.copperAdze)
            .add(BidsItems.bronzeAdze, BidsItems.bismuthBronzeAdze, BidsItems.blackBronzeAdze)
            .add(BidsItems.wroughtIronAdze);

        setup.ores("itemAdzeStone")
            .add(BidsItems.sedStoneAdze, BidsItems.mMStoneAdze, BidsItems.igInStoneAdze, BidsItems.igExStoneAdze);

        setup.ores("itemAdzeMetal")
            .add(BidsItems.copperAdze)
            .add(BidsItems.bronzeAdze, BidsItems.bismuthBronzeAdze, BidsItems.blackBronzeAdze)
            .add(BidsItems.wroughtIronAdze);

        setup.recipes().addShapeless(new ItemStack(BidsItems.copperAdzeHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 2)));
        setup.recipes().addShapeless(new ItemStack(BidsItems.bronzeAdzeHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 3)));
        setup.recipes().addShapeless(new ItemStack(BidsItems.bismuthBronzeAdzeHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 4)));
        setup.recipes().addShapeless(new ItemStack(BidsItems.blackBronzeAdzeHead),
            getStackNoTemp(new ItemStack(BidsItems.clayMoldAdze, 1, 5)));

        setup.recipes().addShaped(new ItemStack(BidsItems.igInStoneAdze),
            "1", "2", '1', BidsItems.igInStoneAdzeHead, '2', "stickWood");
        setup.recipes().addShaped(new ItemStack(BidsItems.sedStoneAdze),
            "1", "2", '1', BidsItems.sedStoneAdzeHead, '2', "stickWood");
        setup.recipes().addShaped(new ItemStack(BidsItems.igExStoneAdze),
            "1", "2", '1', BidsItems.igExStoneAdzeHead, '2', "stickWood");
        setup.recipes().addShaped(new ItemStack(BidsItems.mMStoneAdze),
            "1", "2", '1', BidsItems.mMStoneAdzeHead, '2', "stickWood");

        setup.recipes().addShaped(new ItemStack(BidsItems.igInStoneAdze),
            "1", "2", '1', BidsItems.igInStoneAdzeHead, '2', TFCItems.bone);
        setup.recipes().addShaped(new ItemStack(BidsItems.sedStoneAdze),
            "1", "2", '1', BidsItems.sedStoneAdzeHead, '2', TFCItems.bone);
        setup.recipes().addShaped(new ItemStack(BidsItems.igExStoneAdze),
            "1", "2", '1', BidsItems.igExStoneAdzeHead, '2', TFCItems.bone);
        setup.recipes().addShaped(new ItemStack(BidsItems.mMStoneAdze),
            "1", "2", '1', BidsItems.mMStoneAdzeHead, '2', TFCItems.bone);

        setup.recipes().addShaped(new ItemStack(BidsItems.copperAdze, 1),
            "#", "I", '#', BidsItems.copperAdzeHead, 'I', "stickWood");
        setup.recipes().addShaped(new ItemStack(BidsItems.bronzeAdze, 1),
            "#", "I", '#', BidsItems.bronzeAdzeHead, 'I', "stickWood");
        setup.recipes().addShaped(new ItemStack(BidsItems.bismuthBronzeAdze, 1),
            "#", "I", '#', BidsItems.bismuthBronzeAdzeHead, 'I', "stickWood");
        setup.recipes().addShaped(new ItemStack(BidsItems.blackBronzeAdze, 1),
            "#", "I", '#', BidsItems.blackBronzeAdzeHead, 'I', "stickWood");
        setup.recipes().addShaped(new ItemStack(BidsItems.wroughtIronAdze, 1),
            "#", "I", '#', BidsItems.wroughtIronAdzeHead, 'I', "stickWood");

        for (StoneIndex stone : StoneScheme.DEFAULT.getStones()) {
            setup.registry(TfcRegistry.Recipes.knapping)
                .add(KnappingRecipe.add(stone.items.getItem(EnumStoneItemType.ADZE_HEAD),
                    "     ", " ### ", "#####", " ### ", "  #  ",
                    '#', stone.items.getItem(EnumStoneItemType.FLAT_ROCK)));
        }

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.clayMoldAdze),
                "  #  ", "  #  ", "  #  ", " ### ", "  #  ",
                '#', new ItemStack(TFCItems.flatClay, 1, 1)));

        setup.registry(TfcRegistry.Recipes.kiln)
            .add(KilnRecipe.add(new ItemStack(BidsItems.clayMoldAdze), 0,
                new ItemStack(BidsItems.clayMoldAdze, 1, 1)));

        setup.registry(TfcRegistry.Values.molds)
            .add(PartialMold.add(Metals.COPPER, BidsItems.clayMoldAdze, 2, BidsItems.clayMoldAdze, 4, 2))
            .add(PartialMold.add(Metals.BRONZE, BidsItems.clayMoldAdze, 3, BidsItems.clayMoldAdze, 4, 2))
            .add(PartialMold.add(Metals.BISMUTHBRONZE, BidsItems.clayMoldAdze, 4, BidsItems.clayMoldAdze, 4, 2))
            .add(PartialMold.add(Metals.BLACKBRONZE, BidsItems.clayMoldAdze, 5, BidsItems.clayMoldAdze, 4, 2));

        setup.registry(TfcRegistry.Recipes.anvilPlans)
            .add(AnvilPlan.add(ADZE_PLAN, AnvilRules.PUNCHLAST, AnvilRules.PUNCHSECONDFROMLAST, AnvilRules.HITTHIRDFROMLAST));

        setup.registry(TfcRegistry.Recipes.anvil)
            .add(AnvilRecipe.add(new ItemStack(TFCItems.copperIngot), null, ADZE_PLAN, 1,
                new ItemStack(BidsItems.copperAdzeHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.bronzeIngot), null, ADZE_PLAN, 2,
                new ItemStack(BidsItems.bronzeAdzeHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.bismuthBronzeIngot), null, ADZE_PLAN, 2,
                new ItemStack(BidsItems.bismuthBronzeAdzeHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.blackBronzeIngot), null, ADZE_PLAN, 2,
                new ItemStack(BidsItems.blackBronzeAdzeHead), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.wroughtIronIngot), null, ADZE_PLAN, 3,
                new ItemStack(BidsItems.wroughtIronAdzeHead), Skills.TOOLSMITH));
    }

}
