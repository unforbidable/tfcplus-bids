package com.unforbidable.tfc.bids.features.resource.well.waila;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.common.block.itemblock.ItemGenericSoil;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaDataProvider;
import com.unforbidable.tfc.bids.features.resource.well.block.BlockAquifer;
import com.unforbidable.tfc.bids.features.resource.well.tileentity.TileEntityAquifer;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class GenericSoilWailaProvider extends WailaDataProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (itemStack.getItem() instanceof ItemGenericSoil) {
            int dam = itemStack.getItemDamage();

            TileEntityAquifer te = (TileEntityAquifer) accessor.getTileEntity();
            int total = te.getNeighborTotalCount();

            currenttip.add(EnumChatFormatting.AQUA + "Aquifer (" + total + ")");

            Block block = Block.getBlockFromItem(itemStack.getItem());
            if (block instanceof BlockAquifer) {
                dam += ((BlockAquifer) block).getTextureOffset();
            }

            if (dam < Global.STONE_ALL.length) {
                currenttip.add(EnumChatFormatting.GRAY + Global.STONE_ALL[dam]);
            } else {
                currenttip.add(EnumChatFormatting.DARK_RED + "Unknown");
            }
        }

        return currenttip;
    }
}
