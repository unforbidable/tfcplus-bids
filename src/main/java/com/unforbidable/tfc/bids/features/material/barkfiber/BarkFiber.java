package com.unforbidable.tfc.bids.features.material.barkfiber;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.api.features.drying.DryingSurfaceRecipe;
import com.unforbidable.tfc.bids.api.features.drying.WetnessInfo;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.api.features.handwork.HandworkRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.RopeMakingRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.SpinningRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.crafting.drying.DryingRegistry;
import com.unforbidable.tfc.bids.features.crafting.handwork.HandworkRegistry;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.RopeMakingRegistry;
import com.unforbidable.tfc.bids.features.crafting.spinning.SpinningRegistry;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import com.unforbidable.tfc.bids.features.device.dryingsurface.DryingSurfaceRegistry;
import com.unforbidable.tfc.bids.features.device.firepit.FirepitRegistry;
import com.unforbidable.tfc.bids.features.device.firepit.item.ItemKindling;
import com.unforbidable.tfc.bids.features.material.textile.item.ItemTextile;
import com.unforbidable.tfc.bids.features.material.textile.main.TextileHints;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("barkFiber")
public class BarkFiber extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.BARK_FIBER, ItemTextile::new)
            .hints(TextileHints.DRYING_FIBER);
        init.item(ItemNames.BARK_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.RUBBING, TextileHints.SPINNING_CORDAGE);
        init.item(ItemNames.BARK_FIBER_SMOOTH, ItemTextile::new)
            .hints(TextileHints.SPINNING_CORDAGE);
        init.item(ItemNames.BARK_CORDAGE, ItemTextile::new)
            .hints(TextileHints.TWISTING)
            .apply(i -> i.setMaterialColor(0x8c7a4b));

        init.item(ItemNames.BARK_FIBER_KINDLING, ItemKindling::new)
            .apply(i -> i.setFuelKindlingQuality(1f));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (wood.hasBarkFibers) {
                setup.ores("itemBarkHasFibers")
                    .add(wood.items.getBark());

                setup.help(wood.items.getBark())
                    .hints(TextileHints.EXTRACTING);
            }
        }

        setup.ores("materialFiber")
            .add(BidsItems.barkFiberCoarse, BidsItems.barkFiberSmooth);

        setup.ores("materialString")
            .add(BidsItems.barkCordage);

        setup.recipes().addShapeless(new ItemStack(BidsItems.barkFibreKindling),
            "stickWood", "stickWood", "stickWood", "materialFiber");
        setup.recipes().addShapeless(new ItemStack(BidsItems.barkFibreKindling),
            BidsItems.smallStickBundle, "materialFiber");

        setup.recipes().addShapeless(new ItemStack(BidsItems.barkFiber),
                "itemBarkHasFibers", "itemScrapingTool")
            .action(damageTool("itemScrapingTool"));

        setup.registry(FirepitRegistry.fuel)
            .add(BidsItems.barkFibreKindling, (FirepitFuelMaterial) BidsItems.barkFibreKindling);

        setup.registry(DryingRegistry.wetness)
            .add(BidsItems.barkFiber, new WetnessInfo(500, 1f));

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(new ItemStack(BidsItems.barkFiber))
                .produces(new ItemStack(BidsItems.barkFiberCoarse))
                .dry()
                .hours(12)
                .build());

        setup.registry(DryingSurfaceRegistry.recipes)
            .add((DryingSurfaceRecipe) DryingSurfaceRecipe.builder()
                .consumes(new ItemStack(BidsItems.barkFiber))
                .produces(new ItemStack(BidsItems.barkFiberCoarse))
                .dry()
                .hours(12)
                .build());

        setup.registry(HandworkRegistry.recipes)
            .add(new HandworkRecipe(new ItemStack(BidsItems.barkFiberCoarse), new ItemStack(BidsItems.barkFiberSmooth), 80));

        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(BidsItems.barkFiberSmooth), new ItemStack(BidsItems.barkCordage, 2), 80))
            .add(new SpinningRecipe(new ItemStack(BidsItems.barkFiberCoarse), new ItemStack(BidsItems.barkCordage, 2), 80 * 4));

        setup.registry(RopeMakingRegistry.recipes)
            .add(new RopeMakingRecipe(new ItemStack(BidsItems.barkCordage, 12), new ItemStack(TFCItems.rope), 600));

        setup.registry(DryingRackRegistry.tyingEquipment)
            .add(new DryingRackTyingEquipment(BidsItems.barkCordage, false, Blocks.wool, 1));
    }

}
