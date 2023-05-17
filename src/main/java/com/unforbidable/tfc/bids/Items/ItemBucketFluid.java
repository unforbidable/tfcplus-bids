package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Handlers.Network.ItemPotterySmashPacket;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Interfaces.ISmashable;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class ItemBucketFluid extends ItemTerra implements ISmashable {

    protected final boolean isPottery;
    IIcon overlayIcon;

    public ItemBucketFluid(boolean isPottery) {
        this.isPottery = isPottery;

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(1);
    }

    @Override
    public int getItemStackLimit(ItemStack is)
    {
        return 1;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String folder = isPottery ? "pottery" : "wood";
        overlayIcon = registerer.registerIcon(Tags.MOD_ID + ":" + folder + "/Bucket.Overlay");
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return getContainerItem().getIcon(getContainerItem(stack), pass);
        } else if (pass == 1) {
            return overlayIcon;
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack is, int pass) {
        return pass == 1
            ? FluidContainerRegistry.getFluidForFilledItem(is).getFluid().getColor()
            : super.getColorFromItemStack(new ItemStack(isPottery ? TFCItems.potteryJug : TFCItems.woodenBucketEmpty, 1, 1), pass);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
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
