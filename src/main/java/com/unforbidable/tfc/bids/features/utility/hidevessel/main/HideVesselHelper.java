package com.unforbidable.tfc.bids.features.utility.hidevessel.main;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.nbt.HideVesselTag;
import net.minecraft.item.ItemStack;

public class HideVesselHelper {

    public static final int HIDE_VESSEL_CAPACITY = 1000;
    public static final long HIDE_VESSEL_EMPTY_DAMAGE_INTERVAL = TFC_Time.HOUR_LENGTH * 16;
    public static final long HIDE_VESSEL_FILLED_DAMAGE_INTERVAL = TFC_Time.HOUR_LENGTH * 8;
    public static final long HIDE_VESSEL_GROUND_DAMAGE_INTERVAL = TFC_Time.HOUR_LENGTH * 8;
    public static final int HIDE_VESSEL_MAX_DAMAGE = 20;
    public static final float HIDE_VESSEL_DAMAGE_PER_STONE = 0.2f;

    public static void damageHideVessel(ItemStack is, long damageInterval) {
        HideVesselTag tag = HideVesselTag.of(is);
        if (is.getItemDamage() < HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE) {
            if (tag.getLastDamageTicks() == 0) {
                tag.setLastDamageTicks(TFC_Time.getTotalTicks());
            }

            long lastDamageTicks = tag.getLastDamageTicks();
            if (lastDamageTicks + damageInterval < TFC_Time.getTotalTicks()) {
                int newDamage = is.getItemDamage() + 1;
                is.setItemDamage(newDamage);

                if (newDamage == HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE) {
                    tag.clearLastDamageTicks();

                    Bids.LOG.info("Hide vessel destroyed");
                } else {
                    tag.setLastDamageTicks(TFC_Time.getTotalTicks());

                    Bids.LOG.info("Hide vessel damaged {}/{}", newDamage, HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE);
                }
            }
        }
    }

}
