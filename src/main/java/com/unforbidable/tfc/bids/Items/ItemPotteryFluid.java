package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Handlers.Network.ItemPotterySmashPacket;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.TerraFirmaCraft;
import com.dunk.tfc.api.Interfaces.ISmashable;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class ItemPotteryFluid extends ItemTerra implements ISmashable {

    IIcon overlayIcon;

    public ItemPotteryFluid(int volume) {
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(1);
        setMaxDamage(volume / 50);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(getContainerItem(), 1, 1);
    }

    @Override
    public int getItemStackLimit(ItemStack is)
    {
        return 1;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        overlayIcon = registerer.registerIcon(Tags.MOD_ID + ":pottery/" +
            getContainerItem().getUnlocalizedName().replace("item.", "") + ".Overlay");
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
            : super.getColorFromItemStack(new ItemStack(TFCItems.potteryJug, 1, 1), pass);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public boolean canSmash(ItemStack item) {
        return item.getItemDamage() == 0;
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
        String smashBlock = "blockdust_" + Block.getIdFromBlock(TFCBlocks.pottery) + "_15";

        for(double i = 0.0; i < 6.283185307179586; i += 0.7853981633974483) {
            world.spawnParticle(smashBlock, x, y, z, Math.cos(i) * 0.2, 0.15 + world.rand.nextDouble() * 0.1, Math.sin(i) * 0.2);
        }
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
                             float hitY, float hitZ) {
        // Only fresh water and salt water can be emptied in the world
        Fluid fluid = FluidContainerRegistry.getFluidForFilledItem(is).getFluid();
        if (fluid == TFCFluids.FRESHWATER || fluid == TFCFluids.SALTWATER) {
            ForgeDirection d = ForgeDirection.getOrientation(side);
            int x2 = x + d.offsetX;
            int y2 = y + d.offsetY;
            int z2 = z + d.offsetZ;

            if (world.isAirBlock(x2, y2, z2)) {
                world.setBlock(x2, y2, z2, fluid == TFCFluids.FRESHWATER ? TFCBlocks.freshWater : TFCBlocks.saltWater, 2, 0x1);

                player.setCurrentItemOrArmor(0, new ItemStack(getContainerItem(), 1, 1));
                return true;
            }
        }

        return false;
    }

}
