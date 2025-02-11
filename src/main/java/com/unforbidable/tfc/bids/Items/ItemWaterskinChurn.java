package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Churning.ChurningPlayerState;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Player.PlayerStateManager;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.BidsSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemWaterskinChurn extends ItemWaterskinFluid {

    private static final long TICKS_TO_CHURN_BUCKET_OF_CREAM = TFC_Time.HOUR_LENGTH;
    private static final float BUTTER_WEIGHT_PER_BUCKET_OF_CREAM = 40;

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        // Block animation with short duration imitates violent shaking
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        // The use animation is only 5 ticks, but churning cycle is greater
        // so the item isn't update as often
        return 5;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        double currentProgress = getChurningProgress(is);
        if (currentProgress < 1f) {
            ChurningPlayerState state = PlayerStateManager.getPlayerState(player, ChurningPlayerState.class);
            if (state != null) {
                long ticksSinceLastUpdate = TFC_Time.getTotalTicks() - state.ticksUpdated;
                if (ticksSinceLastUpdate > getMaxItemUseDuration(is)) {
                    // In case the previous churning wasn't stopped when player released the right mouse button
                    // too much time has passed, we restart the churn cycle
                    Bids.LOG.warn("Too many ticks since last update: " + ticksSinceLastUpdate);
                    state = null;
                } else if (state.slot != player.inventory.currentItem) {
                    // If the player changes current item during churning to another valid item
                    // start a new churn cycle for the new item
                    Bids.LOG.warn("Current item slot changed: " + state.slot + " vs " + player.inventory.currentItem);
                    state = null;
                }
            }

            if (state == null) {
                // Tracking churning cycle over multiple use actions
                state = new ChurningPlayerState();
                state.slot = player.inventory.currentItem;
                state.ticksStarted = TFC_Time.getTotalTicks();
                state.ticksUpdated = TFC_Time.getTotalTicks();
                state.ticksSoundPlayed = 0;
                state.progress = getChurningProgress(is);
                PlayerStateManager.setPlayerState(player, state);
            }

            player.setItemInUse(is, getMaxItemUseDuration(is));

            return is;
        } else {
            PlayerStateManager.clearPlayerState(player, ChurningPlayerState.class);

            int volume = getVolumeToChurn(is);
            float weight = BUTTER_WEIGHT_PER_BUCKET_OF_CREAM / 1000 * volume;

            ItemStack butter = ItemFoodTFC.createTag(new ItemStack(BidsItems.butter), weight);
            BidsEventFactory.onWaterskinChurnDone(player, is, butter);

            TFC_Core.giveItemToPlayer(butter, player);

            return getContainerItem(is);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            ChurningPlayerState state = PlayerStateManager.getPlayerState(player, ChurningPlayerState.class);

            long ticksElapsedSinceStart = TFC_Time.getTotalTicks() - state.ticksStarted;
            long ticksElapsedSinceUpdate = TFC_Time.getTotalTicks() - state.ticksUpdated;
            long ticksElapsedSinceSoundPlayed = TFC_Time.getTotalTicks() - state.ticksSoundPlayed;
            float progressToAdd = ticksElapsedSinceUpdate / (float)getTicksToChurn(stack, player);

            // Updating progress only in player state
            // without updating the item
            state.progress += progressToAdd;
            state.ticksUpdated = TFC_Time.getTotalTicks();

            if (state.progress >= 1f || ticksElapsedSinceStart >= 50) {
                // After 50 ticks or when the progress is done
                // stop the churning cycle to update the item
                player.stopUsingItem();
            } else if (ticksElapsedSinceSoundPlayed >= 27) {
                // Sound last 26 ticks and is played twice per churning cycle
                player.worldObj.playSoundAtEntity(player, BidsSounds.WATERSKIN_SLOSH, 1F, 1f);

                state.ticksSoundPlayed = TFC_Time.getTotalTicks();
            }
        }
    }

    protected long getTicksToChurn(ItemStack stack, EntityPlayer player) {
        return (long)Math.ceil((float)getVolumeToChurn(stack) / 1000 * TICKS_TO_CHURN_BUCKET_OF_CREAM);
    }

    protected int getVolumeToChurn(ItemStack is) {
        int damage = getDamage(is);
        int maxDamage = getMaxDamage(is);
        return (maxDamage - damage) * 50;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int count) {
        if (!world.isRemote) {
            ChurningPlayerState state = PlayerStateManager.getPlayerState(player, ChurningPlayerState.class, true);
            if (state != null) {
                setChurningProgress(stack, state.progress);

                player.inventoryContainer.detectAndSendChanges();
            }
        }
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        super.addExtraInformation(is, player, arraylist);

        int progress = Math.round(getChurningProgress(is) * 100);
        if (progress > 0) {
            arraylist.add(EnumChatFormatting.WHITE + TFC_Core.translate("gui.Butter") + ": " + progress + "%");
        }

        if (ItemHelper.showShiftInformation()) {
            arraylist.add(StatCollector.translateToLocal("gui.Help"));
            arraylist.add(StatCollector.translateToLocal("gui.Help.Waterskin.Churn"));
        } else {
            arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

    private float getChurningProgress(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().hasKey("ChurningProgress")) {
            return is.getTagCompound().getFloat("ChurningProgress");
        }

        return 0;
    }

    private void setChurningProgress(ItemStack is, float progress) {
        if (!is.hasTagCompound()) {
            is.setTagCompound(new NBTTagCompound());
        }

        is.getTagCompound().setFloat("ChurningProgress", Math.min(progress, 1f));
    }

}
