package com.unforbidable.tfc.bids.util;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.scheduler.Scheduler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class LeashHelper {

    public static final double LEASH_RADIUS = 10;

    private static final Field leashNBTTagField = ReflectionHelper.findField(EntityLiving.class, "leashNBTTag", "field_110170_bx");

    public static <T extends EntityLeashKnot> void updateLashedState(EntityLiving entityLiving, Block block, Class<T> type) {
        // This method is called before EntityLiving.onUpdate()
        // and attempts to recreate custom a leash knot entity for a custom leash block,
        // thus preventing EntityLiving.recreateLeash() from creating the vanilla leash knot entity
        NBTTagCompound leashTag = getLeashNBTTag(entityLiving);
        if (leashTag != null) {
            if (leashTag.hasKey("X") && leashTag.hasKey("Y") && leashTag.hasKey("Z")) {
                int x = leashTag.getInteger("X");
                int y = leashTag.getInteger("Y");
                int z = leashTag.getInteger("Z");

                Block knotBlock = entityLiving.worldObj.getBlock(x, y, z);
                if (knotBlock == block) {
                    T leashKnot = findOrCreateLeashKnot(entityLiving.worldObj, x, y, z, type);

                    entityLiving.setLeashedToEntity(leashKnot, true);
                    Bids.LOG.info("Entity re-leashed: {}", entityLiving);

                    // EntityLiving.setLeashedToEntity sometimes fails to send the S1BPacketEntityAttach packet
                    // to the client, so we repeat in a short while
                    Scheduler.server(() -> {
                        entityLiving.setLeashedToEntity(leashKnot, true);
                        Bids.LOG.info("Entity re-leashed (again): {}", entityLiving);
                    }, 20);

                    clearLeashNBTTag(entityLiving);
                }
            }
        }
    }

    private static void clearLeashNBTTag(EntityLiving entityLiving) {
        try {
            leashNBTTagField.set(entityLiving, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static NBTTagCompound getLeashNBTTag(EntityLiving entityLiving) {
        try {
            return (NBTTagCompound) leashNBTTagField.get(entityLiving);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends EntityLeashKnot> boolean leashToBlock(World world, int x, int y, int z, EntityPlayer player, Class<T> type) {
        AxisAlignedBB searchBox = AxisAlignedBB.getBoundingBox(
            player.posX - LEASH_RADIUS, player.posY - LEASH_RADIUS, player.posZ - LEASH_RADIUS,
            player.posX + LEASH_RADIUS, player.posY + LEASH_RADIUS, player.posZ + LEASH_RADIUS
        );
        List<?> nearbyEntities = world.getEntitiesWithinAABB(EntityLiving.class, searchBox);
        if (nearbyEntities != null) {
            T leashKnot = findLeashKnot(world, x, y, z, type);

            boolean anyAnimalLeashed = false;
            for (Object nearbyEntity : nearbyEntities) {
                EntityLiving livingEntity = (EntityLiving) nearbyEntity;
                if (livingEntity.getLeashed() && livingEntity.getLeashedToEntity() == player) {
                    if (leashKnot == null) {
                        leashKnot = createLeashKnot(world, x, y, z, type);
                    }

                    livingEntity.setLeashedToEntity(leashKnot, true);
                    anyAnimalLeashed = true;
                    Bids.LOG.info("Entity leashed: {}", livingEntity);
                }
            }

            return anyAnimalLeashed;
        }

        return false;
    }

    private static <T extends EntityLeashKnot> T findOrCreateLeashKnot(World world, int x, int y, int z, Class<T> type) {
        T leashKnot = findLeashKnot(world, x, y, z, type);
        if (leashKnot != null) {
            return leashKnot;
        }

        return createLeashKnot(world, x, y, z, type);
    }

    @SuppressWarnings("unchecked")
    private static <T extends EntityLeashKnot> T createLeashKnot(World world, int x, int y, int z, Class<T> type) {
        try {
            Constructor<? extends EntityLeashKnot> constructor = type.getConstructor(World.class, int.class, int.class, int.class);
            T leashKnot = (T) constructor.newInstance(world, x, y, z);
            world.spawnEntityInWorld(leashKnot);
            return leashKnot;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends EntityLeashKnot> T findLeashKnot(World world, int x, int y, int z, Class<T> type) {
        List<?> existingKnots = world.getEntitiesWithinAABB(type,
            AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));

        if (existingKnots != null && !existingKnots.isEmpty()) {
            return (T) existingKnots.get(0);
        }

        return null;
    }

}
