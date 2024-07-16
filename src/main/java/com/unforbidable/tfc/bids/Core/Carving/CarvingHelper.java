package com.unforbidable.tfc.bids.Core.Carving;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionHelper;
import com.unforbidable.tfc.bids.Core.Common.Collision.CollisionInfo;
import com.unforbidable.tfc.bids.Core.Player.PlayerStateManager;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.CarvingRegistry;
import com.unforbidable.tfc.bids.api.Enums.EnumAdzeMode;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CarvingHelper {

    public static boolean carveBlockAt(World world, EntityPlayer player, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ, ItemStack tool) {
        if (!world.isRemote) {
            ForgeDirection d = ForgeDirection.getOrientation(side);
            TileEntity te = world.getTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityCarving) {
                TileEntityCarving tec = (TileEntityCarving) te;

                ICarving carving = CarvingRegistry.getBlockCarving(tec);
                if (!carving.isSufficientEquipmentTier(Block.getBlockById(tec.getCarvedBlockId()), tec.getCarvedBlockMetadata(), ((ICarvingTool)tool.getItem()).getCarvingToolEquipmentTier(tool))) {
                    Bids.LOG.debug("Insufficient tool to continue to carve block at " + x + ", " + y + ", " + z);
                    return false;
                }

                if (player.isSneaking()) {
                    tec.setCarvingLocked(!tec.isCarvingLocked());
                    world.markBlockForUpdate(x, y, z);

                    return true;
                } else if (!tec.isCarvingLocked()) {
                    Bids.LOG.debug("Carving at " + x + ", " + y + ", " + z + " hit at side " + d
                            + " with vector " + hitX + ", " + hitY + ", " + hitZ);

                    if (tec.carveSelectedBit()) {
                        if (tec.getCarvedBitCount() < tec.getTotalBitCount()) {
                            // Update unless the last bit was just removed
                            TileEntityCarving.sendUpdateMessage(world, x, y, z, 0);
                        }

                        double x2 = x + 0.5D;
                        double y2 = y + 0.5D;
                        double z2 = z + 0.5D;

                        String soundEffect = carving.getCarvingSoundEffect();
                        if (soundEffect != null) {
                            world.playSoundEffect(x2, y2, z2, soundEffect,
                                    0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());
                        }

                        world.notifyBlocksOfNeighborChange(x, y, z, world.getBlock(x, y, z));

                        float damageChance = 0.25f;
                        if (world.rand.nextDouble() < damageChance) {
                            ItemStack toolStack = player.inventory.getCurrentItem();
                            if (toolStack != null) {
                                boolean destroyed = toolStack.getItemDamage() + 1 > toolStack.getMaxDamage();
                                toolStack.damageItem(1, player);

                                if (destroyed) {
                                    world.playSoundEffect(x2, y2, z2, "random.break",
                                            0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());
                                }
                            }
                        }
                    }

                    return true;
                }
            } else if (player.isSneaking()) {
                Block block = world.getBlock(x, y, z);
                int metadata = world.getBlockMetadata(x, y, z);
                Bids.LOG.debug("Block " + block.getUnlocalizedName() + ":" + metadata
                        + " at " + x + ", " + y + ", " + z + " hit with a carving tool");

                ICarving carving = CarvingRegistry.getBlockCarvingAt(world, x, y, z);
                if (carving != null && carving.isSufficientEquipmentTier(block, metadata, ((ICarvingTool)tool.getItem()).getCarvingToolEquipmentTier(tool))
                        && carving.canCarveBlockAt(block, metadata, world, x, y, z, side)) {
                    Bids.LOG.debug("Block " + block.getUnlocalizedName() + ":" + metadata
                            + " at " + x + ", " + y + ", " + z + " can be carved");

                    world.setBlock(x, y, z, carving.getCarvingBlock(block, metadata), 0, 2);
                    te = world.getTileEntity(x, y, z);
                    if (te != null && te instanceof TileEntityCarving) {
                        Bids.LOG.debug("Carving started at " + x + ", " + y + ", " + z);

                        TileEntityCarving tec = (TileEntityCarving) te;
                        tec.setCarvedBlockId(Block.getIdFromBlock(block));
                        tec.setCarvedBlockMetadata(metadata);
                        TileEntityCarving.sendUpdateMessage(world, x, y, z, 0);

                        return true;
                    } else {
                        // We get here when the carving block
                        // fails to create a TileEntityCarving
                        world.setBlock(x, y, z, block, metadata, 2);
                        Bids.LOG.warn("Carving block " + carving.getCarvingBlock(block, metadata).getUnlocalizedName()
                                + " at " + x + ", " + y + ", " + z + " did not create TileEntityCarving tile entity");
                    }
                } else {
                    // This block cannot be carved
                }
            } else {
                // Not sneaking
            }
        }

        return false;
    }

    public static void onCarvingHarvested(World world, int x, int y, int z, EntityPlayer player) {

    }

    public static CarvingBit getBitFromHit(float hitX, float hitY, float hitZ) {
        int dim = TileEntityCarving.CARVING_DIMENSION;
        int max = dim - 1;
        return new CarvingBit((int) Math.min((hitX * dim), max),
                (int) Math.min((hitY * dim), max),
                (int) Math.min((hitZ * dim), max));
    }

    public static void setBlockBoundsBasedOnSelection(IBlockAccess access, int x, int y, int z, int side, EnumAdzeMode mode) {
        Block block = access.getBlock(x, y, z);
        TileEntityCarving te = (TileEntityCarving) access.getTileEntity(x, y, z);
        CarvingBit selected = te.getSelectedBit();

        if (!selected.isEmpty()) {
            AxisAlignedBB bound = mode.getCarvingMode().getSelectedBitBounds(selected, side);
            block.setBlockBounds((float) bound.minX, (float) bound.minY, (float) bound.minZ,
                    (float) bound.maxX, (float) bound.maxY, (float) bound.maxZ);
        } else {
            block.setBlockBounds(0f, 0f, 0f, 0f, 0f, 0f);
        }
    }

    public static void setBlockBoundsBasedOnCarving(IBlockAccess access, int x, int y, int z) {
        Block block = access.getBlock(x, y, z);
        AxisAlignedBB bound = getCarvingBoundsAt(access, x, y, z);
        block.setBlockBounds((float) bound.minX, (float) bound.minY, (float) bound.minZ,
                (float) bound.maxX, (float) bound.maxY, (float) bound.maxZ);
    }

    private static void setBlockBoundsToNone(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        block.setBlockBounds(0, 0, 0, 0, 0, 0);
    }

    public static void setBlockBounds(IBlockAccess access, int x, int y, int z, AxisAlignedBB bound) {
        Block block = access.getBlock(x, y, z);
        block.setBlockBounds((float) bound.minX, (float) bound.minY, (float) bound.minZ,
                (float) bound.maxX, (float) bound.maxY, (float) bound.maxZ);
    }

    public static AxisAlignedBB getCarvingBoundsAt(IBlockAccess access, int x, int y, int z) {
        TileEntityCarving te = (TileEntityCarving) access.getTileEntity(x, y, z);
        return getCarvingBounds(te);
    }

    public static AxisAlignedBB getCarvingBounds(TileEntityCarving te) {
        int dimension = TileEntityCarving.CARVING_DIMENSION;
        float div = 1f / dimension;

        float minX = 0;
        float maxX = 1;
        float minY = 0;
        float maxY = 1;
        float minZ = 0;
        float maxZ = 1;
        boolean first = true;

        for (int bitX = 0; bitX < dimension; bitX++) {
            for (int bitY = 0; bitY < dimension; bitY++) {
                for (int bitZ = 0; bitZ < dimension; bitZ++) {
                    if (!te.isBitCarved(bitX, bitY, bitZ)) {
                        float bitMinX = bitX * div;
                        float bitMaxX = bitMinX + div;
                        float bitMinY = bitY * div;
                        float bitMaxY = bitMinY + div;
                        float bitMinZ = bitZ * div;
                        float bitMaxZ = bitMinZ + div;

                        if (first) {
                            minX = bitMinX;
                            maxX = bitMaxX;
                            minY = bitMinY;
                            maxY = bitMaxY;
                            minZ = bitMinZ;
                            maxZ = bitMaxZ;
                            first = false;
                        } else {
                            if (minX > bitMinX)
                                minX = bitMinX;
                            if (maxX < bitMaxX)
                                maxX = bitMaxX;
                            if (minY > bitMinY)
                                minY = bitMinY;
                            if (maxY < bitMaxY)
                                maxY = bitMaxY;
                            if (minZ > bitMinZ)
                                minZ = bitMinZ;
                            if (maxZ < bitMaxZ)
                                maxZ = bitMaxZ;
                        }
                    }
                }
            }
        }

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @SideOnly(Side.CLIENT)
    public static MovingObjectPosition onCarvingCollisionRayTrace(World world, int x, int y, int z,
            Vec3 startVec, Vec3 endVec) {
        TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);
        Vec3 startVecRel = startVec.addVector(-x, -y, -z);
        Vec3 endVecRel = endVec.addVector(-x, -y, -z);

        // Make sure the ray origin is the player
        if (isCollisionRayTraceInvokedByPlayer(startVec, endVec)) {
            int dimension = TileEntityCarving.CARVING_DIMENSION;
            double stride = 1f / dimension;

            EnumAdzeMode carvingMode = getPlayerCarvingMode(Minecraft.getMinecraft().thePlayer);
            ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
            ICarvingTool tool = item != null && item.getItem() instanceof ICarvingTool ? (ICarvingTool) item.getItem()
                    : null;

            if (tool != null && !te.isCarvingLocked()) {
                CollisionInfo nearestCol = null;
                CarvingBit nearestBit = null;

                for (int bitX = 0; bitX < dimension; bitX++) {
                    for (int bitY = 0; bitY < dimension; bitY++) {
                        for (int bitZ = 0; bitZ < dimension; bitZ++) {
                            if (!te.isBitCarved(bitX, bitY, bitZ)) {
                                AxisAlignedBB bound = getBitAABB(bitX, bitY, bitZ, stride);
                                CollisionInfo col = CollisionHelper.rayTraceAABB(bound, startVecRel, endVecRel);

                                // When the bit collides
                                // Save if first or closer than the nearest so far
                                if (col != null && (nearestCol == null || col.distance < nearestCol.distance)) {
                                    nearestCol = col;
                                    nearestBit = new CarvingBit(bitX, bitY, bitZ);
                                }
                            }
                        }
                    }
                }

                if (nearestCol != null) {
                    if (te.setSelectedBit(nearestBit) || te.getCarvingMode() != carvingMode || te.getSelectedSide() != nearestCol.side) {
                        te.setCarvingMode(carvingMode);
                        te.setSelectedSide(nearestCol.side);
                        TileEntityCarving.sendSelectBitMessage(world, x, y, z, nearestBit, nearestCol.side, carvingMode);
                    }

                    setBlockBoundsToNone(world, x, y, z);

                    setPlayerCarvingActive(Minecraft.getMinecraft().thePlayer, true);
                    setPlayerCarvedSide(Minecraft.getMinecraft().thePlayer, nearestCol.side);

                    return new MovingObjectPosition(x, y, z,
                            nearestCol.side,
                            nearestCol.hitVec.addVector(x, y, z));
                } else {
                    if (te.setSelectedBit(CarvingBit.Empty)) {
                        TileEntityCarving.sendSelectBitMessage(world, x, y, z, CarvingBit.Empty, 0, carvingMode);
                    }

                    setBlockBoundsBasedOnCarving(world, x, y, z);

                    setPlayerCarvingActive(Minecraft.getMinecraft().thePlayer, false);

                    return null;
                }
            } else {
                if (te.setSelectedBit(CarvingBit.Empty)) {
                    TileEntityCarving.sendSelectBitMessage(world, x, y, z, CarvingBit.Empty, 0, carvingMode);
                }

                setBlockBoundsBasedOnCarving(world, x, y, z);

                setPlayerCarvingActive(Minecraft.getMinecraft().thePlayer, false);

                // Ray tracing the whole carving cuboid when not wielding a ICarvingTool
                // or if the carving is locked
                AxisAlignedBB bounds = getCarvingBounds(te);
                CollisionInfo col = CollisionHelper.rayTraceAABB(bounds, startVecRel, endVecRel);
                if (col != null) {
                    return new MovingObjectPosition(x, y, z,
                            col.side,
                            col.hitVec.addVector(x, y, z));
                } else {
                    return null;
                }
            }
        } else {
            // Ray tracing the whole carving cuboid when non player is looking at the carving
            // Without changing the bounds, selecting bits or activating/deactivating the carving
            AxisAlignedBB bounds = getCarvingBounds(te);
            CollisionInfo col = CollisionHelper.rayTraceAABB(bounds, startVecRel, endVecRel);
            if (col != null) {
                return new MovingObjectPosition(x, y, z,
                    col.side,
                    col.hitVec.addVector(x, y, z));
            } else {
                return null;
            }
        }
    }

    private static boolean isCollisionRayTraceInvokedByPlayer(Vec3 startVec, Vec3 endVec) {
        // Block.collisionRayTrace can be called from multiple sources
        // other than the player, such as mob AI
        // We use that method to determine what part of carving the player is looking at,
        // so we need to ignore calls from other source other than the player

        // One way option is to check the stack trace
        // but beware, as this will break when our method chain call changes
        // or when the EntityRenderTFC.getMouseOver implementation changes
        // so this is far from ideal, but it can help with debugging false positive results
        //return Thread.currentThread().getStackTrace()[5].getClassName().equals("net.minecraft.entity.EntityLivingBase");

        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().renderViewEntity != null) {
            // We can use the start and end vectors of the ray trace to determine, if the origin is player
            // However the start vector is the point at the face where the player is looking at and that is tricky to make use of
            // The end vector is where the player's "sight" ends, which allows us to calculate the "reach"

            // The reach value is based on the held item's EnumItemReach
            // When the player looks at a block, the reach should always be either NEAR, MEDIUM or FAR.
            // This will include ray tracing made by WAILA which uses range equivalent to EnumItemReach.MEDIUM
            // but WAILA ray tracing originates from the player, so it will not cause problems
            // The sight range of mob's AI is rarely any of these values exactly and so those should be excluded
            double baseReach = Minecraft.getMinecraft().playerController.getBlockReachDistance();
            double shortReach = baseReach * EnumItemReach.SHORT.multiplier;
            double mediumReach = baseReach * EnumItemReach.MEDIUM.multiplier;
            double farReach = baseReach * EnumItemReach.FAR.multiplier;

            // Reach value may not be exactly the expected reach value
            // because the player could be moving and partial ticks might be involved
            // when the player's position is determined,
            // and also because of approximation and rounding errors along the way,
            // so we need to round the distance
            Vec3 playerPos = Minecraft.getMinecraft().renderViewEntity.getPosition(1f);
            double reach = playerPos.distanceTo(endVec);
            double roundedReach = Math.round(reach * 1000.0) / 1000.0;

            //Bids.LOG.info("roundedReach: " + roundedReach);

            return roundedReach == shortReach || roundedReach == mediumReach || roundedReach == farReach;
        }

        return false;
    }

    private static AxisAlignedBB getBitAABB(int bitX, int bitY, int bitZ, double stride) {
        double minX = bitX * stride;
        double maxX = minX + stride;
        double minY = bitY * stride;
        double maxY = minY + stride;
        double minZ = bitZ * stride;
        double maxZ = minZ + stride;
        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @SideOnly(Side.CLIENT)
    public static EnumAdzeMode getPlayerCarvingMode(EntityPlayer player) {
        CarvingPlayerState state = PlayerStateManager.getPlayerState(player, CarvingPlayerState.class);
        if (state == null) {
            return EnumAdzeMode.DEFAULT_MODE;
        } else {
            return state.adzeMode;
        }
    }

    @SideOnly(Side.CLIENT)
    public static int getPlayerCarvedSide(EntityPlayer player) {
        CarvingPlayerState state = PlayerStateManager.getPlayerState(player, CarvingPlayerState.class);
        if (state == null) {
            return 0;
        } else {
            return state.carvedSide;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void setPlayerCarvingMode(EntityPlayer player) {
        CarvingPlayerState state = PlayerStateManager.getPlayerState(player, CarvingPlayerState.class);
        if (state == null) {
            state = new CarvingPlayerState();
            PlayerStateManager.setPlayerState(player, state);
        }

        state.adzeMode = state.adzeMode.getNext();
    }

    @SideOnly(Side.CLIENT)
    public static void setPlayerCarvedSide(EntityPlayer player, int carvedSide) {
        CarvingPlayerState state = PlayerStateManager.getPlayerState(player, CarvingPlayerState.class);
        if (state == null) {
            state = new CarvingPlayerState();
            PlayerStateManager.setPlayerState(player, state);
        }

        state.carvedSide = carvedSide;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isPlayerCarvingActive(EntityPlayer player) {
        CarvingPlayerState state = PlayerStateManager.getPlayerState(player, CarvingPlayerState.class);
        if (state == null) {
            return false;
        } else {
            return state.isCarvingActive && state.carvingActivityChangedTime + 100 > System.currentTimeMillis();
        }
    }

    @SideOnly(Side.CLIENT)
    public static void setPlayerCarvingActive(EntityPlayer player, boolean active) {
        CarvingPlayerState state = PlayerStateManager.getPlayerState(player, CarvingPlayerState.class);
        if (state == null) {
            state = new CarvingPlayerState();
            PlayerStateManager.setPlayerState(player, state);
        }

        state.isCarvingActive = active;
        state.carvingActivityChangedTime = System.currentTimeMillis();
    }

}
