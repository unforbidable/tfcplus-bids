package com.unforbidable.tfc.bids.common.item;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.util.ItemHelper;
import com.unforbidable.tfc.bids.util.accessor.ItemMetaNamesAccessor;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemCommonMisc extends Item implements ISize, ItemMetaNamesAccessor {

    protected String[] metaNames;
    protected IIcon[] metaIcons;

    protected String textureFolder;

    public ItemCommonMisc() {
        setMaxStackSize(64);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public ItemCommonMisc setTextureFolder(String textureFolder) {
        this.textureFolder = textureFolder;

        return this;
    }

    @Override
    public Item setMetaNames(String[] names) {
        this.metaNames = names;

        setHasSubtypes(names != null);

        return this;
    }

    @Override
    public boolean getHasSubtypes() {
        return metaNames != null;
    }

    @Override
    public String[] getMetaNames() {
        return metaNames;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String folder = textureFolder == null || textureFolder.length() == 0 ? "" : textureFolder + "/";
        if (metaNames != null) {
            metaIcons = new IIcon[metaNames.length];
            for (int i = 0; i < metaNames.length; i++) {
                metaIcons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + folder
                    + this.getUnlocalizedName().replace("item.", "") + "." + metaNames[i]);
            }
        } else {
            itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + folder
                + this.getUnlocalizedName().replace("item.", ""));
        }
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (metaNames != null) {
            if (i < metaNames.length && metaIcons[i] != null) {
                return metaIcons[i];
            } else {
                return metaIcons[0];
            }
        } else {
            return this.itemIcon;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        if (metaNames != null) {
            for (int i = 0; i < this.metaNames.length; ++i) {
                list.add(new ItemStack(this, 1, i));
            }
        } else {
            list.add(new ItemStack(this, 1, 0));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (metaNames != null && itemstack.getItemDamage() < metaNames.length) {
            return this.getUnlocalizedName() + "." + metaNames[itemstack.getItemDamage()];
        } else {
            return super.getUnlocalizedName(itemstack);
        }
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public boolean canStack() {
        return true;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.TINY;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

}
