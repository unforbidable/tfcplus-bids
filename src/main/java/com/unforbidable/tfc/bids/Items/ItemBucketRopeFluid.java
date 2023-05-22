package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ItemBucketRopeFluid extends ItemBucketFluid {

    public ItemBucketRopeFluid(boolean isPottery) {
        super(isPottery);

        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
                             float hitY, float hitZ) {
        Item bucketEmpty = isPottery ? BidsItems.ceramicBucketRope : BidsItems.woodenBucketRope;
        Random random = new Random();

        ForgeDirection d = ForgeDirection.getOrientation(side);
        int x2 = x + d.offsetX;
        int y2 = y + d.offsetY;
        int z2 = z + d.offsetZ;

        if (world.isAirBlock(x2, y2, z2)) {
            //world.setBlock(x2, y2, z2, TFCBlocks.freshWater, 2, 0x1);

            // Use normal bucket mechanics to spawn water in the world
            // and create mud if possible
            ItemStack fakeBucket = new ItemStack(TFCItems.woodenBucketWater, 1, 0);
            TFCItems.woodenBucketWater.onItemUse(fakeBucket, player, world, x, y, z, side, hitX, hitY, hitZ);

            // Handle pottery breaking here to return rope
            if (isPottery && random.nextInt(40) == 0) {
                player.setCurrentItemOrArmor(0, new ItemStack(TFCItems.rope, 1, 0));
                return true;
            }

            player.setCurrentItemOrArmor(0, new ItemStack(bucketEmpty, 1, 0));
            return true;
        }

        return false;
    }

    @Override
    public void onSmashed(ItemStack stack, World world, double x, double y, double z) {
        if (canSmash(stack)) {
            // Clay bucket will break so drop rope
            TFC_Core.smashItemInWorld(world, x, y, z, new ItemStack(TFCItems.rope));
        }

        super.onSmashed(stack, world, x, y, z);
    }

}
