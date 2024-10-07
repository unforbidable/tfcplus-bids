package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsGui;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemUnshapedDough extends ItemExtraFood {

    private int flatDoughDamage = 0;

    public ItemUnshapedDough(EnumFoodGroup fg, int sw, int so, int sa, int bi, int um) {
        super(fg, sw, so, sa, bi, um, false, false);
    }

    public ItemUnshapedDough setFlatDoughDamage(int flatDoughDamage) {
        this.flatDoughDamage = flatDoughDamage;

        return this;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(player);
        pi.specialCraftingType = new ItemStack(BidsItems.flatDough, 1, flatDoughDamage);
        pi.specialCraftingTypeAlternate = null;

        if(is.stackSize > 0) {
            player.openGui(Bids.instance, BidsGui.doughKnappingGui, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        }

        return is;
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        if (TFC_Core.showShiftInformation())
        {
            arraylist.add(TFC_Core.translate("gui.Help"));
            arraylist.add(TFC_Core.translate("gui.Help.UnshapedDough"));
        }
        else
        {
            arraylist.add(TFC_Core.translate("gui.ShowHelp"));
        }
    }

}
