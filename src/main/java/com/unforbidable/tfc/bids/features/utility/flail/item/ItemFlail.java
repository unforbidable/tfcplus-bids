package com.unforbidable.tfc.bids.features.utility.flail.item;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Enums.EnumDamageType;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.features.threshing.ThreshingRecipe;
import com.unforbidable.tfc.bids.common.item.ItemCommonWeapon;
import com.unforbidable.tfc.bids.features.crafting.threshing.ThreshingConfig;
import com.unforbidable.tfc.bids.features.crafting.threshing.ThreshingRegistry;
import com.unforbidable.tfc.bids.features.crafting.threshing.main.ThreshingHelper;
import com.unforbidable.tfc.bids.features.crafting.threshing.main.ThreshingPlayerState;
import com.unforbidable.tfc.bids.util.playerstate.PlayerStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFlail extends ItemCommonWeapon {

    public ItemFlail(ToolMaterial material, float damage) {
        super(material, damage, EnumDamageType.CRUSHING);

        setMaxDamage(material.getMaxUses() * 2);
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.FAR;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
                                  float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            for (EntityItem entityItem : ThreshingHelper.getEntityItemNearBy(world, x, y, z)) {
                ThreshingRecipe recipe = ThreshingRegistry.recipes.findMatchingRecipe(entityItem.getEntityItem());
                if (recipe != null) {
                    int duration = Math.round(recipe.getDuration() * ThreshingHelper.getToolDurationMultiplier(stack) * ThreshingConfig.threshingDurationMultiplier);
                    ThreshingPlayerState state = new ThreshingPlayerState();
                    state.threshingFinishedTick = TFC_Time.getTotalTicks() + duration;
                    state.x = x;
                    state.y = y;
                    state.z = z;
                    PlayerStateManager.setPlayerState(player, state);
                }
            }
        }

        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            ThreshingPlayerState state = PlayerStateManager.getPlayerState(player, ThreshingPlayerState.class);
            if (state != null) {
                long timeRemaining = state.threshingFinishedTick - TFC_Time.getTotalTicks();
                if (timeRemaining < 0) {
                    if (timeRemaining >= -5) {
                        for (EntityItem entityItem : ThreshingHelper.getEntityItemNearBy(player.worldObj, state.x, state.y, state.z)) {
                            ThreshingRecipe recipe = ThreshingRegistry.recipes.findMatchingRecipe(entityItem.getEntityItem());
                            if (recipe != null) {
                                ThreshingHelper.doThresh(player.worldObj, state.x, state.y, state.z, player, stack, entityItem, recipe);
                            }
                        }

                        player.stopUsingItem();
                    }

                    PlayerStateManager.clearPlayerState(player, ThreshingPlayerState.class);
                }
            }
        }
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            PlayerStateManager.clearPlayerState(player, ThreshingPlayerState.class);
        }
    }

}
