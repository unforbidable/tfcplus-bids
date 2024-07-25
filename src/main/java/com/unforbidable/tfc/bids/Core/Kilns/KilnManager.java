package com.unforbidable.tfc.bids.Core.Kilns;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.Core.Timer;
import com.unforbidable.tfc.bids.api.Events.KilnEvent;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnManager;
import com.unforbidable.tfc.bids.api.KilnRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class KilnManager implements IKilnManager {

    private final Timer kilnDiscoveryTimer = new Timer(200);
    private final Timer kilnValidationTimer = new Timer(20);
    private final Timer kilnChimneyEffectTimer = new Timer(100);

    private final IKilnHeatSource kilnHeatSource;
    private final List<IKilnChamber> kilns;

    private IKilnChamber currentKiln;

    private boolean initialized = false;

    public KilnManager(IKilnHeatSource kilnHeatSource) {
        this.kilnHeatSource = kilnHeatSource;
        this.kilns = createKilnInstances(kilnHeatSource);
    }

    @Override
    public void writeKilnManagerToNBT(NBTTagCompound tag) {
        if (currentKiln != null) {
            NBTTagCompound kilnManagerTag = new NBTTagCompound();

            kilnManagerTag.setString("currentKilnName", currentKiln.getName());

            tag.setTag("kilnManager", kilnManagerTag);
        }
    }

    @Override
    public void readKilnManagerFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("kilnManager")) {
            NBTTagCompound kilnManagerTag = tag.getCompoundTag("kilnManager");

            if (kilnManagerTag.hasKey("currentKilnName")) {
                String kilnName = kilnManagerTag.getString("currentKilnName");
                currentKiln = findKilnByName(kilnName);

                if (currentKiln == null) {
                    Bids.LOG.warn("Unknown current kiln loaded: " + kilnName);
                }
            }
        }
    }

    @Override
    public void update() {
        if (!kilnHeatSource.getWorld().isRemote) {
            if (!initialized) {
                // Look for new kiln immediately
                if (currentKiln == null && kilnHeatSource.isActive()) {
                    handleKilnValidation();
                }

                initialized = true;
            }

            // Look for new kiln
            if (kilnDiscoveryTimer.tick()) {
                if (currentKiln == null && kilnHeatSource.isActive()) {
                    handleKilnValidation();
                }
            }

            // Validate current kiln
            if (kilnValidationTimer.tick()) {
                if (currentKiln != null && kilnHeatSource.isActive()) {
                    handleKilnValidation();
                }
            }

            // Kiln chimney smoke
            if (kilnChimneyEffectTimer.tick()) {
                if (currentKiln != null && kilnHeatSource.getProgress() < 1f && kilnHeatSource.isActive()) {
                    setCurrentKilnChimneyEffect(kilnChimneyEffectTimer.getTicksToGo());
                }
            }
        }
    }

    @Override
    public void updateProgress(double lastKilnProgress, double currentKilnProgress) {
        // Watch for progress
        if (currentKiln != null) {
            Bids.LOG.debug("Kiln progress {}", currentKilnProgress);

            if (currentKilnProgress >= 1f) {
                Bids.LOG.debug("Kiln done");

                // Deactivate smoke
                setCurrentKilnChimneyEffect(0);
            }

            // Process content
            for (BlockCoord bc : currentKiln.getPotteryLocations()) {
                KilnEvent.FireBlock event = new KilnEvent.FireBlock(kilnHeatSource.getWorld(), bc.x, bc.y, bc.z, currentKilnProgress);
                MinecraftForge.EVENT_BUS.post(event);
            }
        }
    }

    private void handleKilnValidation() {
        IKilnChamber kiln = findValidKiln();

        if (currentKiln != null) {
            if (currentKiln != kiln) {
                // Current kiln structure lost
                onKilnBroken();

                currentKiln = null;

                // Delay new kiln discovery
                kilnDiscoveryTimer.reset();
            } else {
                // Kiln is still here
                onKilnRevalidated();
            }
        }

        if (currentKiln == null && kiln != null) {
            // New kiln structure found
            currentKiln = kiln;

            onKilnDiscovered();
        }
    }

    private void onKilnRevalidated() {
        //Bids.LOG.debug("Revalidated kiln: " + currentKiln.getName());
    }

    private void onKilnDiscovered() {
        Bids.LOG.debug("Detected kiln: " + currentKiln.getName());

        // Activate smoke and reset the timer
        kilnChimneyEffectTimer.reset();
        setCurrentKilnChimneyEffect(kilnChimneyEffectTimer.getTicksToGo());

        // Anytime kiln is detected the burning item counters are reset
        // This includes re-detection after being broken, at which point the progress should reset
        kilnHeatSource.resetProgress();
    }

    private void onKilnBroken() {
        Bids.LOG.debug("Broken kiln: " + currentKiln.getName());

        // Deactivate smoke
        setCurrentKilnChimneyEffect(0);
    }

    public void setCurrentKilnChimneyEffect(int ticks) {
        if (currentKiln.isValid()) {
            BlockCoord bc = currentKiln.getChimneyLocation();
            if (bc != null) {
                TileEntity te = kilnHeatSource.getWorld().getTileEntity(bc.x, bc.y, bc.z);
                if (te != null) {
                    if (ChimneyHelper.isChimney(te)) {
                        ChimneyHelper.setChimneyFire(te, ticks);
                    } else {
                        Bids.LOG.warn("Expected chimney at {},{},{}", bc.x, bc.y, bc.z);
                    }
                }
            }
        }
    }

    private IKilnChamber findKilnByName(String name) {
        for (IKilnChamber kiln : kilns) {
            if (kiln.getName().equals(name)) {
                return kiln;
            }
        }

        return null;
    }

    private IKilnChamber findValidKiln() {
        if (currentKiln != null) {
            // Validate current kiln first
            if (currentKiln.validate()) {
                // Current kiln is still valid
                return currentKiln;
            }
        }

        // Validate all but the current kiln
        // which would have been checked above
        for (IKilnChamber kiln : kilns) {
            if (kiln != currentKiln) {
                if (kiln.validate()) {
                    return kiln;
                }
            }
        }

        // No valid kiln found
        return null;
    }

    private static List<IKilnChamber> createKilnInstances(IKilnHeatSource heatSource) {
        List<IKilnChamber> list = new ArrayList<IKilnChamber>();
        for (Class<?> c : KilnRegistry.getKilnChambers()) {
            IKilnChamber kiln = createKilnInstance(c, heatSource);
            if (kiln != null) {
                list.add(kiln);
            }
        }

        return list;
    }

    private static IKilnChamber createKilnInstance(Class<?> cls, IKilnHeatSource kilnHeatSource) {
        try {
            Constructor<?> constructor = cls.getConstructor(IKilnHeatSource.class);
            Object instance = constructor.newInstance(kilnHeatSource);
            if (instance instanceof IKilnChamber) {
                return (IKilnChamber) instance;
            } else {
                Bids.LOG.warn("Kiln class {} does not implement interface IKilnChamber", cls.getName());
            }
        } catch (Exception e) {
            Bids.LOG.warn("Kiln class {} is lacking constructor with a single parameter IKilnHeatSource", cls.getName());
        }

        return null;
    }

}
