package com.unforbidable.tfc.bids.core.drink;

import com.dunk.tfc.Items.ItemDrink;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.common.item.ItemCommonAlcohol;
import com.unforbidable.tfc.bids.common.item.ItemCommonDrink;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkFluid;
import com.unforbidable.tfc.bids.core.drink.registry.DrinkVessel;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DrinkUtil {

    // Sip is 50 mB by convention
    public static final int SIP = 50;

    public static void registerVessel(DrinkVessel vessel) {
        Bids.LOG.info("Register drinks for container item {}", vessel.containerItem.getUnlocalizedName());

        DrinkRegistry.drinks.stream()
            .forEach(drink -> registerDrinkForVessel(drink, vessel));
    }

    private static void registerDrinkForVessel(DrinkFluid drink, DrinkVessel vessel) {
        Bids.LOG.info("Register drink '{}' for container item {}", drink.fluid.getName(), vessel.containerItem.getUnlocalizedName());

        boolean canDrinkInParts = vessel.volume / SIP > 1;

        ItemDrink item = drink.alcoholContent > 0
            ? new ItemCommonAlcohol(vessel.volume, vessel.pottery, vessel.overlays)
            .setAlcoholContent(drink.alcoholContent)
            : new ItemCommonDrink(vessel.volume, vessel.pottery, vessel.overlays);

        item.setContainerItem(vessel.containerItem);
        item.setCanDrinkInParts(canDrinkInParts);
        item.setUnlocalizedName(getDrinkItemName(vessel.containerItem, drink));
        item.setCalories(drink.calories);
        item.setFoodGroup(drink.foodGroup);
        item.setWaterRestoreRatio(drink.waterRestoreRatio);
        item.setTier(drink.alcoholTier);

        GameRegistry.registerItem(item, item.getUnlocalizedName());

        int emptyDmg = vessel.pottery ? 1 : 0;
        if (canDrinkInParts) {
            FluidHelper.registerPartialFluidContainer(drink.fluid, vessel.containerItem, emptyDmg, item, 50, vessel.volume);
        } else {
            FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.fluid, vessel.volume),
                new ItemStack(item), new ItemStack(vessel.containerItem, 1, emptyDmg));
        }
    }

    private static String getDrinkItemName(Item containerItem, DrinkFluid drink) {
        return containerItem.getUnlocalizedName().replace("item.", "") + "." + drink.name;
    }

}
