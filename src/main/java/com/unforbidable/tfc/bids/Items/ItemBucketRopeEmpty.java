package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Handlers.Network.ItemPotterySmashPacket;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Interfaces.ISmashable;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Tags;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
        return FluidHelper.fillContainerFromWorld(is, world, player);
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
