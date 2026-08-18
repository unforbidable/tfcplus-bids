package com.unforbidable.tfc.bids.features.material.nettle;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.api.features.drying.WetnessInfo;
import com.unforbidable.tfc.bids.api.features.handwork.HandworkRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.HecklingRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.RopeMakingRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.SpinningRecipe;
import com.unforbidable.tfc.bids.api.features.soaking.SoakingRecipe;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.BarrelRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.LoomRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.drying.DryingRegistry;
import com.unforbidable.tfc.bids.features.crafting.handwork.HandworkRegistry;
import com.unforbidable.tfc.bids.features.crafting.heckling.HecklingRegistry;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.RopeMakingRegistry;
import com.unforbidable.tfc.bids.features.crafting.soaking.SoakingRegistry;
import com.unforbidable.tfc.bids.features.crafting.spinning.SpinningRegistry;
import com.unforbidable.tfc.bids.features.device.dryingrack.DryingRackRegistry;
import com.unforbidable.tfc.bids.features.material.textile.item.ItemTextile;
import com.unforbidable.tfc.bids.features.material.textile.main.TextileHints;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("nettle")
public class Nettle extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.NETTLE, ItemTextile::new)
            .hints(TextileHints.REFINING_STALK);
        init.item(ItemNames.NETTLE_STALK, ItemTextile::new)
            .hints(TextileHints.RETTING_STALK);
        init.item(ItemNames.NETTLE_STALK_RETTED, ItemTextile::new)
            .hints(TextileHints.PEELING_STALK);
        init.item(ItemNames.NETTLE_FIBER, ItemTextile::new)
            .hints(TextileHints.DRYING_FIBER);
        init.item(ItemNames.NETTLE_FIBER_COARSE, ItemTextile::new)
            .hints(TextileHints.HECKLING, TextileHints.SPINNING_TWINE);
        init.item(ItemNames.NETTLE_FIBER_REFINED, ItemTextile::new)
            .hints(TextileHints.SPINNING_TWINE)
            .apply(i -> i.setMaterialColor(0xc1d674));
        init.item(ItemNames.NETTLE_TWINE, ItemTextile::new)
            .hints(TextileHints.TWISTING, TextileHints.WEAVING_BURLAP)
            .apply(i -> i.setMaterialColor(0x574c2f));
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("materialFiber")
            .add(BidsItems.nettleFiber, BidsItems.nettleFiberCoarse, BidsItems.nettleFiberRefined);

        setup.ores("materialString")
            .add(BidsItems.nettleTwine);

        setup.recipes().addShapeless(new ItemStack(BidsItems.nettleStalk),
                BidsItems.nettle, "itemScrapingTool")
            .action(damageTool("itemScrapingTool"));

        setup.registry(SoakingRegistry.recipes)
            .add(new SoakingRecipe(new ItemStack(BidsItems.nettleStalk), new ItemStack(BidsItems.nettleStalkRetted),
                new FluidStack(TFCFluids.FRESHWATER, 200), 8000));

        setup.registry(HandworkRegistry.recipes)
            .add(new HandworkRecipe(new ItemStack(BidsItems.nettleStalkRetted), new ItemStack(BidsItems.nettleFiber), 80));

        setup.registry(DryingRegistry.wetness)
            .add(BidsItems.nettleStalk, new WetnessInfo(500, 1f));

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackRecipe) DryingRackRecipe.builder()
            .consumes(new ItemStack(BidsItems.nettleFiber))
            .produces(new ItemStack(BidsItems.nettleFiberCoarse))
            .dry()
            .hours(12)
            .build());

        setup.registry(SpinningRegistry.recipes)
            .add(new SpinningRecipe(new ItemStack(BidsItems.nettleFiberRefined), new ItemStack(BidsItems.nettleTwine, 2), 150))
            .add(new SpinningRecipe(new ItemStack(BidsItems.nettleFiberCoarse), new ItemStack(BidsItems.nettleTwine, 2), 150 * 4));

        setup.registry(RopeMakingRegistry.recipes)
            .add(new RopeMakingRecipe(new ItemStack(BidsItems.nettleTwine, 16), new ItemStack(TFCItems.rope), 600));

        setup.registry(HecklingRegistry.recipes)
            .add(new HecklingRecipe(new ItemStack(BidsItems.nettleFiberCoarse), new ItemStack(BidsItems.nettleFiberRefined), 80));

        setup.registry(TfcRegistry.Barrel.recipes)
            .add(BarrelRecipe.add(builder -> builder
                .consumes(new ItemStack(BidsItems.nettleStalk), new FluidStack(TFCFluids.FRESHWATER, 200))
                .produces(new ItemStack(BidsItems.nettleStalkRetted), new FluidStack(TFCFluids.FRESHWATER, 200))
                .withMinTechLevel(0).beingSealed(false)));

        setup.registry(TfcRegistry.Loom.recipes)
            .add(LoomRecipe.add(new ItemStack(BidsItems.nettleTwine, 24), new ItemStack(TFCItems.burlapCloth, 1),
                new ResourceLocation("terrafirmacraftplus", "textures/blocks/Rope.png")));

        setup.registry(DryingRackRegistry.tyingEquipment)
            .add(new DryingRackTyingEquipment(BidsItems.nettleTwine, false, Blocks.wool, 1));

    }

}
