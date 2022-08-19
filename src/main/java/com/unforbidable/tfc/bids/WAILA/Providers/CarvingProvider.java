package com.unforbidable.tfc.bids.WAILA.Providers;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class CarvingProvider extends WailaProvider {

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCarving) {
            TileEntityCarving carving = (TileEntityCarving) accessor.getTileEntity();
            Block block = Block.getBlockById(carving.getCarvedBlockId());
            int metadata = carving.getCarvedBlockMetadata();
            return new ItemStack(block, 1, metadata);
        }

        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCarving) {
            TileEntityCarving carving = (TileEntityCarving) accessor.getTileEntity();
            ItemStack output = carving.getCraftingResult();
            if (output != null) {
                currenttip.add(ChatFormatting.GRAY + StatCollector.translateToLocal("gui.Output") + ": "
                        + ChatFormatting.WHITE + output.getDisplayName());
            }
        }

        return super.getWailaBody(itemStack, currenttip, accessor, config);
    }

}
