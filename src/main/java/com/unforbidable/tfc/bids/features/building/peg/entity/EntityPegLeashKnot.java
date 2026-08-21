package com.unforbidable.tfc.bids.features.building.peg.entity;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.features.building.peg.block.BlockPeg;
import com.unforbidable.tfc.bids.util.LeashHelper;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityPegLeashKnot extends EntityLeashKnot {

    public EntityPegLeashKnot(World world) {
        super(world);
    }

    public EntityPegLeashKnot(World world, int x, int y, int z) {
        super(world, x, y, z);
        this.setPosition((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D);
    }

    @Override
    public boolean onValidSurface() {
        Block block = this.worldObj.getBlock(this.field_146063_b, this.field_146064_c, this.field_146062_d);
        return block instanceof BlockPeg;
    }

    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (!worldObj.isRemote) {
            boolean anyAnimalLeashed = false;

            AxisAlignedBB searchBox = AxisAlignedBB.getBoundingBox(
                player.posX - LeashHelper.LEASH_RADIUS, player.posY - LeashHelper.LEASH_RADIUS, player.posZ - LeashHelper.LEASH_RADIUS,
                player.posX + LeashHelper.LEASH_RADIUS, player.posY + LeashHelper.LEASH_RADIUS, player.posZ + LeashHelper.LEASH_RADIUS
            );
            List<?> nearbyEntities = worldObj.getEntitiesWithinAABB(EntityLiving.class, searchBox);
            if (nearbyEntities != null) {

                for (Object nearbyEntity : nearbyEntities) {
                    EntityLiving livingEntity = (EntityLiving) nearbyEntity;
                    if (livingEntity.getLeashed() && livingEntity.getLeashedToEntity() == player) {
                        livingEntity.setLeashedToEntity(this, true);
                        anyAnimalLeashed = true;
                        Bids.LOG.info("More entity leashed: {}", livingEntity);
                    }
                }

                if (!anyAnimalLeashed) {
                    for (Object nearbyEntity : nearbyEntities) {
                        EntityLiving livingEntity = (EntityLiving) nearbyEntity;
                        if (livingEntity.getLeashed() && livingEntity.getLeashedToEntity() == this) {
                            livingEntity.clearLeashed(true, false);
                            if (!player.capabilities.isCreativeMode) {
                                livingEntity.dropItem(TFCItems.rope, 1);
                            }
                            Bids.LOG.info("Entity released: {}", livingEntity);
                        }
                    }

                    setDead();
                }
            }
        }

        return true;
    }

}
