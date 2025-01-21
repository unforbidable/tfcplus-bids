package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Events.AnimalMilkEvent;
import com.unforbidable.tfc.bids.api.Events.FillContainerEvent;
import com.unforbidable.tfc.bids.api.Events.KilnEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

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

    public static Fluid onAnimalMilkCheck(EntityPlayer player, Entity entity) {
        AnimalMilkEvent.Check event = new AnimalMilkEvent.Check(player, entity);
        MinecraftForge.EVENT_BUS.post(event);
        return event.fluid;
    }

    public static boolean onAnimalMilking(EntityPlayer player, Entity entity, int amount) {
        AnimalMilkEvent.Milking event = new AnimalMilkEvent.Milking(player, entity, amount);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

    public static void onAnimalMilked(EntityPlayer player, Entity entity, FluidStack result) {
        AnimalMilkEvent.Milked event = new AnimalMilkEvent.Milked(player, entity, result);
        MinecraftForge.EVENT_BUS.post(event);
    }

}
