package com.unforbidable.tfc.bids.features.device.firepit.item;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;
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
import com.unforbidable.tfc.bids.features.device.woodpile.main.WoodpileHelper;
import com.unforbidable.tfc.bids.util.ItemHelper;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemTiedStickBundle extends Item implements ISize, FirepitFuelMaterial, WoodpileRenderable {

    public ItemTiedStickBundle() {
        super();

        setCreativeTab(BidsCreativeTabs.bidsMaterials);
        setMaxStackSize(16);
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
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":wood/sticks/"
                + getUnlocalizedName().replace("item.", ""));
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
        return EnumWeight.LIGHT;
    }

    @SuppressWarnings({"unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
    }

    @Override
    public boolean isFuelValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public float getFuelKindlingQuality(ItemStack itemStack) {
        return 0.25f;
    }

    @Override
    public int getFuelBurnTime(ItemStack itemStack) {
        return (int) (EnumFuelMaterial.STICKBUNDLE.burnTimeMax * 1.1f);
    }

    @Override
    public int getFuelMaxTemp(ItemStack itemStack) {
        return (int) (EnumFuelMaterial.STICKBUNDLE.burnTempMax * 1f);
    }

    @Override
    public int getFuelTasteProfile(ItemStack itemStack) {
        return EnumFuelMaterial.STICKBUNDLE.ordinal();
    }

    @Override
    public boolean renderAsLargeWoodpileItem(ItemStack itemStack) {
        return false;
    }

    @Override
    public void configureWoodpileRenderer(ItemStack itemStack, boolean rotated, WoodpileRenderConfigurator renderer) {
        for (int i = 0; i < 6; i++) {
            renderer.setTexture(i, getTiedStickBundleBlockIcon(i, rotated));
        }

        // Rotate top and bottom when not rotated
        if (!rotated) {
            renderer.setTextureRotation(0, 1);
            renderer.setTextureRotation(1, 1);
        }
    }

    private IIcon getTiedStickBundleBlockIcon(int side, boolean rotated) {
        switch (side) {
            case 0:
            case 1:
                return BidsBlocks.tiedStickBundle.getIcon(2, 0);

            case 2:
            case 3:
                return BidsBlocks.tiedStickBundle.getIcon(rotated ? 2 : 0, 0);

            case 4:
            case 5:
            default:
                return BidsBlocks.tiedStickBundle.getIcon(rotated ? 0 : 2, 0);
        }
    }

}
