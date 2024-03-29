package com.unforbidable.tfc.bids.Items.ItemBlocks;

import com.dunk.tfc.TileEntities.TEPottery;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCBlocks;
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

public class ItemCookingPot extends ItemBlock implements ISize {

    public ItemCookingPot(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        if (is.hasTagCompound()) {
            return 1;
        } else {
            return 4;
        }
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.LARGE;
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        if (is.hasTagCompound()) {
            return EnumWeight.HEAVY;
        } else {
            return EnumWeight.MEDIUM;
        }
    }

    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.SHORT;
    }

    @Override
    public int getMetadata(int damage) {
        return damage == 0 ? 0 : 1;
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
        } else if (metadata > 0) {
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

        NBTTagCompound tag = is.getTagCompound();
        if (tag != null) {
            TileEntityCookingPot dummyCookingPot = new TileEntityCookingPot();
            dummyCookingPot.readDataFromNBT(tag);

            CookingHelper.getCookingPotInfo(dummyCookingPot, list, false);
        }

        if (ItemHelper.showShiftInformation()) {
            list.add(StatCollector.translateToLocal("gui.Help"));
            if (is.getItemDamage() == 0) {
                list.add(StatCollector.translateToLocal("gui.Help.Pottery"));
            } else {
                list.add(StatCollector.translateToLocal("gui.Help.CookingPot"));
            }
        } else {
            list.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

}
