package com.unforbidable.tfc.bids.features.device.woodpile.block;

import com.dunk.tfc.Blocks.Terrain.BlockIgIn;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.core.features.registry.BlockRenderIdProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class BlockCrackedStoneIgIn extends BlockIgIn {

    public BlockCrackedStoneIgIn() {
        super(Material.rock);

        setHardness(4F);
        setCreativeTab(null);
    }

    @Override
    public int getRenderType() {
        return BlockRenderIdProvider.get(this);
    }

    @Override
    public void dropCarvedStone(World world, int x, int y, int z) {
        if(!world.getBlock(x + 1, y, z).isOpaqueCube() &&
            !world.getBlock(x - 1, y, z).isOpaqueCube() &&
            !world.getBlock(x, y, z + 1).isOpaqueCube() &&
            !world.getBlock(x, y, z - 1).isOpaqueCube() &&
            !world.getBlock(x, y + 1, z).isOpaqueCube() &&
            !world.getBlock(x, y - 1, z).isOpaqueCube()) {

            // Drop loose rocks
            int meta = world.getBlockMetadata(x, y, z);
            this.dropBlockAsItem(world, x, y, z, new ItemStack(TFCItems.looseRock, world.rand.nextInt(2) + 1, meta + this.looseStart));

            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
        //Make sure that the player gets exhausted from harvesting this block since we override the vanilla method
        if(entityplayer != null) {
            entityplayer.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
            entityplayer.addExhaustion(0.075F);
        }

        // Drop loose rocks
        this.dropBlockAsItem(world, i, j, k, new ItemStack(TFCItems.looseRock, world.rand.nextInt(2) + 1, l + this.looseStart));

        // No cave-ins
    }

}
