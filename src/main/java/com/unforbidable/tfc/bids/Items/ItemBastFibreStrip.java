package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.dunk.tfc.Items.ItemLooseRock;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.api.BidsGui;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemBastFibreStrip extends ItemLooseRock {

    static final String[] names = new String[] { "Fresh", "Cured" };
    IIcon[] icons;

    public ItemBastFibreStrip() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(64);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (itemstack.getItemDamage() == 1 && itemstack.stackSize >= 5) {
            // Open bark crafting gui
            PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(entityplayer);
            pi.specialCraftingType = new ItemStack(specialCraftingType, 1, itemstack.getItemDamage());
            if (specialCraftingTypeAlternate != null)
                pi.specialCraftingTypeAlternate = specialCraftingTypeAlternate;
            else
                pi.specialCraftingTypeAlternate = null;
            entityplayer.openGui(Bids.instance, BidsGui.barkFibreKnappingGui, world, (int) entityplayer.posX,
                    (int) entityplayer.posY, (int) entityplayer.posZ);
        }

        return itemstack;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        icons = new IIcon[names.length];
        for (int i = 0; i < names.length; i++) {
            icons[i] = registerer.registerIcon(Tags.MOD_ID + ":plants/"
                    + this.getUnlocalizedName().replace("item.", "") + "." + names[i]);
        }
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (i < names.length) {
            return icons[i];
        } else {
            return this.itemIcon;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        return this.getUnlocalizedName() + "." + names[is.getItemDamage()];
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
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);

        // Cured strips can be twisted into cordage, etc
        if (is.getItemDamage() == 1) {
            if (ItemHelper.showShiftInformation()) {
                list.add(StatCollector.translateToLocal("gui.Help"));
                list.add(StatCollector.translateToLocal("gui.Help.BastFibreStrip"));
            } else {
                list.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }
    }

}
