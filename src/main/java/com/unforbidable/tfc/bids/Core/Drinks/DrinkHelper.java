package com.unforbidable.tfc.bids.Core.Drinks;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemDrink;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Items.ItemGenericAlcohol;
import com.unforbidable.tfc.bids.Items.ItemGenericDrink;
import com.unforbidable.tfc.bids.api.DrinkRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.IDrinkable;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class DrinkHelper {

    public static void registerFluidContainers(Item containerItem, int volume, boolean canDrinkInParts, int... overlayParts) {
        registerFluidContainers(containerItem, false, volume, canDrinkInParts, overlayParts);
    }

    public static void registerPotteryFluidContainers(Item containerItem, int volume, boolean canDrinkInParts, int... overlayParts) {
        registerFluidContainers(containerItem, true, volume, canDrinkInParts, overlayParts);
    }

    private static void registerFluidContainers(Item containerItem, boolean isPottery, int volume,
            boolean canDrinkInParts, int... overlayParts) {
        for (IDrinkable drink : DrinkRegistry.getDrinks()) {
            ItemDrink item = drink.getAlcoholContent() > 0
                    ? new ItemGenericAlcohol(volume, isPottery).setAlcoholContent(drink.getAlcoholContent())
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
                int sip = 50;
                int count = volume / sip;

                FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.getFluid(), sip),
                        new ItemStack(item, 1, count - 1), new ItemStack(containerItem, 1, emptyDmg));

                for (int i = 0; i < count - 1; i++) {
                    FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.getFluid(), sip),
                            new ItemStack(item, 1, i), new ItemStack(item, 1, i + 1));
                }
            } else {
                FluidContainerRegistry.registerFluidContainer(new FluidStack(drink.getFluid(), volume),
                        new ItemStack(item), new ItemStack(containerItem, 1, emptyDmg));
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

    public static ItemStack getFreshWaterDrink(ItemStack is, World world, EntityPlayer entity,
            MovingObjectPosition mop) {
        if (mop != null) {
            if (mop.typeOfHit == MovingObjectType.BLOCK) {
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;

                // Handle flowing water
                int flowX = x;
                int flowY = y;
                int flowZ = z;
                switch (mop.sideHit) {
                    case 0:
                        flowY = y - 1;
                        break;
                    case 1:
                        flowY = y + 1;
                        break;
                    case 2:
                        flowZ = z - 1;
                        break;
                    case 3:
                        flowZ = z + 1;
                        break;
                    case 4:
                        flowX = x - 1;
                        break;
                    case 5:
                        flowX = x + 1;
                        break;
                }

                if (!entity.isSneaking() && !world.isRemote &&
                        TFC_Core.isFreshWater(world.getBlock(x, y, z))
                        || TFC_Core.isFreshWater(world.getBlock(flowX, flowY, flowZ))) {
                    Item item = DrinkHelper.findFluidContainerItem(is.getItem(),
                            TFCFluids.FRESHWATER);
                    if (item != null) {
                        ItemStack filledDrinkingGlass = new ItemStack(item);

                        --is.stackSize;
                        if (!entity.inventory.addItemStackToInventory(filledDrinkingGlass)) {
                            if (is.stackSize == 0) {
                                return filledDrinkingGlass;
                            } else {
                                entity.dropPlayerItemWithRandomChoice(filledDrinkingGlass, false);
                            }
                        }
                    } else {
                        Bids.LOG.warn("Drinking glass containing fresh water not found!");
                    }
                }

                if (!world.canMineBlock(entity, x, y, z)) {
                    return is;
                }

                if (!entity.canPlayerEdit(x, y, z, mop.sideHit, is)) {
                    return is;
                }

            }
        }
        return is;
    }
}
