package com.unforbidable.tfc.bids.features.material.logs.item;

import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderConfigurator;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.util.wood.LogWallHelper;
import com.unforbidable.tfc.bids.util.wood.WoodHelper;
import com.unforbidable.tfc.bids.features.device.woodpile.main.WoodpileHelper;
import com.unforbidable.tfc.bids.util.ItemHelper;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemPeeledLog extends Item implements ISize, WoodpileRenderable {

    private IIcon[] icons;
    protected String[] names;

    public ItemPeeledLog() {
        super();
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(16);
        setNames(Global.WOOD_ALL);
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
        icons = new IIcon[names.length];
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (hasSubItem(wood)) {
                icons[wood.index] = registerer.registerIcon(Tags.MOD_ID + ":" + "wood/"
                        + names[wood.index] + " "
                        + getUnlocalizedName().replace("item.", "").replace(" Seasoned", ""));
            }
        }
    }

    protected boolean hasSubItem(WoodIndex wood) {
        return wood.items.hasPeeledLog();
    }

    protected ItemStack getSubItem(WoodIndex wood) {
        return wood.items.getPeeledLog();
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        if (names != null && i < names.length && icons[i] != null) {
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
        for (WoodIndex wood : WoodScheme.DEFAULT.getWoods()) {
            if (hasSubItem(wood)) {
                list.add(getSubItem(wood));
            }
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
        if (WoodpileHelper.createWoodpileAt(itemStack, player, world, x, y, z, side)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean renderAsLargeWoodpileItem(ItemStack itemStack) {
        return false;
    }

    @Override
    public void configureWoodpileRenderer(ItemStack itemStack, boolean rotated, WoodpileRenderConfigurator renderer) {
        for (int i = 0; i < 6; i++) {
            renderer.setTexture(i, getLogWallBlockIcon(itemStack, i, rotated));
        }
    }

    private IIcon getLogWallBlockIcon(ItemStack itemStack, int side, boolean rotated) {
        // We are using the log wall to get the texture
        final int offset = itemStack.getItemDamage() - itemStack.getItemDamage() % 16;
        final int meta = itemStack.getItemDamage() % 16;

        Block block = rotated ? LogWallHelper.getLogWallBlock(offset, 4, false)
                : LogWallHelper.getLogWallBlock(offset, 2, true);

        return block.getIcon(side, meta);
    }

}
