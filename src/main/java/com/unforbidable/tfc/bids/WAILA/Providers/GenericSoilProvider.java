package com.unforbidable.tfc.bids.WAILA.Providers;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Blocks.BlockAquifer;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemGenericSoil;
import com.unforbidable.tfc.bids.TileEntities.TileEntityAquifer;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class GenericSoilProvider extends WailaProvider {

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
