package com.unforbidable.tfc.bids.compat.waila.providers;

import com.unforbidable.tfc.bids.compat.waila.registry.WailaRegistry;
import com.unforbidable.tfc.bids.compat.waila.registry.WailaRegistryEntry;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class WailaEntityProvider implements IWailaEntityProvider {

    protected static final int PROVIDES_BODY = 2;

    public static void setup(IWailaRegistrar reg) {
        for (WailaRegistryEntry<WailaEntityProvider> entry : WailaRegistry.entityProviders) {
            int flags = entry.provider.provides();

            if ((flags & PROVIDES_BODY) != 0) {
                for (Class<?> t : entry.types) {
                    reg.registerBodyProvider(entry.provider, t);
                }
            }
        }
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
