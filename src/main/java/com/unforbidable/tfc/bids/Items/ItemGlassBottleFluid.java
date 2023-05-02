package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Handlers.Network.ItemPotterySmashPacket;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Interfaces.ISmashable;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.FluidOverlayMap;
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

public class ItemGlassBottleFluid extends ItemTerra implements ISmashable {

    private static final int[] OVERLAY_PARTS = new int[] { 0, 20, 40, 60, 80, 100 };

    final FluidOverlayMap overlays;

    public ItemGlassBottleFluid() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(4);

        this.overlays = new FluidOverlayMap(OVERLAY_PARTS);
    }

    @Override
    public int getItemStackLimit(ItemStack is)
    {
        return 4;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        overlays.registerOverlayIcons(registerer, "glassware", "Glass Bottle");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return getContainerItem().getIcon(getContainerItem(stack), pass);
        } else if (pass == 1) {
            return overlays.getOverlayIcon(stack.getItemDamage(), 1000);
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack is, int pass) {
        if (pass == 1 && FluidContainerRegistry.getFluidForFilledItem(is) == null) {
            return super.getColorFromItemStack(is, pass);
        } else {
            return pass == 1 ?
                FluidContainerRegistry.getFluidForFilledItem(is).getFluid().getColor() :
                super.getColorFromItemStack(is, pass);
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public boolean canSmash(ItemStack item) {
        return true;
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
