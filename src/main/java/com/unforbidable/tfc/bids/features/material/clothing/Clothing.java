package com.unforbidable.tfc.bids.features.material.clothing;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ExtraClothingMaterialNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemExtraClothingPiece;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import net.minecraft.item.ItemStack;

@FeatureName("clothingParts")
public class Clothing extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.EXTRA_STRAP, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));
        init.item(ItemNames.EXTRA_REPAIR_PATCH, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));
        init.item(ItemNames.EXTRA_BAG_PIECE, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));
        init.item(ItemNames.CUP_PIECE, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));

        init.item(ItemNames.EXTRA_COAT_BODY_FRONT, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.LEATHER));
        init.item(ItemNames.EXTRA_COAT_BODY_BACK, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.LEATHER));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(TfcRegistry.Knapping.recipes)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraStrap, 3),
                "# # #", "# # #", "# # #", "# # #", "# # #", '#', BidsItems.flatBirchBark))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraBagPiece, 2, 0),
                " ### ", " ### ", "     ", " ### ", " ### ", '#', BidsItems.flatBirchBark))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraBagPiece, 2, 0),
                "     ", "## ##", "## ##", "## ##", "     ", '#', BidsItems.flatBirchBark))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraRepairPatch, 4, 0),
                "## ##", "## ##", "     ", "## ##", "## ##", '#', BidsItems.flatBirchBark))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.cupPiece, 1, 0),
                "     ", "     ", "#### ", "### #", "#### ", '#', BidsItems.flatBirchBark));

        setup.registry(TfcRegistry.Knapping.recipes)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraCoatBodyFront, 1, 0),
                "#   #", "## ##", "## ##", "## ##", "## ##", '#', TFCItems.flatLeather))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraCoatBodyBack, 1, 0),
                "## ##", "#####", "#####", "#####", "#####", '#', TFCItems.flatLeather));
    }

}
