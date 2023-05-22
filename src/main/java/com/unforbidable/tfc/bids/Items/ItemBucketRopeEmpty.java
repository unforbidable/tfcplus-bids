package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Sounds;
import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Handlers.Network.ItemPotterySmashPacket;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Entities.IAnimal;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Interfaces.ISmashable;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsItems;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ItemBucketRopeEmpty extends ItemTerra implements ISmashable {

    private final boolean isPottery;

    public ItemBucketRopeEmpty(boolean isPottery) {
        this.isPottery = isPottery;

        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(1);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String folder = isPottery ? "pottery" : "wood";
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + folder + "/Bucket Rope");
    }


    @Override
    public EnumItemReach getReach(ItemStack is) {
        return EnumItemReach.FAR;
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 1;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        Random random = new Random();

        double reachBase = player instanceof EntityPlayerMP ? ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance() : 5;
        int reach = (int) Math.round(reachBase * getReach(is).multiplier);
        MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(world, player, true, reach);

        Item bucketWater = isPottery ? BidsItems.ceramicBucketRopeWater : BidsItems.woodenBucketRopeWater;

        if (mop != null) {
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;

                if (!world.canMineBlock(player, x, y, z))
                    return is;

                if (!player.canPlayerEdit(x, y, z, mop.sideHit, is))
                    return is;

                FillBucketEvent event = new FillBucketEvent(player, is, world, mop);
                if (MinecraftForge.EVENT_BUS.post(event) || event.isCanceled())
                    return is;

                if (event.getResult() == Event.Result.ALLOW)
                    return event.result;

                if (TFC_Core.isFreshWater(world.getBlock(x, y, z)))  {
                    if (player.capabilities.isCreativeMode)
                        return is;

                    if (isPottery && random.nextInt(40) == 0) {
                        world.playSoundAtEntity(player, TFC_Sounds.CERAMICBREAK, 0.7f,
                            player.worldObj.rand.nextFloat() * 0.2F + 0.8F);
                        return new ItemStack(TFCItems.rope, 1, 0);
                    }
                    return new ItemStack(bucketWater, 1, 0);
                }

                // Handle flowing water
                int flowX = x;
                int flowY = y;
                int flowZ = z;
                switch (mop.sideHit) {
                    case 0:
                        flowY = y - 1;
                        break;
                    case 1:
                        flowY = y + 1;
                        break;
                    case 2:
                        flowZ = z - 1;
                        break;
                    case 3:
                        flowZ = z + 1;
                        break;
                    case 4:
                        flowX = x - 1;
                        break;
                    case 5:
                        flowX = x + 1;
                        break;
                }

                if (TFC_Core.isFreshWater(world.getBlock(flowX, flowY, flowZ))) {
                    if (isPottery && random.nextInt(40) == 0)  {
                        world.playSoundAtEntity(player, TFC_Sounds.CERAMICBREAK, 0.7f,
                            player.worldObj.rand.nextFloat() * 0.2F + 0.8F);
                        return new ItemStack(TFCItems.rope, 1, 0);
                    }
                    return new ItemStack(bucketWater, 1, 0);
                }
            }
        }

        return is;
    }

    @Override
    public boolean canSmash(ItemStack item) {
        return isPottery;
    }

    @Override
    public void onSmashed(ItemStack stack, World world, double x, double y, double z) {
        if (stack != null && stack.getItem() != null && stack.getItem() instanceof ISmashable) {
            world.playSoundEffect(x, y, z, "terrafirmacraftplus:item.ceramicbreak", 1.0F, 0.8F + world.rand.nextFloat() * 0.4F);
            ItemPotterySmashPacket smashPkt = new ItemPotterySmashPacket(Item.getIdFromItem(stack.getItem()), x, y, z);
            NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 32.0);
            TerraFirmaCraft.PACKET_PIPELINE.sendToAllAround(smashPkt, tp);

            TFC_Core.smashItemInWorld(world, x, y, z, new ItemStack(TFCItems.rope));
        }
    }

    @Override
    public void smashAnimate(World world, double x, double y, double z) {
        String smashBlock = "blockdust_" + Block.getIdFromBlock(Blocks.glass) + "_0";

        for(double i = 0.0; i < 6.283185307179586; i += 0.7853981633974483) {
            world.spawnParticle(smashBlock, x, y, z, Math.cos(i) * 0.2, 0.15 + world.rand.nextDouble() * 0.1, Math.sin(i) * 0.2);
        }
    }

}
