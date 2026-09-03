package com.unforbidable.tfc.bids.features.device.dryingrack;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.api.features.drying.WetnessInfo;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.drying.DryingRegistry;
import com.unforbidable.tfc.bids.features.device.dryingrack.block.BlockDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.block.BlockDryingRackSide;
import com.unforbidable.tfc.bids.features.device.dryingrack.eventhandler.DryingRackEventHandler;
import com.unforbidable.tfc.bids.features.device.dryingrack.render.RenderDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.render.RenderDryingRackSide;
import com.unforbidable.tfc.bids.features.device.dryingrack.render.RenderTileDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.tileentity.TileEntityDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.waila.DryingRackWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@FeatureName("dryingRack")
public class DryingRack extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.DRYING_RACK, BlockDryingRack::new);
        init.block(BlockNames.DRYING_RACK_SIDE, BlockDryingRackSide::new);

        init.tileEntity(TileEntityDryingRack.class, "BidsDryingRack");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderDryingRack())
            .block(BlockDryingRack.class);
        client.render(new RenderDryingRackSide())
            .block(BlockDryingRackSide.class);

        client.render(new RenderTileDryingRack())
            .tileEntity(TileEntityDryingRack.class);

        client.waila()
            .data(new DryingRackWailaProvider(), TileEntityDryingRack.class);

        client.nei()
            .hide(BidsBlocks.dryingRack)
            .hide(BidsBlocks.dryingRackSide);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(DryingRackRegistry.tyingEquipment)
            .add(new DryingRackTyingEquipment(TFCItems.woolYarn, false, Blocks.wool, 0))
            .add(new DryingRackTyingEquipment(TFCItems.linenString, false, Blocks.wool, 0))
            .add(new DryingRackTyingEquipment(TFCItems.cottonYarn, false, Blocks.wool, 0))
            .add(new DryingRackTyingEquipment(TFCItems.silkString, false, Blocks.wool, 0));

        final Item[] foodToDry = new Item[]{TFCItems.venisonRaw, TFCItems.beefRaw, TFCItems.chickenRaw,
            TFCItems.porkchopRaw, TFCItems.fishRaw, TFCItems.seastarRaw, TFCItems.scallopRaw,
            TFCItems.calamariRaw, TFCItems.muttonRaw, TFCItems.horseMeatRaw, TFCItems.cheese};
        for (Item food : foodToDry) {
            setup.registry(DryingRackRegistry.recipes)
                .add((DryingRackRecipe) DryingRackRecipe.builder()
                    .tied()
                    .consumes(ItemFoodTFC.createTag(new ItemStack(food), 1))
                    .dry()
                    .hours(16)
                    .canSmokeInHours(12)
                    .build());
        }

        setup.registry(DryingRegistry.wetness)
            .add(TFCItems.seaWeed, new WetnessInfo(500, 1f));

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackRecipe) DryingRackRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.seaWeed), 1))
                .dry()
                .hours(16)
                .build());

        setup.event()
            .handler(new DryingRackEventHandler());
    }

}
