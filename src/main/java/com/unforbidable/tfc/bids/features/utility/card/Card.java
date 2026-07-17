package com.unforbidable.tfc.bids.features.utility.card;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingOreRecipe;
import com.unforbidable.tfc.bids.api.names.WoodworkingPlanNames;
import com.unforbidable.tfc.bids.common.item.ItemCommonToolPart;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.crafting.handwork.render.HandworkToolItemRenderer;
import com.unforbidable.tfc.bids.features.crafting.woodworking.WoodworkingRegistry;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.geometry.Shape;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.plan.Plan;
import com.unforbidable.tfc.bids.features.utility.card.eventhandler.CardEventHandler;
import com.unforbidable.tfc.bids.features.utility.card.item.ItemCard;
import com.unforbidable.tfc.bids.features.utility.card.item.ItemThornBunch;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.api.names.ItemNames.THORN_BUNCH;
import static com.unforbidable.tfc.bids.api.names.ItemNames.THORN_CARD;
import static com.unforbidable.tfc.bids.api.names.ItemNames.WOODEN_COMB_PADDLE;

@FeatureName("card")
public class Card extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(THORN_BUNCH, ItemThornBunch::new);
        init.item(WOODEN_COMB_PADDLE, ItemCommonToolPart::new);
        init.item(THORN_CARD, () -> new ItemCard(TFCItems.boneToolMaterial));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new HandworkToolItemRenderer())
            .item(BidsItems.thornCard);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.event()
            .handler(new CardEventHandler());

        setup.recipes().addShapeless(new ItemStack(BidsItems.thornCard),
                BidsItems.thornBunch, BidsItems.thornBunch, TFCItems.resin, BidsItems.woodenCombPaddle);

        setup.registry(WoodworkingRegistry.plans)
            .add(Plan.create(WoodworkingPlanNames.PLAN_COMB_PADDLE)
                .cutout(Shape.rectFrom(0, 0).size(5, 12)) // top 1/2 left cut off
                .cutout(Shape.rectFrom(8, 0).size(5, 12)) // top 1/2 right cut off
                .cutout(Shape.rectFrom(0, 12).size(3, 13)) // bottom 1/2 left cut off
                .cutout(Shape.rectFrom(10, 12).size(3, 13)) // bottom 1/2 right cut off
                .cutout(Shape.triFrom(3, 12).size(2, 2)) // top left corner
                .cutout(Shape.triFrom(10, 12).size(-2, 2)) // top right corner
                .cutout(Shape.pointAt(4, 23)) // hole
                .cutout(Shape.pointAt(6, 23)) // hole
                .cutout(Shape.pointAt(8, 23)) // hole
                .cutout(Shape.pointAt(4, 21)) // hole
                .cutout(Shape.pointAt(6, 21)) // hole
                .cutout(Shape.pointAt(8, 21)) // hole
                .cutout(Shape.pointAt(4, 19)) // hole
                .cutout(Shape.pointAt(6, 19)) // hole
                .cutout(Shape.pointAt(8, 19)) // hole
                .cutout(Shape.pointAt(4, 17)) // hole
                .cutout(Shape.pointAt(6, 17)) // hole
                .cutout(Shape.pointAt(8, 17)) // hole
                .cutout(Shape.pointAt(4, 15)) // hole
                .cutout(Shape.pointAt(6, 15)) // hole
                .cutout(Shape.pointAt(8, 15)) // hole
                .build());

        setup.registry(WoodworkingRegistry.recipes)
            .add(new WoodworkingOreRecipe(WoodworkingPlanNames.PLAN_COMB_PADDLE,
                "boardWood", new ItemStack(BidsItems.woodenCombPaddle)));
    }

}
