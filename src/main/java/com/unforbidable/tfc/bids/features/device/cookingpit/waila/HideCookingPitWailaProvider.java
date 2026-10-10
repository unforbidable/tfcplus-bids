package com.unforbidable.tfc.bids.features.device.cookingpit.waila;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingRecipeProgress;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityHideCookingPit;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.nbt.HideVesselTag;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class HideCookingPitWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY | PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityHideCookingPit) {
            TileEntityHideCookingPit tileEntityCookingPit = (TileEntityHideCookingPit) accessor.getTileEntity();

            if (tileEntityCookingPit.isInputItemSelected()) {
                return tileEntityCookingPit.getInputItemStack();
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityHideCookingPit) {
            TileEntityHideCookingPit tileEntityCookingPit = (TileEntityHideCookingPit) accessor.getTileEntity();

            if (!tileEntityCookingPit.isInputItemSelected()) {
                CookingPitHelper.getCookingPitInfo(tileEntityCookingPit, currenttip);

                CookingRecipeProgress recipeProgress = tileEntityCookingPit.getRecipeProgress();
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
