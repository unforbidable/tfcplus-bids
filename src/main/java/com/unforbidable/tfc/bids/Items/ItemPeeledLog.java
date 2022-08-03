package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Seasoning.SeasoningHelper;
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileHelper;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemPeeledLog extends Item implements ISize, IWoodPileRenderProvider {

    private IIcon[] icons;
    protected String[] names;

    public ItemPeeledLog() {
        super();
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(16);
    }

    public ItemPeeledLog setNames(String[] names) {
        this.names = names;
        return this;
    }

    public String[] getNames() {
        return names;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        icons = new IIcon[Global.WOOD_ALL.length];
        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            if (subItemExists(i)) {
                icons[i] = registerer.registerIcon(Tags.MOD_ID + ":" + "wood/"
                        + Global.WOOD_ALL[i] + " "
                        + getUnlocalizedName().replace("item.", "").replace(" Seasoned", ""));
            }
        }
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (names != null && i < names.length && subItemExists(i)) {
            return icons[i];
        } else {
            return this.itemIcon;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        return this.getUnlocalizedName() + "." + names[itemstack.getItemDamage()];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List list) {
        for (int i = 0; i < Global.WOOD_ALL.length; i++) {
            if (subItemExists(i)) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public boolean subItemExists(int meta) {
        return WoodHelper.canPeelLog(meta);
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
        return EnumSize.MEDIUM;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.MEDIUM;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
        SeasoningHelper.addSeasoningInformation(is, list);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (WoodPileHelper.createWoodPileAt(itemStack, player, world, x, y, z, side)) {
            return true;
        }

        return false;
    }

    @Override
    public IIcon getWoodPileIcon(ItemStack itemStack, int side, boolean rotated) {
        // We are using the log wall to get the texture
        final int offset = itemStack.getItemDamage() - itemStack.getItemDamage() % 16;
        final int meta = itemStack.getItemDamage() % 16;

        Block block = rotated ? WoodHelper.getLogWallBlock(offset, 4, false)
                : WoodHelper.getLogWallBlock(offset, 2, true);

        return block.getIcon(side, meta);
    }

    @Override
    public float getWoodPileIconScale(ItemStack itemStack) {
        return 0.5f;
    }

    @Override
    public boolean isWoodPileLargeItem(ItemStack itemStack) {
        return false;
    }

}
