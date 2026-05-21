package com.unforbidable.tfc.bids.features.material.firewood.item;

import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.features.firepit.FirepitFuelMaterial;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderConfigurator;
import com.unforbidable.tfc.bids.api.features.woodpile.WoodpileRenderable;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodIndex;
import com.unforbidable.tfc.bids.core.schemes.wood.WoodScheme;
import com.unforbidable.tfc.bids.features.device.woodpile.main.WoodpileHelper;
import com.unforbidable.tfc.bids.util.ItemHelper;
import com.unforbidable.tfc.bids.util.accessor.ItemMetaNamesAccessor;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemFirewood extends Item implements ISize, WoodpileRenderable, FirepitFuelMaterial, ItemMetaNamesAccessor {

    private IIcon[] icons;
    protected String[] names;

    public ItemFirewood() {
        super();

        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(16);
    }

    @Override
    public Item setMetaNames(String[] names) {
        this.names = names;
        return this;
    }

    @Override
    public String[] getMetaNames() {
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

    protected boolean hasSubItem(WoodIndex wood) {
        return wood.items.hasFirewood();
    }

    protected Object getSubItem(WoodIndex wood) {
        return wood.items.getFirewood();
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

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return WoodScheme.DEFAULT.findWood(itemStack).maxBurnTime;
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return WoodScheme.DEFAULT.findWood(itemStack).maxBurnTemp;
    }

    @Override
    public int getFuelTasteProfile(ItemStack itemStack) {
        return WoodScheme.DEFAULT.findWood(itemStack).tasteProfile;
    }

}
