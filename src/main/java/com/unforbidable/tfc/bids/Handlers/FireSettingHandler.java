package com.unforbidable.tfc.bids.Handlers;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.WoodPile.FireSetting.StoneCracker;
import com.unforbidable.tfc.bids.api.Events.FireSettingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Random;

public class FireSettingHandler {

    @SubscribeEvent
    public void onBurning(FireSettingEvent.BurningEvent event) {
        float avgTemp = event.items > 0 ? event.temp / (float)event.items : 0;
        int heat = (int) (event.ticks * avgTemp / 1000);
        Bids.LOG.debug("Accumulated fire-setting heat: " + heat);

        List<BlockCoord> stonesToCrack = StoneCracker.findNearbyStoneToCrack(event.world, event.sourceX, event.sourceY, event.sourceZ, heat);
        if (stonesToCrack.size() > 0) {
            BlockCoord lastCracked = null;
            int actuallyCrackedStones = 0;

            for (BlockCoord bc : stonesToCrack) {
                FireSettingEvent.BlockCrackingEvent crackingEvent = new FireSettingEvent.BlockCrackingEvent(event.world, event.sourceX, event.sourceY, event.sourceZ, bc.x, bc.y, bc.z);
                MinecraftForge.EVENT_BUS.post(crackingEvent);

                if (!crackingEvent.isCanceled()) {
                    FireSettingEvent.BlockCrackedEvent crackedEvent = new FireSettingEvent.BlockCrackedEvent(event.world, event.sourceX, event.sourceY, event.sourceZ, bc.x, bc.y, bc.z);
                    MinecraftForge.EVENT_BUS.post(crackedEvent);

                    lastCracked = bc;
                    actuallyCrackedStones++;
                }
            }

            Bids.LOG.debug("Cracked {} out of {} blocks", actuallyCrackedStones, stonesToCrack.size());

            Random rand = new Random();

            if (lastCracked != null) {
                event.world.playSoundEffect(lastCracked.x, lastCracked.y, lastCracked.z, "dig.stone",
                    0.4F + (rand.nextFloat() / 2), 0.7F + rand.nextFloat());
            }
        }
    }

    @SubscribeEvent
    public void onBlockCracking(FireSettingEvent.BlockCrackingEvent event) {
        if (!event.isCanceled()) {
            Random rand = new Random();
            if (rand.nextFloat() < 0.4f) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onBlockCracked(FireSettingEvent.BlockCrackingEvent event) {
        StoneCracker.replaceStoneWithCracked(event.world, event.targetX, event.targetY, event.targetZ);
    }

}
