package com.unforbidable.tfc.bids.Core.Crops;

import com.dunk.tfc.Core.TFC_Climate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class CropCultivationTemp extends CropCultivation {

    private final float minimumAverageBioTemp;

    public CropCultivationTemp(Item cultivatedSeedItem, int minimumSkillRank, float baseChance, float minimumAverageBioTemp) {
        super(cultivatedSeedItem, minimumSkillRank, baseChance);

        this.minimumAverageBioTemp = minimumAverageBioTemp;
    }

    @Override
    public float getPlayerCultivationChance(CropAccess crop, EntityPlayer player) {
        float temp = TFC_Climate.getBioTemperatureHeight(crop.getCropTileEntity().getWorldObj(), crop.getCropTileEntity().xCoord, crop.getCropTileEntity().yCoord, crop.getCropTileEntity().zCoord);
        float tempDelta = minimumAverageBioTemp - temp;

        if (tempDelta < 0) {
            return 0;
        }

        float tempBonusChance = 1 + (tempDelta + 1) * (tempDelta + 1) * 0.01f;

        return super.getPlayerCultivationChance(crop, player) * tempBonusChance;
    }

}
