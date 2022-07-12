package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.TileEntities.TEPottery;
import com.dunk.tfc.api.TFCBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemClayCrucible extends ItemCrucible {

    public ItemClayCrucible(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return stack.getItemDamage() == 0;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world,
            int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (metadata == 1 && side == 1 && player.isSneaking()) {
            Block base = world.getBlock(x, y - 1, z);
            if (base != TFCBlocks.pottery && world.isAirBlock(x, y, z)) {
                // We only want the pottery to be placeable if the block is solid on top.
                if (!world.isSideSolid(x, y - 1, z, ForgeDirection.UP))
                    return false;
                world.setBlock(x, y, z, TFCBlocks.pottery);
            } else {
                return false;
            }

            if (world.getTileEntity(x, y, z) instanceof TEPottery) {
                TEPottery te = (TEPottery) world.getTileEntity(x, y, z);
                if (te.canAddItem(0)) {
                    te.inventory[0] = stack.copy();
                    te.inventory[0].stackSize = 1;
                    world.markBlockForUpdate(x, y, z);
                    return true;
                }
            }

            return false;
        } else if (metadata == 0) {
            return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        }

        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        switch (itemstack.getItemDamage()) {
            case 1:
                return getUnlocalizedName() + ".Clay";

            case 2:
                return getUnlocalizedName() + ".Ruined";
        }

        return getUnlocalizedName() + ".Ceramic";
    }

}
