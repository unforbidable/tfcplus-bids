package com.unforbidable.tfc.bids.features.material.powder.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSummary;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingEvent;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.api.meta.MorePowderMeta;
import com.unforbidable.tfc.bids.api.names.WoodworkingMaterialNames;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.WoodworkingHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import java.util.Random;

public class SawdustWoodworkingEventHandler {

    @SubscribeEvent
    public void onWoodworkingItemPickedUp(WoodworkingEvent.ItemPickedUp event) {
        WoodworkingMaterial material = WoodworkingHelper.getWoodworkingMaterial(event.input);
        if (material != null) {
            float materialMultiplier = getSawdustMaterialMultiplier(material);
            if (materialMultiplier > 0) {
                float sawdustAmount = 0;

                for (WoodworkingActionSummary summary : event.summary) {
                    sawdustAmount += getSawdustAmountForAction(summary.actionName) * summary.count;
                }

                if (sawdustAmount > 0) {
                    sawdustAmount *= materialMultiplier;
                    int integralAmount = (int) Math.floor(sawdustAmount);
                    float partialAmount = sawdustAmount - integralAmount;
                    int totalAmount = integralAmount + (new Random().nextFloat() < partialAmount ? 1 : 0);
                    if (totalAmount > 0) {
                        TFC_Core.giveItemToPlayer(new ItemStack(BidsItems.morePowder, totalAmount, MorePowderMeta.SAWDUST), event.player);
                    }
                }
            }
        }
    }

    private float getSawdustMaterialMultiplier(WoodworkingMaterial material) {
        switch (material.getMaterialName()) {
            case WoodworkingMaterialNames.WOOD_THICK:
                return 1;
            case WoodworkingMaterialNames.WOOD_FLAT:
                return 0.5f;
        }

        return 0;
    }

    private float getSawdustAmountForAction(String actionName) {
        if (actionName.startsWith("saw")) {
            // Sawing a whole length of a thick material gives 1 sawdust
            return 1 / 25f;
        } else if (actionName.startsWith("drill")) {
            // Drilling 15 holes in flat gives 1 sawdust
            return 1 / 7.5f;
        }

        return 0;
    }

}
