package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Core.TFC_Textures;
import com.dunk.tfc.Items.ItemLeatherBag;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class ItemExtraBag extends ItemLeatherBag {

    boolean[][] clothingAlpha;
    ResourceLocation res;

    public ItemExtraBag() {
        super(new String[] {});

        setCreativeTab(BidsCreativeTabs.bidsDefault);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "armor/clothing/"
                + getUnlocalizedName().replace("item.", ""));

        res = new ResourceLocation(Tags.MOD_ID, "textures/items/armor/clothing/"
                + getUnlocalizedName().replace("item.", "Flat ") + ".png");

        clothingAlpha = TFC_Textures.loadClothingPattern(res);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return itemIcon;
    }

    @Override
    public ResourceLocation getFlatTexture() {
        return res;
    }

    @Override
    public boolean[][] getClothingAlpha() {
        return this.clothingAlpha;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);

        if (ItemHelper.showShiftInformation()) {
            list.add(StatCollector.translateToLocal("gui.Help"));
            list.add(StatCollector.translateToLocal("gui.Help.Bag"));
        } else {
            list.add(StatCollector.translateToLocal("gui.ShowHelp"));
        }
    }

}
