package com.unforbidable.tfc.bids.features.crafting.flintknapping.eventhandler;

import com.dunk.tfc.Blocks.Terrain.BlockGravel;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemBlocks.ItemSoil;
import com.dunk.tfc.Items.ItemLooseRock;
import com.dunk.tfc.api.TFCBlocks;
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
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;

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
                        TFC_Core.giveItemToPlayer(new ItemStack(BidsItems.flintFlake, totalAmount), event.player);
                    }
                }
            }
        }
    }

    private float getFlakeAmountForAction(String actionName) {
        if (actionName.startsWith("hardHammerReduce") || actionName.startsWith("softHammerReduce")) {
            return 1 / 4f;
        }

        return 0;
    }

    @SubscribeEvent
    public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.block instanceof BlockGravel) {
            // Find gravel block to be replaced
            // If a flint is already dropping by chance instead, do nothing
            for (int i = 0; i < event.drops.size(); i++) {
                ItemStack drop = event.drops.get(i);
                if (drop.getItem() instanceof ItemSoil) {
                    Block dropBlock = Block.getBlockFromItem(drop.getItem());
                    if (dropBlock instanceof BlockGravel) {
                        // Dropped soil item block is that of Gravel
                        // just need to find out which and get the whole meta
                        int damage = drop.getItemDamage() + (dropBlock == TFCBlocks.gravel2 ? 16 : 0);
                        if (hasRockTypeAffinity(damage) && event.world.rand.nextInt(3) == 0) {
                            // Replace the dropped gravel block item stack
                            ItemStack newDrop = new ItemStack(Items.flint);
                            event.drops.set(i, newDrop);
                        }
                    }
                }
            }
        }
    }

    private boolean hasRockTypeAffinity(int damage) {
        return damage == StoneMeta.CHERT;
    }

}
