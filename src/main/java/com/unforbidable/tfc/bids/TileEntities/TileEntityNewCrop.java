package com.unforbidable.tfc.bids.TileEntities;

import com.dunk.tfc.Blocks.BlockFarmland;
import com.dunk.tfc.Core.TFC_Achievements;
import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.CropIndex;
import com.dunk.tfc.Food.CropManager;
import com.dunk.tfc.TileEntities.TECrop;
import com.dunk.tfc.TileEntities.TEFarmland;
import com.dunk.tfc.TileEntities.TEWorldItem;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFCOptions;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropIndex;
import com.unforbidable.tfc.bids.Core.Crops.CropAccess;
import com.unforbidable.tfc.bids.Core.Crops.CropHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class TileEntityNewCrop extends TECrop {

    // Cloned private TFC members
    protected long growthTimer = TFC_Time.getTotalTicks();
    protected byte sunLevel = 1;
    protected int killLevel = 0;

    // Additional crop info
    public float growthSpeedBonus = new Random().nextFloat();

    public static int getTotalDaysFromTick(long tick) {
        return (int) (tick / TFC_Time.DAY_LENGTH);
    }

    @Override
    public void updateEntity() {
        Random r = new Random();
        if (!worldObj.isRemote) {
            float timeMultiplier = 96f / TFC_Time.daysInYear;
            CropIndex crop = CropManager.getInstance().getCropFromId(cropId);
            long time = TFC_Time.getTotalTicks();
            if (crop != null && growthTimer < time && sunLevel > 0)
            {
                sunLevel--;
                if (crop.needsSunlight && hasSunlight(worldObj, xCoord, yCoord, zCoord))
                {
                    sunLevel++;
                    if (sunLevel > 30)
                        sunLevel = 30;
                }

                TEFarmland tef = null;
                TileEntity te = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
                if (te instanceof TEFarmland)
                    tef = (TEFarmland) te;

                // FIX: Get current year temp instead of first year temp
                float ambientTemp = TFC_Climate.getHeightAdjustedTempSpecificDay(worldObj,
                    getTotalDaysFromTick(growthTimer), xCoord, yCoord, zCoord);
                boolean isDormant = false;

                // Attempt to start an infestation
                /*
                 * if(cd != null) { //Works but removed until this feature can
                 * be more clearly defined if(worldObj.rand.nextInt(2000) == 0)
                 * { cd.infest(); } if(cd.cropInfestation > 0) {
                 * if(worldObj.rand.nextInt(40/cd.cropInfestation) == 0) {
                 * if(tef != null) tef.infest();
                 * worldObj.markBlockForUpdate(xCoord, yCoord-1, zCoord); } } }
                 */
                // End Infestation Code

                if (!crop.dormantInFrost && ambientTemp < crop.minAliveTemp)
                {
                    int baseKillChance = 6;
                    if (this.worldObj.rand.nextInt(baseKillChance - this.killLevel) == 0) {
                        killCrop(crop);
                    }
                    else
                    {
                        if (killLevel < baseKillChance - 1)
                            this.killLevel++;
                    }
                }
                else if (crop.dormantInFrost && ambientTemp < crop.minAliveTemp)
                {
                    // FIX: Determine young plant based to growth % rather than stage 0
                    // otherwise crop with more stages are at a disadvantage
                    if (growth / (crop.numGrowthStages + 1) > 0.20)
                    {
                        int baseKillChance = 6;
                        if (this.worldObj.rand.nextInt(baseKillChance - this.killLevel) == 0) {
                            killCrop(crop);
                        }
                        else
                        {
                            if (killLevel < baseKillChance - 1)
                                this.killLevel++;
                        }
                    } else {
                        isDormant = true;
                    }
                }
                else
                {
                    this.killLevel = 0;
                }

                int minWaterDistance = crop.minWaterDistance;

                float waterModifier = BlockFarmland.isFreshWaterNearby(worldObj, xCoord, yCoord - 1, zCoord) <= minWaterDistance ? 1f : 0;
                if(tef == null)
                {
                    waterModifier = 1f;
                }

                if(waterModifier == 0 || ambientTemp < crop.minGrowthTemp)
                {
                    growthTimer += (r.nextInt(2) + 23) * TFC_Time.HOUR_LENGTH;
                    return;
                }

                int nutriType = crop.cycleType;
                int nutri = tef != null ? tef.nutrients[nutriType] : (int) (TEFarmland.getSoilMax() * 0.5f);
                int fert = tef != null ? tef.nutrients[3] : 0;
                int soilMax = TEFarmland.getSoilMax();
                // waterBoost only helps if you are playing on a longer than
                // default year length.

                // Allow the fertilizer to make up for lost nutrients
                nutri = Math.min(nutri + fert, (int) (soilMax * 1.5f));

                int deltaNutri = growth >= crop.numGrowthStages? 0 : 5;

                if (tef != null && !isDormant)
                {
                    if (tef.nutrients[nutriType] > 0)
                    {
                        deltaNutri += tef.drainNutrients(nutriType, 1f/*crop.nutrientUsageMult*/)/3;
                        deltaNutri += tef.drainNutrients((nutriType+1)%3, /*crop.nutrientUsageMult **/ 0.9f)/3;
                        deltaNutri += tef.drainNutrients((nutriType+2)%3, /*crop.nutrientUsageMult **/ 0.9f)/3;
                    }
                    // Drain Fertilizer
                    if (tef.nutrients[3] > 0)
                    {
                        deltaNutri += 0.5f * tef.drainNutrients(3, 1f/* crop.nutrientUsageMult*/);
                    }
                }
                else if(tef == null)
                {
                    int drained = (int) Math.min((100 /* * crop.nutrientUsageMult*/) * timeMultiplier * (nutri/soilMax < 0.4f? Math.sqrt(nutri/soilMax):1f), nutri);
                    deltaNutri += drained;
                }

                boolean reqUpdate = false;
                nutrients +=  Math.max(growth >= crop.numGrowthStages? 0 : deltaNutri,0);
                reqUpdate |=  Math.max(growth >= crop.numGrowthStages? 0 : deltaNutri,0) != 0;
                //System.out.println(crop.cropName + " Nutrients: " + nutrients);




                float growthRate = Math.max(0.0f,
                    ((((crop.numGrowthStages / ((float)crop.growthTime)))) * timeMultiplier * waterModifier * TFCOptions.cropGrowthMultiplier));

                growthRate += (growthSpeedBonus - 0.5) * growthRate * (growth / crop.numGrowthStages) * 0.5;

                if (tef != null && tef.isInfested)
                    growthRate /= 2;
                int oldGrowth = (int) Math.floor(growth);

                if (!isDormant)
                {
                    growth += growthRate;
                    reqUpdate |=  growthRate != 0;
                }
                if (reqUpdate)
                {
                    // TerraFirmaCraft.log.info(xCoord+","+yCoord+","+zCoord);
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }

                // Wild crops will always die of old age, regardless of the
                // config setting
                // FIX: Only planted crops die of old age
                if ((TFCOptions.enableCropsDie && CropHelper.isFarmland(worldObj.getBlock(xCoord, yCoord - 1, zCoord)))
                    && (crop.maxLifespan == -1 && growth > crop.numGrowthStages + ((float) crop.numGrowthStages / 2)) || growth < 0)
                {
                    killCrop(crop);
                }


                growthTimer += (r.nextInt(2) + 23) * TFC_Time.HOUR_LENGTH;
            }
            // Not enough sunlight
            else if (crop != null && crop.needsSunlight && sunLevel <= 0)
            {
                killCrop(crop);
            }

            // Snowing
            if (TFC_Core.isExposedToRain(worldObj, xCoord, yCoord,
                zCoord) && TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord) < 0)
            {
                if (crop != null && !crop.dormantInFrost)
                {
                    killCrop(crop);
                }
            }
        }
    }

    @Override
    public void killCrop(CropIndex crop) {
        ItemStack is = crop.getSeed();
        is.stackSize = 1;
        if (CropHelper.isFarmland(worldObj.getBlock(xCoord, yCoord - 1, zCoord)) && TFCOptions.enableSeedDrops) {
            if(growth >= crop.numGrowthStages - 1) {
                float nutrientBonus = 1f;
                float standardNutri = crop.growthTime * 90;
                nutrientBonus = nutrients / standardNutri;
                Random r = new Random();
                is.stackSize = (int)Math.max(1,(nutrientBonus * CropIndex.getWeight(crop.output1Avg,r) / 5f));
            }

            if (worldObj.setBlock(xCoord, yCoord, zCoord, TFCBlocks.worldItem)) {
                TEWorldItem te = (TEWorldItem) worldObj.getTileEntity(xCoord, yCoord, zCoord);
                te.storage[0] = is;
                if (crop.requiresLadder) {
                    worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                        new ItemStack(Blocks.ladder)));
                } else if (crop instanceof BidsCropIndex && ((BidsCropIndex)crop).requiresPole) {
                    worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5,
                        new ItemStack(TFCItems.pole)));
                }

                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        } else {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void onHarvest(World world, EntityPlayer player, boolean isBreaking) {
        if (!world.isRemote) {
            CropAccess crop = new CropAccess(this);

            List<ItemStack> produce = crop.getIndex().getHarvestProduce(crop, player);
            if (produce != null && produce.size() > 0) {
                for (ItemStack is : produce) {
                    world.spawnEntityInWorld(new EntityItem(world, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, is));
                }

                // Only harvesting fully grown crops increases skill
                if (crop.getIndex().isFullyGrown(crop)) {
                    // For crops found in the wild, consider the chance if applicable
                    if (crop.hasFarmland() || crop.getIndex().giveSkillChanceWild == 100 || new Random().nextInt(100) < crop.getIndex().giveSkillChanceWild) {
                        TFC_Core.getSkillStats(player).increaseSkill(Global.SKILL_AGRICULTURE, 1);
                    }
                }

                if (!crop.hasFarmland()) {
                    player.addStat(TFC_Achievements.achWildVegetable, 1);
                }
            }

            if (isBreaking) {
                // Seeds drop when breaking the crop
                // For crops found in the wild, consider the chance if applicable
                if (crop.hasFarmland() || crop.getIndex().giveSeedChanceWild == 100 || new Random().nextInt(100) < crop.getIndex().giveSeedChanceWild) {
                    List<ItemStack> seeds = crop.getIndex().getHarvestSeeds(crop, player);
                    if (seeds != null && seeds.size() > 0) {
                        for (ItemStack seedStack : seeds) {
                            world.spawnEntityInWorld(
                                new EntityItem(world, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, seedStack));
                        }
                    }
                }

                // Also drop ladders and poles
                if (crop.hasFarmland()) {
                    if (crop.getIndex().requiresLadder) {
                        world.spawnEntityInWorld(
                            new EntityItem(world, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, new ItemStack(Blocks.ladder)));
                    } else if (crop.getIndex().requiresPole) {
                        world.spawnEntityInWorld(
                            new EntityItem(world, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, new ItemStack(TFCItems.pole)));
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        growthTimer = nbt.getLong("growthTimer");
        killLevel = nbt.getInteger("killLevel");
        sunLevel = nbt.getByte("sunLevel");

        growthSpeedBonus = nbt.getFloat("growthSpeedBonus");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setLong("growthTimer", growthTimer);
        nbt.setInteger("killLevel", killLevel);
        nbt.setByte("sunLevel", sunLevel);

        nbt.setFloat("growthSpeedBonus", growthSpeedBonus);
    }

}
