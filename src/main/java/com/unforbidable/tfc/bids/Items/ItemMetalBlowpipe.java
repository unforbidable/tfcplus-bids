package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Core.TFC_Sounds;
import com.dunk.tfc.Core.Player.PlayerInfo;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.Items.Pottery.ItemPotteryMold;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsGui;
import com.unforbidable.tfc.bids.api.BidsItems;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemMetalBlowpipe extends ItemPotteryMold {

    protected IIcon icon;
    protected IIcon glassIcon;

    public ItemMetalBlowpipe() {
        super();
        stackable = false;
        setMaxDamage(101);
        setMaxUnits(100);
        setCounter(1);
        setBaseDamage(2);
        setCreativeTab(BidsCreativeTabs.BidsDefault);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean canSmash(ItemStack item) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (itemstack.getItemDamage() == baseDamage && !world.isRemote && !entityplayer.isSneaking()) {
            world.playSoundEffect(entityplayer.posX, entityplayer.posY, entityplayer.posZ,
                    TFC_Sounds.BELLOWS, 0.4F, 1);

            // Open glass crafting gui
            PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(entityplayer);
            pi.specialCraftingType = new ItemStack(BidsItems.flatGlass, 1, 0);
            pi.specialCraftingTypeAlternate = null;
            entityplayer.openGui(Bids.instance, BidsGui.glassKnappingGui, world, (int) entityplayer.posX,
                    (int) entityplayer.posY, (int) entityplayer.posZ);
        }
        return itemstack;
    }

    @Override
    public boolean isValidMold(ItemStack i) {
        return getPossibleUnits(i) > 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItemDamage() >= baseDamage)
            return getUnlocalizedName().concat(".Glass");

        return getUnlocalizedName();
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return damage >= baseDamage ? glassIcon : icon;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        icon = registerer.registerIcon(Tags.MOD_ID + ":Metal Blowpipe");
        glassIcon = registerer.registerIcon(Tags.MOD_ID + ":Metal Blowpipe Glass");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemTerra.addSizeInformation(is, list);

        if (is.getItemDamage() > 1) {
            int units = 100 - (is.getItemDamage() - baseDamage);
            list.add(StatCollector.translateToLocal("gui.Units") + ": " + units + " / 100");
        }

        if (is.getItemDamage() == 0) {
            list.add("Obsolete! Use crafting grid to upgrade.");
        }
    }

    @Override
    public boolean getHasSubtypes() {
        return false;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, baseDamage - 1));
    }

}
