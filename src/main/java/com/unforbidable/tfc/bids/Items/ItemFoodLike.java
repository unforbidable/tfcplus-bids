package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.dunk.tfc.api.Interfaces.ISize;
import com.dunk.tfc.api.Util.Helper;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Tags;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemFoodLike extends ItemExtraFood implements ISize {

    public ItemFoodLike() {
        super(EnumFoodGroup.None, 0, 0, 0, 0, 0, false, false);
        setCreativeTab(BidsCreativeTabs.bidsMaterials);
    }

    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(ItemFoodTFC.createTag(new ItemStack(this, 1), 160.0F));
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":"
                + this.getUnlocalizedName().replace("item.", ""));
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int count) {
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

    @Override
    public int getItemStackLimit(ItemStack arg0) {
        return 1;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean arg3) {
        ItemHelper.addSizeInformation(is, list);
        addFoodWeightInformation(is, player, list);
    }

    private void addFoodWeightInformation(ItemStack is, EntityPlayer player, List list) {
        float ounces = Helper.roundNumber(Food.getWeight(is), 100.0F);
        if (ounces > 0.0F && ounces <= 160.0F) {
            list.add(TFC_Core.translate("gui.food.amount") + " " + ounces + " oz / " + 160.0F + " oz");
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    public int getRenderPasses(int metadata) {
        return 1;
    }

    @Override
    public EnumFoodGroup getFoodGroup() {
        return null;
    }

    @Override
    public int getFoodID() {
        return 0;
    }

    @Override
    public float getDecayRate(ItemStack itemStack) {
        return 0;
    }

    @Override
    public float getFoodMaxWeight(ItemStack itemStack) {
        return Global.FOOD_MAX_WEIGHT;
    }

    @Override
    public ItemStack onDecayed(ItemStack itemStack, World world, int i, int i1, int i2) {
        return null;
    }

    @Override
    public boolean isEdible(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isUsable(ItemStack itemStack) {
        return false;
    }

    @Override
    public int getTasteSweet(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getTasteSour(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getTasteSalty(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getTasteBitter(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int getTasteSavory(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean renderDecay() {
        return false;
    }

    @Override
    public boolean renderWeight() {
        return true;
    }

}
