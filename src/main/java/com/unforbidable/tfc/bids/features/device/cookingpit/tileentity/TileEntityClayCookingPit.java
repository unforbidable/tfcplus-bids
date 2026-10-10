package com.unforbidable.tfc.bids.features.device.cookingpit.tileentity;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.features.cooking.CookingHeatLevel;
import com.unforbidable.tfc.bids.features.crafting.drying.main.Environment.DynamicEnvironment;
import com.unforbidable.tfc.bids.features.crafting.drying.main.Environment.StaticEnvironment;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.ClayCookingPitBounds;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitBounds;
import com.unforbidable.tfc.bids.util.Timer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

public class TileEntityClayCookingPit extends TileEntityCookingPit {

    private static final int DRYING_CHECK_INTERVAL = 100;
    private static final int TICKS_TO_DRY = 20000;

    private boolean ready;
    private long ticksDryingStarted;
    private long ticksDryingLastChecked;

    private final Timer dryingCheckTimer = new Timer(DRYING_CHECK_INTERVAL);

    private CookingPitBounds cachedBounds;

    public boolean isReady() {
        return ready;
    }

    public int getDryingPercent() {
        if (ready) {
            return 1;
        } else if (ticksDryingStarted > 0) {
            long elapsed = TFC_Time.getTotalTicks() - ticksDryingStarted;
            float progress =  elapsed / (float)TICKS_TO_DRY;
            return  (int)Math.floor(progress * 10) * 10;
        } else {
            return 0;
        }
    }

    public boolean isDryingPaused() {
        StaticEnvironment staticEnvironment = new StaticEnvironment(worldObj, xCoord, yCoord, zCoord);
        DynamicEnvironment env = staticEnvironment.ofTicks(ticksDryingLastChecked);
        return env.getHumidity() == 1;
    }

    @Override
    public void onBreakBlock() {
        super.onBreakBlock();

        if (!ready) {
            ItemStack is = new ItemStack(TFCItems.clayBall, 25, 0);
            EntityItem ei = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, is);
            worldObj.spawnEntityInWorld(ei);
        }
    }

    @Override
    public boolean interactWithItemStack(ItemStack equippedItem, EntityPlayer player) {
        return ready &&
            super.interactWithItemStack(equippedItem, player);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!ready && dryingCheckTimer.tick()) {
            if (ticksDryingLastChecked == 0) {
                ticksDryingStarted = TFC_Time.getTotalTicks();
                ticksDryingLastChecked = TFC_Time.getTotalTicks();
            } else  if (ticksDryingLastChecked + TFC_Time.HOUR_LENGTH < TFC_Time.getTotalTicks()) {
                // Check drying conditions
                StaticEnvironment staticEnvironment = new StaticEnvironment(worldObj, xCoord, yCoord, zCoord);
                while (ticksDryingLastChecked + TFC_Time.HOUR_LENGTH < TFC_Time.getTotalTicks()) {
                    ticksDryingLastChecked += TFC_Time.HOUR_LENGTH;

                    // Humidity affects the drying speed
                    // by musing the starting ticks
                    DynamicEnvironment env = staticEnvironment.ofTicks(ticksDryingLastChecked);
                    float humidity = env.getHumidity();
                    if (humidity > 0) {
                        ticksDryingStarted += TFC_Time.HOUR_LENGTH * humidity;

                        markDirty();
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                }

                // Check finished
                if (ticksDryingStarted + TICKS_TO_DRY < TFC_Time.getTotalTicks()) {
                    ready = true;
                    ticksDryingStarted = 0;
                    ticksDryingLastChecked = 0;

                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);

                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    clientNeedToUpdate = true;
                }
            }
        }
    }

    @Override
    public CookingPitBounds getCachedBounds() {
        if (cachedBounds == null) {
            cachedBounds = ClayCookingPitBounds.getBounds();
        }

        return cachedBounds;
    }

    @Override
    public int getMaxFluidVolume() {
        return 5000;
    }

    @Override
    protected CookingHeatLevel getMaxHeatLevel() {
        return CookingHeatLevel.MEDIUM;
    }

    @Override
    public boolean isValidFluid(Fluid fluid) {
        return true;
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tag) {
        super.readDataFromNBT(tag);

        ticksDryingStarted = tag.getLong("ticksDryingStarted");
        ticksDryingLastChecked = tag.getLong("ticksDryingLastChecked");
        ready = tag.getBoolean("ready");
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tag) {
        super.writeDataToNBT(tag);

        tag.setLong("ticksDryingStarted", ticksDryingStarted);
        tag.setLong("ticksDryingLastChecked", ticksDryingLastChecked);
        tag.setBoolean("ready", ready);
    }

}
