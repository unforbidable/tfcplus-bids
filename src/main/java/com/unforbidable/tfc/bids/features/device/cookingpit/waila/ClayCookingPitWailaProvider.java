package com.unforbidable.tfc.bids.features.device.cookingpit.waila;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.crafting.cooking.main.CookingRecipeProgress;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.device.cookingpit.tileentity.TileEntityClayCookingPit;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import java.util.List;

public class ClayCookingPitWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY | PROVIDES_STACK;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityClayCookingPit) {
            TileEntityClayCookingPit tileEntityCookingPit = (TileEntityClayCookingPit) accessor.getTileEntity();

            if (tileEntityCookingPit.isReady() && tileEntityCookingPit.isInputItemSelected()) {
                return tileEntityCookingPit.getInputItemStack();
            }
        }

        return super.getWailaStack(accessor, config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityClayCookingPit) {
            TileEntityClayCookingPit tileEntityCookingPit = (TileEntityClayCookingPit) accessor.getTileEntity();

            if (tileEntityCookingPit.isReady()) {
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
            } else {
                String progress = tileEntityCookingPit.isDryingPaused()
                    ? " (" + StatCollector.translateToLocal("gui.Paused") + ")"
                    : tileEntityCookingPit.getDryingPercent() > 0 ? String.format(" (%d%%)", tileEntityCookingPit.getDryingPercent()) : "";
                currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Drying") + progress);
            }
        }

        return currenttip;
    }

}
