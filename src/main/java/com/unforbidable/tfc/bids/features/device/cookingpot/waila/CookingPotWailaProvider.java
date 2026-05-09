package com.unforbidable.tfc.bids.features.device.cookingpot.waila;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingHelper;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingRecipeProgress;
import com.unforbidable.tfc.bids.features.device.cookingpot.tileentity.TileEntityCookingPot;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class CookingPotWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY | PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCookingPot) {
            TileEntityCookingPot tileEntityCookingPot = (TileEntityCookingPot) accessor.getTileEntity();

            if (tileEntityCookingPot.isInputItemSelected()) {
                return tileEntityCookingPot.getInputItemStack();
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCookingPot) {
            TileEntityCookingPot tileEntityCookingPot = (TileEntityCookingPot) accessor.getTileEntity();

            if (!tileEntityCookingPot.isInputItemSelected()) {
                CookingHelper.getCookingPotInfo(tileEntityCookingPot, currenttip, true);

                CookingRecipeProgress recipeProgress = tileEntityCookingPot.getRecipeProgress();
                if (recipeProgress != null) {
                    String output = recipeProgress.getOutputDisplayText();
                    String progress = recipeProgress.isProgressPaused()
                        ? " (" + StatCollector.translateToLocal("gui.Paused") + ")"
                        : recipeProgress.getProgressRounded() > 0 ? String.format(" (%d%%)", recipeProgress.getProgressRounded()) : "";

                    currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                        + ChatFormatting.WHITE + output + progress);
                }
            }
        }

        return currenttip;
    }

}
