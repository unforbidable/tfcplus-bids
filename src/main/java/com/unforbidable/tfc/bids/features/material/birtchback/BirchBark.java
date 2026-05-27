package com.unforbidable.tfc.bids.features.material.birtchback;

import com.dunk.tfc.api.Armor;
import com.dunk.tfc.api.Interfaces.IEquipable;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.common.item.ItemCommonClothSheet;
import com.unforbidable.tfc.bids.common.item.ItemCommonFlat;
import com.unforbidable.tfc.bids.common.item.ItemCommonSewable;
import com.unforbidable.tfc.bids.common.item.ItemDrinkingCloth;
import com.unforbidable.tfc.bids.common.item.ItemExtraBag;
import com.unforbidable.tfc.bids.common.item.ItemExtraBoots;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.SewingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import com.unforbidable.tfc.bids.features.device.firepit.item.ItemKindling;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.unforbidable.tfc.bids.api.names.ItemNames.BIRCH_BARK_BAG;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BIRCH_BARK_CUP;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BIRCH_BARK_CUP_UNFINISHED;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BIRCH_BARK_KINDLING;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BIRCH_BARK_SHEET;
import static com.unforbidable.tfc.bids.api.names.ItemNames.BIRCH_BARK_SHOES;
import static com.unforbidable.tfc.bids.api.names.ItemNames.FLAT_BIRCH_BARK;
import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("birchBark")
public class BirchBark extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(FLAT_BIRCH_BARK, ItemCommonFlat::new)
            .apply(i -> i.setTextureFolder("armor/clothing"));

        init.item(BIRCH_BARK_SHEET, ItemCommonClothSheet::new)
            .apply(i -> i.setSpecialCraftingType(lookup.item(FLAT_BIRCH_BARK)));

        init.item(BIRCH_BARK_BAG, ItemExtraBag::new)
            .apply(i -> i.setMaxDamage(12));

        init.item(BIRCH_BARK_CUP_UNFINISHED, ItemCommonSewable::new);

        init.item(BIRCH_BARK_CUP, ItemDrinkingCloth::new)
            .drink(250, false)
            .overlays(0, 100);

        init.item(BIRCH_BARK_KINDLING, ItemKindling::new)
            .apply(i -> i.setFuelKindlingQuality(1f));

        init.item(BIRCH_BARK_SHOES, () -> new ItemExtraBoots(IEquipable.ClothingType.BOOTS))
            .apply(i -> {
                i.setResourceLocation(Tags.MOD_ID, "textures/models/armor/clothing/birch_bark_shoes_color.png")
                    .setArmorCoverage("SOCKS", 4)
                    .setArmorType(Armor.linenCloth)
                    .setMaxDamage(TFCItems.strawUses);
                i.setTrueBoots(false)
                    .setDefaultWalkable(0.07f)
                    .addWalkableSurface(Material.sand, 0.02f);
                i.setRepairCost(2);
            });
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShapeless(new ItemStack(BidsItems.birchBarkKindling),
            "stickWood", "stickWood", "stickWood", BidsItems.extraStrap);
        setup.recipes().addShapeless(new ItemStack(BidsItems.birchBarkKindling),
            BidsItems.smallStickBundle, BidsItems.extraStrap);

        setup.recipes().addShapeless(new ItemStack(BidsItems.birchBarkCup),
            BidsItems.birchBarkCupUnfinished, Items.slime_ball);

        setup.recipes().addShapeless(new ItemStack(BidsItems.birchBarkSheet),
                new ItemStack(BidsItems.bark, 1, 2), "itemKnife")
            .action(damageTool("itemKnife"));

        setup.registry(FirepitRegistry.fuel)
                .add(BidsItems.birchBarkKindling, (FirepitFuelMaterial) BidsItems.birchBarkKindling);

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.birchBarkShoes),
                "  ###", "   ##", "     ", "##   ", "###  ", '#', BidsItems.flatBirchBark));

        int[][][] bagSewing = new int[][][]{{
            {25, 21},
            {11, 74},
            {19, 87},
            {79, 87},
            {87, 74},
            {73, 21}
        }};

        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.add(new ItemStack(BidsItems.birchBarkBag), bagSewing,
                new ItemStack(BidsItems.extraBagPiece, 1, 0),
                new ItemStack(BidsItems.extraBagPiece, 1, 0),
                new ItemStack(BidsItems.extraStrap, 1, 0))
            );

        int[][][] cupSewing = new int[][][]{{
            {11, 74},
            {19, 87},
            {64, 87},
            {72, 74},
            {72, 37},
            {60, 40},
            {21, 40},
            {11, 37}
        }};

        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.add(new ItemStack(BidsItems.birchBarkCupUnfinished), cupSewing,
                new ItemStack(BidsItems.cupPiece, 1, 0),
                new ItemStack(BidsItems.extraStrap, 1, 0))
            );

        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.addRepair(new ItemStack(BidsItems.birchBarkBag),
                new ItemStack(BidsItems.birchBarkBag, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(BidsItems.extraRepairPatch, 1, 0))
            );

        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.addRepair(new ItemStack(BidsItems.birchBarkShoes),
                new ItemStack(BidsItems.birchBarkShoes, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(BidsItems.extraStrap, 1, 0))
            );
    }

}
