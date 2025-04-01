package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IProcessingSurfaceIconProvider {

    IIcon registerProcessingSurfaceIcon(IIconRegister registerer, ItemStack is);

}
