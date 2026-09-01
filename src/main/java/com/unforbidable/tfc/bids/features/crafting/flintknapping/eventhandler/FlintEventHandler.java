package com.unforbidable.tfc.bids.features.crafting.flintknapping.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemLooseRock;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingActionSummary;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingEvent;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingMaterial;
import com.unforbidable.tfc.bids.api.features.woodworking.WoodworkingToolEvent;
import com.unforbidable.tfc.bids.api.meta.StoneMeta;
import com.unforbidable.tfc.bids.features.crafting.flintknapping.main.FlintKnappingMaterials;
import com.unforbidable.tfc.bids.features.crafting.woodworking.main.WoodworkingHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.item.ItemStack;

public class FlintEventHandler {

    @SubscribeEvent
    public void onWoodworkingToolDamage(WoodworkingToolEvent.Damage event) {
        if (event.tool.getItem() instanceof ItemLooseRock) {
            event.newDamage = event.initialDamage * getLooseRockDamageMultiplier(event.tool.getItemDamage());
        }
    }

    private float getLooseRockDamageMultiplier(int damage) {
        switch (damage) {
            case StoneMeta.QUARTZITE:
                return 0.05f;
            case StoneMeta.BASALT:
            case StoneMeta.ANDESITE:
            case StoneMeta.DACITE:
            case StoneMeta.GABBRO:
                return 0.1f;
            case StoneMeta.DIORITE:
            case StoneMeta.GRANITE:
            case StoneMeta.GNEISS:
                return 0.2f;
            case StoneMeta.SANDSTONE:
            case StoneMeta.LIMESTONE:
            case StoneMeta.DOLOMITE:
                return 0.5f;
        }

        return 1f;
    }

    @SubscribeEvent
    public void onWoodworkingItemPickedUp(WoodworkingEvent.ItemPickedUp event) {
        WoodworkingMaterial material = WoodworkingHelper.getWoodworkingMaterial(event.input);
        if (material != null) {
            if (material.getMaterialName().equals(FlintKnappingMaterials.FLINT_RAW) ||
                material.getMaterialName().equals(FlintKnappingMaterials.FLINT_CORE)) {
                float flake = 0;

                for (WoodworkingActionSummary summary : event.summary) {
                    flake += getFlakeAmountForAction(summary.actionName) * summary.count;
                }

                if (flake > 0) {
                    int integralAmount = (int) Math.floor(flake);
                    float partialAmount = flake - integralAmount;
                    int totalAmount = integralAmount + (new Random().nextFloat() < partialAmount ? 1 : 0);
                    if (totalAmount > 0) {
                        TFC_Core.giveItemToPlayer(new ItemStack(BidsItems.flintFlake), event.player);
                    }
                }
            }
        }
    }

    private float getFlakeAmountForAction(String actionName) {
        if (actionName.startsWith("hardHammerReduce") || actionName.startsWith("softHammerReduce")) {
            return 1 / 8f;
        }

        return 0;
    }

}
