package com.unforbidable.tfc.bids.features.crafting.firestarting.main.handlers;

import com.dunk.tfc.Blocks.Devices.BlockEarlyBloomery;
import com.dunk.tfc.TileEntities.TEBloomery;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.features.crafting.firestarting.main.FireStartingHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BloomeryFireStartingHandler extends FireStartingHandler {

    public BloomeryFireStartingHandler(int priority) {
        super(priority);
    }

    @Override
    public boolean ignite(EntityPlayer player, World world, int x, int y, int z, int side) {
        return lightBloomery(player, world, x, y, z, side, true);
    }

    private boolean lightBloomery(EntityPlayer player, World world, int x, int y, int z, int side, boolean ignite) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TEBloomery) {
            TEBloomery teBloomery = (TEBloomery) te;
            if (canLightBloomery(teBloomery)) {
                if (ignite) {
                    return teBloomery.canLight();
                }

                return true;
            }
        }

        return false;
    }

    private boolean canLightBloomery(TEBloomery teBloomery) {
        // This code is adapted from TEBloomery::canLight
        // which contrary to its name lights the bloomer on a successful check
        if (!teBloomery.bloomeryLit && teBloomery.charcoalCount >= teBloomery.oreCount && teBloomery.oreCount != 0) {
            //get the direction that the bloomery is facing so that we know where the stack should be
            int meta = teBloomery.getWorldObj().getBlockMetadata(teBloomery.xCoord, teBloomery.yCoord, teBloomery.zCoord);
            int[] direction = BlockEarlyBloomery.BLOOMERY_TO_STACK_MAP[getCharcoalDir(meta)];
            int x = teBloomery.xCoord + direction[0];
            int z = teBloomery.zCoord + direction[1];
            Block block = teBloomery.getWorldObj().getBlock(x, teBloomery.yCoord, z);
            return block == TFCBlocks.charcoal &&
                teBloomery.getWorldObj().getBlockMetadata(x, teBloomery.yCoord, z) >= 7;
        }

        return false;
    }

    private int getCharcoalDir(int meta) {
        return meta & 3;
    }

}
