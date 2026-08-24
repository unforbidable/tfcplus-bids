package com.unforbidable.tfc.bids.features.resource.butchery.waila;

import com.dunk.tfc.Core.Player.SkillStats;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Entities.IAnimal;
import com.dunk.tfc.api.TFCOptions;
import com.unforbidable.tfc.bids.compat.waila.providers.WailaEntityProvider;
import com.unforbidable.tfc.bids.features.resource.butchery.main.AverageAnimalProvider;
import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class AnimalButcheryWailaProvider extends WailaEntityProvider {

    @Override
    public int provides() {
        return PROVIDES_BODY;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor iWailaEntityAccessor, IWailaConfigHandler iWailaConfigHandler) {
        if (entity instanceof IAnimal) {
            IAnimal animal = (IAnimal) entity;

            EntityPlayer player = iWailaEntityAccessor.getPlayer();
            if (player.isSneaking()) {
                int rank = TFCOptions.enableDebugMode ? SkillStats.SkillRank.Master.ordinal() :
                    TFC_Core.getSkillStats(player).getSkillRank(Global.SKILL_BUTCHERING).ordinal();

                if (animal.isAdult()) {
                    float avg = AverageAnimalProvider.getAverageAnimalSizeAndStrength(animal);

                    // Adept/Expert
                    if (rank >= 1) {
                        float size = animal.getSizeMod();
                        currenttip.add(StatCollector.translateToLocal("gui.animal.size") + ": " +
                            getAverageValueString(avg, size, rank >= 2));
                    }

                    // Expert/Master
                    if (rank >= 2) {
                        float strength = animal.getStrengthMod();
                        currenttip.add(StatCollector.translateToLocal("gui.animal.strength") + ": " +
                            getAverageValueString(avg, strength, rank >= 3));
                    }
                } else {
                    // Adept/Master
                    if (rank >= 1) {
                        float age = TFC_Core.getPercentGrown(animal);
                        currenttip.add(StatCollector.translateToLocal("gui.animal.growth") + ": " +
                            getAgeValueString(age, rank >= 3));
                    }
                }
            }
        }

        return currenttip;
    }

    private String getAgeValueString(float value, boolean detailed) {
        StringBuilder text = new StringBuilder();

        if (value < 0.4) {
            text.append(StatCollector.translateToLocal("gui.animal.growthVeryYoung"));
        } else if (value < 0.8) {
            text.append(StatCollector.translateToLocal("gui.animal.growthYoung"));
        } else {
            text.append(StatCollector.translateToLocal("gui.animal.growthAlmostAdult"));
        }

        if (detailed) {
            text.append(" (")
                .append(String.format("%.1f", Math.round(value * 1000) / 10f))
                .append("%)");
        }

        return text.toString();
    }

    private String getAverageValueString(float avg, float value, boolean detailed) {
        StringBuilder text = new StringBuilder();

        float diversion = value - avg;
        if (diversion >= 0.02f) {
            text.append(StatCollector.translateToLocal("gui.animal.aboveAverage"));
        } else if (diversion <= -0.02f) {
            text.append(StatCollector.translateToLocal("gui.animal.belowAverage"));
        } else {
            text.append(StatCollector.translateToLocal("gui.animal.average"));
        }

        if (detailed) {
            text.append(" (")
                .append(diversion >= 0 ? '+' : '-')
                .append(String.format("%.1f", Math.round(Math.abs(diversion) * 1000) / 10f))
                .append("%)");
        }

        return text.toString();
    }



}
