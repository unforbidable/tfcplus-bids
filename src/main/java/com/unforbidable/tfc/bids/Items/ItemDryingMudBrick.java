package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemDryingMudBrick extends ItemTerra {

    public ItemDryingMudBrick() {
        this.hasSubtypes = true;
        this.setMaxDamage(0);
        this.setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list) {
        for (int i = 0; i < Global.STONE_ALL.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        super.addInformation(is, player, arraylist, flag);
        if ((is.getItemDamage() & 31) < 21)
            arraylist.add(EnumChatFormatting.DARK_GRAY + Global.STONE_ALL[is.getItemDamage() & 31]);
        else
            arraylist.add(EnumChatFormatting.DARK_RED + "Unknown: " + is.getItemDamage());
    }

}
