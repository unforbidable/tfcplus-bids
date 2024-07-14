package com.unforbidable.tfc.bids.WAILA.Providers;

import com.dunk.tfc.WAILA.WAILAData;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.WAILA.WailaProvider;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class CrackedOreProvider extends WailaProvider {

    private final WAILAData tfcWailaData = new WAILAData();

    @Override
    public int provides() {
        return PROVIDES_STACK | PROVIDES_BODY | PROVIDES_HEAD;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return tfcWailaData.oreStack(new OriginalOreBlockWailaDataAccessor(accessor), config);
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return tfcWailaData.oreHead(itemStack, currenttip, new OriginalOreBlockWailaDataAccessor(accessor), config);
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return tfcWailaData.oreBody(itemStack, currenttip, new OriginalOreBlockWailaDataAccessor(accessor), config);
    }

    static class OriginalOreBlockWailaDataAccessor implements IWailaDataAccessor {

        private final IWailaDataAccessor accessor;

        public OriginalOreBlockWailaDataAccessor(IWailaDataAccessor accessor) {
            this.accessor = accessor;
        }

        @Override
        public World getWorld() {
            return accessor.getWorld();
        }

        @Override
        public EntityPlayer getPlayer() {
            return accessor.getPlayer();
        }

        @Override
        public Block getBlock() {
            // Custom accessor returns the original ore block
            // which allows us to use TFCs WAILAData ore block providers directly
            // Other solution would be to copy the provider code here
            if (accessor.getBlock() == BidsBlocks.crackedOre) {
                return TFCBlocks.ore;
            } else if (accessor.getBlock() == BidsBlocks.crackedOre1b) {
                return TFCBlocks.ore1b;
            } else if (accessor.getBlock() == BidsBlocks.crackedOre2) {
                return TFCBlocks.ore2;
            } else if (accessor.getBlock() == BidsBlocks.crackedOre3) {
                return TFCBlocks.ore3;
            } else {
                return accessor.getBlock();
            }
        }

        @Override
        public int getBlockID() {
            return accessor.getBlockID();
        }

        @Override
        public String getBlockQualifiedName() {
            return accessor.getBlockQualifiedName();
        }

        @Override
        public int getMetadata() {
            return accessor.getMetadata();
        }

        @Override
        public TileEntity getTileEntity() {
            return accessor.getTileEntity();
        }

        @Override
        public MovingObjectPosition getPosition() {
            return accessor.getPosition();
        }

        @Override
        public Vec3 getRenderingPosition() {
            return accessor.getRenderingPosition();
        }

        @Override
        public NBTTagCompound getNBTData() {
            return accessor.getNBTData();
        }

        @Override
        public int getNBTInteger(NBTTagCompound nbtTagCompound, String s) {
            return accessor.getNBTInteger(nbtTagCompound, s);
        }

        @Override
        public double getPartialFrame() {
            return accessor.getPartialFrame();
        }

        @Override
        public ForgeDirection getSide() {
            return accessor.getSide();
        }

        @Override
        public ItemStack getStack() {
            return accessor.getStack();
        }
    }

}
