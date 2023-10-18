package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.MilkHelper;
import com.unforbidable.tfc.bids.api.BidsFluids;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

public class EntityFluidHelper {

    public static Fluid getFluidFromEntity(Entity entity, EntityPlayer player) {
        if (entity instanceof EntityCowTFC && MilkHelper.canEntityBeMilkedByPlayerSafe(entity, player)) {
            return TFCFluids.MILK;
        }

        if (entity instanceof EntityGoat && MilkHelper.canEntityBeMilkedByPlayerSafe(entity, player)) {
            return BidsFluids.GOATMILK;
        }

        return null;
    }

    public static boolean drainEntityFluid(Entity entity, EntityPlayer player, int amount) {
        if (entity instanceof EntityCowTFC || entity instanceof EntityGoat) {
            return MilkHelper.doMilkEntityByPlayerSafe(entity, player, amount);
        }

        return false;
    }

}
