package com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.TEForge;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class ForgeFireStartingHandler extends FireStartingHandler {

    public ForgeFireStartingHandler(int priority) {
        super(priority);
    }

    @Override
    public boolean start(EntityPlayer player, World world, int x, int y, int z, int side) {
        return lightExistingForge(player, world, x, y, z, side, false) || createForge(player, world, x, y, z, side, false);
    }

    @Override
    public boolean ignite(EntityPlayer player, World world, int x, int y, int z, int side) {
        return lightExistingForge(player, world, x, y, z, side, true) || createForge(player, world, x, y, z, side, true);
    }

    @Override
    public boolean propagate(EntityPlayer player, World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEForge) {
            TEForge teForge = (TEForge) te;
            return teForge.fireTemp > 1;
        }

        return false;
    }

    private boolean createForge(EntityPlayer player, World world, int x, int y, int z, int side, boolean ignite) {
        Block block = world.getBlock(x, y, z);
        if (side == 1 && (block == TFCBlocks.charcoal && world.getBlockMetadata(x, y, z) > 6 || block == Blocks.coal_block) &&
            TFC_Core.isSurroundedStone(world, x, y, z)) {
            if (ignite) {
                world.setBlock(x, y, z, TFCBlocks.forge, 1, 2);
            }

            return true;
        }

        return false;
    }

    private boolean lightExistingForge(EntityPlayer player, World world, int x, int y, int z, int side, boolean ignite) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEForge) {
            TEForge teForge = (TEForge) te;
            if (teForge.fireTemp <= 1) {
                if (ignite) {
                    if (teForge.isSmokeStackValid) {
                        if (teForge.fireItemStacks[7] != null) {
                            teForge.fireTemp = 10;
                            teForge.fuelBurnTemp = 20;
                            teForge.fuelTimeLeft = 10;
                            world.setBlockMetadataWithNotify(x, y, z, 2, 3);

                            return true;
                        }
                    } else if (player != null) {
                        TFC_Core.sendInfoMessage(player, new ChatComponentTranslation("gui.forge.badChimney"));
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

}
