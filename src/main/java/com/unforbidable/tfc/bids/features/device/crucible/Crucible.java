package com.unforbidable.tfc.bids.features.device.crucible;

import com.dunk.tfc.TileEntities.TEChimney;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.common.tileentity.TileEntityChimney;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KilnRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.config.FeatureConfig;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.crucible.block.BlockClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.block.BlockFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.block.itemblock.ItemClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.block.itemblock.ItemFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.container.ContainerClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.container.ContainerFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.gui.GuiClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.gui.GuiFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.item.ItemGlassLump;
import com.unforbidable.tfc.bids.features.device.crucible.recipe.RecipeCrucibleConversion;
import com.unforbidable.tfc.bids.features.device.crucible.render.RenderClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.render.RenderFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.features.device.crucible.waila.CrucibleWailaProvider;
import com.unforbidable.tfc.bids.features.device.crucible.waila.FurnaceWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

/**
 * <li><b>clay/pottery crucible</b> - primitive crucible</li>
 * <li><b>fire clay crucible</b> - full sized crucible</li>
 * <li><b>furnace</b> - structure that enables making glass in a crucible</li>
 * <li><b>glass lump</b> - product of breaking a ruined clay crucible that has been used to make glass</li>
 */
@FeatureName("crucible")
public class Crucible extends Feature {

    @Override
    public void config(FeatureConfig config) {
        config.using(CrucibleConfig::load);
    }

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.CLAY_CRUCIBLE, BlockClayCrucible::new, ItemClayCrucible.class)
            .apply(b -> b.setBlockTextureName("Pottery Crucible")
                .setHardness(CrucibleConfig.enableClayHandBreakable ? 0.5f : 4.0f));
        init.tileEntity(TileEntityClayCrucible.class, "BidsClayCrucible");
        init.gui(BlockNames.CLAY_CRUCIBLE, ContainerClayCrucible::new);

        init.block(BlockNames.FIRE_CLAY_CRUCIBLE, BlockFireClayCrucible::new, ItemFireClayCrucible.class)
            .apply(b -> b.setBlockTextureName("Fire Clay Crucible")
                .setHardness(CrucibleConfig.enableClayHandBreakable ? 0.5f : 4.0f));
        init.tileEntity(TileEntityFireClayCrucible.class, "BidsFireClayCrucible");
        init.gui(BlockNames.FIRE_CLAY_CRUCIBLE, ContainerFireClayCrucible::new);

        init.item(ItemNames.GLASS_LUMP, ItemGlassLump::new);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderClayCrucible())
            .block(BlockClayCrucible.class);
        client.render(new RenderFireClayCrucible())
            .block(BlockFireClayCrucible.class);

        client.gui(BlockNames.CLAY_CRUCIBLE, GuiClayCrucible::new);
        client.gui(BlockNames.FIRE_CLAY_CRUCIBLE, GuiFireClayCrucible::new);

        client.waila()
            .data(new CrucibleWailaProvider(), TileEntityCrucible.class)
            .data(new FurnaceWailaProvider(), TEChimney.class, TileEntityChimney.class);

        client.nei()
            .hide(BidsBlocks.clayCrucible, 2);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        // These enable conversion between TFC crucible and new crucible and back
        // for valid blasting furnace
        setup.recipes().add(new RecipeCrucibleConversion(true));
        setup.recipes().add(new RecipeCrucibleConversion(false));

        setup.registry(TfcRegistry.Knapping.recipes)
            .add(KnappingRecipe.add(new ItemStack(BidsBlocks.clayCrucible, 1, 1),
                "#####", " ### ", " ### ", " ### ", "     ",
                '#', new ItemStack(TFCItems.flatClay, 1, 1)))
            .add(KnappingRecipe.add(new ItemStack(BidsBlocks.fireClayCrucible, 1),
                "#####", " ### ", " ### ", " ### ", "     ",
                '#', new ItemStack(TFCItems.flatClay, 1, 3)));

        setup.registry(TfcRegistry.Kiln.recipes)
            .add(KilnRecipe.add(new ItemStack(BidsBlocks.clayCrucible, 1, 1), 0,
                new ItemStack(BidsBlocks.clayCrucible, 1, 0)));

        if (CrucibleConfig.enableClassicHandBreakable) {
            // Lower the hardness of the classic TFC crucible
            // The original value is 4.0f
            setup.run(() -> {
                Bids.LOG.info("Classic TFC crucible hardness reduced");
                TFCBlocks.crucible.setHardness(0.5f);
            });
        }
    }

}
