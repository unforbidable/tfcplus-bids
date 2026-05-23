package com.unforbidable.tfc.bids.features.material.clothing;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ExtraClothingMaterialNames;
import com.unforbidable.tfc.bids.common.item.ItemExtraClothingPiece;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.EXTRA_BAG_PIECE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.EXTRA_COAT_BODY_BACK;
import static com.unforbidable.tfc.bids.api.names.ItemNames.EXTRA_COAT_BODY_FRONT;
import static com.unforbidable.tfc.bids.api.names.ItemNames.CUP_PIECE;
import static com.unforbidable.tfc.bids.api.names.ItemNames.EXTRA_REPAIR_PATCH;
import static com.unforbidable.tfc.bids.api.names.ItemNames.EXTRA_STRAP;

@FeatureName("clothingParts")
public class Clothing extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(EXTRA_STRAP, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));
        init.item(EXTRA_REPAIR_PATCH, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));
        init.item(EXTRA_BAG_PIECE, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));
        init.item(CUP_PIECE, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.BIRCH_BARK));

        init.item(EXTRA_COAT_BODY_FRONT, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.LEATHER));
        init.item(EXTRA_COAT_BODY_BACK, ItemExtraClothingPiece::new)
            .apply(i -> i.setExtraPieceTypes(ExtraClothingMaterialNames.LEATHER));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraStrap, 3),
                new Object[]{"# # #", "# # #", "# # #", "# # #", "# # #", '#', BidsItems.flatBirchBark}))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraBagPiece, 2, 0),
                new Object[]{" ### ", " ### ", "     ", " ### ", " ### ", '#', BidsItems.flatBirchBark}))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraBagPiece, 2, 0),
                new Object[]{"     ", "## ##", "## ##", "## ##", "     ", '#', BidsItems.flatBirchBark}))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraRepairPatch, 4, 0),
                new Object[]{"## ##", "## ##", "     ", "## ##", "## ##", '#', BidsItems.flatBirchBark}))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.cupPiece, 1, 0),
                new Object[]{"     ", "     ", "#### ", "### #", "#### ", '#', BidsItems.flatBirchBark}));

        setup.registry(TfcRegistry.Recipes.knapping)
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraCoatBodyFront, 1, 0),
                new Object[]{"#   #", "## ##", "## ##", "## ##", "## ##", '#', TFCItems.flatLeather}))
            .add(KnappingRecipe.add(new ItemStack(BidsItems.extraCoatBodyBack, 1, 0),
                new Object[]{"## ##", "#####", "#####", "#####", "#####", '#', TFCItems.flatLeather}));
    }

}
