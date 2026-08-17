package com.unforbidable.tfc.bids.features.material.skin.item;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemFinishedSkin extends ItemSkin {

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_SMALL + 8f));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_SMALL));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_MEDIUM));
        list.add(SkinHelper.createStack(this, SkinHelper.WEIGHT_LARGE));
    }

    @Override
    protected String getSkinSizeUnlocalizedName(ItemStack itemStack) {
        return "";
    }

    @Override
    protected void addSkinProcessingStageShiftInformation(ItemStack itemStack, EntityPlayer player, List<String> list) {
        if (itemStack.getItem() == BidsItems.rawhide) {
            list.add(StatCollector.translateToLocal("gui.Help.Skin.Stage.Preserved"));
        }
    }

    @Override
    public float getDecayRate(ItemStack itemStack) {
        return 0f;
    }

}
