package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Items.ItemDrink;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Items.ItemGenericAlcohol;
import com.unforbidable.tfc.bids.Items.ItemGenericDrink;
import com.unforbidable.tfc.bids.api.DrinkRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IDrinkable;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DrinkHelper {

    public static void registerFluidContainers(Item containerItem, int volume, boolean canDrinkInParts,
            int... overlayParts) {
        registerFluidContainers(containerItem, false, volume, canDrinkInParts, overlayParts);
    }

    public static void registerPotteryFluidContainers(Item containerItem, int volume, boolean canDrinkInParts,
            int... overlayParts) {
        registerFluidContainers(containerItem, true, volume, canDrinkInParts, overlayParts);
    }

    private static void registerFluidContainers(Item containerItem, boolean isPottery, int volume,
            boolean canDrinkInParts, int... overlayParts) {
        for (IDrinkable drink : DrinkRegistry.getDrinks()) {
            ItemDrink item = drink.getAlcoholContent() > 0
                    ? new ItemGenericAlcohol(volume, isPottery, overlayParts)
                            .setAlcoholContent(drink.getAlcoholContent())
                    : new ItemGenericDrink(volume, isPottery, overlayParts);
            item.setContainerItem(containerItem);
            item.setCanDrinkInParts(canDrinkInParts);
            item.setUnlocalizedName(getDrinkItemName(containerItem, drink));
            item.setCalories(drink.getCalories());
            item.setFoodGroup(drink.getFoodGroup());
            item.setWaterRestoreRatio(drink.getWaterRestoreRatio());
            item.setTier(drink.getAlcoholTier());

            GameRegistry.registerItem(item, item.getUnlocalizedName());

            int emptyDmg = isPottery ? 1 : 0;
            if (canDrinkInParts) {
                FluidHelper.registerPartialFluidContainer(drink.getFluid(), containerItem, emptyDmg, item, 50, volume);
            } else {
                FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.getFluid(), volume),
                        new ItemStack(item), new ItemStack(containerItem, 1, emptyDmg));
            }

            Bids.LOG.debug("Registered drink item: " + item.getUnlocalizedName());
        }
    }

    private static String getDrinkItemName(Item containerItem, IDrinkable drink) {
        return containerItem.getUnlocalizedName().replace("item.", "") + "." + drink.getName();
    }

}
