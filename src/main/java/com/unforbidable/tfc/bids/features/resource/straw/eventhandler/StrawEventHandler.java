package com.unforbidable.tfc.bids.features.resource.straw.eventhandler;

import com.dunk.tfc.Items.Tools.ItemCustomShovel;
import com.dunk.tfc.Items.Tools.ItemWeapon;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.util.harvest.HarvestHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.lang.reflect.Field;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

public class StrawEventHandler {

    @SubscribeEvent
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
        if (event.block == TFCBlocks.tallGrass) {
            // Allow normal block destroy event tall grass has no hardness
            // otherwise handle here to prevent tools taking extra damage
            // The method can handle both cases, but for better compatibility default processing is preferred for no hardness
            float hardness = event.block.getBlockHardness(event.world, event.x, event.y, event.z);
            if (hardness > 0) {
                HarvestHelper.harvestStraw(event.getPlayer(), event.world, event.x, event.y, event.z, event.block, event.blockMetadata);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem != null) {
            // This cancels axe efficiency on specified plant blocks
            if (heldItem.getItem() instanceof ItemAxe && shouldCancelAxeEffectiveness(event.block, event.metadata)) {
                event.newSpeed = 1;
            }

            // Tool harvest is not considered for digging speed calculation of weapons, such as knives, and a stone flake
            // If the "tool" is effective, use the digging speed directly from the material
            // And add smithing bonus
            // If speed is less than 4, then harvest effectivity was likely not calculated correctly
            if (event.newSpeed < 4 && ForgeHooks.isToolEffective(heldItem, event.block, event.metadata)) {
                if (heldItem.getItem() instanceof ItemWeapon) {
                    float speed = getWeaponEfficiencyOnProperMaterial(heldItem.getItem());
                    event.newSpeed = speed + speed * AnvilManager.getDurabilityBuff(heldItem);
                } else if (heldItem.getItem() instanceof ItemAxe) {
                    float speed = getAxeEfficiencyOnProperMaterial(heldItem.getItem());
                    event.newSpeed = speed + speed * AnvilManager.getDurabilityBuff(heldItem);
                } else if (heldItem.getItem() instanceof ItemHoe) {
                    float speed = getHoeEfficiencyOnProperMaterial(heldItem.getItem());
                    event.newSpeed = speed + speed * AnvilManager.getDurabilityBuff(heldItem);
                } else if (heldItem.getItem() == TFCItems.stoneFlake) {
                    // Take stone efficiency, will certain penalty, but still better than using not tool
                    event.newSpeed = TFCItems.igInToolMaterial.getEfficiencyOnProperMaterial() * 0.5f;
                }
            }

            // Give bonus speed to destroy grass quickly using a shovel for the player's convenience during landscaping
            if (event.block == TFCBlocks.tallGrass && heldItem.getItem() instanceof ItemCustomShovel) {
                event.newSpeed = ((ItemCustomShovel)heldItem.getItem()).func_150913_i().getEfficiencyOnProperMaterial() * 1.5f;
            }
        }
    }

    private boolean shouldCancelAxeEffectiveness(Block block, int metadata) {
        return block.getMaterial() == Material.plants || block.getMaterial() == Material.vine;
    }

    private static float getWeaponEfficiencyOnProperMaterial(Item item) {
        try {
            Field toolMatField = ItemWeapon.class.getDeclaredField("toolMat");
            toolMatField.setAccessible(true);
            Item.ToolMaterial mat = (Item.ToolMaterial)toolMatField.get(item);
            return mat.getEfficiencyOnProperMaterial();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return 4;
        }
    }

    @SuppressWarnings("all")
    private static float getAxeEfficiencyOnProperMaterial(Item item) {
        try {
            Field efficiencyOnProperMaterialField = ItemTool.class.getDeclaredField("field_77864_a");
            efficiencyOnProperMaterialField.setAccessible(true);
            return (float)efficiencyOnProperMaterialField.get(item);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return 4;
        }
    }

    @SuppressWarnings("all")
    private static float getHoeEfficiencyOnProperMaterial(Item item) {
        try {
            Field theToolMaterialField = ItemHoe.class.getDeclaredField("field_77843_a");
            theToolMaterialField.setAccessible(true);
            Item.ToolMaterial mat = (Item.ToolMaterial)theToolMaterialField.get(item);
            return mat.getEfficiencyOnProperMaterial();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return 4;
        }
    }

}
