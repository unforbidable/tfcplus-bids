package com.unforbidable.tfc.bids.features.device.cookingpit.tileentity;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.HideVesselHelper;
import com.unforbidable.tfc.bids.util.Timer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityHideCookingPit extends TileEntityCookingPit {

    private static final int DAMAGE_CHECK_INTERVAL = 1000;

    private final Timer damageCheckTimer = new Timer(DAMAGE_CHECK_INTERVAL);

    float hideVesselDamage = 0;
    long lastDamageTicks = 0;

    public float getHideVesselDamage() {
        return hideVesselDamage;
    }

    public void setHideVesselDamage(int hideVesselDamage) {
        this.hideVesselDamage = hideVesselDamage;
    }

    @Override
    public int getMaxFluidVolume() {
        return 2000;
    }

    @Override
    protected CookingHeatLevel getMaxHeatLevel() {
        return CookingHeatLevel.LOW;
    }

    @Override
    protected boolean isValidFluid(FluidStack fluid) {
        return fluid.getFluid() == TFCFluids.FRESHWATER;
    }

    @Override
    public void onBreakBlock() {
        super.onBreakBlock();

        ItemStack is = new ItemStack(BidsItems.hideVessel);
        is.setItemDamage((int) Math.ceil(hideVesselDamage));

        EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, is);
        worldObj.spawnEntityInWorld(ei);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            if (damageCheckTimer.tick()) {
                checkDamage();
            }
        }
    }

    private void checkDamage() {
        if (lastDamageTicks + HideVesselHelper.HIDE_VESSEL_GROUND_DAMAGE_INTERVAL < TFC_Time.getTotalTicks()) {
            hideVesselDamage++;

            lastDamageTicks = TFC_Time.getTotalTicks();
        }

        if (hideVesselDamage > HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE) {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    protected void onHeatingStoneConsumed() {
        super.onHeatingStoneConsumed();

        hideVesselDamage += HideVesselHelper.HIDE_VESSEL_DAMAGE_PER_STONE;
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        super.readDataFromNBT(tag);

        hideVesselDamage = tag.getFloat("hideVesselDamage");
        lastDamageTicks = tag.getLong("lastDamageTicks");
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        super.writeDataToNBT(tag);

        tag.setFloat("hideVesselDamage", hideVesselDamage);
        tag.setLong("lastDamageTicks", lastDamageTicks);
    }

}
