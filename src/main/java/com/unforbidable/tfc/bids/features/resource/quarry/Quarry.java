package com.unforbidable.tfc.bids.features.resource.quarry;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.names.AnvilRules;
import com.unforbidable.tfc.bids.compat.tfc.names.Skills;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilPlan;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.resource.quarry.block.BlockQuarry;
import com.unforbidable.tfc.bids.features.resource.quarry.item.ItemPlugAndFeather;
import com.unforbidable.tfc.bids.features.resource.quarry.nei.QuarryNeiHandler;
import com.unforbidable.tfc.bids.features.resource.quarry.render.RenderQuarry;
import com.unforbidable.tfc.bids.features.resource.quarry.tileentity.TileEntityQuarry;
import com.unforbidable.tfc.bids.features.resource.quarry.waila.QuarryWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

/**
 * <li>quarry - allows mining whole blocks using the drill and wedge method</li>
 * <li>plug and feather - consumable material used in a quarry, from wood and metal up to wrought iron</li>
 */
@FeatureName("quarry")
public class Quarry extends Feature {

    private static final String PLUG_AND_FEATHER_PLAN = "plugandfeather";

    @Override
    public void config(FeatureConfig config) {
        config.using(QuarryConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.QUARRY, BlockQuarry::new)
            .harvest("hammer", 0)
            .apply(b -> b.setBlockName("Quarry"));

        init.tileEntity(TileEntityQuarry.class, "BidsQuarry");

        init.item(ItemNames.PLUG_AND_FEATHER, ItemPlugAndFeather::new)
            .meta("Wood", "Copper", "Bronze", "Bismuth Bronze", "Black Bronze", "Wrought Iron");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderQuarry())
            .block(BlockQuarry.class);

        client.waila()
            .data(new QuarryWailaProvider(), TileEntityQuarry.class);

        client.nei()
            .handler(new QuarryNeiHandler())
            .hide(BidsBlocks.quarry);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        for (int i = 0; i < 6; i++) {
            setup.ores("itemPlugAndFeather")
                .add(new ItemStack(BidsItems.plugAndFeather, 1, i));
        }

        setup.recipes().addShapeless(new ItemStack(BidsItems.plugAndFeather, 4),
                "logWoodPlugAndFeather", "itemAdze")
            .action(damageTool("itemAdze"));

        setup.registry(TfcRegistry.Recipes.anvilPlans)
            .add(AnvilPlan.add(PLUG_AND_FEATHER_PLAN, AnvilRules.HITLAST, AnvilRules.BENDSECONDFROMLAST, AnvilRules.SHRINKTHIRDFROMLAST));

        setup.registry(TfcRegistry.Recipes.anvil)
            .add(AnvilRecipe.add(new ItemStack(TFCItems.copperIngot), null,
                PLUG_AND_FEATHER_PLAN, 1, new ItemStack(BidsItems.plugAndFeather, 8, 1), Skills.GENERAL_SMITHING))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.bronzeIngot), null,
                PLUG_AND_FEATHER_PLAN, 2, new ItemStack(BidsItems.plugAndFeather, 8, 2), Skills.GENERAL_SMITHING))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.bismuthBronzeIngot), null,
                PLUG_AND_FEATHER_PLAN, 2, new ItemStack(BidsItems.plugAndFeather, 8, 3), Skills.GENERAL_SMITHING))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.blackBronzeIngot), null,
                PLUG_AND_FEATHER_PLAN, 2, new ItemStack(BidsItems.plugAndFeather, 8, 4), Skills.GENERAL_SMITHING))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.wroughtIronIngot), null,
                PLUG_AND_FEATHER_PLAN, 3, new ItemStack(BidsItems.plugAndFeather, 8, 5), Skills.GENERAL_SMITHING));
    }

}
