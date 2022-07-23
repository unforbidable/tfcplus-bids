package com.unforbidable.tfc.bids.WAILA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WailaProvider implements IWailaDataProvider {

    protected static final int PROVIDES_HEAD = 1;
    protected static final int PROVIDES_BODY = 2;
    protected static final int PROVIDES_TAIL = 4;
    protected static final int PROVIDES_STACK = 8;
    protected static final int PROVIDES_NBT = 16;

    static final Map<WailaProvider, Class<?>[]> providers = new HashMap<WailaProvider, Class<?>[]>();

    public static void setup(IWailaRegistrar reg) {
        for (Entry<WailaProvider, Class<?>[]> p : providers.entrySet()) {
            WailaProvider provider = p.getKey();
            Class<?>[] types = p.getValue();

            int flags = provider.provides();

            if ((flags & PROVIDES_HEAD) != 0) {
                for (Class<?> t : types) {
                    reg.registerHeadProvider(provider, t);
                }
            }

            if ((flags & PROVIDES_BODY) != 0) {
                for (Class<?> t : types) {
                    reg.registerBodyProvider(provider, t);
                }
            }

            if ((flags & PROVIDES_TAIL) != 0) {
                for (Class<?> t : types) {
                    reg.registerTailProvider(provider, t);
                }
            }

            if ((flags & PROVIDES_STACK) != 0) {
                for (Class<?> t : types) {
                    reg.registerStackProvider(provider, t);
                }
            }

            if ((flags & PROVIDES_NBT) != 0) {
                for (Class<?> t : types) {
                    reg.registerNBTProvider(provider, t);
                }
            }
        }
    }

    public static void addProvider(WailaProvider provider, Class<?>... types) {
        providers.put(provider, types);
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
