package com.unforbidable.tfc.bids.Blocks;

import java.util.Random;

import com.dunk.tfc.Core.TFC_Sounds;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWattleTrapDoorCover extends Block {

    public BlockWattleTrapDoorCover() {
        super(Material.grass);

        setHardness(1);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
            int side, float hitX, float hitY, float hitZ) {
        ItemStack heldItemStack = player.getCurrentEquippedItem();
        if (heldItemStack != null && heldItemStack.getItem() == TFCItems.straw) {
            // Hitting cover with more straw does nothing
            // but the action is consumed to prevent opening straw knapping GUI
            // because that is very likely accidental
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (canEntityCollapseTrap(entity)) {
            final double quadX = x + 0.5 - entity.posX;
            final double quadZ = z + 0.5 - entity.posZ;

            boolean isCollapsing = false;
            int collapseX = 0;
            int collapseZ = 0;

            if (quadX < 0 && quadZ >= 0) {
                final boolean canCollapseN = isNeighborTrapOrAir(world, x, y, z - 1);
                final boolean canCollapseW = isNeighborTrapOrAir(world, x + 1, y, z);

                if (canCollapseN && canCollapseW) {
                    final boolean canCollapseNW = isNeighborTrapOrAir(world, x + 1, y, z - 1);

                    if (canCollapseNW) {
                        isCollapsing = true;
                        collapseX = 1;
                        collapseZ = -1;
                    }
                }
            } else if (quadX >= 0 && quadZ >= 0) {
                final boolean canCollapseN = isNeighborTrapOrAir(world, x, y, z - 1);
                final boolean canCollapseE = isNeighborTrapOrAir(world, x - 1, y, z);

                if (canCollapseN && canCollapseE) {
                    final boolean canCollapseNE = isNeighborTrapOrAir(world, x - 1, y, z - 1);

                    if (canCollapseNE) {
                        isCollapsing = true;
                        collapseX = -1;
                        collapseZ = -1;
                    }
                }
            } else if (quadX < 0 && quadZ < 0) {
                final boolean canCollapseS = isNeighborTrapOrAir(world, x, y, z + 1);
                final boolean canCollapseW = isNeighborTrapOrAir(world, x + 1, y, z);

                if (canCollapseS && canCollapseW) {
                    final boolean canCollapseSW = isNeighborTrapOrAir(world, x + 1, y, z + 1);

                    if (canCollapseSW) {
                        isCollapsing = true;
                        collapseX = 1;
                        collapseZ = 1;
                    }
                }
            } else if (quadX >= 0 && quadZ < 0) {
                final boolean canCollapseS = isNeighborTrapOrAir(world, x, y, z + 1);
                final boolean canCollapseE = isNeighborTrapOrAir(world, x - 1, y, z);

                if (canCollapseS && canCollapseE) {
                    final boolean canCollapseSE = isNeighborTrapOrAir(world, x - 1, y, z + 1);

                    if (canCollapseSE) {
                        isCollapsing = true;
                        collapseX = -1;
                        collapseZ = 1;
                    }
                }
            }

            if (isCollapsing) {
                collapseTrapAt(world, x + collapseX, y, z + collapseZ);
                collapseTrapAt(world, x + collapseX, y, z);
                collapseTrapAt(world, x, y, z + collapseZ);
                collapseTrapAt(world, x, y, z);
            } else {
                // Slow entity down when it's on top of the trap
                // before it has collapsed
                // This simulates the mob loosing foothold
                // as the trap begins to collapse
                final float slowdown = 0.8f;
                entity.motionX *= slowdown;
                entity.motionZ *= slowdown;

                if (entity.posX != entity.lastTickPosX && entity.posZ != entity.lastTickPosZ) {
                    world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "step.grass",
                            0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());
                }
            }
        }
    }

    protected boolean isNeighborTrapOrAir(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) instanceof BlockWattleTrapDoorCover ||
                world.isAirBlock(x, y - 1, z);
    }

    protected boolean canEntityCollapseTrap(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    protected void collapseTrapAt(World world, int x, int y, int z) {
        if (world.getBlock(x, y - 1, z) == BidsBlocks.wattleTrapdoor) {
            Bids.LOG.debug("Collapse trapdoor at: " + x + "," + z);

            // Destroying the trapdoor below
            // causes the cover to break as well
            world.setBlockToAir(x, y - 1, z);

            if (!world.isRemote) {
                // Trapdoor is dropped as sticks
                ItemStack sticks = new ItemStack(TFCItems.stick, 4);
                EntityItem ei = new EntityItem(world, x, y - 1, z, sticks);
                world.spawnEntityInWorld(ei);
            }

            world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, TFC_Sounds.TWIGSNAP,
                    0.5f + (world.rand.nextFloat() * 0.7f), 0.2f + (world.rand.nextFloat() * 0.6f));
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        Bids.LOG.debug("Cover harvested");

        dropBlockAsItem(world, x, y, z, metadata, 0);
    }

    @Override
    public Item getItemDropped(int i, Random r, int j) {
        return TFCItems.straw;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        if (!canPlaceBlockAt(world, x, y, z)) {
            world.setBlock(x, y, z, Blocks.air, 0, 2);

            // Cover is dropped as straw
            ItemStack straw = new ItemStack(TFCItems.straw);
            EntityItem ei = new EntityItem(world, x, y, z, straw);
            world.spawnEntityInWorld(ei);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z) instanceof BlockWattleTrapDoor
                && BlockWattleTrapDoor.canPlaceCover(world, x, y - 1, z);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return TFCBlocks.thatch.getIcon(side, meta);
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
        return true;
    }

}
