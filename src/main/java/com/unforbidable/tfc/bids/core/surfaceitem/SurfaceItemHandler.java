package com.unforbidable.tfc.bids.core.surfaceitem;

import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.BidsEventFactory;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class SurfaceItemHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.entityPlayer.getHeldItem() != null) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                if (!event.world.isRemote) {
                    MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(event.world, event.entityPlayer, false);
                    Vec3 hit = mop != null ? mop.hitVec.addVector(-mop.blockX, -mop.blockY, -mop.blockZ) : Vec3.createVectorHelper(0, 0, 0);
                    if (placeSurfaceItem(event.world, event.x, event.y, event.z, event.face, (float)hit.xCoord, (float)hit.yCoord, (float)hit.zCoord, event.entityPlayer)) {
                        event.setCanceled(true);
                    }
                }
            } else if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
                // After placing surface item above,
                // to prevent right-clicking with the remaining stack
                // this event needs to be cancelled too
                if (event.world.isRemote) {
                    MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(event.world, event.entityPlayer, false);
                    if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        Vec3 hit = mop.hitVec.addVector(-mop.blockX, -mop.blockY, -mop.blockZ);
                        if (placeSurfaceItem(event.world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, (float)hit.xCoord, (float)hit.yCoord, (float)hit.zCoord, event.entityPlayer)) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean placeSurfaceItem(World world, int x, int y, int z, int face, float hitX, float hitY, float hitZ, EntityPlayer entityPlayer) {
        return BidsEventFactory.onSurfaceItemPlace(entityPlayer.getHeldItem(), world, x, y, z, face, hitX, hitY, hitZ, entityPlayer);
    }

}
