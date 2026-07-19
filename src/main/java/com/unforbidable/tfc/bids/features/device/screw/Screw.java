package com.unforbidable.tfc.bids.features.device.screw;

import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.screw.block.BlockScrew;
import com.unforbidable.tfc.bids.features.device.screw.render.RenderScrew;
import com.unforbidable.tfc.bids.features.device.screw.render.RenderTileScrew;
import com.unforbidable.tfc.bids.features.device.screw.tileentity.TileEntityScrew;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;

@FeatureName("screw")
public class Screw extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.WOOD_SCREW, () -> new BlockScrew(Material.wood))
            .hardness(0.5f)
            .texture("Wood Screw")
            .fireInfo(5, 5);

        init.tileEntity(TileEntityScrew.class, "BidsScrew");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderScrew())
            .block(BlockScrew.class);

        client.render(new RenderTileScrew())
            .tileEntity(TileEntityScrew.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.recipes().addShapeless(new ItemStack(BidsBlocks.woodScrew),
                TFCBlocks.woodAxle, "itemChisel")
            .action(damageTool("itemChisel"));
    }

}
