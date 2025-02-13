package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Churning.ChurningPlayerState;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Player.PlayerStateManager;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import com.unforbidable.tfc.bids.api.BidsSounds;
import com.unforbidable.tfc.bids.api.Crafting.ChurningManager;
import com.unforbidable.tfc.bids.api.Crafting.ChurningRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ItemWaterskinChurn extends ItemWaterskinFluid {

    // The use animation is only 5 ticks, but churning cycle is greater
    // so the item isn't update as often
    private static final int CHURN_ANIMATION_TICKS = 5;

    // After 50 ticks
    // stop the churning cycle to update the item
    private static final int CHURN_CYCLE_TICKS = 50;

    // Sound last 26 ticks and is played twice per churning cycle
    private static final int CHURN_SOUND_TICKS = 26;

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        // Block animation with short duration imitates violent shaking
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        return CHURN_ANIMATION_TICKS;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        FluidStack fluidToChurn = getFluidToChurn(is);
        if (fluidToChurn != null) {
            ChurningRecipe recipe = ChurningManager.findMatchingRecipe(fluidToChurn);
            if (recipe != null) {
                // Check progress saved in NBT
                float currentProgress = getChurningProgress(is);
                if (currentProgress == 1f) {
                    // If completed
                    // give butter to player
                    // and return empty waterskin
                    PlayerStateManager.clearPlayerState(player, ChurningPlayerState.class);

                    ItemStack result = recipe.getResult(fluidToChurn);

                    BidsEventFactory.onWaterskinChurnDone(player, is, result);

                    TFC_Core.giveItemToPlayer(result, player);

                    return getContainerItem(is);
                }

                // Validate current churning cycle if any
                // or start a new one if needed
                if (!doesPlayerHaveValidChurningState(player)) {
                    // Tracking churning cycle over multiple use actions
                    // Player state is used
                    ChurningPlayerState state = new ChurningPlayerState();
                    state.slot = player.inventory.currentItem;
                    state.ticksStarted = TFC_Time.getTotalTicks();
                    state.ticksSoundPlayed = 0;
                    PlayerStateManager.setPlayerState(player, state);
                }

                player.setItemInUse(is, getMaxItemUseDuration(is));
            }
        }

        return is;
    }

    private boolean doesPlayerHaveValidChurningState(EntityPlayer player) {
        ChurningPlayerState state = PlayerStateManager.getPlayerState(player, ChurningPlayerState.class);
        if (state != null) {
            // Churn cycle is in progress
            // check validity and restart if needed
            long ticksSinceStart = TFC_Time.getTotalTicks() - state.ticksStarted;
            if (ticksSinceStart > CHURN_CYCLE_TICKS) {
                // In case the previous churning wasn't stopped when player released the right mouse button
                // too much time has passed, we restart the churn cycle
                Bids.LOG.warn("Too many ticks since start: " + ticksSinceStart);
                return false;
            } else if (state.slot != player.inventory.currentItem) {
                // If the player changes current item during churning to another valid item
                // start a new churn cycle for the new item
                Bids.LOG.warn("Current item slot changed: " + state.slot + " vs " + player.inventory.currentItem);
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            ChurningPlayerState state = PlayerStateManager.getPlayerState(player, ChurningPlayerState.class);

            long ticksElapsedSinceStart = TFC_Time.getTotalTicks() - state.ticksStarted;
            long ticksElapsedSinceSoundPlayed = TFC_Time.getTotalTicks() - state.ticksSoundPlayed;

            if (ticksElapsedSinceStart >= CHURN_CYCLE_TICKS) {
                player.stopUsingItem();
            } else if (ticksElapsedSinceSoundPlayed >= CHURN_SOUND_TICKS) {
                player.worldObj.playSoundAtEntity(player, BidsSounds.WATERSKIN_SLOSH, 1F, 1f);

                state.ticksSoundPlayed = TFC_Time.getTotalTicks();
            }
        }
    }

    protected FluidStack getFluidToChurn(ItemStack is) {
        FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(is);
        if (fs != null) {
            int damage = getDamage(is);
            int maxDamage = getMaxDamage(is);
            fs.amount = (maxDamage - damage) * 50;
            return fs;
        }

        return null;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int count) {
        if (!world.isRemote) {
            ChurningPlayerState state = PlayerStateManager.getPlayerState(player, ChurningPlayerState.class, true);
            if (state != null) {
                FluidStack fluidToChurn = getFluidToChurn(stack);
                if (fluidToChurn != null) {
                    ChurningRecipe recipe = ChurningManager.findMatchingRecipe(fluidToChurn);
                    if (recipe != null) {
                        long ticksElapsedSinceStart = TFC_Time.getTotalTicks() - state.ticksStarted;
                        float progress = ticksElapsedSinceStart / recipe.getTotalDuration(fluidToChurn);
                        setChurningProgress(stack, getChurningProgress(stack) + progress);

                        player.inventoryContainer.detectAndSendChanges();
                    }
                }
            }
        }
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        super.addExtraInformation(is, player, arraylist);

        FluidStack fs = getFluidToChurn(is);
        if (fs != null) {
            ChurningRecipe recipe = ChurningManager.findMatchingRecipe(fs);
            if (recipe != null) {
                int progress = Math.round(getChurningProgress(is) * 100);
                if (progress > 0) {
                    arraylist.add(EnumChatFormatting.WHITE + recipe.getResult(fs).getDisplayName() + ": " + progress + "%");
                }

                if (ItemHelper.showShiftInformation()) {
                    arraylist.add(StatCollector.translateToLocal("gui.Help"));
                    arraylist.add(StatCollector.translateToLocal("gui.Help.Waterskin.Churn") +
                            recipe.getResult(fs).getDisplayName() +
                            StatCollector.translateToLocal("gui.Help.Waterskin.Churn2"));
                } else {
                    arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
                }
            }
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
