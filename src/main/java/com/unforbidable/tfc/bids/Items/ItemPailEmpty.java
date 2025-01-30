package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPailEmpty extends ItemTerra implements ISize {

    public ItemPailEmpty() {
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(1);
        setFolder("tools");
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
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", ""));
    }

}
