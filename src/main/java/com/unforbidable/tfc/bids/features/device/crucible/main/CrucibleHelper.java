package com.unforbidable.tfc.bids.features.device.crucible.main;

import com.dunk.tfc.Core.Metal.Alloy;
import com.dunk.tfc.Core.Metal.AlloyManager;
import com.dunk.tfc.Core.Metal.AlloyMetal;
import com.dunk.tfc.Core.Metal.AlloyMetalCompare;
import com.dunk.tfc.TileEntities.TEForge;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Metal;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.features.device.crucible.tileentity.TileEntityCrucible;
import com.unforbidable.tfc.bids.util.chimney.ChimneyHelper;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CrucibleHelper {

    static final int FORGE_FUEL_SLOT_FIRST = 5;
    static final int FORGE_FUEL_SLOT_LAST = 9;

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


    public static TileEntity findValidGlassmakingStructureChimney(TileEntityCrucible tileEntityCrucible) {
        World world = tileEntityCrucible.getWorldObj();
        int x = tileEntityCrucible.xCoord;
        int y = tileEntityCrucible.yCoord;
        int z = tileEntityCrucible.zCoord;

        int chimneyCount = 0;
        TileEntity validChimneyFound = null;

        // Forge bellow and solid, non-flamable above
        boolean valid = world.getTileEntity(x, y - 1, z) instanceof TEForge
            && world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN)
            && !Blocks.fire.canCatchFire(world, x, y + 1, z, ForgeDirection.DOWN);

        // Surrounding blocks need to be solid and non flamable
        // or a chimney
        if (valid) {
            ForgeDirection[] checkList = { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH,
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
                } else if (Blocks.fire.canCatchFire(world, x + dir.offsetX, y, z + dir.offsetZ, dir)) {
                    // Flamable side
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
