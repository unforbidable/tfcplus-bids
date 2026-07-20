package com.unforbidable.tfc.bids.features.crafting.dough.item;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.GuiNames;
import com.unforbidable.tfc.bids.common.item.ItemExtraFood;
import com.unforbidable.tfc.bids.util.GuiUtil;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
        if(is.stackSize > 0) {
            PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(player);
            pi.specialCraftingType = new ItemStack(BidsItems.flatDough, 1, flatDoughDamage);
            pi.specialCraftingTypeAlternate = null;

            GuiUtil.openGui(GuiNames.DOUGH, player);
        }

        return is;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
        super.addInformation(is, player, arraylist, flag);

        // ItemFoodTFC addInformation does not invoke addExtraInformation
        addExtraInformation(is, player, arraylist);
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
