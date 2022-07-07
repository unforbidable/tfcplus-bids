package com.unforbidable.tfc.bids.Core.Crucible;

import java.util.Arrays;
import java.util.List;

import com.dunk.tfc.Core.TFC_Achievements;
import com.dunk.tfc.Core.Metal.Alloy;
import com.dunk.tfc.Core.Metal.AlloyManager;
import com.dunk.tfc.Core.Metal.AlloyMetal;
import com.dunk.tfc.Core.Metal.AlloyMetalCompare;
import com.dunk.tfc.Core.Metal.MetalRegistry;
import com.dunk.tfc.Items.Pottery.ItemPotteryMold;
import com.dunk.tfc.Items.Pottery.ItemPotteryMoldBase;
import com.dunk.tfc.Items.Pottery.ItemPotterySheetMold;
import com.dunk.tfc.TileEntities.TEForge;
import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Interfaces.ISmeltable;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCrucible;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.IExtraSmeltable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CrucibleHelper {

    static final int FORGE_FUEL_SLOT_FIRST = 5;
    static final int FORGE_FUEL_SLOT_LAST = 9;

    static final List<Item> oreItems = Arrays
            .asList(new Item[] { TFCItems.oreChunk, TFCItems.smallOreChunk, BidsItems.oreBit });

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

    public static Metal getMetalFromLiquid(List<CrucibleLiquidItem> liquid) {
        if (liquid.size() == 0) {
            return null;
        } else if (liquid.size() == 1) {
            return liquid.get(0).getMetal();
        } else {
            float totalVolume = 0;
            for (CrucibleLiquidItem it : liquid) {
                totalVolume += it.volume;
            }

            Metal m = Global.UNKNOWN;

            // Get list of alloys from AlloyManager
            for (Alloy alloy : AlloyManager.INSTANCE.alloys) {
                // Only interested in actual alloys
                if (alloy.alloyIngred.size() > 1) {
                    int matchCount = 0;
                    float existingAlloyVolume = 0;

                    // First look for the alloy already melted in the mixture
                    for (CrucibleLiquidItem item : liquid) {
                        if (item.metal == alloy.outputType) {
                            // Presence of the output alloy is tolerated, so we ignore it
                            // just account for it when calculating part percentages
                            existingAlloyVolume = item.volume;
                            Bids.LOG.debug("Existing alloy " + alloy.outputType + " will be ignored");
                            break;
                        }
                    }

                    // Match every alloy ingredient
                    // with every item in our liquid mixture
                    for (AlloyMetal am : alloy.alloyIngred) {
                        AlloyMetalCompare amc = (AlloyMetalCompare) am;
                        for (CrucibleLiquidItem item : liquid) {
                            if (item.getMetal() == amc.metalType) {
                                float part = item.getVolume() / (totalVolume - existingAlloyVolume) * 100;
                                if (amc.getMetalMax() >= part && amc.getMetalMin() <= part) {
                                    matchCount++;
                                    break;
                                }
                            }
                        }
                    }

                    // Make sure we matched all components, no less and no more
                    // but ignore existing alloy
                    if (matchCount == alloy.alloyIngred.size()
                            && (matchCount == liquid.size() && existingAlloyVolume == 0
                                    || matchCount + 1 == liquid.size() && existingAlloyVolume > 0)) {
                        m = alloy.outputType;
                        break;
                    }
                }
            }

            return m;
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
                && itemStack.getItemDamage() > 1 /* Ceramic */
                && itemStack.getItemDamage() <= 5 /* Full of Metal */;
    }

    public static ItemStack fillMold(ItemStack liquidOutputStack, Metal metal, int units) {
        ItemPotteryMoldBase prevMoldItem = ((ItemPotteryMoldBase) (liquidOutputStack.getItem()));
        int prev = prevMoldItem.getUnits(liquidOutputStack);
        ItemStack mold = new ItemStack(metal.getResultFromMold(liquidOutputStack.getItem()), 1, 0);
        mold.setItemDamage(metal.getBaseValueForResult(liquidOutputStack.getItem()));
        mold = ((ItemPotteryMoldBase) (mold.getItem())).setToMinimumUnits(mold);
        mold = ((ItemPotteryMoldBase) (mold.getItem())).addUnits(mold, units + prev);
        return mold;
    }

    public static int getMoldUnits(ItemStack mold) {
        return ((ItemPotteryMoldBase) mold.getItem()).getUnits(mold);
    }

    public static boolean isOreIron(ItemStack is) {
        return oreItems.contains(is.getItem()) && getMetalFromSmeltable(is) == Global.PIGIRON;
    }

    public static void triggerCopperAgeAchievement(EntityPlayer player) {
        player.triggerAchievement(TFC_Achievements.achCopperAge);
        Bids.LOG.debug("Copper Age achievement triggered");
    }

    public static void triggerCrucibleAchievement(EntityPlayer player) {
        player.triggerAchievement(TFC_Achievements.achCrucible);
        Bids.LOG.debug("Crucible achievement triggered");
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
        if (itemstack.getItem() instanceof IExtraSmeltable) {
            return ((IExtraSmeltable) itemstack.getItem()).getPurity(itemstack);
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
                return 0.25f;
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

    public static TileEntity findValidGlassmakingStructureChimney(TileEntityCrucible tileEntityCrucible) {
        World world = tileEntityCrucible.getWorldObj();
        int x = tileEntityCrucible.xCoord;
        int y = tileEntityCrucible.yCoord;
        int z = tileEntityCrucible.zCoord;

        int chimneyCount = 0;
        TileEntity validChimneyFound = null;

        // Forge bellow and non-flamable roof
        boolean valid = world.getTileEntity(x, y - 1, z) instanceof TEForge
                && !Blocks.fire.canCatchFire(world, x, y + 1, z, ForgeDirection.DOWN);

        // Surrounding blocks need to be solid and non flamable
        // or a chimney
        if (valid) {
            ForgeDirection checkList[] = { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH,
                    ForgeDirection.WEST };

            for (ForgeDirection dir : checkList) {
                TileEntity te = world.getTileEntity(x + dir.offsetX, y, z + dir.offsetZ);
                if (te != null && ChimneyHelper.isChimney(te)) {
                    if (!ChimneyHelper.canChimneySeeSky(te)) {
                        // Chimney cannot see the sky
                        valid = false;
                    }

                    validChimneyFound = te;
                    chimneyCount++;
                } else if (!world.isSideSolid(x + dir.offsetX, y, z + dir.offsetZ, dir)) {
                    // Non-solid side
                    valid = false;
                } else if (!Blocks.fire.canCatchFire(world, x + dir.offsetX, y, z + dir.offsetZ, dir)) {
                    // Solid and non-flamable side
                } else {
                    valid = false;
                }

                if (!valid || chimneyCount > 1)
                    break;
            }
        }

        // Exactly 1 chimney block is needed
        if (valid && chimneyCount == 1)
            return validChimneyFound;
        else
            return null;
    }

    public static int getForgeFuelCount(TileEntityCrucible crucible) {
        TileEntity te = crucible.getWorldObj().getTileEntity(crucible.xCoord, crucible.yCoord - 1, crucible.zCoord);
        if (te instanceof TEForge) {
            TEForge forge = (TEForge) te;
            int fuelCount = 0;
            for (int i = FORGE_FUEL_SLOT_FIRST; i <= FORGE_FUEL_SLOT_LAST; i++) {
                ItemStack fuel = forge.getStackInSlot(i);
                if (fuel != null)
                    fuelCount++;
            }

            return fuelCount;
        }

        return 0;
    }

    public static void clearForgeFuel(TileEntityCrucible crucible) {
        TileEntity te = crucible.getWorldObj().getTileEntity(crucible.xCoord, crucible.yCoord - 1, crucible.zCoord);
        if (te instanceof TEForge) {
            TEForge forge = (TEForge) te;
            for (int i = FORGE_FUEL_SLOT_FIRST; i <= FORGE_FUEL_SLOT_LAST; i++) {
                ItemStack fuel = forge.getStackInSlot(i);
                if (fuel != null) {
                    forge.setInventorySlotContents(i, null);
                    Bids.LOG.debug("Cleared forge fuel in slot: " + i);
                }
            }
        }
    }

    public static float getForgeFuelTimeLeft(TileEntityCrucible crucible) {
        TileEntity te = crucible.getWorldObj().getTileEntity(crucible.xCoord, crucible.yCoord - 1, crucible.zCoord);
        if (te instanceof TEForge) {
            TEForge forge = (TEForge) te;
            return forge.fuelTimeLeft;
        }

        return 0;
    }

    public static void setForgeFuelTimeLeft(TileEntityCrucible crucible, float timeLeft) {
        TileEntity te = crucible.getWorldObj().getTileEntity(crucible.xCoord, crucible.yCoord - 1, crucible.zCoord);
        if (te instanceof TEForge) {
            TEForge forge = (TEForge) te;
            forge.fuelTimeLeft = timeLeft;
            Bids.LOG.debug("Set forge fuel time left: " + timeLeft);
        }
    }

    public static void reduceForgeTemp(TileEntityCrucible crucible) {
        TileEntity te = crucible.getWorldObj().getTileEntity(crucible.xCoord, crucible.yCoord - 1, crucible.zCoord);
        if (te instanceof TEForge) {
            TEForge forge = (TEForge) te;
            // Rapidly reduce the temperature of the forge
            // Test higher temp than target temp
            // because the current implementation causes temp to forever approach
            // the target temp, never reaching it
            // Below 20 is low enough, it means that the forge no longer makes fire sound
            while (forge.getHeatSourceTemp() > 20) {
                forge.handleTempFlux(0);
            }
            Bids.LOG.debug("Forge temp rapidly reduced to: " + forge.getHeatSourceTemp());
        }
    }

}
