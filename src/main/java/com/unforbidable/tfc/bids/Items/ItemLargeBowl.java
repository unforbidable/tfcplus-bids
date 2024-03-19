package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemLargeBowl extends ItemGenericPottery {

    public ItemLargeBowl() {
        super(true);
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(16);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 16;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        return FluidHelper.fillContainerFromWorld(is, world, player, true);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side,
                             float hitX, float hitY, float hitZ) {
        if (!world.isRemote && !player.isSneaking()) {
            if (world.isAirBlock(x, y + 1, z) && world.isSideSolid(x, y, z, ForgeDirection.UP)) {
                Block block = world.getBlock(x, y, z);
                if (block.getMaterial() == Material.rock || block.getMaterial() == Material.wood || block.getMaterial() == Material.iron) {
                    world.setBlock(x, y + 1, z, BidsBlocks.cookingPrep);
                    return true;
                }
            }

            return false;
        } else {
            return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
    }

}
