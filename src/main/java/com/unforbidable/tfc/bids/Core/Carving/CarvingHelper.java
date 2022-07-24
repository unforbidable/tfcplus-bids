package com.unforbidable.tfc.bids.Core.Carving;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCarving;
import com.unforbidable.tfc.bids.api.CarvingRegistry;
import com.unforbidable.tfc.bids.api.Interfaces.ICarving;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingTool;

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
            float hitX, float hitY, float hitZ, ICarvingTool tool) {
        if (!world.isRemote) {
            ForgeDirection d = ForgeDirection.getOrientation(side);
            TileEntity te = world.getTileEntity(x, y, z);
            if (te != null && te instanceof TileEntityCarving) {
                TileEntityCarving tec = (TileEntityCarving) te;

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

                        ICarving carving = CarvingRegistry.getBlockCarving(tec);
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
                                boolean destroyed = toolStack.getItemDamage() + 1 >= toolStack.getMaxDamage();
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
                if (carving != null && carving.canCarveBlockWithTool(block, metadata, tool)
                        && carving.canCarveBlockAt(block, metadata, world, x, y, z, side)) {
                    Bids.LOG.debug("Block " + block.getUnlocalizedName() + ":" + metadata
                            + " at " + x + ", " + y + ", " + z + " can be carved");

                    world.setBlock(x, y, z, carving.getCarvingBlock(block, metadata));
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
                        world.setBlock(x, y, z, block, metadata, 3);
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

    public static void setBlockBoundsBasedOnSelection(IBlockAccess access, int x, int y, int z) {
        Block block = access.getBlock(x, y, z);
        TileEntityCarving te = (TileEntityCarving) access.getTileEntity(x, y, z);
        CarvingBit selected = te.getSelectedBit();

        if (!selected.isEmpty()) {
            int d = TileEntityCarving.CARVING_DIMENSION;
            float div = 1f / d;

            float minX = selected.bitX * div;
            float maxX = minX + div;
            float minY = selected.bitY * div;
            float maxY = minY + div;
            float minZ = selected.bitZ * div;
            float maxZ = minZ + div;

            AxisAlignedBB bound = AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
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

    public static void updateBlockBoundsAfterCollisions(World world, int x, int y, int z) {
        if (world.isRemote) {
            System.out.println("!");
            TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);
            ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
            ICarvingTool tool = item != null && item.getItem() instanceof ICarvingTool ? (ICarvingTool) item.getItem()
                    : null;
            if (!te.isCarvingLocked() && !te.getSelectedBit().isEmpty() && tool != null) {
                CarvingHelper.setBlockBoundsBasedOnSelection(world, x, y, z);
            } else {
                CarvingHelper.setBlockBoundsBasedOnCarving(world, x, y, z);
            }
        }
    }

    public static MovingObjectPosition onCarvingCollisionRayTrace(World world, int x, int y, int z,
            Vec3 startVec, Vec3 endVec) {
        TileEntityCarving te = (TileEntityCarving) world.getTileEntity(x, y, z);
        startVec = startVec.addVector(-x, -y, -z);
        endVec = endVec.addVector(-x, -y, -z);

        int dimension = TileEntityCarving.CARVING_DIMENSION;
        double stride = 1f / dimension;

        ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
        ICarvingTool tool = item != null && item.getItem() instanceof ICarvingTool ? (ICarvingTool) item.getItem()
                : null;

        if (tool != null && !te.isCarvingLocked()) {
            CarvingBitCollision nearestCol = null;
            CarvingBit nearestBit = null;

            for (int bitX = 0; bitX < dimension; bitX++) {
                for (int bitY = 0; bitY < dimension; bitY++) {
                    for (int bitZ = 0; bitZ < dimension; bitZ++) {
                        if (!te.isBitCarved(bitX, bitY, bitZ)) {
                            AxisAlignedBB bound = getBitAABB(bitX, bitY, bitZ, stride);
                            CarvingBitCollision col = rayTraceAABB(bound, startVec, endVec);

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
                if (te.setSelectedBit(nearestBit)) {
                    TileEntityCarving.sendSelectBitMessage(world, x, y, z, nearestBit);
                }

                setBlockBoundsBasedOnSelection(world, x, y, z);

                return new MovingObjectPosition(x, y, z,
                        nearestCol.side,
                        nearestCol.hitVec.addVector(x, y, z));
            } else {
                if (te.setSelectedBit(CarvingBit.Empty)) {
                    TileEntityCarving.sendSelectBitMessage(world, x, y, z, CarvingBit.Empty);
                }

                setBlockBoundsBasedOnSelection(world, x, y, z);

                return null;
            }
        } else {
            // Ray tracing the whole carving cuboid when not wielding a ICarvingTool
            // or if the carcing is locked
            AxisAlignedBB bounds = getCarvingBounds(te);
            setBlockBounds(world, x, y, z, bounds);

            if (te.setSelectedBit(CarvingBit.Empty)) {
                TileEntityCarving.sendSelectBitMessage(world, x, y, z, CarvingBit.Empty);
            }

            CarvingBitCollision col = rayTraceAABB(bounds, startVec, endVec);
            if (col != null) {

                return new MovingObjectPosition(x, y, z,
                        col.side,
                        col.hitVec.addVector(x, y, z));
            } else {
                return null;
            }
        }
    }

    private static CarvingBitCollision rayTraceAABB(AxisAlignedBB bound, Vec3 startVec, Vec3 endVec) {
        Vec3 minX = startVec.getIntermediateWithXValue(endVec, bound.minX);
        Vec3 maxX = startVec.getIntermediateWithXValue(endVec, bound.maxX);
        Vec3 minY = startVec.getIntermediateWithYValue(endVec, bound.minY);
        Vec3 maxY = startVec.getIntermediateWithYValue(endVec, bound.maxY);
        Vec3 minZ = startVec.getIntermediateWithZValue(endVec, bound.minZ);
        Vec3 maxZ = startVec.getIntermediateWithZValue(endVec, bound.maxZ);

        if (!isVecInsideYZBounds(bound, minX))
            minX = null;
        if (!isVecInsideYZBounds(bound, maxX))
            maxX = null;
        if (!isVecInsideXZBounds(bound, minY))
            minY = null;
        if (!isVecInsideXZBounds(bound, maxY))
            maxY = null;
        if (!isVecInsideXYBounds(bound, minZ))
            minZ = null;
        if (!isVecInsideXYBounds(bound, maxZ))
            maxZ = null;

        Vec3 hitVec = null;
        if (minX != null && (hitVec == null || startVec.distanceTo(minX) < startVec.distanceTo(hitVec)))
            hitVec = minX;
        if (maxX != null && (hitVec == null || startVec.distanceTo(maxX) < startVec.distanceTo(hitVec)))
            hitVec = maxX;
        if (minY != null && (hitVec == null || startVec.distanceTo(minY) < startVec.distanceTo(hitVec)))
            hitVec = minY;
        if (maxY != null && (hitVec == null || startVec.distanceTo(maxY) < startVec.distanceTo(hitVec)))
            hitVec = maxY;
        if (minZ != null && (hitVec == null || startVec.distanceTo(minZ) < startVec.distanceTo(hitVec)))
            hitVec = minZ;
        if (maxZ != null && (hitVec == null || startVec.distanceTo(maxZ) < startVec.distanceTo(hitVec)))
            hitVec = maxZ;

        if (hitVec == null)
            return null;

        int side = -1;
        if (hitVec == minX)
            side = 4;
        if (hitVec == maxX)
            side = 5;
        if (hitVec == minY)
            side = 0;
        if (hitVec == maxY)
            side = 1;
        if (hitVec == minZ)
            side = 2;
        if (hitVec == maxZ)
            side = 3;

        return new CarvingBitCollision(hitVec, side, startVec.distanceTo(hitVec));
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

    private static boolean isVecInsideYZBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.yCoord >= bound.minY && vec3.yCoord <= bound.maxY && vec3.zCoord >= bound.minZ
                    && vec3.zCoord <= bound.maxZ;
    }

    private static boolean isVecInsideXZBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.xCoord >= bound.minX && vec3.xCoord <= bound.maxX && vec3.zCoord >= bound.minZ
                    && vec3.zCoord <= bound.maxZ;
    }

    private static boolean isVecInsideXYBounds(AxisAlignedBB bound, Vec3 vec3) {
        if (vec3 == null)
            return false;
        else
            return vec3.xCoord >= bound.minX && vec3.xCoord <= bound.maxX && vec3.yCoord >= bound.minY
                    && vec3.yCoord <= bound.maxY;
    }

}
