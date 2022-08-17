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
import com.unforbidable.tfc.bids.Core.Wood.WoodHelper;
import com.unforbidable.tfc.bids.Core.WoodPile.WoodPileHelper;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderProvider;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodPileRenderer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemFirewood extends Item implements ISize, IWoodPileRenderProvider {

    private IIcon[] icons;
    protected String[] names;

    public ItemFirewood() {
        super();

        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(16);
    }

    public ItemFirewood setNames(String[] names) {
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
        return WoodHelper.canMakeFirewood(meta);
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
        return EnumSize.VERYLARGE;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.MEDIUM;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
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
    public boolean isWoodPileLargeItem(ItemStack itemStack) {
        return false;
    }

    @Override
    public void onWoodPileRender(ItemStack itemStack, boolean rotated, IWoodPileRenderer renderer) {
        for (int i = 0; i < 6; i++) {
            // The "top" texture is used for the short side
            if (!rotated && (i / 2) == 1 || rotated && (i / 2) == 2) {
                renderer.setTexture(i, getStackedFirewoodBlockIcon(itemStack, 0));
            } else {
                renderer.setTexture(i, getStackedFirewoodBlockIcon(itemStack, 2));
            }
        }

        // Rotate long sides
        // Except rotated top
        if (rotated) {
            renderer.setTextureRotation(0, 1);
            renderer.setTextureRotation(1, 1);
            renderer.setTextureRotation(2, 1);
            renderer.setTextureRotation(3, 1);
        } else {
            renderer.setTextureRotation(4, 1);
            renderer.setTextureRotation(5, 2); // Why 2?

            // I would expect east (5) side to rotate by "1"
            // but that renders a bugged texture.
            // "3" doesn't seem to do anything.
            // "2" does exactly what we need. Why?
            // Possibly a bug in TFC's RenderBlocksWithRotation or
            // the very Minecraft render engine
        }
    }

    private IIcon getStackedFirewoodBlockIcon(ItemStack itemStack, int side) {
        final int damage = itemStack.getItemDamage();
        final int offset = damage - damage % 16;
        final int meta = damage % 16;

        switch (offset) {
            case 0:
                return BidsBlocks.stackedFirewood.getIcon(side, meta);
            case 16:
                return BidsBlocks.stackedFirewood2.getIcon(side, meta);
            default: // 32
                return BidsBlocks.stackedFirewood3.getIcon(side, meta);
        }
    }

}
