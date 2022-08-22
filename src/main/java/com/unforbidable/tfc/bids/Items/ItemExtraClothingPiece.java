package com.unforbidable.tfc.bids.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dunk.tfc.Items.ItemClothingPiece;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemExtraClothingPiece extends ItemClothingPiece {

    String[] extraMetaNames;
    IIcon[] extraMetaIcons;
    Map<String, Integer> extraIndices = new HashMap<String, Integer>();

    public ItemExtraClothingPiece() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public ItemExtraClothingPiece setExtraPieceTypes(String... materialNames) {
        setPieceTypes(new boolean[] { false, false, false, false, false, false, false });

        extraMetaNames = new String[materialNames.length];

        for (int i = 0; i < materialNames.length; i++) {
            extraMetaNames[i] = materialNames[i];
            extraIndices.put(materialNames[i], i);
        }

        return this;
    }

    public int getIndexForExtraClothingMaterial(String name) {
        return extraIndices.get(name);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        super.getSubItems(item, tabs, list);

        int j = metaNames.length;
        for (int i = 0; i < extraMetaNames.length; i++) {
            list.add(new ItemStack(this, 1, j + i));
        }
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        super.registerIcons(registerer);

        extraMetaIcons = new IIcon[extraMetaNames.length];

        for (int i = 0; i < extraMetaNames.length; i++) {
            extraMetaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "armor/clothing/"
                    + getUnlocalizedName().substring(5) + " " + extraMetaNames[i]);
        }
    }

    @Override
    public IIcon getIconFromDamage(int dmg) {
        if (dmg < metaNames.length) {
            return super.getIconFromDamage(dmg);
        } else if (dmg < metaNames.length + extraMetaNames.length) {
            return extraMetaIcons[dmg - metaNames.length];
        }

        return itemIcon;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        final int dmg = itemstack.getItemDamage();
        if (dmg < metaNames.length) {
            return super.getUnlocalizedName(itemstack);
        } else if (dmg < metaNames.length + extraMetaNames.length) {
            return getUnlocalizedName().concat("." + extraMetaNames[dmg - metaNames.length]);
        }

        return super.getUnlocalizedName(itemstack);
    }

}
