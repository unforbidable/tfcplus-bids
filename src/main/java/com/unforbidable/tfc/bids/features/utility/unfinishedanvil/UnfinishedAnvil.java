package com.unforbidable.tfc.bids.features.utility.unfinishedanvil;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.names.AnvilRules;
import com.unforbidable.tfc.bids.compat.tfc.names.Skills;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilPlan;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.HeatValue;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.utility.unfinishedanvil.block.BlockUnfinishedAnvil;
import com.unforbidable.tfc.bids.features.utility.unfinishedanvil.block.blockitem.ItemUnfinishedAnvil;
import com.unforbidable.tfc.bids.features.utility.unfinishedanvil.render.RenderUnfinishedAnvil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.UNFINISHED_ANVIL_STAGE_1;
import static com.unforbidable.tfc.bids.api.names.BlockNames.UNFINISHED_ANVIL_STAGE_2;
import static com.unforbidable.tfc.bids.api.names.BlockNames.UNFINISHED_ANVIL_STAGE_3;
import static com.unforbidable.tfc.bids.api.names.BlockNames.UNFINISHED_ANVIL_STAGE_4;
import static com.unforbidable.tfc.bids.api.names.BlockNames.UNFINISHED_ANVIL_STAGE_5;
import static com.unforbidable.tfc.bids.api.names.BlockNames.UNFINISHED_ANVIL_STAGE_6;

@FeatureName("unfinishedAnvil")
public class UnfinishedAnvil extends Feature {

    private static final String ANVIL_PLAN = "anvil";

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        // TODO use items instead of blocks

        init.block(UNFINISHED_ANVIL_STAGE_1, () -> new BlockUnfinishedAnvil(0), ItemUnfinishedAnvil.class);
        init.block(UNFINISHED_ANVIL_STAGE_2, () -> new BlockUnfinishedAnvil(1), ItemUnfinishedAnvil.class);
        init.block(UNFINISHED_ANVIL_STAGE_3, () -> new BlockUnfinishedAnvil(2), ItemUnfinishedAnvil.class);
        init.block(UNFINISHED_ANVIL_STAGE_4, () -> new BlockUnfinishedAnvil(3), ItemUnfinishedAnvil.class);
        init.block(UNFINISHED_ANVIL_STAGE_5, () -> new BlockUnfinishedAnvil(4), ItemUnfinishedAnvil.class);
        init.block(UNFINISHED_ANVIL_STAGE_6, () -> new BlockUnfinishedAnvil(5), ItemUnfinishedAnvil.class);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderUnfinishedAnvil())
            .block(BlockUnfinishedAnvil.class);

        // meta 11 does not exist but NEI still tries to show it and causes errors
        client.nei()
            .hide(BidsBlocks.unfinishedAnvilStage1, 11)
            .hide(BidsBlocks.unfinishedAnvilStage2, 11)
            .hide(BidsBlocks.unfinishedAnvilStage3, 11)
            .hide(BidsBlocks.unfinishedAnvilStage4, 11)
            .hide(BidsBlocks.unfinishedAnvilStage5, 11)
            .hide(BidsBlocks.unfinishedAnvilStage6, 11);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(TfcRegistry.Recipes.anvilPlans)
            .add(AnvilPlan.add(ANVIL_PLAN, AnvilRules.HITLAST, AnvilRules.HITSECONDFROMLAST, AnvilRules.HITTHIRDFROMLAST));

        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 1, TFCItems.copperIngot2x, 0);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 2, TFCItems.bronzeIngot2x, 1);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 3, TFCItems.wroughtIronIngot2x, 2);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 4, TFCItems.steelIngot2x, 3);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 5, TFCItems.blackSteelIngot2x, 4);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 6, TFCItems.blueSteelIngot2x, 5);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 7, TFCItems.redSteelIngot2x, 5);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 8, TFCItems.roseGoldIngot2x, 1);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 9, TFCItems.bismuthBronzeIngot2x, 1);
        setupUnfinishedAnvilRecipesAndHeatForMaterial(setup, 10, TFCItems.blackBronzeIngot2x, 1);
    }

    private void setupUnfinishedAnvilRecipesAndHeatForMaterial(FeatureSetupBuilder setup, int material, Item input, int req) {
        setup.registry(TfcRegistry.Recipes.anvil)
            .add(AnvilRecipe.addWeld(new ItemStack(input), new ItemStack(input),
                req, BlockUnfinishedAnvil.getUnfinishedAnvil(material, 0)))
            .add(AnvilRecipe.addWeld(new ItemStack(input), BlockUnfinishedAnvil.getUnfinishedAnvil(material, 0),
                req, BlockUnfinishedAnvil.getUnfinishedAnvil(material, 1)))
            .add(AnvilRecipe.addWeld(new ItemStack(input), BlockUnfinishedAnvil.getUnfinishedAnvil(material, 1),
                req, BlockUnfinishedAnvil.getUnfinishedAnvil(material, 2)))
            .add(AnvilRecipe.addWeld(new ItemStack(input), BlockUnfinishedAnvil.getUnfinishedAnvil(material, 2),
                req, BlockUnfinishedAnvil.getUnfinishedAnvil(material, 3)))
            .add(AnvilRecipe.addWeld(new ItemStack(input), BlockUnfinishedAnvil.getUnfinishedAnvil(material, 3),
                req, BlockUnfinishedAnvil.getUnfinishedAnvil(material, 4)))
            .add(AnvilRecipe.addWeld(new ItemStack(input), BlockUnfinishedAnvil.getUnfinishedAnvil(material, 4),
                req, BlockUnfinishedAnvil.getUnfinishedAnvil(material, 5)))
            .add(AnvilRecipe.add(BlockUnfinishedAnvil.getUnfinishedAnvil(material, 5), null,
                ANVIL_PLAN, req, BlockUnfinishedAnvil.getFinishedAnvil(material), Skills.GENERAL_SMITHING));

        setup.registry(TfcRegistry.Values.heat)
            .add(HeatValue.clone(new ItemStack(input))
                .as(BlockUnfinishedAnvil.getUnfinishedAnvil(material, 0), 4)
                .as(BlockUnfinishedAnvil.getUnfinishedAnvil(material, 1), 6)
                .as(BlockUnfinishedAnvil.getUnfinishedAnvil(material, 2), 8)
                .as(BlockUnfinishedAnvil.getUnfinishedAnvil(material, 3), 10)
                .as(BlockUnfinishedAnvil.getUnfinishedAnvil(material, 4), 12)
                .as(BlockUnfinishedAnvil.getUnfinishedAnvil(material, 5), 14));
    }

}
