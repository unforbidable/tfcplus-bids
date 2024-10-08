package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemMoreRawhide extends ItemTerra implements ISize {

    public ItemMoreRawhide() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMetaNames(new String[] { "Tiny" });
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        metaIcons = new IIcon[metaNames.length];
        for (int i = 0; i < metaNames.length; i++) {
            metaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":"
                + this.getUnlocalizedName().replace("item.", "") + "." + metaNames[i]);
        }
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @Override
    public int getItemStackLimit(ItemStack arg0) {
        return 64;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
