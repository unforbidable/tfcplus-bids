package com.unforbidable.tfc.bids.Blocks;

import com.dunk.tfc.Blocks.Terrain.BlockSed;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCrackedSed extends BlockSed {

    private IIcon[] destroyStageIcons;

    public BlockCrackedSed(Material material) {
        super(material);

        setCreativeTab(null);
    }

    @Override
    public int getRenderType() {
        return BidsBlocks.crackedStoneRenderId;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
        // No dropping carved stone
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

    @Override
    public void registerBlockIcons(IIconRegister iconRegisterer) {
        super.registerBlockIcons(iconRegisterer);

        destroyStageIcons = new IIcon[10];
        for (int i = 0; i < 10; i++) {
            destroyStageIcons[i] = iconRegisterer.registerIcon("destroy_stage_" + i);
        }
    }

    public IIcon getDestroyStageIcon(int i) {
        return destroyStageIcons[i];
    }

}
