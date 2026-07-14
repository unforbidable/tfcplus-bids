package com.unforbidable.tfc.bids.features.device.dryingrack;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackFoodRecipe;
import com.unforbidable.tfc.bids.api.features.drying.DryingRackTyingEquipment;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.dryingrack.block.BlockDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.eventhandler.DryingRackEventHandler;
import com.unforbidable.tfc.bids.features.device.dryingrack.render.RenderDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.render.RenderTileDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.tileentity.TileEntityDryingRack;
import com.unforbidable.tfc.bids.features.device.dryingrack.waila.DryingRackWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.BlockNames.DRYING_RACK;

@FeatureName("dryingRack")
public class DryingRack extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(DRYING_RACK, BlockDryingRack::new);

        init.tileEntity(TileEntityDryingRack.class, "BidsDryingRack");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderDryingRack())
            .block(BlockDryingRack.class);

        client.render(new RenderTileDryingRack())
            .tileEntity(TileEntityDryingRack.class);

        client.waila()
            .data(new DryingRackWailaProvider(), TileEntityDryingRack.class);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.registry(DryingRackRegistry.tyingEquipment)
            .add(new DryingRackTyingEquipment(TFCItems.woolYarn, false, Blocks.wool, 0))
            .add(new DryingRackTyingEquipment(TFCItems.linenString, false, Blocks.wool, 0))
            .add(new DryingRackTyingEquipment(TFCItems.cottonYarn, false, Blocks.wool, 0))
            .add(new DryingRackTyingEquipment(TFCItems.silkString, false, Blocks.wool, 0));
        // TODO register in respective features
        //.add(new DryingRackTyingEquipment(BidsItems.barkCordage, false, Blocks.wool, 1))
        //.add(new DryingRackTyingEquipment(BidsItems.sisalTwine, false, Blocks.wool, 1))
        //.add(new DryingRackTyingEquipment(BidsItems.juteTwine, false, Blocks.wool, 1));

        // TODO add BidsItems.goatCheese drying to respective feature
        final Item[] foodToDry = new Item[]{TFCItems.venisonRaw, TFCItems.beefRaw, TFCItems.chickenRaw,
            TFCItems.porkchopRaw, TFCItems.fishRaw, TFCItems.seastarRaw, TFCItems.scallopRaw,
            TFCItems.calamariRaw, TFCItems.muttonRaw, TFCItems.horseMeatRaw, TFCItems.cheese};
        for (Item food : foodToDry) {
            setup.registry(DryingRackRegistry.recipes)
                .add((DryingRackFoodRecipe) DryingRackFoodRecipe.builder()
                    .smoke(12)
                    .tied()
                    .consumes(ItemFoodTFC.createTag(new ItemStack(food), 1))
                    .dry()
                    .hours(16)
                    .build());
        }

        setup.registry(DryingRackRegistry.recipes)
            .add((DryingRackFoodRecipe) DryingRackFoodRecipe.builder()
                .consumes(ItemFoodTFC.createTag(new ItemStack(TFCItems.seaWeed), 1))
                .dry()
                .hours(16)
                .build());

        setup.event()
            .handler(new DryingRackEventHandler());
    }

}
