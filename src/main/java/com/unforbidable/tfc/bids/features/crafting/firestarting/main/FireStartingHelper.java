package com.unforbidable.tfc.bids.features.crafting.firestarting.main;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.TFC_ItemHeat;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsEventFactory;
import com.unforbidable.tfc.bids.core.network.Network;
import com.unforbidable.tfc.bids.features.crafting.drying.main.Environment.DynamicEnvironment;
import com.unforbidable.tfc.bids.features.crafting.drying.main.Environment.StaticEnvironment;
import com.unforbidable.tfc.bids.features.crafting.firestarting.FireStartingConfig;
import com.unforbidable.tfc.bids.features.crafting.firestarting.item.ItemTinder;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.network.TinderBurningPacket;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class FireStartingHelper {

    public static final int SMOLDERING_TEMP = 400;

    public static boolean isTinder(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemTinder;
    }

    public static TinderQuality getTinderQuality(ItemStack itemStack) {
        if (OreDictionaryHelper.itemStackIsOre(itemStack, "materialTinderExcellent")) {
            return TinderQuality.EXCELLENT;
        }

        if (OreDictionaryHelper.itemStackIsOre(itemStack, "materialTinderGood")) {
            return TinderQuality.GOOD;
        }

        return itemStack.getItem() instanceof ItemTinder ? TinderQuality.POOR : TinderQuality.NONE;
    }

    public static void onFireStartingProgress(ItemStack firestarter, World world, EntityPlayer player, int maxTicks, int remainingTicks) {
        MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(world, player, false);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;
            int side = mop.sideHit;

            if (world.isRemote) {
                // The closer to finish the more smoke
                if (remainingTicks < 40 && remainingTicks % 4 == 0 || remainingTicks < 20 && remainingTicks % 2 == 0 || remainingTicks < 5) {
                    FireStartingEntityScanner scanner = new FireStartingEntityScanner(world, x, y, z);
                    TinderQuality tinderQuality = scanner.getTinderQuality();

                    // The better tinder the more smoke
                    if (world.rand.nextFloat() < getChanceForTinderQuality(tinderQuality)) {
                        world.spawnParticle("smoke", mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord,
                            0.0, 0.10000000149011612, 0.0);
                    }
                }
            } else {
                if (remainingTicks % 20 == 0) {
                    // Keep delaying picking up during fire starting
                    FireStartingEntityScanner scanner = new FireStartingEntityScanner(world, x, y, z);
                    for (EntityItem entityItem : scanner.getEntities()) {
                        entityItem.delayBeforeCanPickup = 50;
                    }
                }

                int ticksElapsed = maxTicks - remainingTicks;
                if (ticksElapsed < 5 && ticksElapsed % 3 == 0) {
                    player.worldObj.playSoundEffect(mop.blockX, mop.blockY, mop.blockZ,
                        "terrafirmacraftplus:item.firestarter", 0.5F, 0.05F);
                }

                if (remainingTicks == 1 &&
                    BidsEventFactory.onFireStartingStart(player, world, x, y, z, side) &&
                    BidsEventFactory.onFireStartingComplete(player, world, x, y, z, side)) {
                    FireStartingEntityScanner scanner = new FireStartingEntityScanner(world, x, y, z);
                    TinderQuality tinderQuality = scanner.getTinderQuality();

                    float humidityModifier = getHumidityModifier(world, x, y, z);
                    float chance = getChanceForTinderQuality(tinderQuality) * humidityModifier;
                    boolean success = world.rand.nextFloat() < chance;

                    Bids.LOG.info("Fire starting chance {} -> success: {}", chance, success);

                    if (success) {
                        boolean ignited = BidsEventFactory.onFireStartingIgnite(player, world, x, y, z, side);

                        EntityItem tinderEntityItem = scanner.getTinder();
                        if (tinderEntityItem != null) {
                            Network.sendToClient(new TinderBurningPacket(tinderEntityItem.posX, tinderEntityItem.posY, tinderEntityItem.posZ), player);

                            if (!ignited) {
                                // Spawn smoldering embers instead when block does not ignite
                                ItemStack smolderingTinder = tinderEntityItem.getEntityItem().copy();
                                smolderingTinder.stackSize = 1;
                                FireStartingHelper.setTinderSmolderingTemperature(smolderingTinder, false);
                                EntityItem entityItem = new EntityItem(player.worldObj,
                                    tinderEntityItem.posX, tinderEntityItem.posY + 0.1, tinderEntityItem.posZ, smolderingTinder);
                                entityItem.delayBeforeCanPickup = 20;
                                player.worldObj.spawnEntityInWorld(entityItem);
                            }

                            tinderEntityItem.getEntityItem().stackSize--;

                            if (tinderEntityItem.getEntityItem().stackSize == 0) {
                                tinderEntityItem.delayBeforeCanPickup = 100;
                                tinderEntityItem.setInvisible(true);
                                tinderEntityItem.setDead();
                            } else {
                                tinderEntityItem.delayBeforeCanPickup = 20;
                            }
                        }
                    }

                    firestarter.damageItem(1, player);
                }
            }
        }
    }

    private static float getHumidityModifier(World world, int x, int y, int z) {
        if (FireStartingConfig.fireStartingHumidityImpact > 0) {
            DynamicEnvironment env = new StaticEnvironment(world, x, y, z).ofTicks(TFC_Time.getTotalTicks());
            float humidity = env.getHumidity();
            return 1f - humidity * FireStartingConfig.fireStartingHumidityImpact;
        } else {
            return 1f;
        }
    }

    private static float getChanceForTinderQuality(TinderQuality tinderQuality) {
        switch (tinderQuality) {
            case EXCELLENT:
                return FireStartingConfig.fireStartingChanceExcellentTinder;
            case GOOD:
                return FireStartingConfig.fireStartingChanceGoodTinder;
            case POOR:
                return FireStartingConfig.fireStartingChancePoorTinder;
        }

        return FireStartingConfig.fireStartingChanceNoTinder;
    }

    private static int getExtraTempForTinderQuality(TinderQuality tinderQuality) {
        switch (tinderQuality) {
            case EXCELLENT:
                return 200;
            case GOOD:
                return 100;
        }

        return 25;
    }

    public static boolean isTinderSmoldering(ItemStack itemStack) {
        return TFC_ItemHeat.getTemp(itemStack) >= SMOLDERING_TEMP;
    }

    public static boolean isTinderSpent(ItemStack itemStack) {
        return TFC_ItemHeat.hasTemp(itemStack) && TFC_ItemHeat.getTemp(itemStack) < SMOLDERING_TEMP;
    }

    public static boolean isTinderLit(ItemStack itemStack) {
        return TFC_ItemHeat.hasTemp(itemStack);
    }

    public static void setTinderSmolderingTemperature(ItemStack itemStack, boolean propagated) {
        // Ensure item stack has nbt data
        // so that the temp can be set without item having registered heat index
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        // When tinder is lit from a fire source instead of by fire starting mechanics
        // the tinder time is reduced to 20%
        float extraTemp = getExtraTempForTinderQuality(getTinderQuality(itemStack));
        TFC_ItemHeat.setTemp(itemStack, SMOLDERING_TEMP + (propagated ? extraTemp * 0.2f : extraTemp));
    }

    public static String getTinderDisplayNamePrefix(ItemStack itemStack) {
        if (TFC_ItemHeat.hasTemp(itemStack)) {
            float temp = TFC_ItemHeat.getTemp(itemStack);
            if (temp < SMOLDERING_TEMP) {
                return StatCollector.translateToLocal("gui.tinder.spent") + " ";
            } else {
                return StatCollector.translateToLocal("gui.tinder.smoldering") + " ";
            }
        }

        return "";
    }

    public static float getTinderSmolderingProgress(ItemStack itemStack) {
        if (TFC_ItemHeat.hasTemp(itemStack)) {
            float temp = TFC_ItemHeat.getTemp(itemStack);
            float maxTemp = getExtraTempForTinderQuality(getTinderQuality(itemStack));
            return (temp - SMOLDERING_TEMP) / maxTemp;
        }

        return 0;
    }

}
