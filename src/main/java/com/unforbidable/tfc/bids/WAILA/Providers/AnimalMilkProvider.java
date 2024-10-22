package com.unforbidable.tfc.bids.WAILA.Providers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.WAILA.WailaEntityProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class AnimalMilkProvider extends WailaEntityProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor iWailaEntityAccessor, IWailaConfigHandler iWailaConfigHandler) {
        if (entity instanceof IAnimal) {
            IAnimal animal = (IAnimal) entity;

            // Only undomesticated (ibex) as TFC does this for domesticated (goat) ones
            if (animal instanceof EntityGoat && animal.getGender() == IAnimal.GenderEnum.FEMALE && animal.isAdult() && !animal.isDomesticated()) {
                EntityGoat goat = (EntityGoat) entity;
                boolean canMilk = goat.getEntityData().getBoolean("canMilkClient");
                if (canMilk)
                    currenttip.add(TFC_Core.translate("fluid.milk") + EnumChatFormatting.GREEN + " \u2714");
                else
                    currenttip.add(TFC_Core.translate("fluid.milk") + EnumChatFormatting.RED + " \u2718");
            }
        }

        return currenttip;
    }

}
