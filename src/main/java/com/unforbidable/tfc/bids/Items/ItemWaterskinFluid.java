package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemTerra;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class ItemWaterskinFluid extends ItemTerra {

    IIcon overlayIcon;

    public ItemWaterskinFluid() {
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(1);
    }

    @Override
    public int getItemStackLimit(ItemStack is)
    {
        return 1;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        overlayIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "tools" + "/Waterskin.Overlay");
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

}
