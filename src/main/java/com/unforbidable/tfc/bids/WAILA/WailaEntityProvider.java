package com.unforbidable.tfc.bids.WAILA;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WailaEntityProvider implements IWailaEntityProvider {

    protected static final int PROVIDES_BODY = 2;

    static final Map<WailaEntityProvider, Class<?>[]> providers = new HashMap<WailaEntityProvider, Class<?>[]>();

    public static void setup(IWailaRegistrar reg) {
        for (Entry<WailaEntityProvider, Class<?>[]> p : providers.entrySet()) {
            WailaEntityProvider provider = p.getKey();
            Class<?>[] types = p.getValue();

            int flags = provider.provides();

            if ((flags & PROVIDES_BODY) != 0) {
                for (Class<?> t : types) {
                    reg.registerBodyProvider(provider, t);
                }
            }
        }
    }

    public static void addProvider(WailaEntityProvider provider, Class<?>... types) {
        providers.put(provider, types);
    }

    public int provides() {
        return 0;
    }

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor iWailaEntityAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> list, IWailaEntityAccessor iWailaEntityAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> list, IWailaEntityAccessor iWailaEntityAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaTail(Entity entity, List<String> list, IWailaEntityAccessor iWailaEntityAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, Entity entity, NBTTagCompound nbtTagCompound, World world) {
        return null;
    }

}
