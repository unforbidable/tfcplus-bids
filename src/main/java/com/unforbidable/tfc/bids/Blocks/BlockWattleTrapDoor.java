package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWattleTrapDoor extends BlockTrapDoor {

    protected boolean ignoreRedstone = false;

    public BlockWattleTrapDoor() {
        super(Material.wood);

        setHardness(1);
        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    public BlockWattleTrapDoor setIgnoreRedstone(boolean ignoreRedstone) {
        this.ignoreRedstone = ignoreRedstone;

        return this;
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
            int side, float hitX, float hitY, float hitZ) {
        if (world.getBlock(x, y + 1, z) == BidsBlocks.wattleTrapdoorCover) {
            // Don't do anything if there is already straw cover
            return true;
        }

        ItemStack heldItemStack = player.getCurrentEquippedItem();
        if (heldItemStack != null && heldItemStack.getItem() == TFCItems.straw) {
            if (canPlaceCover(world, x, y, z)) {
                // Activate
                // with a straw
                // place straw cover
                if (heldItemStack.stackSize > 0 && world.isAirBlock(x, y + 1, z) && !world.isRemote) {
                    world.setBlock(x, y + 1, z, BidsBlocks.wattleTrapdoorCover, 0, 3);

                    world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "dig.grass",
                            0.4F + (world.rand.nextFloat() / 2), 0.7F + world.rand.nextFloat());

                    if (!player.capabilities.isCreativeMode) {
                        heldItemStack.stackSize--;
                    }

                    Bids.LOG.debug("Placed straw cover on top of wattle trapdoor");
                }

                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void registerBlockIcons(IIconRegister registerer) {
        blockIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureName);
    }

    @Override
    public void func_150120_a(World world, int x, int y, int z, boolean isPowerOn) {
        if (!ignoreRedstone) {
            super.func_150120_a(world, x, y, z, isPowerOn);
        }
    }

    public static boolean canPlaceCover(IBlockAccess world, int x, int y, int z) {
        final int metadata = world.getBlockMetadata(x, y, z);

        // Is trapdoor closed and in top position
        return (metadata & 4) == 0 && (metadata & 8) != 0;
    }

}
