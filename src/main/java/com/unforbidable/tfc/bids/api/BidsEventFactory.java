package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Events.FillContainerEvent;
import com.unforbidable.tfc.bids.api.Events.KilnEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;

public class BidsEventFactory {

    public static boolean onKilnFireBlockCheck(World world, int x, int y, int z) {
        KilnEvent.FireBlockCheck event = new KilnEvent.FireBlockCheck(world, x, y, z, false);
        MinecraftForge.EVENT_BUS.post(event);
        return event.success;
    }

    public static void onKilnFireBlock(World world, int x, int y, int z, double currentKilnProgress) {
        KilnEvent.FireBlock event = new KilnEvent.FireBlock(world, x, y, z, currentKilnProgress);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void onFillContainer(EntityPlayer player, ItemStack input, ItemStack output) {
        FillContainerEvent event = new FillContainerEvent(player, input, output);
        MinecraftForge.EVENT_BUS.post(event);
    }

}
