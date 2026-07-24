package com.unforbidable.tfc.bids.util.metal;

import com.dunk.tfc.Core.Metal.MetalRegistry;
import com.dunk.tfc.Items.Pottery.ItemPotteryBlowpipe;
import com.dunk.tfc.Items.Pottery.ItemPotteryMold;
import com.dunk.tfc.Items.Pottery.ItemPotteryMoldBase;
import com.dunk.tfc.Items.Pottery.ItemPotterySheetMold;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.Interfaces.ISmeltable;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.util.metal.MoreSmeltable;
import com.unforbidable.tfc.bids.features.crafting.glassblowing.item.ItemMetalBlowpipe;
import java.util.Arrays;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MetalHelper {

    static final List<Item> oreItems = Arrays.asList(TFCItems.oreChunk, TFCItems.smallOreChunk, BidsItems.oreBit);

    public static int getMetalReturnAmount(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ISmeltable)
            return ((ISmeltable) itemstack.getItem()).getMetalReturnAmount(itemstack);
        else
            return isGlass(itemstack) ? getGlassReturnAmount(itemstack) : 0;
    }

    private static int getGlassReturnAmount(ItemStack itemstack) {
        if (itemstack.getItem() == Item.getItemFromBlock(Blocks.glass_pane))
            return 175;
        else if (itemstack.getItem() == Item.getItemFromBlock(Blocks.glass))
            return 850;

        return 0;
    }

    public static float getHeatCapacity(ItemStack itemstack) {
        return TFC_ItemHeat.getSpecificHeat(itemstack);
    }

    public static boolean isMeltedAtTemp(ItemStack itemstack, float temp) {
        Metal metal = getMetalFromSmeltable(itemstack);
        return isMeltedAtTemp(metal, temp);
    }

    public static boolean isMeltedAtTemp(Metal metal, float temp) {
        HeatRegistry manager = HeatRegistry.getInstance();
        if (manager != null) {
            if (metal == Global.GLASS) {
                // This is how molten glass is registered in the heat index
                HeatIndex hi = manager.findMatchingIndex(new ItemStack(TFCItems.clayMoldSheet, 1, 5));
                if (hi != null)
                    return hi.meltTemp <= temp;
            } else {
                Item mold = metal.getResultFromMold(TFCItems.ceramicMold);
                if (mold != null) {
                    HeatIndex hi = manager.findMatchingIndex(new ItemStack(mold));
                    if (hi != null)
                        return hi.meltTemp <= temp;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static Metal getMetalFromItem(Item item) {
        return MetalRegistry.instance.getMetalFromItem(item);
    }

    public static Metal getMetalFromSmeltable(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ISmeltable)
            return ((ISmeltable) itemstack.getItem()).getMetalType(itemstack);
        else
            return isGlass(itemstack) ? Global.GLASS : Global.GARBAGE;
    }

    public static boolean isValidMold(ItemStack liquidOutputStack, Metal metal) {
        return liquidOutputStack.getItem() instanceof ItemPotteryMoldBase
                && (metal == null || metal.isValidMold(liquidOutputStack))
                && ((ItemPotteryMoldBase) (liquidOutputStack.getItem())).isValidMold(liquidOutputStack);
    }

    public static boolean isFullToolMold(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemPotteryMold
                && !(itemStack.getItem() instanceof ItemPotterySheetMold)
                && !(itemStack.getItem() instanceof ItemPotteryBlowpipe)
                && !(itemStack.getItem() instanceof ItemMetalBlowpipe)
                && itemStack.getItemDamage() > 1 /* Ceramic */
                && itemStack.getItemDamage() <= 5 /* Full of Metal */;
    }

    public static ItemStack fillMold(ItemStack liquidOutputStack, Metal metal, int units) {
        int damage = metal.getBaseValueForResult(liquidOutputStack.getItem());
        ItemStack mold = new ItemStack(metal.getResultFromMold(liquidOutputStack.getItem()), 1, damage);
        mold = setMoldToMinimumUnits(mold);
        int prev = getMoldUnits(liquidOutputStack);
        mold = addMoldUnits(mold, prev + units);

        int newUnits = getMoldUnits(mold);
        if (newUnits == 0 && prev == 0) {
            // Filling empty mold this way can produce a filled mold with 0 units and that is bad
            // So we fill this new mold once again
            mold = addMoldUnits(mold, units);
        }

        return mold;
    }

    public static ItemStack drainMold(ItemStack liquidOutputStack, int units) {
        ItemStack mold = addMoldUnits(liquidOutputStack.copy(), -units);

        int newUnits = getMoldUnits(mold);
        if (newUnits > 0) {
            return mold;
        } else {
            // Filled mold becomes a filled mold containing 0 units
            // So we replace it with the actual empty mold item
            return new ItemStack(TFCItems.ceramicMold, 1, 1);
        }
    }

    public static ItemStack setMoldToMinimumUnits(ItemStack mold) {
        return ((ItemPotteryMoldBase) (mold.getItem())).setToMinimumUnits(mold);
    }

    public static ItemStack addMoldUnits(ItemStack mold, int units) {
        return ((ItemPotteryMoldBase) (mold.getItem())).addUnits(mold, units);
    }

    public static int getMoldUnits(ItemStack mold) {
        return ((ItemPotteryMoldBase) mold.getItem()).getUnits(mold);
    }

    public static boolean isOreIron(ItemStack is) {
        return oreItems.contains(is.getItem()) && getMetalFromSmeltable(is) == Global.PIGIRON;
    }

    public static boolean isNativeOre(ItemStack itemstack) {
        if (itemstack.getItem() == TFCItems.oreChunk
                || itemstack.getItem() == TFCItems.smallOreChunk) {
            switch (itemstack.getItemDamage() % Global.oreGrade1Offset) {
                case 0:
                case 1:
                case 2:
                case 4:
                    return true;

                default:
                    return false;
            }
        }

        return false;
    }

    public static float getPurity(ItemStack itemstack) {
        if (itemstack.getItem() instanceof MoreSmeltable) {
            return ((MoreSmeltable) itemstack.getItem()).getPurity(itemstack);
        } else {
            if (itemstack.getItem() == TFCItems.smallOreChunk) {
                return isNativeOre(itemstack) ? 0.9f : 0.5f;
            } else if (itemstack.getItem() == TFCItems.oreChunk) {
                if (itemstack.getItemDamage() < Global.oreGrade1Offset) {
                    return isNativeOre(itemstack) ? 0.9f : 0.5f; // Normal
                } else if (itemstack.getItemDamage() < Global.oreGrade2Offset) {
                    return isNativeOre(itemstack) ? 0.95f : 0.65f; // Rich
                } else {
                    return isNativeOre(itemstack) ? 0.85f : 0.35f; // Poor
                }
            } else if (isGlass(itemstack)) {
                // Melting glass is hard
                return 0.50f;
            } else if (isGlassIngredient(itemstack)) {
                // Making glass is harder
                return getGlassIngredientPurity(itemstack);
            } else {
                // Not a nugget or ore, or glass stuff,
                // then this is a ready metal ingot
                // (or sheet, anvil, etc)
                // which is 100% pure
                return 1f;
            }
        }
    }

    public static boolean isGlass(ItemStack itemstack) {
        return itemstack.getItem() == Item.getItemFromBlock(Blocks.glass_pane)
                || itemstack.getItem() == Item.getItemFromBlock(Blocks.glass);
    }

    public static boolean isGlassIngredient(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ISmeltable) {
            Metal metal = getMetalFromSmeltable(itemstack);
            return metal == Global.SILICA || metal == Global.SODA || metal == Global.LIME;
        } else {
            return false;
        }
    }

    private static float getGlassIngredientPurity(ItemStack itemstack) {
        if (itemstack.getItem() == TFCItems.soda) {
            // Soda ash effectively lowers the difficulty of making glass
            return 3f;
        } else {
            Metal metal = getMetalFromSmeltable(itemstack);
            if (metal == Global.SILICA) {
                // Only the amount of sand rises the difficulty of making glass
                return 0.25f;
            } else {
                return 1f;
            }
        }
    }

}
