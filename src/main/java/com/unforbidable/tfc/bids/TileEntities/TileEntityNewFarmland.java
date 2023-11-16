package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.CropIndex;
import com.dunk.tfc.Food.CropManager;
import com.dunk.tfc.TileEntities.TECrop;
import com.dunk.tfc.TileEntities.TEFarmland;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.tileentity.TileEntity;

public class TileEntityNewFarmland extends TEFarmland {

    @Override
    public void updateEntity() {
        if(!worldObj.isRemote) {
            if (nutrientTimer <= 0) {
                nutrientTimer = TFC_Time.getTotalHours();
            }

            if (!fallow && fallowTimer <= 0 && worldObj.getBlock(xCoord, yCoord + 1, zCoord) != BidsBlocks.newCrops) {
                //the number of days in a month * 6 = the number of hours. This is 1/4 of the month
                fallowTimer = TFC_Time.getTotalHours() + (6 * (TFC_Time.ticksInMonth / 24000));
            }

            if (fallowTimer >= 0 && fallowTimer <= TFC_Time.getTotalHours() && worldObj.getBlock(xCoord, yCoord + 1, zCoord) != BidsBlocks.newCrops) {
                fallowTimer = -1;
                fallow = true;
                this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                this.broadcastPacketInRange();
            } else if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) == BidsBlocks.newCrops && fallowTimer >= 0) {
                fallowTimer = -1;
                this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                this.broadcastPacketInRange();
            }

            if (nutrientTimer < TFC_Time.getTotalHours()) {
                CropIndex crop = null;
                int soilMax = getSoilMax();
                int restoreAmount = 139;

                if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) == BidsBlocks.newCrops) {
                    crop = CropManager.getInstance().getCropFromId(((TECrop) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).cropId);
                    fallowTimer = -1;
                    if (crop.cycleType != 0 && crop.nutrientExtraRestore[0] > 0) {
                        if (nutrients[0] < soilMax) {
                            nutrients[0] += restoreAmount + crop.nutrientExtraRestore[0];
                        }
                    }

                    if (crop.cycleType != 1 && crop.nutrientExtraRestore[1] > 0) {
                        if (nutrients[1] < soilMax) {
                            nutrients[1] += restoreAmount + crop.nutrientExtraRestore[1];
                        }
                    }

                    if (crop.cycleType != 2 && crop.nutrientExtraRestore[2] > 0) {
                        if (nutrients[2] < soilMax) {
                            nutrients[2] += restoreAmount + crop.nutrientExtraRestore[2];
                        }
                    }
                    nutrients[0] += 7;
                    nutrients[1] += 7;
                    nutrients[2] += 7;
                } else if (fallow) {
                    if (nutrients[0] < soilMax)
                        nutrients[0] += restoreAmount;
                    if (nutrients[1] < soilMax)
                        nutrients[1] += restoreAmount;
                    if (nutrients[2] < soilMax)
                        nutrients[2] += restoreAmount;
                }

                if (nutrients[0] > soilMax)
                    nutrients[0] = soilMax;
                if (nutrients[1] > soilMax)
                    nutrients[1] = soilMax;
                if (nutrients[2] > soilMax)
                    nutrients[2] = soilMax;

                nutrientTimer += 24;

                if (isInfested) {
                    float temp = TFC_Climate.getHeightAdjustedTempSpecificDay(worldObj, TFC_Time.getDayFromTotalHours(this.nutrientTimer), xCoord, yCoord, zCoord);
                    if (temp > 10 && worldObj.rand.nextInt(10) == 0) {
                        TileEntity te = worldObj.getTileEntity(xCoord, yCoord, zCoord);
                        if (te instanceof TEFarmland) {
                            ((TEFarmland) te).infest();
                        }
                    } else if (temp <= 10) {
                        if (worldObj.rand.nextInt(5) == 0)
                            uninfest();
                    }
                }

            }
        }
    }

}
