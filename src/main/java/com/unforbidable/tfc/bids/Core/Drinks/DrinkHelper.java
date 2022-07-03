package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Items.ItemDrink;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Items.ItemGenericAlcohol;
import com.unforbidable.tfc.bids.Items.ItemGenericDrink;
import com.unforbidable.tfc.bids.api.DrinkRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IDrinkable;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DrinkHelper {

    public static void registerFluidContainers(Item containerItem, int volume, boolean canDrinkInParts) {
        for (IDrinkable drink : DrinkRegistry.getDrinks()) {
            ItemDrink item = drink.getAlcoholContent() > 0
                    ? new ItemGenericAlcohol(volume).setAlcoholContent(drink.getAlcoholContent())
                    : new ItemGenericDrink(volume);
            item.setContainerItem(containerItem);
            item.setCanDrinkInParts(canDrinkInParts);
            item.setUnlocalizedName(getDrinkItemName(containerItem, drink));
            item.setCalories(drink.getCalories());
            item.setFoodGroup(drink.getFoodGroup());
            item.setWaterRestoreRatio(drink.getWaterRestoreRatio());
            item.setTier(drink.getAlcoholTier());

            GameRegistry.registerItem(item, item.getUnlocalizedName());

            if (canDrinkInParts) {
                int sip = 50;
                int count = volume / sip;

                FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.getFluid(), sip),
                        new ItemStack(item, 1, count - 1), new ItemStack(containerItem));

                for (int i = 0; i < count - 1; i++) {
                    FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.getFluid(), sip),
                            new ItemStack(item, 1, i), new ItemStack(item, 1, i + 1));
                }
            } else {
                FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.getFluid(), volume),
                        new ItemStack(item), new ItemStack(containerItem));
            }

            Bids.LOG.info("Registered drink item: " + item.getUnlocalizedName());
        }
    }

    private static String getDrinkItemName(Item containerItem, IDrinkable drink) {
        return containerItem.getUnlocalizedName().replace("item.", "") + "." + drink.getName();
    }

    public static Item findFluidContainerItem(Item containerItem, Fluid fluid) {
        // Find the drink corresponding to the fluid
        for (IDrinkable drink : DrinkRegistry.getDrinks()) {
            if (drink.getFluid() == fluid) {
                // Find drink item
                String drinkItemName = getDrinkItemName(containerItem, drink);
                return GameRegistry.findItem(Tags.MOD_ID, "item." + drinkItemName);
            }
        }

        return null;
    }
}
