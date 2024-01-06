package com.unforbidable.tfc.bids.Core.Crops;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Food.CropIndex;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.TileEntities.TECrop;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BidsCropIndex extends CropIndex {

    public EnumCropCoastal coastal = EnumCropCoastal.EITHER;
    public int commonness = 1;
    public boolean requiresPole = false;
    public int giveSkillChanceWild = 100;
    public int giveSeedChanceWild = 100;

    public CropRenderer cropRenderer;
    public List<CropCultivation> cropCultivation;

    public BidsCropIndex(int id, String name) {
        super(id, name,0, 0, 0, 0, 0, 0, null, null, 0, 0, 0, 0);
    }

    public static CropBuilder builder(int cropId, String cropName) {
        return new CropBuilder(new BidsCropIndex(cropId, cropName));
    }

    public List<ItemStack> getHarvestProduce(CropAccess crop, EntityPlayer player, Random random) {
        List<ItemStack> produce = new ArrayList<ItemStack>(getOutput(crop, player, random));

        Bids.LOG.info("growth: " + crop.getGrowth());
        Bids.LOG.info("numGrowthStages: " + crop.getIndex().numGrowthStages);
        Bids.LOG.info("nutrients: " + crop.getCropTileEntity().nutrients);
        Bids.LOG.info("nutrients expected: " + crop.getIndex().growthTime * 92.4f);
        Bids.LOG.info("growthMultiplier: " + crop.getLastStageGrowthMultiplier());
        Bids.LOG.info("nutrientMultiplier: " + crop.getNutrientMultiplier());

        if (isAtLeastPartiallyGrown(crop)) {
            float growthMultiplier = crop.getLastStageGrowthMultiplier();
            float nutrientMultiplier = crop.getNutrientMultiplier();

            for (ItemStack is : produce) {
                float weight = Food.getWeight(is);
                Food.setWeight(is, weight * growthMultiplier * nutrientMultiplier);
            }
        }

        return produce;
    }

    protected List<ItemStack> getOutput(CropAccess crop, EntityPlayer player, Random random) {
        List<ItemStack> output = new ArrayList<ItemStack>();

        if (crop.getIndex().isAtLeastPartiallyGrown(crop)) {
            if (output1 != null && random.nextInt(100) < chanceForOutput1) {
                ItemStack is = new ItemStack(output1);
                ItemFoodTFC.createTag(is, getWeight(output1Avg, random));
                addFlavorProfile(crop.getCropTileEntity(), is);
                output.add(is);
            }

            if (output2 != null && random.nextInt(100) < chanceForOutput2) {
                ItemStack is = new ItemStack(output2);
                ItemFoodTFC.createTag(is, getWeight(output2Avg, random));
                addFlavorProfile(crop.getCropTileEntity(), is);
                output.add(is);
            }
        }

        return output;
    }

    public List<ItemStack> getHarvestSeeds(CropAccess crop, EntityPlayer player, Random random) {
        List<ItemStack> seeds = new ArrayList<ItemStack>();

        if (crop.getIndex().seedItem != null) {
            ItemStack seedStack = new ItemStack(crop.getIndex().seedItem);

            if (isFullyGrown(crop)) {
                int skill = 20 - (int) (20 * TFC_Core.getSkillStats(player).getSkillMultiplier(Global.SKILL_AGRICULTURE));
                seedStack.stackSize = 1 + (crop.getCropTileEntity().getWorldObj().rand.nextInt(1 + skill) == 0 ? 1 : 0);
            }

            Bids.LOG.info("Seed drop: " + seedStack.getUnlocalizedName() + "[" + seedStack.stackSize + "]");

            seeds.add(seedStack);
        }

        if (crop.hasFarmland() && isAtLeastPartiallyGrown(crop) && crop.getIndex().cropCultivation != null) {
            for (CropCultivation cultivation : crop.getIndex().cropCultivation) {
                float chance = cultivation.getPlayerCultivationChance(crop, player);

                chance *= crop.getNutrientMultiplier() * crop.getLastStageGrowthMultiplier();
                Bids.LOG.info("Cultivation chance is " + chance + " for " + cultivation.getCultivatedSeedItem().getUnlocalizedName());

                if (random.nextFloat() < chance) {
                    Item cultivatedSeed = cultivation.getCultivatedSeedItem();
                    seeds.add(new ItemStack(cultivatedSeed));
                }
            }
        }

        return seeds;
    }

    // Clone of private TFC method
    protected void addFlavorProfile(TECrop te, ItemStack outFood)
    {
        Random r = getGrowthRand(te);
        if(r != null) {
            Food.adjustFlavor(outFood, r);
        }
    }

    // Clone of private TFC method
    protected Random getGrowthRand(TECrop te)
    {
        Block farmBlock = te.getWorldObj().getBlock(te.xCoord, te.yCoord-1, te.zCoord);
        //Block underFarmBlock = te.getWorldObj().getBlock(te.xCoord, te.yCoord-2, te.zCoord);
        if(!TFC_Core.isSoil(farmBlock))
        {
            int soilType1 = (farmBlock == BidsBlocks.newTilledSoil ? te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord-1, te.zCoord) :
                te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord-1, te.zCoord)+16);
            int soilType2 = (farmBlock == TFCBlocks.dirt ? te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord-2, te.zCoord)*2 :
                farmBlock == TFCBlocks.dirt2 ? (te.getWorldObj().getBlockMetadata(te.xCoord, te.yCoord-2, te.zCoord)+16)*2 : 0);

            int ph = TFC_Climate.getCacheManager(te.getWorldObj()).getPHLayerAt(te.xCoord, te.zCoord).data1*100;
            int drainage = 0;

            for(int y = 2; y < 8; y++)
            {
                if(TFC_Core.isGravel(te.getWorldObj().getBlock(te.xCoord, te.yCoord-y, te.zCoord)))
                {
                    drainage++;
                }
            }
            drainage *= 1000;

            return new Random(soilType1+soilType2+ph+drainage);
        }

        return null;
    }

    public boolean isFullyGrown(CropAccess crop) {
        return crop.getCropTileEntity().growth >= numGrowthStages;
    }

    public boolean isAtLeastPartiallyGrown(CropAccess crop) {
        return crop.getCropTileEntity().growth >= numGrowthStages - 1;
    }

}
