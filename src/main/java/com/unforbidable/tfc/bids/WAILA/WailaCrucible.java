package com.unforbidable.tfc.bids.WAILA;

import java.util.List;

import com.dunk.tfc.TileEntities.TEChimney;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.Core.Quarry.QuarryHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityChimney;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityQuarry;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class WailaCrucible implements IWailaDataProvider {

    public static void callbackRegister(IWailaRegistrar reg) {
        reg.registerBodyProvider(new WailaCrucible(), TileEntityCrucible.class);
        reg.registerNBTProvider(new WailaCrucible(), TileEntityCrucible.class);

        reg.registerBodyProvider(new WailaCrucible(), TEChimney.class);
        reg.registerBodyProvider(new WailaCrucible(), TileEntityChimney.class);

        reg.registerBodyProvider(new WailaCrucible(), TileEntityQuarry.class);
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP playerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound,
            World world, int arg4, int arg5, int arg6) {
        if (tileEntity != null)
            tileEntity.writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCrucible) {
            TileEntityCrucible tileEntityCrucible = (TileEntityCrucible) accessor.getTileEntity();

            for (String line : tileEntityCrucible
                    .getLiquidInfo(StatCollector.translateToLocal("gui.symbol.BulletPoint") + " ")) {
                currenttip.add(EnumChatFormatting.GRAY + line);
            }

            int liquidTemp = tileEntityCrucible.getCombinedTemp();
            if (liquidTemp > 0) {
                String liquidTempString = TFC_ItemHeat.getHeatColor(liquidTemp, Integer.MAX_VALUE);
                currenttip.add(liquidTempString);
            }

            if (tileEntityCrucible.isRuined()) {
                currenttip.add(EnumChatFormatting.DARK_RED
                        + StatCollector.translateToLocal("gui.Ruined"));
            }
        }
        if (ChimneyHelper.isChimney(accessor.getTileEntity())) {
            TileEntityCrucible crucible = ChimneyHelper.findActiveFurnaceCrucible(accessor.getTileEntity());
            if (crucible != null)
                currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Glassmaking") + ": "
                        + crucible.getGlassMakingRemainingHours() + " "
                        + StatCollector.translateToLocal("gui.HoursRemaining"));

        }
        if (accessor.getTileEntity() instanceof TileEntityQuarry) {
            TileEntityQuarry quarry = (TileEntityQuarry) accessor.getTileEntity();
            currenttip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("gui.Wedges") + ": "
                    + quarry.getWedgeCount() + "/" + quarry.getMaxWedgeCount());

            // Wait for the quarry to be initialized
            // before even considering showing readiness
            if (quarry.getMaxWedgeCount() > 0
                    && QuarryHelper.isQuarryReadyAt(accessor.getWorld(), accessor.getPosition().blockX,
                            accessor.getPosition().blockY, accessor.getPosition().blockZ)) {
                currenttip.add(EnumChatFormatting.WHITE + StatCollector.translateToLocal("gui.QuarryReady"));
            }
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler arg3) {
        return currenttip;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        return null;
    }

}
