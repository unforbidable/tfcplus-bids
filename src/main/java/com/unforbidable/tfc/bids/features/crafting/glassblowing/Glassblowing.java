package com.unforbidable.tfc.bids.features.crafting.glassblowing;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.GuiNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonFlat;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.names.AnvilRules;
import com.unforbidable.tfc.bids.compat.tfc.names.Metals;
import com.unforbidable.tfc.bids.compat.tfc.names.Skills;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilPlan;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.AnvilRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.values.PartialMold;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.container.ContainerSpecialCraftingGlass;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.gui.GuiKnappingGlass;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.item.ItemMetalBlowpipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

/**
 * <li>metal blowpipe - iron blow pipe for glassblowing</li>
 * <li>brass blowpipe - brass blow pipe for glassblowing</li>
 * <li>flat glass - flat crafting grid item for forming glass</li>
 */
@FeatureName("glassblowing")
public class Glassblowing extends Feature {

    private static final String BLOWPIPE_PLAN = "blowpipe";

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.METAL_BLOWPIPE, ItemMetalBlowpipe::new);

        init.item(ItemNames.BRASS_BLOWPIPE, ItemMetalBlowpipe::new);

        init.item(ItemNames.FLAT_GLASS, ItemCommonFlat::new);

        init.gui(GuiNames.GLASSBLOWING, ContainerSpecialCraftingGlass::new);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.gui(GuiNames.GLASSBLOWING, GuiKnappingGlass::new);

        client.nei()
            .hide(BidsItems.flatGlass);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(TfcRegistry.Values.molds)
            .add(PartialMold.add(Metals.GLASS, BidsItems.metalBlowpipe, 2, BidsItems.metalBlowpipe, 1, 2))
            .add(PartialMold.add(Metals.GLASS, BidsItems.brassBlowpipe, 2, BidsItems.brassBlowpipe, 1, 2));

        setup.registry(TfcRegistry.Recipes.anvilPlans)
            .add(AnvilPlan.add(BLOWPIPE_PLAN, AnvilRules.BENDLAST, AnvilRules.BENDSECONDFROMLAST, AnvilRules.ANY));

        setup.registry(TfcRegistry.Recipes.anvil)
            .add(AnvilRecipe.add(new ItemStack(TFCItems.wroughtIronSheet), null, BLOWPIPE_PLAN, 3,
                new ItemStack(BidsItems.metalBlowpipe, 1, 1), Skills.TOOLSMITH))
            .add(AnvilRecipe.add(new ItemStack(TFCItems.brassSheet), null, BLOWPIPE_PLAN, 2,
                new ItemStack(BidsItems.brassBlowpipe, 1, 1), Skills.TOOLSMITH));
    }

}
