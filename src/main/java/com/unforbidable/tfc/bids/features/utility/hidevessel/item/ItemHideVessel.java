package com.unforbidable.tfc.bids.features.utility.hidevessel.item;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.IBag;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.meta.HideVesselMeta;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import com.unforbidable.tfc.bids.features.device.cookingpit.main.CookingPitHelper;
import com.unforbidable.tfc.bids.features.utility.hidevessel.main.HideVesselHelper;
import com.unforbidable.tfc.bids.util.ItemHelper;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemHideVessel extends ItemTerra implements IBag {

    IIcon ruinedIcon;

    public ItemHideVessel() {
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setFolder("tools");
        setHasSubtypes(true);
        setMaxDamage(HideVesselHelper.HIDE_VESSEL_MAX_DAMAGE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(this, 1, HideVesselMeta.EMPTY));
        list.add(new ItemStack(this, 1, 19));
    }

    @Override
    public boolean onUpdate(ItemStack is, World world, int x, int y, int z) {
        if (!world.isRemote) {
            HideVesselHelper.damageHideVessel(is, HideVesselHelper.HIDE_VESSEL_EMPTY_DAMAGE_INTERVAL);
        }

        return true;
    }

    @Override
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
                             float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (side == 1) {
                int damage = is.getItemDamage();
                if (CookingPitHelper.placeHideCookingPitAt(world, x, y + 1, z, null, damage)) {
                    is.stackSize--;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return stack.getItemDamage() == HideVesselMeta.RUINED;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        return FluidHelper.fillContainerFromWorld(is, world, player);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", ""));
        ruinedIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" +
            getUnlocalizedName().replace("item.", "") + ".Ruined");
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getItemDamage() != HideVesselMeta.RUINED && super.showDurabilityBar(stack);
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return getIconFromDamage(stack.getItemDamage());
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return damage != HideVesselMeta.RUINED ? itemIcon : ruinedIcon;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return super.getUnlocalizedName(itemstack) +
            (itemstack.getItemDamage() != HideVesselMeta.RUINED ? ".Empty" : ".Ruined");
    }

    @Override
    public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
        if (is.getItemDamage() != HideVesselMeta.RUINED) {
            if (ItemHelper.showShiftInformation()) {
                arraylist.add(StatCollector.translateToLocal("gui.Help"));
                arraylist.add(StatCollector.translateToLocal("gui.Help.HideVessel.Empty"));
            } else {
                arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }
    }

    @Override
    public ItemStack[] loadBagInventory(ItemStack itemStack) {
        return new ItemStack[0];
    }

    @Override
    public EnumSize getSize(ItemStack is) {
        return EnumSize.SMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack is) {
        return EnumWeight.LIGHT;
    }

}
