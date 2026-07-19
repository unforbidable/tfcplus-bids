package com.unforbidable.tfc.bids.features.utility.leatherwear;

import com.dunk.tfc.api.Armor;
import com.dunk.tfc.api.Interfaces.IEquipable;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemExtraCoat;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.SewingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@FeatureName("leatherwear")
public class Leatherwear extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.LEATHER_COAT, () -> new ItemExtraCoat(IEquipable.ClothingType.COAT))
            .apply(i -> {
                i.setResourceLocation(Tags.MOD_ID, "textures/models/armor/leather_coat_color.png")
                    .setBodySunProtection(1f)
                    .setColdResistance(2)
                    .setHeatResistance(-1)
                    .setArmorType(Armor.leather)
                    .setMaxDamage(TFCItems.leatherUses);
                i.setRepairCost(8);
                i.setArmorCoverage("LONG_SLEEVES", 2)
                    .setArmorCoverage("FULL_SHIRT_TORSO", 1)
                    .setArmorCoverage("SHORTS_LEGS", 3);
            });
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        int[][][] coatSewing = new int[][][]{
            // the left side of the coat and underarm
            {{24, 86}, {27, 33}, {24, 33}, {18, 71}},
            // the outer left arm and shoulder
            {{8, 71}, {11, 33}, {16, 19}, {22, 13}, {37, 12}},
            // the arm attached to the sleeve
            {{25, 33}, {21, 15}},
            // the right side of the coat and underarm
            {{97 - 24, 86}, {97 - 27, 33}, {97 - 24, 33}, {97 - 18, 71}},
            // the outer right arm and shoulder
            {{97 - 8, 71}, {97 - 11, 33}, {97 - 16, 19}, {97 - 22, 13}, {97 - 37, 12}},
            // the right arm attached to the sleeve
            {{97 - 25, 33}, {97 - 21, 15}}
        };

        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.add(new ItemStack(BidsItems.leatherCoat), coatSewing,
                new ItemStack(BidsItems.extraCoatBodyFront, 1, 0),
                new ItemStack(BidsItems.extraCoatBodyBack, 1, 0),
                new ItemStack(TFCItems.shirtSleeves, 1, 2),
                new ItemStack(TFCItems.shirtSleeves, 1, 2))
            );

        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.addRepair(new ItemStack(BidsItems.leatherCoat),
                new ItemStack(BidsItems.leatherCoat, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(TFCItems.repairPatch, 1, 2))
            );

        // Adding missing TFC+ recipe for repairing leather boots
        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.addRepair(new ItemStack(TFCItems.leatherBoots, 1),
                new ItemStack(TFCItems.leatherBoots, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(TFCItems.repairPatch, 1, 2)));

        // Adding missing TFC+ recipe for repairing leather cap
        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.addRepair(new ItemStack(TFCItems.leatherCoif, 1),
                new ItemStack(TFCItems.leatherCoif, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(TFCItems.repairPatch, 1, 2)));

        // Adding missing TFC+ recipe for repairing leather shorts
        setup.registry(TfcRegistry.Recipes.sewing)
            .add(SewingRecipe.addRepair(new ItemStack(TFCItems.leatherShorts, 1),
                new ItemStack(TFCItems.leatherShorts, 1, OreDictionary.WILDCARD_VALUE),
                new ItemStack(TFCItems.repairPatch, 1, 2)));
    }

}
