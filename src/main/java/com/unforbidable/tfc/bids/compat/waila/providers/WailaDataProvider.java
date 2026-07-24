package com.unforbidable.tfc.bids.compat.waila.providers;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.waila.registry.WailaRegistry;
import com.unforbidable.tfc.bids.compat.waila.registry.WailaRegistryEntry;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WailaDataProvider implements IWailaDataProvider {

    protected static final int PROVIDES_HEAD = 1;
    protected static final int PROVIDES_BODY = 2;
    protected static final int PROVIDES_TAIL = 4;
    protected static final int PROVIDES_STACK = 8;
    protected static final int PROVIDES_NBT = 16;

    public static void setup(IWailaRegistrar reg) {
        try {
            for (WailaRegistryEntry<WailaDataProvider> entry : WailaRegistry.dataProviders) {
                int flags = entry.provider.provides();

                if ((flags & PROVIDES_HEAD) != 0) {
                    for (Class<?> t : entry.types) {
                        reg.registerHeadProvider(entry.provider, t);
                    }
                }

                if ((flags & PROVIDES_BODY) != 0) {
                    for (Class<?> t : entry.types) {
                        reg.registerBodyProvider(entry.provider, t);
                    }
                }

                if ((flags & PROVIDES_TAIL) != 0) {
                    for (Class<?> t : entry.types) {
                        reg.registerTailProvider(entry.provider, t);
                    }
                }

                if ((flags & PROVIDES_STACK) != 0) {
                    for (Class<?> t : entry.types) {
                        reg.registerStackProvider(entry.provider, t);
                    }
                }

                if ((flags & PROVIDES_NBT) != 0) {
                    for (Class<?> t : entry.types) {
                        reg.registerNBTProvider(entry.provider, t);
                    }
                }
            }
        } catch (Exception ex) {
            Bids.LOG.error(ex);
        }
    }

    public int provides() {
        return 0;
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
        return currenttip;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
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
