package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.TileEntities.TEPottery;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.TileEntities.TileEntityCookingPot;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ItemCookingPotLid extends ItemBlock implements ISize {

    public ItemCookingPotLid(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 4;
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.MEDIUM;
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.LIGHT;
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
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
        if (metadata == 0 && side == 1 && player.isSneaking()) {
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
        } else if (metadata == 1) {
            if (!world.isAirBlock(x, y, z)) {
                return false;
            }

            return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
        }

        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() == 0) {
            return getUnlocalizedName() + ".Clay";
        } else {
            return getUnlocalizedName() + ".Ceramic";
        }
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);

        if (ItemHelper.showShiftInformation()) {
            list.add(StatCollector.translateToLocal("gui.Help"));
            if (is.getItemDamage() == 0) {
                list.add(StatCollector.translateToLocal("gui.Help.Pottery"));
            } else {
                list.add(StatCollector.translateToLocal("gui.Help.CookingPotLid"));
            }
        } else {
            list.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

}
