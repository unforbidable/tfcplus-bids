package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.Core.Drying.DryingItem;
import com.unforbidable.tfc.bids.TileEntities.TileEntityProcessingSurface;
import com.unforbidable.tfc.bids.api.Crafting.DryingRecipe;
import com.unforbidable.tfc.bids.api.Events.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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

    public static void onWaterskinChurnDone(EntityPlayer player, ItemStack waterskin, ItemStack result) {
        WaterskinChurnEvent event = new WaterskinChurnEvent(player, WaterskinChurnEvent.Action.DONE, waterskin, result);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static float onProcessingSurfaceToolEfficiencyCheck(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float effort, float originalEfficiency) {
        ProcessingSurfaceEvent.ToolEfficiencyCheck event = new ProcessingSurfaceEvent.ToolEfficiencyCheck(tileEntity, input, result, tool, player, effort, originalEfficiency);
        MinecraftForge.EVENT_BUS.post(event);
        return event.newEfficiency;
    }

    public static void onProcessingSurfaceProgress(TileEntityProcessingSurface tileEntity, ItemStack input, ItemStack result, ItemStack tool, EntityPlayer player, float effort, float progress) {
        ProcessingSurfaceEvent.Progress event = new ProcessingSurfaceEvent.Progress(tileEntity, input, result, tool, player, effort, progress);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static String onSurfaceItemIcon(ItemStack itemStack, String iconName) {
        SurfaceItemEvent.Icon event = new SurfaceItemEvent.Icon(itemStack, iconName);
        MinecraftForge.EVENT_BUS.post(event);
        return event.iconName;
    }

    public static void onHandworkItemCrafted(EntityPlayer player, ItemStack input, ItemStack result, ItemStack tool) {
        HandworkPlayerEvent event = new HandworkPlayerEvent(player, HandworkPlayerEvent.Action.ITEM_CRAFTED, input, result, tool);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void onWoodworkingItemCrafted(EntityPlayer player, ItemStack input, ItemStack result) {
        WoodworkingPlayerEvent event = new WoodworkingPlayerEvent(player, WoodworkingPlayerEvent.Action.ITEM_CRAFTED, input, result);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void onWoodworkingItemPickedUp(EntityPlayer player, ItemStack input, ItemStack result) {
        WoodworkingPlayerEvent event = new WoodworkingPlayerEvent(player, WoodworkingPlayerEvent.Action.ITEM_PICKED_UP, input, result);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static boolean onDryingItemActivated(TileEntity dryingTileEntity, DryingItem dryingItem, DryingRecipe dryingRecipe, EntityPlayer player, int slot) {
        DryingItemEvent.Activate event = new DryingItemEvent.Activate(dryingTileEntity, dryingItem, dryingRecipe, player, slot);
        MinecraftForge.EVENT_BUS.post(event);
        return event.handled;
    }

    public static void onDryingItemRetrieved(TileEntity dryingTileEntity, DryingItem dryingItem, DryingRecipe dryingRecipe, EntityPlayer player, int slot) {
        DryingItemEvent.Retrieve event = new DryingItemEvent.Retrieve(dryingTileEntity, dryingItem, dryingRecipe, player, slot);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static boolean onDryingItemNextRecipeSelected(TileEntity dryingTileEntity, DryingItem dryingItem, DryingRecipe dryingRecipe, DryingRecipe nextDryingRecipe) {
        DryingItemEvent.SelectNextRecipe event = new DryingItemEvent.SelectNextRecipe(dryingTileEntity, dryingItem, dryingRecipe, nextDryingRecipe);
        MinecraftForge.EVENT_BUS.post(event);
        return !event.isCanceled();
    }

}
