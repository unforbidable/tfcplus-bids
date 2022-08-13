package com.unforbidable.tfc.bids.Items.ItemBlocks;

import java.util.List;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.DryingRack.DryingRackHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemDryingRack extends ItemBlock implements ISize {

    public ItemDryingRack(Block block) {
        super(block);

        setMaxStackSize(8);
    }

    public int getMaxDryingRackSpan() {
        return 3;
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.MEDIUM;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        final ForgeDirection dir = ForgeDirection.getOrientation(side);

        if (!world.isRemote && DryingRackHelper.canPlaceDryingRackAt(world, x, y, z, dir)) {
            final int maxSpan = getMaxDryingRackSpan();
            DryingRackHelper.placeDryingRackAt(stack, player, world, x, y, z, dir, maxSpan);
        }

        return true;
    }

}
