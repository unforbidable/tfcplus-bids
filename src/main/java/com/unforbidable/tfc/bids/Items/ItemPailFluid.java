package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Tags;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class ItemPailFluid extends ItemTerra implements ISize {

    IIcon overlayIcon;

    public ItemPailFluid(int volume) {
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(1);
        setMaxDamage(volume / 50);
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
    public void registerIcons(IIconRegister registerer) {
        overlayIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "tools/" +
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
            : super.getColorFromItemStack(getContainerItem(is), pass);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
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

                player.setCurrentItemOrArmor(0, new ItemStack(getContainerItem(), 1, 0));
                return true;
            }
        }

        return false;
    }

}
